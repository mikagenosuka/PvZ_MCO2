package model;

import java.util.List;

/** The abstract Peashooter represents the superclass at which all
 * peashooter variations derives from
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.3
  */
  

public abstract class Peashooter extends Plant {
    protected int attackCooldown = 0; // Timer until next attack

    /**
     * This constructor initializes the Peashooter and sets its
     * attributes according to base values for all Peashooter variants.
     * 
     * @param row the row where the plant is placed
     * @param col the column where the plant is placed
     */
    public Peashooter(int row, int col) {
        super(row, col);

        // Spec-mandated plant attributes
        this.health = 100;
        this.damage = 20;
        this.directDamage = 20; // same as base damage
        this.regenerateRate = 8;
        this.range = 10; // 10 Tiles
        this.speed = 2; // cooldown time in ticks
    }

    /* Behaviors */

    /** This method identifies the nearest Zombie
     * in front of the Peashooter on the same row
     * 
     * @param model the GameModel to scan
     * @return the first Zombie found, or null if none
     */
    protected Zombie identifyZombie(GameModel model) {
    for (int c = col + 1; c < model.getCOLS(); c++) { 
        Tile tile = model.getTile(row, c);
        if (tile == null) continue;

        List<Zombie> zombies = tile.getZombies();
        if (zombies != null && !zombies.isEmpty()) {
            System.out.println("Peashooter sees zombie at (" + row + ", " + c + ")");
            return zombies.get(0);
        } else {
            System.out.println("No zombie at (" + row + ", " + c + ")");
        }
    }
    return null;
}



    /** This method performs the attack of the Peashooter
     * if not on cooldown and a Zombie is in range
     * 
     * @param model the GameModel being interacted with
     */
    @Override
    public final void performAction(GameModel model) {
        System.out.println("Peashooter at (" + row + "," + col + ") acting...");
        if (attackCooldown > 0) {
            attackCooldown--;
            return;
        }

        Zombie target = identifyZombie(model);
        if (target != null) {
            fireProjectile(model); // subclass decides what to shoot
            attackCooldown = speed; // speed is cooldown between attacks
        }
    }

    /** This method fires the specific projectile
     * and is implemented by the child class
     * 
     * @param model the GameModel to apply projectile to
     */
    protected abstract void fireProjectile(GameModel model);

    /** This method reduces the Peashooter's health
     * based on incoming damage from a Zombie
     * 
     * @param dmg amount of damage taken
     */
    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
        System.out.println("Shooter at (" + row + "," + col + ") took " + damage + " damage. HP: " + health);

        if (health <= 0) {
            this.isDead = true; // or however you're marking plants dead
            System.out.println("Shooter at (" + row + "," + col + ") has died.");
        }
    }


    /** This method checks if the Peashooter has died
     * 
     * @return TRUE if health is 0 or less
     */
    @Override
    public boolean isDead() {
        return health <= 0;
    }

    /** This method returns the Peashooter's health
     * 
     * @return current health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /** This method returns the Peashooter's cost
     * 
     * @return sun cost
     */
    @Override
    public int getCost() {
        return cost;
    }

    /** This method returns the cooldown of planting the Peashooter
     * 
     * @return cooldown
     */
    @Override
    public int getCooldown() {
        return coolDown;
    }
}
