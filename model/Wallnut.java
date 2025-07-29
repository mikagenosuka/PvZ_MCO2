package model;

/** The class Wallnut represents a plant 
 * that can be planted by the player.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */
  public class Wallnut extends Plant {

    /**
     * This constructor initializes the WallNut
     * and sets all required spec attributes.
     *
     * @param row the row the WallNut is placed on
     * @param col the column the WallNut is placed on
     */
    public Wallnut(int row, int col) {
        super(row, col);

        // Spec-required attributes
        this.cost = 50;
        this.regenerateRate = 20;
        this.damage = 0;           // No attack
        this.directDamage = 0;
        this.health = 300;         // Very tanky
        this.range = 0;
        this.speed = 0;            // No active behavior
        this.coolDown = 15;
    }

    /**
     * Does nothing, meant to be defensive.
     *
     * @param model the GameModel being updated
     */
    @Override
    public void performAction(GameModel model) {
        // Does NOTHING
        // #FREE MUAHAHHAHAAHA
    }

    /**
     * This method applies zombie damage to the Wallnut.
     *
     * @param dmg the damage received
     */
    @Override
    public void takeDamage(int dmg) {
        this.health -= dmg;
        System.out.println("WallNut at (" + row + "," + col + ") took " + dmg + " damage.");
    }

    /**
     * This method checks if the Wallnut is destroyed.
     *
     * @return true if its health is zero or less
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * This method returns the Wallnut's current health.
     *
     * @return current health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * This method returns the sun cost to place Wallnut.
     *
     * @return 50 sun
     */
    @Override
    public int getCost() {
        return cost;
    }

    /** This method returns the cooldown of planting the Wallnut
     * 
     * @return cooldown
     */
    @Override
    public int getCooldown() {
        return coolDown;
    }
}
