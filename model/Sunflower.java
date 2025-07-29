package model;

/** The class Sunflower represents a plant 
 * that can be planted by the player.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */

public class Sunflower extends Plant {

    private int generateCooldown = 0; // Timer until next SunToken

    /** This constructor initializes the Sunflower's position
     * and all required plant attributes
     * 
     * @param row row where the Sunflower is planted
     * @param col column where the Sunflower is planted
     */
    public Sunflower(int row, int col) {
        super(row, col);

        // Required attributes per spec
        this.cost = 50;
        this.regenerateRate = 5;
        this.damage = 0;          // Does not perform attacks
        this.directDamage = 0;
        this.health = 80;
        this.range = 0;
        this.speed = 8;           // Generates sun every 5 ticks
        this.coolDown = 8;
    }

    /** This method is called every tick.
     * If cooldown reaches 0, spawns a SunToken and resets timer
     * 
     * @param model the GameModel where the sun is spawned
     */
    @Override
    public void performAction(GameModel model) {
        if (generateCooldown > 0) {
            generateCooldown--;
        } else {
            model.spawnSunToken(new SunToken(row, col));
            generateCooldown = speed;
            System.out.println("Sunflower at (" + row + "," + col + ") generated 25 Sun.");
        }
    }

    /** This method applies damage to the Sunflower
     * from attacking zombies
     * 
     * @param dmg amount of damage received
     */
    @Override
    public void takeDamage(int dmg) {
        this.health -= dmg;
    }

    /** This method returns TRUE if the Sunflower is dead
     * 
     * @return true if health is 0 or less
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /** This method returns the Sunflower's health
     * 
     * @return current health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /** This method returns the cost of planting the Sunflower
     * 
     * @return sun cost
     */
    @Override
    public int getCost() {
        return cost;
    }

    /** This method returns the cooldown of planting the Sunflower
     * 
     * @return cooldown
     */
    @Override
    public int getCooldown() {
        return coolDown;
    }


}
