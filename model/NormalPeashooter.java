package model;

import controller.GameController;

/** The subclass NormalPeashooter represents a plant
 * that can be planted by the player
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.3
  */


public class NormalPeashooter extends Peashooter {

    /** This constructor initializes the NormalPeashooter
     * with its default attributes inherited from Peashooter
     *
     * @param row row where the plant is placed
     * @param col column where the plant is placed
     */
    public NormalPeashooter(int row, int col) {
        super(row, col);

        // You may override attributes here if different from base Peashooter
        this.cost = 100;
        this.damage = 20;
        this.directDamage = 20;
        this.regenerateRate = 7;
        this.range = 10;
        this.health = 100;
        this.speed = 3;
        this.coolDown = 8;
    }

    /** This method fires a normal pea
     * at the Zombie using GameModel logic
     * 
     * @param model the GameModel in which to add the projectile
     */
    @Override
    protected void fireProjectile(GameModel model) {
        Projectile proj = new Pea(row, col + 1);
        model.addProjectile(proj);
        GameController.getInstance().getView().displayProjectile(proj);
        System.out.println("Normal Peashooter at (" + row + "," + col + ") fired a pea.");
    }


    /** This method returns the sun cost
     * required to place the plant
     * 
     * @return 100 sun cost
     */
    @Override
    public int getCost() {
        return cost;
    }


}
