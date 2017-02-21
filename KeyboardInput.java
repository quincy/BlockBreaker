package blockBreaker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class provides methods for checking the current status of the keyboard.
 * 
 * @author qabowers
 */
public class KeyboardInput implements KeyListener
{
    private static final int KEY_COUNT = 256;
    
    private enum KeyState 
    {
        RELEASED, // Not down
        PRESSED,  // Down, but not the first time
        ONCE      // Down for the first time
    }
    
    // Current state of the keyboard
    private boolean[] currentKeys = null;
    
    // Polled keyboard state
    private KeyState[] keys = null;

    /**
     * Constructor initializes all keys to a RELEASED state.
     */
    public KeyboardInput() 
    {
        currentKeys = new boolean[ KEY_COUNT ];
        keys = new KeyState[ KEY_COUNT ];
        
        for( int i = 0; i < KEY_COUNT; ++i ) 
        {
            keys[ i ] = KeyState.RELEASED;
        }
    }
    
    /**
     * Checks the current status of each key.
     */
    public synchronized void poll() 
    {
        for( int i = 0; i < KEY_COUNT; ++i ) 
        {
            // Set the key state 
            if( currentKeys[ i ] ) 
            {
                // If the key is down now, but was not
                // down last frame, set it to ONCE,
                // otherwise, set it to PRESSED
                if( keys[ i ] == KeyState.RELEASED )
                {
                    keys[ i ] = KeyState.ONCE;
                }
                else
                {
                    keys[ i ] = KeyState.PRESSED;
                }
            }
            else 
            {
                keys[ i ] = KeyState.RELEASED;
            }
        }
    }

    /**
     * Checks if the given key is currently pressed.
     * 
     * @param keyCode the key to check.
     * @return true if the key is pressed, false otherwise.
     */
    public boolean keyDown( int keyCode ) 
    {
        return keys[ keyCode ] == KeyState.ONCE || keys[ keyCode ] == KeyState.PRESSED;
    }
    
    /**
     * Checks if this is the first time the given key has been pressed down.
     * 
     * @param keyCode the key to check.
     * @return true if the key was not pressed on the previous poll but is this time, false otherwise.
     */
    public boolean keyDownOnce( int keyCode ) 
    {
        return keys[ keyCode ] == KeyState.ONCE;
    }
    
    /**
     * Sets the key to the pressed state.
     * 
     * @param e the KeyEvent that caused this key to be pressed.
     */
    public synchronized void keyPressed( KeyEvent e ) 
    {
        int keyCode = e.getKeyCode();
    
        if( keyCode >= 0 && keyCode < KEY_COUNT ) 
        {
            currentKeys[ keyCode ] = true;
        }
    }
    
    /**
     * Sets the key to the released state.
     * 
     * @param e the KeyEvent that caused this key to be released.
     */
    public synchronized void keyReleased( KeyEvent e ) 
    {
        int keyCode = e.getKeyCode();
    
        if( keyCode >= 0 && keyCode < KEY_COUNT ) 
        {
            currentKeys[ keyCode ] = false;
        }
    }

    /**
     * Unused.
     */
    public void keyTyped( KeyEvent e ) 
    {
        // Not needed
    }
}
