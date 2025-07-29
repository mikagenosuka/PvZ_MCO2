package model;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    private Plant plant;
    private final List<Zombie> zombies;

    public Tile() {
        this.plant = null;
        this.zombies = new ArrayList<>();
    }

    

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public boolean hasPlant() {
        return plant != null && !plant.isDead();
    }

    public void removePlant() {
        this.plant = null;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public void addZombie(Zombie z) {
        zombies.add(z);
    }

    public void removeZombie(Zombie z) {
        zombies.remove(z);
    }

    public boolean hasZombies() {
        return !zombies.isEmpty();
    }
}