package model;

/** The abstract Plant represents the superclass at which all
 * plants derive from
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */


public abstract class Plant {
    
    private GameModel model;
    protected int row; // Row at which the plant is found
    protected int col; // Column at which the plant is found

    // All required Plant attributes with respect to the MP Specs
    protected int sunCost; // Cost of each plant
    protected int regenerateRate; // Time BEFORE it can be replanted
    protected int damage; // Base damage
    protected int directDamage; // Close range damage; Cherrybomb and PotatoMine
    protected int cost;
    protected int health; 
    protected int range;
    protected int speed;
    protected int coolDown; // for replanting
    protected boolean isDead = false;



    /** This constructor initializes the row and column of the plant

       @param row row the plant is to be planted at
       @param col column the Sunflower is to be planted at
   */	

    public Plant(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** This method is used every tick
     * to activate the plant’s behavior
     * 
     * @param model the GameModel to interact with
     */
    public abstract void performAction(GameModel model);

    /** This method returns whether or not the plant is dead
     * 
     * @return TRUE if plant should be removed
     */
    public abstract boolean isDead();

    /** This method reduces plant HP
     * when hit by a zombie
     * 
     * @param dmg amount taken
     */
    public abstract void takeDamage(int dmg);

    /** This method returns the current HP
     * 
     * @return plant health
     */
    public abstract int getHealth();

    /** This method returns the cost in Sun
     * 
     * @return sun cost
     */
    public abstract int getCost();

    /** This method returns the replanting cooldown
     * 
     * @return cool down
     */
    public abstract int getCooldown();

    /** This method returns the row 
     * 
     * @return row
     */
    public int getRow(){ return row; }

    /** This method returns the column
     * 
     * @return col
     */
    public int getCol(){ return col; }
}