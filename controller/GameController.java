package controller;

import model.*;
import view.GameView;
import view.LevelSelectView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameController {
    private GameModel model;
    private GameView view;
    private static GameController instance;
    private String selectedPlantType = null;
    private Map<String, Integer> plantCooldowns = new HashMap<>();
    // Timer attributes
    private Timer gameTimer;
    private int elapsedSeconds = 0;
    private final int LEVEL_DURATION = 180;


    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;
        GameController.instance = this;

        // Set up listeners for plant buttons
        setupPlantButtonListeners();

        // Set up listeners for each tile
        setupTileClickListeners();

        // Initialize cooldowns
        plantCooldowns.put("Sunflower", 0);
        plantCooldowns.put("NormalPeashooter", 0);
        plantCooldowns.put("Wallnut", 0);
        plantCooldowns.put("SnowPeashooter", 0);
        plantCooldowns.put("Cherrybomb", 0);

        // Setup Game Over buttons ONCE
        view.addBackListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
            frame.dispose(); // Close the current GameView window

            // Open the LevelSelectView again
            SwingUtilities.invokeLater(() -> {
                new LevelSelectView(levelEvent -> {
                    int selectedLevel = Integer.parseInt(levelEvent.getActionCommand());

                    GameModel newModel = new GameModel(selectedLevel);       
                    GameView newView = new GameView(newModel);
                    newModel.setView(newView);
                    newView.setModel(newModel);
                    new GameController(newModel, newView);                   
                });
            });
        });




        view.addRetryListener(e -> {
            int currentLevel = model.getCurrentLevel();
            startLevel(currentLevel); // Restart current level
        });

        // Start cooldown updater
        new Timer(1000, e -> {
            for (Map.Entry<String, Integer> entry : plantCooldowns.entrySet()) {
                String type = entry.getKey();
                int remaining = entry.getValue();
                if (remaining > 0) {
                    plantCooldowns.put(type, remaining - 1);
                }
            }
            view.highlightReadyPlants(getReadyPlants());
        }).start();

        // Start first level
        startLevel(model.getCurrentLevel());
    }


    private void startLevel(int level) {
        model.setCurrentLevel(level);
        model.reset(); // Clear plants/zombies/time based on level
        elapsedSeconds = 0;

        // Reset view
        view.resetBoard(); 
        view.hideGameOver(); 
        view.updateSunCount(model.getSun());
        view.updateTime(0);
        view.updateProgress(0);

        // (Optional) Reset all cooldowns
        for (String key : plantCooldowns.keySet()) {
            plantCooldowns.put(key, 0);
        }

        gameTimer = new Timer(1000, e -> {
            if (model.checkGameOver()) {
                gameTimer.stop();
                boolean victory = model.isVictory();
                int lvl = model.getCurrentLevel();
                view.showGameOver(victory, lvl);
                return;
            }

            elapsedSeconds++;
            view.updateTime(elapsedSeconds);
            model.gameTick(elapsedSeconds);

            int progressPercent = (int) ((elapsedSeconds / (double) LEVEL_DURATION) * 100);
            view.updateProgress(progressPercent);
        });

        gameTimer.start();
    }



    private void setupPlantButtonListeners() {
        String[] plantTypes = {
            "Sunflower", "NormalPeashooter", "Wallnut",
            "SnowPeashooter", "Cherrybomb", "Shovel"
        };

        for (String type : plantTypes) {
            JButton btn = view.getPlantButton(type);
            if (btn != null) {
                btn.addActionListener(e -> {
                    selectedPlantType = type;
                });
            }
        }
    }

    private void setupTileClickListeners() {
        JPanel board = view.getBoardPanel();
        int cols = model.getCOLS();

        Component[] tiles = board.getComponents();
        for (int i = 0; i < tiles.length; i++) {
            final int row = i / cols;
            final int col = i % cols;

            tiles[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedPlantType != null) {
                        handlePlantPlacement(row, col);
                    }
                }
            });
        }
    }

    private void handlePlantPlacement(int row, int col) {
        if (selectedPlantType == null) return;

        Plant plant = null;

        switch (selectedPlantType) {
            case "Sunflower":
                plant = new Sunflower(row, col);
                break;
            case "NormalPeashooter":
                plant = new NormalPeashooter(row, col);
                break;
            case "Wallnut":
                plant = new Wallnut(row, col);
                break;
            case "SnowPeashooter":
                plant = new SnowPeashooter(row, col);
                break;
            case "Cherrybomb":
                plant = new Cherrybomb(row, col);
                break;
            case "Shovel":
                boolean removed = model.removePlant(row, col);
                if (removed) {
                    view.removePlant(row, col);
                    System.out.println("Removed plant at " + row + ", " + col);
                } else {
                    System.out.println("No plant to remove at " + row + ", " + col);
                }
                selectedPlantType = null;
                return;
            default:
                System.out.println("Unknown plant type: " + selectedPlantType);
                return;
        }

        boolean success = model.placePlant(plant, row, col, model.getCurrentTime());

        if (success) {
            System.out.println("Planted " + selectedPlantType + " at " + row + ", " + col);
            view.displayPlantImage(plant, row, col);
            view.updateSunCount(model.getSun());

            // Start cooldown for this plant type
            startPlantCooldown(selectedPlantType);

            // Update plant button highlights based on readiness
            view.highlightReadyPlants(getReadyPlants());

            selectedPlantType = null;
        } else {
            System.out.println("Failed to plant " + selectedPlantType + " (insufficient sun / on cd)");
        }
    }

    // Helper function for cooldown highlight
    private void startPlantCooldown(String plantType) {
        int cooldownSeconds = switch (plantType) {
            case "Sunflower" -> 7;
            case "NormalPeashooter" -> 7;
            case "Wallnut" -> 30;
            case "SnowPeashooter" -> 7;
            case "Cherrybomb" -> 50;
            default -> 0;
        };

        // 1000ms -> 1 tick/s
        int cooldownTicks = cooldownSeconds;

        plantCooldowns.put(plantType, cooldownTicks);
    }

    private Set<String> getReadyPlants() {
        Set<String> readyPlants = new HashSet<>();

        for (Map.Entry<String, Integer> entry : plantCooldowns.entrySet()) {
            if (entry.getValue() <= 0) {
                readyPlants.add(entry.getKey());
            }
        }

        return readyPlants;
    }




    public static GameController getInstance() {
        return instance;
    }

    // getters
    public GameView getView() { return view; }
    public GameModel getModel() { return model; }
    
}
