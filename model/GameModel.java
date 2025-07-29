package model;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import view.GameView;

import java.util.HashMap;
import java.util.List;

public class GameModel {

    private GameView view;

    public static int curr_ROWS;
    public static int curr_COLS;

    private Tile[][] board;
    private ArrayList<Zombie> zombies = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<SunToken> sunTokens = new ArrayList<>();
    private ArrayList<Plant> plants = new ArrayList<>();

    // Plant cooldowns
    private Map<String, Integer> plantCooldowns = new HashMap<>();

    private int sunCount = 50;
    private int lastSunGeneratedTime = -99; // Initiated by function later on
    private int timeRemaining = 180;
    private int currentTime = 0; // Needed for cooldown logic & controller
    private boolean gameOver = false;
    private boolean victory = false;
    private int currentLevel = 1;
    // Set board size based on stage number; Initialize cooldowns
    public GameModel(int stage) {
        switch (stage) {
            case 1:
                board = new Tile[5][10];
                setCurrentLevel(1);
                curr_ROWS = 5;
                curr_COLS = 10;
                break;
            case 2:
                board = new Tile[6][9];
                setCurrentLevel(2);
                curr_ROWS = 6;
                curr_COLS = 9;
                break;
            case 3:
                board = new Tile[6][10];
                setCurrentLevel(3);
                curr_ROWS = 6;
                curr_COLS = 10;
                break;
            default:
                break;
        }

        
        for (int r = 0; r < curr_ROWS; r++) {
            for (int c = 0; c < curr_COLS; c++) {
                board[r][c] = new Tile();
            }
        }

        plantCooldowns.put("Sunflower", 0);
        plantCooldowns.put("Peashooter", 0);
        plantCooldowns.put("SnowPea", 0);
        plantCooldowns.put("Cherrybomb", 0);
        plantCooldowns.put("Wallnut", 0);
    }

    /* Return View */
    public GameView getView(){
        return this.view;
    }

    /* Return Tile */
    public Tile getTile(int row, int col) {
        if (row < 0 || row >= curr_ROWS || col < 0 || col >= curr_COLS) {
            return null;
        }
        return board[row][col];
    }

    /* Return Columns*/
    public int getCOLS() {
        return board[0].length; 
    }

    /* Return Rows */
    public int getROWS() {
        return board.length;
    }

    /* Return zombies */
    public List<Zombie> getZombies() {
        return zombies;
    }

    /* Return replanting cooldowns */
    public int getPlantCooldownTicks(String plantType) {
        switch (plantType) {
            case "Sunflower": return 8;
            case "Peashooter": return 8;
            case "Wallnut": return 15;
            case "SnowPeashooter": return 8;
            case "Cherrybomb": return 35;
            case "Potatomine": return 30;
            default: return 0;
        }
    }

    /* Return plant cost */
    public int getPlantCost(String plantType) {
        switch (plantType) {
            case "Sunflower":
                return 50;
            case "NormalPeashooter":
                return 100;
            case "Wallnut":
                return 50;
            case "SnowPeashooter":
                return 175;
            case "Cherrybomb":
                return 150;
            case "Shovel":
                return 0; // no cost
            default:
                return -1; // unknown plant
        }
    }


    /* Set view */
    public void setView(GameView view) {
        this.view = view;
    }

    public void addProjectile(Projectile proj) {
        projectiles.add(proj);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }


    /* Check if inrange of Cherrybomb */
    public boolean inBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    /* Getter for sunTokens to be drawn */
    public List<SunToken> getSunTokens() {
        return sunTokens;
    }

    public int getCurrentTime(){
        return currentTime;
    }

    public int getSun(){
        return sunCount;
    }





    /* Collecting Sun */
    public void collectSunAt(int mouseX, int mouseY) {
        for (int i = 0; i < sunTokens.size(); i++) {
            SunToken token = sunTokens.get(i);
            if (!token.isCollected() && token.getBounds().contains(mouseX, mouseY)) {
                sunCount += token.getValue();
                token.collect();
                sunTokens.remove(i); // remove from list after collection
                break;
            }
        }
    }

    /* Add sun */
    public void addSun(int sunAdded){
        sunCount += 25;
    }


    /* Generated sun on the board */
    public void generateSun() {
        if (timeRemaining % 12 == 0 && timeRemaining != lastSunGeneratedTime) {
            int row = (int)(Math.random() * getROWS());
            int col = (int)(Math.random() * getCOLS());
            SunToken sun = new SunToken(row, col);

            sunTokens.add(sun);
            lastSunGeneratedTime = timeRemaining;

            if (view != null) {
                view.showSunToken(sun.getRow(), sun.getCol());
            } 
        }
    }


    /* Spawn sun for sunflowers */
    public void spawnSunToken(SunToken gridToken) {
        int x = gridToken.getCol();
        int y = gridToken.getRow();

        gridToken.setPosition(x, y);
        sunTokens.add(gridToken);
        view.showSunToken(y, x); // Fix: pass row first, then col
    }



    /* Check if a plant exists on a tile */
    public Plant getPlantAt(int row, int col) {
        for (Plant p : plants) {
            if (p.getRow() == row && p.getCol() == col) {
                return p;
            }
        }
        return null;
    }

    /* Check if plant can be placed */
    public boolean canPlacePlant(String type, int currentTime) {
        int nextAvailable = plantCooldowns.getOrDefault(type, 0); // Check map created for curr cd
        return currentTime >= nextAvailable;
    }

    /* Placing plant logic */
    // Manual placement is left to the controller
    public boolean placePlant(Plant p, int row, int col, int currentTime) {
        String type = p.getClass().getSimpleName(); 

        if (getPlantAt(row, col) != null) return false;
        if (sunCount < p.getCost()) return false;

        int nextAvailable = plantCooldowns.getOrDefault(type, 0);
        if (currentTime < nextAvailable) {
            System.out.println(type + " is cooling down! Wait " + (nextAvailable - currentTime) + " more ticks.");
            return false;
        }

        plants.add(p);
        sunCount -= p.getCost();

        // Set plant on tile (so zombie sees it)
        if (board[row][col] != null) {
            board[row][col].setPlant(p); 
        }

        plantCooldowns.put(type, currentTime + p.getCooldown());

        System.out.println(type + " planted at (" + row + "," + col + "). Cooldown: " + p.getCooldown() + " ticks.");
        return true;
    }




    public boolean removePlant(int row, int col) {
        Iterator<Plant> iter = plants.iterator();
        while (iter.hasNext()) {
            Plant p = iter.next();
            if (p.getRow() == row && p.getCol() == col) {
                iter.remove(); // Safe removal during iteration
                System.out.println("Plant removed at [" + row + ", " + col + "]");

                if (board[row][col] != null) {
                    board[row][col].setPlant(null);
                }
                return true;
            }
        }

        System.out.println("No plant found at [" + row + ", " + col + "] to remove.");
        return false;
    }


    /* Run plants through their actions every tick */
    /* TICK METHOD */
    public void updatePlants() {
        Iterator<Plant> iter = plants.iterator();
        while (iter.hasNext()) {
            Plant p = iter.next();

            if (p.isDead()) {
                int row = p.getRow();
                int col = p.getCol();

                iter.remove(); // Remove from model list

                Tile tile = getTile(row, col);
                if (tile != null) {
                    tile.setPlant(null); // Remove from board
                }

                if (view != null) {
                    view.removePlant(row, col); // Also remove from GUI
                }

                System.out.println(p.getClass().getSimpleName() + " at (" + row + "," + col + ") died.");
            } else {
                p.performAction(this);
            }
        }
    }




    /* Zombie Spawn Logic */
    /* TICK METHOD */
    public void handleZombieSpawning(int time, ArrayList<Zombie> zombies) {
        if (time == 170) {
            int lane = (int)(Math.random() * getROWS());
            int tile = 9;
            FlagZombie flag = new FlagZombie(lane, tile, this);
            zombies.add(flag);
            getTile(lane, tile).addZombie(flag);
            view.displayZombieImage(flag, lane, getCOLS() - 1);
            System.out.println("Flag Zombie has appeared! [Lane: " + lane + ", Tile: " + tile + "]\n");
            return;
        }

        if (time == 171) {
            System.out.println("A huge wave of zombies is approaching!\n");

            int zombiesToSpawn;
            switch (currentLevel) {
                case 1: zombiesToSpawn = 5; break;
                case 2: zombiesToSpawn = 7; break;
                case 3: zombiesToSpawn = 9; break;
                default: zombiesToSpawn = 5;
            }

            for (int i = 0; i < zombiesToSpawn; i++) {
                int lane = (int)(Math.random() * getROWS());
                int tile = getCOLS() - 1;
                Zombie newZombie = getZombieForTime(time, lane, tile);
                zombies.add(newZombie);
                getTile(lane, tile).addZombie(newZombie);
                view.displayZombieImage(newZombie, lane, getCOLS() - 1);
                System.out.println(newZombie.getType() + " has appeared! [Lane: " + lane + ", Tile: " + tile + "]\n");
            }

            return;
        }

        // Normal spawn patterns
        boolean spawnZombie = false;
        if ((time >= 30 && time <= 80 && time % 10 == 0) ||
            (time >= 81 && time <= 140 && time % 5 == 0) ||
            (time >= 141 && time <= 170 && time % 3 == 0)) {
            spawnZombie = true;
        }

        if (spawnZombie) {
            int lane = (int)(Math.random() * getROWS());
            int tile = getCOLS() - 1;
            Zombie newZombie = getZombieForTime(time, lane, tile);
            zombies.add(newZombie);
            getTile(lane, tile).addZombie(newZombie);
            view.displayZombieImage(newZombie, lane, getCOLS() - 1);
            System.out.println(newZombie.getType() + " has appeared! [Lane: " + lane + ", Tile: " + tile + "]\n");
        }
    }




    // Helper method to choose zombie type based on current time
    private Zombie getZombieForTime(int time, int lane, int tile) {
        if (time < 141) {
            return new NormalZombie(lane, tile, this);
        }

        // 60% Normal 30% Conehead 10% Buckethead
        double r = Math.random();
        if (r < 0.5) {
            return new NormalZombie(lane, tile, this);
        } else if (r < 0.8) {
            return new ConeheadZombie(lane, tile, this);
        } else {
            return new BucketheadZombie(lane, tile, this);
        }
    }

    /* Run zombies through their action for every second/tick */
    /* TICK METHOD */
    public void updateZombies() {
        Iterator<Zombie> it = zombies.iterator();
        while (it.hasNext()) {
            Zombie z = it.next();
            int zombieCurrentCol = z.getCol();

            if (z.isDead()) {
                it.remove(); // Safe removal
                view.removeZombieAt(z.getRow(), z.getCol());
            } else {
                z.performAction(this);
                if (zombieCurrentCol != z.getCol()){
                    view.animateZombieMove(z);
                }
            }
        }
    }

    /* Check if zombie and projectile collide */
    public Zombie getZombieAtPixel(int row, int pixelX) {
        for (Zombie z : zombies) {
            if (z.getRow() == row) {
                int zombieX = z.getCol() * 100; 
                if (Math.abs(zombieX - pixelX) < 30) {
                    return z;
                }
            }
        }
        return null;
    }



    /* Check Game Over cons; No time / Zombie wins */
    /* TICK METHOD */
    public boolean checkGameOver() {
        for (Zombie z : zombies) {
            if (!z.isDead() && z.getCol() == 0 && z.getX() <= -z.getWidth()) {
                gameOver = true;
                victory = false;
                System.out.println("A zombie reached the house. Game Over!");
                return true;
            }
        }

        if (timeRemaining <= 0) {
            gameOver = true;
            victory = true;
            System.out.println("Time has run out! You win!");
            return true;
        }

        return false;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }



    /* PROJECTILE METHODS */

    /* Handles Projctile Movement */
    /* TICK METHOD */
    public void moveProjectiles() {
    for (int i = 0; i < projectiles.size(); i++) {
        Projectile proj = projectiles.get(i);
        proj.move(this); // Pea or SnowPea logic is handled in their own move() methods

        if (!proj.isActive()) {
            projectiles.remove(i);
            i--;
        }
    }
}


    public void gameTick(int elapsedSeconds) {
        if (gameOver) return;
        currentTime++;
        timeRemaining--;
        // Generate passive sun
        generateSun();
        // Let each plant perform its action
        updatePlants();
        // Move projectiles and handle collisions
        moveProjectiles();
        // Handle zombie spawning and updating
        handleZombieSpawning(currentTime, zombies);
        // Let zombies perform their actions 
        updateZombies(); 
        // Check for game over condition (either win or lose)
        checkGameOver();


    }

    public void reset() {
        plants.clear();
        zombies.clear();
        sunCount = 50;
        currentTime = 0;
    }


    

}
