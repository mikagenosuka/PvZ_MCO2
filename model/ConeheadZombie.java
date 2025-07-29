package model;

/** The class Conehead Zombie represents an enemy
 * that is to be fought by the player. It
 * is slightly tankier than the average Zombie.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */

  public class ConeheadZombie extends Zombie {

    /**
     * This constructor initializes the ConeheadZombie
     * with higher health but standard behavior.
     *
     * @param row the row where the zombie spawns
     * @param col the column where the zombie spawns
     */
    public ConeheadZombie(int row, int col, GameModel model) {
        super(row, col, model);
        this.health = 120;  // Double the tankiness
        this.damage = 20;   // Standard damage
        this.speed = 6;     // Standard movement
    }

    /**
     * Returns the type of this zombie.
     *
     * @return "Conehead Zombie"
     */
    @Override
    public String getType() {
        return "Conehead Zombie";
    }
}
