package blockBreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class Ball extends Ellipse2D.Double implements Actor
{
    // default initial values
    private static final double XPOS   = 210;
    private static final double YPOS   = 500;
    private static final double WIDTH  = 13;
    private static final double HEIGHT = 13;
    private static final double SPEED  = 3;
    private static final double ANGLE  = 45.0;
    private static final double ACCELERATION_FACTOR = 0.06;
    
    public double speed;
    private double angle;
   
    public Point p0;
    public Point p90;
    public Point p180;
    public Point p270;
    
    public Point lastPointHit;
    
    public Line2D.Double path;

    public Ball()
    {
        this( XPOS, YPOS, WIDTH, HEIGHT, SPEED, ANGLE );
    }
    
    public Ball ( double x, double y, double w, double h, double s, double a )
    {
        super( x, y, w, h );
        
        speed  = s;
        angle  = a;
        lastPointHit = null;
    }
    
    public void draw( Graphics2D g )
    {
        Color origColor = g.getColor();
        
        g.setColor( Color.WHITE );
        g.fill( this );
        g.setColor( Color.RED );
        g.draw( path );
        
        g.setColor( origColor );
    }
    
    /**
     * Move the ball in the specified direction.  The ball moves a number of pixels
     * equal to its speed.
     */
    public void move( BlockMap map, Paddle paddle, int win_width, int win_height )
    {
        double prevX = x;
        double prevY = y;
        
        double vy = Math.sin( Math.toRadians( angle ) ) * speed;
        double vx = Math.cos( Math.toRadians( angle ) ) * speed;
        
        double newX = x + vx;
        double newY = y + vy;
        
        // create a line which traces the movement of the root position of the ball
        path = new Line2D.Double( prevX, prevY, newX, newY );
        
        Ball futureBall = new Ball( newX, newY, WIDTH, HEIGHT, SPEED, this.angle );
        
        if ( futureBall.hitWall( win_width, win_height ) )
        {
            // hit right wall
            if ( lastPointHit == p0 )
            {
                
            }
            // hit left wall
            else if ( lastPointHit == p180 )
            {
                
            }
            // hit ceiling
            else if ( lastPointHit == p270 )
            {
                
            }
            else
            {
                System.out.println( "The ball hit a wall but I don't know which one!");
                System.exit(1);
            }
        }
        
        if ( paddle.checkHit( futureBall ) )
        {
        }
        
        if ( map.checkHit(futureBall) )
        {
        }
    }

    private boolean hitWall( int win_width, int win_height )
    {
        // ball hits left wall
        if (x < 0)
        {
            lastPointHit = p180;
            return true;
        }
        // ball hits right wall
        else if (x + width > win_width)
        {
            lastPointHit = p0;
            return true;
        }
        // ball hits ceiling
        else if (y < 0)
        {
            lastPointHit = p270;
            return true;
        }
        
        return false;
    }
    
    public double getAngle()
    {
        return angle;
    }
    
    public void setAngle( double a )
    {
        if ( a > 360.0 )
        {
            angle = a - 360.0;
        }
        else if ( a < 0.0 )
        {
            angle = a + 360.0;
        }
        else
        {
            angle = a;
        }
    }
    
    public int getAngleQuadrant()
    {
        // quadrant I
        if ( angle > 0.0 && angle < 90.0 )
        {
            return 1;
        }
        // quadrant II
        else if ( angle > 90.0 && angle < 180.0 )
        {
            return 2;
        }
        // quadrant III
        else if ( angle > 180.0 && angle < 270.0 )
        {
            return 3;
        }
        // quadrant IV
        else if ( angle > 270.0 && angle < 360.0 )
        {
            return 4;
        }
        
        return 0;
    }
    
    public String toString()
    {
        String s = "Width  : " + width  + "\n";
              s += "Height : " + height + "\n";
              s += "x      : " + x      + "\n";
              s += "y      : " + y      + "\n";
              s += "Speed  : " + speed  + "\n";
              s += "Angle  : " + angle  + "\n";
        
        return s;
    }
}
