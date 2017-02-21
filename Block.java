package blockBreaker;

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

@SuppressWarnings("serial")
public class Block extends Rectangle implements Actor
{
    private static final int XPOS          = 0;
    private static final int YPOS          = 0;
    private static final int WIDTH         = 64;
    private static final int HEIGHT        = 24;
    private static final int COLOR         = 2;
    private static final int OUTLINE_COLOR = 1;

    public static final int TOP_EDGE    = 1;
    public static final int LEFT_EDGE   = 2;
    public static final int RIGHT_EDGE  = 3;
    public static final int BOTTOM_EDGE = 4;

    public Line2D.Double topEdge;
    public Line2D.Double leftEdge;
    public Line2D.Double rightEdge;
    public Line2D.Double bottomEdge;
    
    public int lastEdgeHit;
    
    public int color;
    public int outline;
    //public Point lastIntersection;
    
    /**
     * Default constructor creates a new paddle with default values.
     */
    public Block()
    {
        this( XPOS, YPOS, COLOR, OUTLINE_COLOR );
    }
    
    /**
     * Constructor for a custom block.
     * 
     * @param w the width of the new paddle.
     * @param h the height of the new paddle.
     * @param x the x-position of the new paddle.
     * @param y the y-position of the new paddle.
     */
    public Block( int x, int y, int c, int o )
    {
        super( x, y, WIDTH, HEIGHT );
        
        color    = c;
        outline  = o;
        
        topEdge    = new Line2D.Double( x, y, x+width, y );
        leftEdge   = new Line2D.Double( x, y, x, y+height );
        rightEdge  = new Line2D.Double( x+width, y, x+width, y+height );
        bottomEdge = new Line2D.Double( x, y+height, x+width, y+height );
    }

    private Color getColor( int c )
    {
        switch ( c )
        {
            case 0:  return Color.magenta;
            case 1:  return Color.white;
            case 2:  return Color.cyan;
            case 3:  return Color.blue;
            case 4:  return Color.green;
            case 5:  return Color.yellow;
            case 6:  return Color.orange;
            case 7:  return Color.pink;
            case 8:  return Color.red;
            case 9:  return Color.lightGray;
            case 10: return Color.gray;
            case 11: return Color.darkGray;
            case 12: return Color.black;
            default: return Color.black;
        }
    }

    @Override
    public void draw( Graphics2D g )
    {
        Color origColor = g.getColor();
        
        g.setColor( getColor(color) );
        g.fill3DRect( x, y, width, height, false );
        g.setColor( getColor(outline) );
        g.drawRect( x, y, width, height );
        
        g.setColor( origColor );
    }
    
    public void hit()
    {
        color--;
    }
    
    /**
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
            switch( ball.getAngleQuadrant() )
            {
                case 1: checkIntersection( ball.path, 1 ); return true;
                case 2: checkIntersection( ball.path, 2 ); return true;
                case 3: checkIntersection( ball.path, 3 ); return true;
                case 4: checkIntersection( ball.path, 4 ); return true;
                default: break;
            }        
        }
        
        return false;
    }

    private boolean checkIntersection( Line2D.Double ballPath, int quadrant )
    {
        switch( quadrant )
        {
            case 1: 
                if ( ballPath.intersectsLine( topEdge ) )
                {
                    lastEdgeHit = TOP_EDGE;
                    return true;
                }
                else if ( ballPath.intersectsLine( leftEdge ) )
                {
                    lastEdgeHit = LEFT_EDGE;
                    return true;
                }
                else
                {
                    System.out.println( "Quadrant " + quadrant + " but did not hit an edge!" );
                    System.out.println( "Ball path: " + ballPath.x1 + "," + ballPath.y1 + " " + ballPath.x2 + "," + ballPath.y2 );
                    System.out.println( "bottomEdge: " + bottomEdge.x1 + "," + bottomEdge.y1 + " " + bottomEdge.x2 + "," + bottomEdge.y2 );
                    return false;
                }
            case 2: 
                if ( ballPath.intersectsLine( topEdge ) )
                {
                    lastEdgeHit = TOP_EDGE;
                    return true;
                }
                else if ( ballPath.intersectsLine( rightEdge ) )
                {
                    lastEdgeHit = RIGHT_EDGE;
                    return true;
                }
                else
                {
                    System.out.println( "Quadrant " + quadrant + " but did not hit an edge!" );
                    System.out.println( "Ball path: " + ballPath.x1 + "," + ballPath.y1 + " " + ballPath.x2 + "," + ballPath.y2 );
                    System.out.println( "bottomEdge: " + bottomEdge.x1 + "," + bottomEdge.y1 + " " + bottomEdge.x2 + "," + bottomEdge.y2 );
                    return false;
                }
            case 3:
                if ( ballPath.intersectsLine( bottomEdge ) )
                {
                    lastEdgeHit = BOTTOM_EDGE;
                    return true;
                }
                else if ( ballPath.intersectsLine( rightEdge ) )
                {
                    lastEdgeHit = RIGHT_EDGE;
                    return true;
                }
                else
                {
                    System.out.println( "Quadrant " + quadrant + " but did not hit an edge!" );
                    System.out.println( "Ball path: " + ballPath.x1 + "," + ballPath.y1 + " " + ballPath.x2 + "," + ballPath.y2 );
                    System.out.println( "bottomEdge: " + bottomEdge.x1 + "," + bottomEdge.y1 + " " + bottomEdge.x2 + "," + bottomEdge.y2 );
                    System.out.println( "rightEdge: " + rightEdge.x1 + "," + rightEdge.y1 + " " + rightEdge.x2 + "," + rightEdge.y2 );
                    return false;
                }
            case 4:
                if ( ballPath.intersectsLine( bottomEdge ) )
                {
                    lastEdgeHit = BOTTOM_EDGE;
                    return true;
                }
                else if ( ballPath.intersectsLine( leftEdge ) )
                {
                    lastEdgeHit = LEFT_EDGE;
                    return true;
                }
                else
                {
                    System.out.println( "Quadrant " + quadrant + " but did not hit an edge!" );
                    System.out.println( "Ball path: " + ballPath.x1 + "," + ballPath.y1 + " " + ballPath.x2 + "," + ballPath.y2 );
                    System.out.println( "bottomEdge: " + bottomEdge.x1 + "," + bottomEdge.y1 + " " + bottomEdge.x2 + "," + bottomEdge.y2 );
                    return false;
                }
            default: System.out.println( "No quadrant returned!" + quadrant ); 
                     return false;
        } // switch       
    } // checkIntersection
}
