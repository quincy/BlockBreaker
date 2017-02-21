package blockBreaker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

@SuppressWarnings("serial")
public class BlockMap extends ArrayList<Block>
{
    //private static final File MAP_FILE = new File( "bin/blockBreaker/level1.lvl" );
    private static final File MAP_FILE = new File( "bin/blockBreaker/level2.lvl" );
        
    private Scanner fs;
    private File map_file;    
    
    public int currentHit;
    
    public BlockMap()
    {
        this( MAP_FILE );
    }
    
    public BlockMap( File f )
    {
        super();
        
        currentHit = -1;
        
        map_file = f;
        readMap( map_file );
    }
    
    public void readMap( File f )
    {
        try
        {
            fs = new Scanner( f );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        
        while ( fs.hasNext() )
        {
            int x = fs.nextInt();
            int y = fs.nextInt();
            int c = fs.nextInt();
            int o = fs.nextInt();

            add( new Block( x, y, c, o ) );
        }
    }
    
    public void draw(Graphics2D g)
    {
        Color origColor = g.getColor();

        Iterator<Block> iter = this.iterator();
        Block curr = null;
        
        while ( iter.hasNext() )
        {
            curr = iter.next();
            curr.draw(g);
        }
        
        g.setColor( origColor );
    }
    
    public void hit( Block b )
    {
        b.hit();
        currentHit = -1;
        
        if ( b.color < 0 )
        {
            this.remove( b );
        }
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
        for ( int i = 0; i < this.size(); i++ )
        {
            if ( this.get(i).checkHit( ball ) )
            {
                currentHit = i;
                return true;
            }
        }
        return false;
    }
}
