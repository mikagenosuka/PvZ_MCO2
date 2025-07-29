package model;

/** The class Zombie represents an enemy
 * that is to be fought by the player. It
 * is the standard enemy.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */

  public class NormalZombie extends Zombie {

    /**
     * This constructor initializes the NormalZombie
     * and sets all base stat values.
     *S
     * @param row the row where the zombie spawns
     * @param col the column where the zombie spawns
     */
    public NormalZombie(int row, int col, GameModel model) {
        super(row, col, model);
        this.health = 80;  // Standard health
        this.damage = 20;   // Standard damage
        this.speed = 6;     // Average speed
        System.out.println(getType() + " speed: " + this.speed);
    }

    /**
     * Returns the type of this zombie.
     *
     * @return "Normal Zombie"
     */
    @Override
    public String getType() {
        return "Normal Zombie";
    }
}
