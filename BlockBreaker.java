package blockBreaker;

import java.awt.*;
import java.awt.event.KeyEvent;
//import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import blockBreaker.KeyboardInput;
import blockBreaker.Paddle;

@SuppressWarnings("serial")
public class BlockBreaker extends JFrame
{
    // static members
    static final int WIDTH = 896;
    static final int HEIGHT = 600;
    static final int MAX_PADDLE_HEIGHT = HEIGHT - 100;
    static final int MIN_PADDLE_HEIGHT = HEIGHT - 50;

    static KeyboardInput keyboard = new KeyboardInput(); // Keyboard polling
    static Canvas canvas; // Our drawing component

    static Paddle paddle;
    static Ball ball;
    static BlockMap map;

    static boolean gameIsRunning = false;

    // instance members
    private final Color BACKGROUND_COLOR = Color.black;

    /**
     * Default constructor for the BlockBreaker game. Sets up the canvas, key
     * listener, etc...
     */
    public BlockBreaker()
    {
        setIgnoreRepaint(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(WIDTH, HEIGHT);
        add(canvas);
        pack();

        // Hook up keyboard polling
        addKeyListener(keyboard);
        canvas.addKeyListener(keyboard);
    }

    /**
     * The main method. Creates the window, initializes the game, and starts the
     * game loop.
     * 
     * @param args command line arguments.
     */
    public static void main(String[] args)
    {
        BlockBreaker app = new BlockBreaker();
        app.setTitle("Block Breaker");
        app.setVisible(true);

        // Create BackBuffer...
        canvas.createBufferStrategy(2);
        BufferStrategy buffer = canvas.getBufferStrategy();

        // Get graphics configuration...
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();

        // Create off-screen drawing surface
        BufferedImage bi = gc.createCompatibleImage(WIDTH, HEIGHT);

        // Objects needed for rendering...
        Graphics graphics = null;
        Graphics2D g2d = null;
        Color background = Color.black;

        while (true)
        {
            /*---- DRAWING ----*/
            // clear back buffer...
            g2d = bi.createGraphics();
            g2d.setColor(background);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);

            // Instructions
            g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
            g2d.setColor(Color.GREEN);
            g2d.drawString(String.format("Press 'N' for New Game."), 20, 20);
            g2d.drawString(String.format("Press 'Q' to Quit."), 20, 35);

            // Blit image and flip...
            graphics = buffer.getDrawGraphics();
            graphics.drawImage(bi, 0, 0, null);

            if (!buffer.contentsLost())
            {
                buffer.show();
            }

            if (!gameIsRunning)
            {
                keyboard.poll();
                if (keyboard.keyDown(KeyEvent.VK_N))
                {
                    app.startGame(buffer, graphics, g2d, bi);
                }
                else if (keyboard.keyDown(KeyEvent.VK_Q))
                {
                    quitApp();
                }

            }
        }
    }

    public static void quitApp()
    {
        System.exit(0);
    }

    public void startGame(BufferStrategy buffer, Graphics graphics,
            Graphics2D g2d, BufferedImage bi)
    {
        gameIsRunning = true;
        initGame();
        run(buffer, graphics, g2d, bi);
    }

    /**
     * Initializes the game state.
     */
    public void initGame()
    {
        paddle = new Paddle();
        ball   = new Ball();
        map    = new BlockMap();
    }

    /**
     * Starts a new game.
     */
    public void run(BufferStrategy buffer, Graphics graphics, Graphics2D g2d, BufferedImage bi)
    {
        // clear back buffer...
        g2d = bi.createGraphics();
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Variables for counting frames per seconds
        int fps = 0;
        int frames = 0;
        long totalTime = 0;
        long curTime = System.currentTimeMillis();
        long lastTime = curTime;

        while (gameIsRunning)
        {
            try
            {
                /*---- TIME MANAGEMENT */
                // count Frames per second...
                lastTime = curTime;
                curTime = System.currentTimeMillis();
                totalTime += curTime - lastTime;

                if (totalTime > 1000)
                {
                    totalTime -= 1000;
                    fps = frames;
                    frames = 0;
                }
                ++frames;

                /*---- INPUT MANAGEMENT ----*/
                // poll the keyboard
                keyboard.poll();
                // Should we exit?
                if (keyboard.keyDownOnce(KeyEvent.VK_Q))
                {
                    gameIsRunning = false;
                }

                // process keyboard/mouse input
                processInput();
                
                // calculate the ball's movement
                ball.move(map, paddle, WIDTH, HEIGHT);
                // ball falls below floor
                if (ball.y > HEIGHT)
                {
                    System.out.println("You lose!");
                    gameIsRunning = false;
                }
                
                /*---- DRAWING ----*/
                // clear back buffer...
                g2d = bi.createGraphics();
                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRect(0, 0, WIDTH, HEIGHT);

                // draw the blocks, ball, ball, and paddle
                map.draw(g2d);
                ball.draw(g2d);
                paddle.draw(g2d);

                // display frames per second...
                g2d.setFont(new Font("Courier New", Font.PLAIN, 12));
                g2d.setColor(Color.GREEN);
                g2d.drawString(String.format("FPS: %s", fps), 20, 20);
                g2d.drawString(String.format("Angle: %f", ball.getAngle()), 20, 40);
                g2d.drawString(String.format("Speed: %f", ball.speed), 20, 60);

                // Blit image and flip...
                graphics = buffer.getDrawGraphics();
                graphics.drawImage(bi, 0, 0, null);

                if (!buffer.contentsLost())
                {
                    buffer.show();
                }

                // Let the OS have a little time...
                Thread.yield();
            }
            finally
            {
                // release resources
                if (graphics != null)
                {
                    graphics.dispose();
                }
                if (g2d != null)
                {
                    g2d.dispose();
                }
            }
        }
    }

    /**
     * Checks for keyboard input.
     */
    protected void processInput()
    {
        // If moving down
        if (keyboard.keyDown(KeyEvent.VK_DOWN))
        {
            paddle.moveDown();

            // Check collision with bottom
            if (paddle.y > MIN_PADDLE_HEIGHT)
            {
                paddle.y = MIN_PADDLE_HEIGHT;
            }
        }

        // If moving up
        if (keyboard.keyDown(KeyEvent.VK_UP))
        {
            paddle.moveUp();
            // Check collision with top
            if (paddle.y < MAX_PADDLE_HEIGHT)
            {
                paddle.y = MAX_PADDLE_HEIGHT;
            }
        }

        // If moving left
        if (keyboard.keyDown(KeyEvent.VK_LEFT))
        {
            paddle.moveLeft();
            // Check collision with left
            if (paddle.x < 0)
            {
                paddle.x = 0;
            }
        }

        // If moving right
        if (keyboard.keyDown(KeyEvent.VK_RIGHT))
        {
            paddle.moveRight();
            // Check collision with right
            if (paddle.x + paddle.getWidth() > WIDTH - 1)
            {
                paddle.x = WIDTH - paddle.width;
            }
        }
    } // processInput
}// BlockBreaker
