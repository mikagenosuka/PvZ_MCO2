package model;

import java.util.List;

/** The class SnowPea represents a projectile fired by a Snow Peashooter.
  *
  * @author Darryl Ac-ac & Brian Garcia
  * @version 1.2
  */


public class SnowPea extends Projectile {
    private final int damage;         // normal damage
    private final int directDamage;   // if zombie is within 2 tiles

    public SnowPea(int row, int col, int damage, int directDamage) {
        super(row, col);
        this.damage = damage;
        this.directDamage = directDamage;
    }

    @Override
    protected void onHit(Zombie zombie) {
        int distance = zombie.getCol() - this.col;
        int finalDamage = (distance <= 2) ? directDamage : damage;

        zombie.takeDamage(finalDamage);
        zombie.applySlow();

        System.out.println("SnowPea hit zombie at (" + row + "," + col + 
                           ") for " + finalDamage + " damage (slowed).");
    }
}
