package model;

import java.util.List;

/** The class Pea represents a projectile fired by a Normal Peashooter.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.1
  */


public class Pea extends Projectile {
    private static final int DAMAGE = 20;

    public Pea(int row, int col) {
        super(row, col);
    }

    @Override
    protected void onHit(Zombie zombie) {
        zombie.takeDamage(DAMAGE);
        System.out.println("Pea hit zombie at (" + row + "," + col + ") for " + DAMAGE + " damage.");
    }
}
