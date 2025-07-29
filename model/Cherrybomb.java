package model;

import java.util.List;

/** The class Cherrybomb represents a plant 
 * that can be planted by the player. 
 * One time use.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.3
  */

public class Cherrybomb extends Plant {

    private boolean exploded = false;  // Tracks whether the bomb has gone off
    private int fuse = 1;              // Fuse countdown in ticks

    /** This constructor initializes the CherryBomb
     * and sets all required plant attributes
     * 
     * @param row the row where the plant is placed
     * @param col the column where the plant is placed
     */
    public Cherrybomb(int row, int col) {
        super(row, col);

        // Required attributes (even if not fully used)
        this.cost = 150;
        this.regenerateRate = 10;
        this.damage = 180;         // Edge damage
        this.directDamage = 250;  // Direct Damage on center tile
        this.health = 999;         // Not meant to be attacked
        this.range = 1;            // Affects adjacent tiles (1 tile radius = 3×3)
        this.speed = 0;            // One time use
        this.coolDown = 35;
    }

    /** This method performs CherryBomb’s explosion
     * after a short fuse. It damages all zombies in
     * a 3×3 radius and then self-destructs. It deals 
     * more damage to the tile that it is planted on.
     * 
     * @param model the GameModel to interact with
     */
    @Override
    public void performAction(GameModel model) {
        if (exploded) return;

        if (fuse > 0) {
            fuse--;
        } else {
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    if (model.inBounds(r, c)) {
                        List<Zombie> zombies = model.getTile(r, c).getZombies();
                        for (Zombie z : zombies) {
                            int dmg = (r == row && c == col) ? directDamage : damage;
                            z.takeDamage(dmg);
                            System.out.println("CherryBomb damaged zombie at (" + r + "," + c + ") for " + dmg + " damage.");
                        }
                    }
                }
            }

            // KABOOM !!!
            if (model.getView() != null) {
                model.getView().showBoomEffect(row, col); 
            }

            exploded = true;
        }
    }



    /** This method ignores damage since CherryBomb
     * is not meant to be attacked.
     * 
     * @param dmg damage dealt (ignored)
     */
    @Override
    public void takeDamage(int dmg) {
        // Not needed
    }

    /** This method checks if the CherryBomb
     * has already exploded and should be removed
     * 
     * @return TRUE if bomb exploded
     */
    @Override
    public boolean isDead() {
        return exploded;
    }

    /** This method returns the health of the CherryBomb
     * (meaningless since it doesn't get attacked)
     * 
     * @return high constant health
     */
    @Override
    public int getHealth() {
        return health;
    }

    /** This method returns the CherryBomb’s cost
     * 
     * @return 150 sun cost
     */
    @Override
    public int getCost() {
        return cost;
    }

    /** This method returns the cooldown of planting the Cherrybomb
     * 
     * @return cooldown
     */
    @Override
    public int getCooldown() {
        return coolDown;
    }

}
