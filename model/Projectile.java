package model;

import java.util.List;

public abstract class Projectile {
    protected int row;
    protected int col;
    protected boolean active = true;

    public Projectile(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isActive() {
        return active;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        if (active) {
            this.col = col;
        }
    }


    /** Moves the projectile forward (right), checks for collision and applies effects. */
    public void move(GameModel model) {
        if (!active) return;

        col++; // move right

        if (col >= model.getCOLS()) {
            active = false;
            return;
        }

        Tile tile = model.getTile(row, col);
        List<Zombie> zombies = tile.getZombies();

        if (!zombies.isEmpty()) {
            onHit(zombies.get(0));
            active = false;
        }
    }

    /** Abstract method to define what happens when a zombie is hit. */
    protected abstract void onHit(Zombie zombie);
}
