package model;

/** The class Buckethead Zombie represents an enemy
 * that is to be fought by the player. It
 * is the tankiest enemy.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */
public class BucketheadZombie extends Zombie {

    /**
     * This constructor initializes the BucketheadZombie
     * with high durability and standard attack behavior.
     *
     * @param row the row where the zombie spawns
     * @param col the column where the zombie spawns
     */
    public BucketheadZombie(int row, int col, GameModel model) {
        super(row, col, model);
        this.health = 160;  // Highest durability among basic zombies
        this.damage = 20;   // Standard damage
        this.speed = 6;     // Moves every 2 ticks
    }

    /**
     * Returns the type of this zombie.
     *
     * @return "Buckethead Zombie"
     */
    @Override
    public String getType() {
        return "Buckethead Zombie";
    }
}

