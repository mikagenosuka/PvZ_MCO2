package model;

/** The class Suntoken represents a Sun Token
 * that can be clicked and retrieved by the player 
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */
import java.awt.Rectangle;

public class SunToken {
    private int row, col;
    private int lifespan; // In ticks
    private boolean collected; // Whether it's been clicked or not 
    private final int value = 25; // How much one token is worth
    private int x, y;

    /** This constructor initializes the row and column of the spawned
     * Sun Token

       @param row row the Sun spawns
       @param col column the Sun spawns
   */	
    public SunToken(int row, int col) {
        this.row = row;
        this.col = col;
        this.lifespan = 10; // Disappears after 10 seconds
        this.collected = false; // Removed from the board if true
    }

    /** This function initializes the location
     * of the token for the GUI

       @param x x coordinate
       @param y y coordinate
   */	
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /** This function initializes the hitbox of the 
     * SunToken

       @param x x coordinate
       @param y y coordinate
   */	
    public Rectangle getBounds() {
        this.x = col * 100;
        this.y = row * 100;
        return new Rectangle(x, y, 40, 40); // hitbox for click detection
    }

    /* METHODS */
    
    /* behaviors */

    /** This method decrements a tick
     * per every ingame second while the Sun
     * has yet to be clicked
   */	
    public void tick() {
        if (lifespan > 0) lifespan--;
    }

    /** This method checks if the
     * Sun has expired
     * (Not been clicked in 10 seconds)
     * 
     * @return TRUE if 10 ticks has passed without it being clicked
   */	
    public boolean isExpired() {
        return lifespan <= 0 && !collected;
    }

    /** This method checks if the
     * Sun has been clicked
     * 
     * @return TRUE if Sun has been clicked
   */	
    public boolean isCollected() {
        return collected;
    }

    /** This method signals the boolean
     * collected to change values from default false
     * to true if clicked
     * 
   */	
    public void collect() {
        collected = true;
    }

    /** These method checks for the
     * row and col that the Sun has spawned in
     * 
     * @return row that the Sun spawns in
     * @return col that the Sun spawns in 
   */	
    public int getRow() { return row; }
    public int getCol() { return col; }
    public int getValue() { return value; }
}
