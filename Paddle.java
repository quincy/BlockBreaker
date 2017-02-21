package blockBreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * The paddle which the player controls.
 * 
 * @author qabowers
 *
 */
@SuppressWarnings("serial")
public class Paddle extends Rectangle implements Actor 
{
    // default initial values
    private static final int XPOS     = 200;
    private static final int YPOS     = 550;
    private static final int WIDTH    = 64;
    private static final int HEIGHT   = 15;
    private static final double SPEED = 4.10;
    
    public double speed;
    
    /**
     * Default constructor creates a new paddle with default values.
     */
    public Paddle()
    {
        this( WIDTH, HEIGHT, XPOS, YPOS, SPEED );
    }
    
    /**
     * Constructor for a custom paddle.
     * 
     * @param w the width of the new paddle.
     * @param h the height of the new paddle.
     * @param x the x-position of the new paddle.
     * @param y the y-position of the new paddle.
     * @param aw the arc-width of the paddle.
     * @param ah the arc-height of the paddle.
     * @param spd the speed of the new paddle.
     */
    public Paddle( int w, int h, int x, int y, double spd )
    {
        super( x, y, w, h );
        speed = spd;
    }

    /**
     * Draws the paddle on the graphics context.
     * 
     * @param g the graphics object this paddle will be drawn on.
     */
    public void draw( Graphics2D g )
    {
        Color origColor = g.getColor();
        
        g.setColor( Color.gray );
        g.fill3DRect( x, y, width, height, true );
        
        g.setColor( origColor );
    }
    
    /**
     * Moves the paddle downward a number of pixels equal to speed.
     */
    public void moveDown()
    {
        y += speed;
    }
    
    /**
     * Moves the paddle upward a number of pixels equal to speed.
     */
    public void moveUp()
    {
        y -= speed;
    }
    
    /**
     * Moves the paddle to the right.  The paddle is moved <tt>speed</tt> pixels.
     */
    public void moveRight()
    {
        x += speed;
    }
    
    /**
     * Moves the paddle to the left.  The paddle is moved <tt>speed</tt> pixels.
     */
    public void moveLeft()
    {
        x -= speed;
    }

    /**
     * Walks the ArrayList to see if the Ball intersects any of the Blocks.
     * When a hit is detected currentHit gets the value of the Block's index and
     * the method returns true.  Otherwise the method returns false.
     * 
     * @param ball the Ball to check a hit with. 
     * @return true if a hit is detected, false otherwise.
     */
    public boolean checkHit( Ball ball )
    {
        if ( ball.intersects( this.getBounds2D() ) )
        {
            // calculate the point on the ball that was hit
            if ( this.contains( ball.p0 ) )
            {
                ball.lastPointHit = ball.p0;
            }
            else if ( this.contains( ball.p90 ) )
            {
                ball.lastPointHit = ball.p90;
            }
            else if ( this.contains( ball.p180 ) )
            {
                ball.lastPointHit = ball.p180;
            }
            else if ( this.contains( ball.p270 ) )
            {
                ball.lastPointHit = ball.p270;   
            }
            
            return true;
        }
        
        return false;
    }
    
    public double hitOffset( Point p )
    {
        double center = x + 0.5 * width;
        
        if ( p.x < center )
        {
            return center - p.x;
        }
        
        return p.x - center;
    }
    
    /**
     * Gets a string describing the paddle.
     * 
     * @return a string describing the current values of the paddle.
     */
    public String toString()
    {
        String s = "Width  : " + width  + "\n";
              s += "Height : " + height + "\n";
              s += "x      : " + x      + "\n";
              s += "y      : " + y      + "\n";
              s += "Speed  : " + speed  + "\n";
        
        return s;
    }
}
