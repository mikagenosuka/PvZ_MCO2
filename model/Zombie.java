package model;


/** The abstract class Zombie represents an enemy
 * that is to be fought by the player.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */

public abstract class Zombie {
    protected GameModel model;
    private static int idCounter = 0;
    private final int id;
    protected int row, col;
    private int x;
    private int width;
    // Attributes as of project specifications
    protected int health;
    protected int damage;
    protected int speed; // How many ticks between steps

    protected int moveCooldown = 0;  // Counter for movement timing

    protected boolean slowed = false; // Logic for snowPea
    protected int slowTimer = 0;     // How many ticks remaining slowed

    /**
     * This constructor initializes the zombie position and sets
     * the base stats (from subclass).
     *
     * @param row the row where the zombie starts
     * @param col the column where the zombie starts 
     */
    public Zombie(int row, int col, GameModel model) {
        this.row = row;
        this.col = col;
        this.model = model;
        this.id = idCounter++;
    }

    /** This method runs every tick.
     * Handles movement, slow status, and attacking plants.
     *
     * @param model the current GameModel being updated
     */
    public void performAction(GameModel model) {
        if (isDead()) return;

        if (slowed) {
            slowTimer--;
            if (slowTimer <= 0) {
                slowed = false;
                speed *= 4; // revert to normal speed
                System.out.println(getType() + " at (" + row + "," + col + ") is no longer slowed.");
            }
        }

        Tile tile = model.getTile(row, col);
        if (tile == null) return;
        Plant plant = tile.getPlant();

        if (plant != null && !plant.isDead()) {
            plant.takeDamage(damage);
            System.out.println(getType() + " at (" + row + "," + col + ") attacked plant for " + damage);
        } else {
            if (moveCooldown <= 0) {
                moveForward(model);
                moveCooldown = speed;
            } else {
                moveCooldown--;
            }
        }
    }

    /** This method moves the zombie one tile to the left. */
    protected void moveForward(GameModel model) {
        int nextCol = col - 1;
        if (nextCol >= 0) {
            // Remove from current tile
            Tile currentTile = model.getTile(row, col);
            if (currentTile != null) {
                currentTile.removeZombie(this);
            }

            col = nextCol; // update position

            // Add to new tile
            Tile nextTile = model.getTile(row, col);
            if (nextTile != null) {
                nextTile.addZombie(this);
            }

            System.out.println(getType() + " moved to (" + row + "," + col + ")");
        } else {
            // Reached the house
            System.out.println("A " + getType() + " reached the house!");
        }
    }


    /** This method reduces the zombie’s health by damage taken.
     *
     * @param dmg the damage to apply
     */
    public void takeDamage(int dmg) {
        health -= dmg;
        System.out.println(getType() + " at (" + row + "," + col + ") took " + dmg + " damage. HP: " + health);
    }

    /** This method applies a slowing effect to the zombie. */
    public void applySlow() {
        if (!slowed) {
            slowed = true;
            slowTimer = 3; // slow effect lasts 3 ticks
            speed *= 2;    // half the speed
            System.out.println(getType() + " at (" + row + "," + col + ") is slowed.");
        }
    }

    /** Checks if the zombie has died.
     *
     * @return true if health is 0 or less
     */
    public boolean isDead() {
        return health <= 0;
    }

    /** Accessor methods for position */
    public int getRow() { return row; }
    public int getCol() { return col; }

    public int getId() { return id; }


    /** Accessor for health */
    public int getHealth() { return health; }

    /* Accessor for speed */
    public int getSpeed() { return speed; }

    /* Accessor for movecooldown */
    public int getMoveCooldown() { return moveCooldown; }

    /* Accessor for slow status */
    public boolean isSlowed(){ return slowed; }

    /* Basis accessors for gameOver*/
    public int getX() { return x; }
    public int getWidth() { return width; }

    /** Abstract method for the zombie's name/type */
    public abstract String getType();
}
