package view;



import javax.swing.*;
import java.awt.*;
import java.net.URL;
import model.GameModel;
import model.Plant;
import model.Projectile;
import model.SnowPea;
import model.Zombie;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameView extends JFrame{
    private GameModel model;
    private JPanel boardPanel;
    private JLabel sunCounter;
    private JPanel topPanel;
    private JLabel timeLabel;
    private JProgressBar progressBar;
    private HashMap<String, JButton> plantButtons;
    private JLayeredPane[][] tilePanels;
    private ImageIcon sunIcon;
    private Map<Zombie, JLabel> zombieLabels = new HashMap<>();
    private JLabel[][] plantLabels;
    private JLayeredPane zombieLayer;
    private JLayeredPane projectileLayer;
    private JPanel gameOverPanel;
    private JLabel gameOverLabel;
    private JButton backButton;
    private JLayeredPane layeredPane;

        public GameView(GameModel model) {
            this.model = model;

            int rows = model.getROWS();
            int cols = model.getCOLS();
            plantLabels = new JLabel[rows][cols];

            setTitle("Plants vs. Zombies");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Load sun icon
            URL url = getClass().getResource("/resources/sun.png");
            if (url == null) {
                System.err.println("Sun icon not found");
            } else {
                Image original = new ImageIcon(url).getImage();
                Image scaled = original.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                sunIcon = new ImageIcon(scaled);
            }

            // ==== TOP PANEL  ====
            topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.setBackground(new Color(180, 140, 90)); // PvZ-like brown color

            plantButtons = new HashMap<>();
            String[] plantTypes = {
                "Sunflower", "NormalPeashooter", "Wallnut",
                "SnowPeashooter", "Cherrybomb", "Shovel"
            };

            for (String type : plantTypes) {
                String imagePath = "/resources/" + type.toLowerCase() + ".png";
                URL iconUrl = getClass().getResource(imagePath);

                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(60, 60));
                btn.setBackground(new Color(210, 170, 110));
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                btn.setFocusPainted(false);
                btn.setActionCommand(type);

                if (iconUrl != null) {
                    ImageIcon icon = new ImageIcon(iconUrl);
                    Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(scaled));
                } else {
                    btn.setText(type);
                }

                // Get plant cost from model
                int cost = model.getPlantCost(type);
                JLabel costLabel = new JLabel(cost + " Sun", SwingConstants.CENTER);
                costLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
                costLabel.setForeground(Color.WHITE);
                costLabel.setPreferredSize(new Dimension(60, 15));

                // Panel with vertical layout (button + cost label)
                JPanel plantPanel = new JPanel();
                plantPanel.setPreferredSize(new Dimension(60, 80));
                plantPanel.setLayout(new BorderLayout());
                plantPanel.setOpaque(false);

                plantPanel.add(btn, BorderLayout.CENTER);
                plantPanel.add(costLabel, BorderLayout.SOUTH);

                plantButtons.put(type, btn);
                topPanel.add(plantPanel);
            }


            // === HUD Info ===
            sunCounter = new JLabel("Sun: 50");
            sunCounter.setFont(new Font("Verdana", Font.BOLD, 16));
            sunCounter.setForeground(Color.WHITE);

            timeLabel = new JLabel("Time: 0s");
            timeLabel.setFont(new Font("Verdana", Font.BOLD, 16));
            timeLabel.setForeground(Color.WHITE);

            progressBar = new JProgressBar(0, 100);
            progressBar.setPreferredSize(new Dimension(200, 20));
            progressBar.setForeground(Color.GREEN);
            progressBar.setBackground(new Color(100, 80, 50));
            progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            progressBar.setStringPainted(true);

            // Add components to panel
            topPanel.add(Box.createHorizontalStrut(20));
            topPanel.add(sunCounter);
            topPanel.add(Box.createHorizontalStrut(20));
            topPanel.add(timeLabel);
            topPanel.add(Box.createHorizontalStrut(20));
            topPanel.add(progressBar);

            add(topPanel, BorderLayout.NORTH);

            // ==== GAME BOARD ====
        boardPanel = new JPanel(new GridLayout(rows, cols));
        tilePanels = new JLayeredPane[rows][cols];

        Color[] dayShades = {
            new Color(96, 156, 83),   // Dark green
            new Color(108, 170, 96),  // Medium green
            new Color(124, 185, 106)  // Light green
        };

        Color[] nightShades = {
            new Color(30, 45, 60),    // Dark blue 1
            new Color(35, 50, 70),    // Dark blue 2
            new Color(40, 55, 75)     // Dark blue 3
        };

        Color[] roofShades = {
            new Color(150, 90, 60),   // Light brown
            new Color(160, 100, 70),  // Medium brown
            new Color(170, 110, 80)   // Darker brown
        };

        boolean isNight = model.getCurrentLevel() == 2;
        boolean isRoof = model.getCurrentLevel() == 3;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JPanel tileContainer = new JPanel(new BorderLayout());

                JLayeredPane layeredTile = new JLayeredPane();
                layeredTile.setPreferredSize(new Dimension(100, 100));
                layeredTile.setLayout(null);

                // === BACKGROUND/GAMEBOARD ===
                JPanel bg = new JPanel();
                int shadeIndex = (r + c) % 3;
                if (model.getCurrentLevel() == 1) {
                    bg.setBackground(dayShades[shadeIndex]);
                } else if (isNight) {
                    bg.setBackground(nightShades[shadeIndex]);
                } else if (isRoof) {
                    bg.setBackground(roofShades[shadeIndex]);
                } else {
                    bg.setBackground(Color.GRAY);
                }

                bg.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                bg.setBounds(0, 0, 100, 100);
                layeredTile.add(bg, Integer.valueOf(0));

                // === PLANT LAYER ===
                JLabel plantLabel = new JLabel();
                plantLabel.setBounds(0, 0, 100, 100);
                layeredTile.add(plantLabel, Integer.valueOf(1));
                plantLabels[r][c] = plantLabel;

                tilePanels[r][c] = layeredTile;
                tileContainer.add(layeredTile, BorderLayout.CENTER);
                boardPanel.add(tileContainer);
            }
        }


            // ==== ZOMBIE LAYER ====
            int tileWidth = cols * 100;
            int tileHeight = rows * 100;

            zombieLayer = new JLayeredPane();
            zombieLayer.setOpaque(false);
            zombieLayer.setBounds(0, 0, tileWidth, tileHeight);
            zombieLayer.setLayout(null);

            // ==== PROJECTILE LAYER ====
            projectileLayer = new JLayeredPane();
            projectileLayer.setOpaque(false);
            projectileLayer.setBounds(0, 0, tileWidth, tileHeight);
            projectileLayer.setLayout(null);

            // ==== LAYERED PANE WRAPPER ====
            layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(tileWidth, tileHeight));

            boardPanel.setBounds(0, 0, tileWidth, tileHeight);
            layeredPane.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(projectileLayer, JLayeredPane.PALETTE_LAYER);
            layeredPane.add(zombieLayer, JLayeredPane.PALETTE_LAYER);

            add(layeredPane, BorderLayout.CENTER);

            // ==== GAME OVER ====
            gameOverPanel = new JPanel();
            gameOverPanel.setOpaque(false);
            gameOverPanel.setBackground(new Color(0, 0, 0, 180)); // semi-transparent black
            gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
            gameOverPanel.setBounds(0, 0, tileWidth, tileHeight);
            gameOverPanel.setVisible(false); // hidden by default

            gameOverLabel = new JLabel("", SwingConstants.CENTER);
            gameOverLabel.setFont(new Font("Verdana", Font.BOLD, 48));
            gameOverLabel.setForeground(Color.WHITE);
            gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            backButton = new JButton("Back to Menu"); 
            backButton.setAlignmentX(Component.CENTER_ALIGNMENT); 
            backButton.setVisible(false); 

            gameOverPanel.add(Box.createVerticalGlue());
            gameOverPanel.add(gameOverLabel);
            gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            gameOverPanel.add(backButton); 
            gameOverPanel.add(Box.createVerticalGlue());

            layeredPane.add(gameOverPanel, JLayeredPane.DRAG_LAYER);

            // ==== FINAL SETTINGS ====
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }







    // Get Zombie layer
    public JLayeredPane getZombieLayer() {
        return zombieLayer;
    }


    // Updates the sun count to be displayed
    public void updateSunCount(int sun) {
        sunCounter.setText("Sun: " + sun);
    }

    // Allows the view to be passed later
    public void setModel(GameModel model) {
        this.model = model;
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    public JButton getPlantButton(String plantType) {
        return plantButtons.get(plantType);
    }
    // Updates current time for timer to be displayed
    public void updateTime(int seconds) {
        timeLabel.setText("Time: " + seconds + "s");
    }
    // Update wave progress bar
    public void updateProgress(int percent) {
        progressBar.setValue(percent);
    }
    // Gets current tile to display a sprite
    public JPanel getTileAt(int row, int col) {
        int index = row * col + col;
        return (JPanel) boardPanel.getComponent(index);
    }

    // Highlight when plants can be replanted
    public void highlightReadyPlants(Set<String> readyPlants) {
        for (Map.Entry<String, JButton> entry : plantButtons.entrySet()) {
            String plantType = entry.getKey();
            JButton button = entry.getValue();

            if (readyPlants.contains(plantType)) {
                // Add glowing yellow highlight
                button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            } else {
                // Revert to normal border
                button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        }
    }


    // Handles showing sun tokens
    public void showSunToken(int row, int col) {
        if (sunIcon == null) {
            System.err.println("Sun icon is null!");
            return;
        }

        JButton sunButton = new JButton(sunIcon);
        sunButton.setOpaque(false);
        sunButton.setContentAreaFilled(false);
        sunButton.setBorderPainted(false);
        sunButton.setFocusPainted(false);

        sunButton.setBounds(-25, -25, 100, 100); // Position and size within tile

        sunButton.addActionListener(e -> {
            JLayeredPane tile = tilePanels[row][col];
            tile.remove(sunButton);
            tile.revalidate();
            tile.repaint();

            model.addSun(25);
            updateSunCount(model.getSun());
        });

        JLayeredPane tile = tilePanels[row][col];
        tile.add(sunButton, Integer.valueOf(100)); // ← Use a higher layer to ensure it appears above plants
        tile.revalidate();
        tile.repaint();

        // Expires after 8 seconds
        new Timer(8000, e -> {
            tile.remove(sunButton);
            tile.revalidate();
            tile.repaint();
        }) {{
            setRepeats(false);
            start();
        }};
    }



    // Handles adding plant images
    public void displayPlantImage(Plant plant, int row, int col) {
        String imagePath = "/resources/" + plant.getClass().getSimpleName().toLowerCase() + ".png";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            System.err.println("Could not load plant image: " + imagePath);
            return;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);

        JLabel label = new JLabel(icon);
        label.setBounds(20, 20, 80, 80);

        JLayeredPane tile = tilePanels[row][col];
        tile.add(label, Integer.valueOf(2)); // Plant layer
        tile.revalidate();
        tile.repaint();

        // Save the label reference for safe removal later
        plantLabels[row][col] = label;
    }


    public void removePlant(int row, int col) {
        JLabel plantLabel = plantLabels[row][col];
        if (plantLabel != null) {
            JLayeredPane tile = tilePanels[row][col];
            tile.remove(plantLabel);
            tile.revalidate();
            tile.repaint();
            plantLabels[row][col] = null; // Clear reference
        }
    }


    // Handles removing plant images
    public void clearTile(int row, int col) {
        int cols = ((GridLayout) boardPanel.getLayout()).getColumns();
        int index = row * cols + col;
        Component tile = boardPanel.getComponent(index);

        if (tile instanceof JPanel panel) {
            panel.removeAll();            
            panel.revalidate();        
            panel.repaint();               
        }
    }

    public void displayZombieImage(Zombie zombie, int row, int col) {
        // Remove old label if exists
        JLabel oldLabel = zombieLabels.get(zombie);
        if (oldLabel != null) {
            zombieLayer.remove(oldLabel);
            zombieLabels.remove(zombie); 
        }

        String imagePath = "/resources/" + zombie.getType().toLowerCase() + ".png";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl == null) {
            System.err.println("Could not load zombie image: " + imagePath);
            return;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled));
        label.setName("zombie_" + zombie.getId()); // use unique name if possible

        int tileWidth = tilePanels[0][0].getWidth(); // or just use 100
        int tileHeight = tilePanels[0][0].getHeight(); // real tile height
        int imageHeight = 100;

        int x = col * tileWidth;
        int y = row * tileHeight + (tileHeight - imageHeight) / 2; // center vertically

        label.setBounds(x, y, 75, imageHeight);

        zombieLayer.add(label, JLayeredPane.PALETTE_LAYER);
        zombieLayer.revalidate();
        zombieLayer.repaint();

        zombieLabels.put(zombie, label);
    }




    //Handles removing Zombie images
    public void removeZombieAt(int row, int col) {
        int tileWidth = tilePanels[0][0].getWidth();
        int tileHeight = tilePanels[0][0].getHeight();
        int imageHeight = 100;

        int x = col * tileWidth;
        int y = row * tileHeight + (tileHeight - imageHeight) / 2;

        for (Component comp : zombieLayer.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)) {
            if (comp instanceof JLabel label &&
                label.getName() != null &&
                label.getX() == x &&
                label.getY() == y) {
                zombieLayer.remove(label);
                break;
            }
        }

        zombieLayer.revalidate();
        zombieLayer.repaint();
    }

    public void animateZombieMove(Zombie zombie) {
        JLabel label = zombieLabels.get(zombie);
        if (label == null) return;

        int tileWidth = tilePanels[0][0].getWidth(); // e.g. 100
        int startX = label.getX();
        int endX = zombie.getCol() * tileWidth;

        int duration = 300; // total slide duration in ms
        int frames = 30;    // number of animation frames
        int delay = duration / frames;

        Timer timer = new Timer(delay, null);
        final int[] frame = {0};

        timer.addActionListener(e -> {
            frame[0]++;
            double t = frame[0] / (double) frames;
            int newX = (int) (startX + t * (endX - startX));

            label.setLocation(newX, label.getY());

            if (frame[0] >= frames) {
                label.setLocation(endX, label.getY());
                timer.stop();
            }
        });

        timer.start();
    }

    /* Display Projectiles */
    public void displayProjectile(Projectile proj) {
        String imagePath = (proj instanceof SnowPea) ? "/resources/snowpea.png" : "/resources/pea.png";
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl == null) {
            System.err.println("Could not load image for projectile.");
            return;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaled));

        int tileHeight = tilePanels[0][0].getHeight();
        int startX = proj.getCol() * tilePanels[0][0].getWidth();
        int y = proj.getRow() * tileHeight + (tileHeight - 30) / 2;

        label.setBounds(startX, y, 30, 30);
        projectileLayer.add(label, JLayeredPane.MODAL_LAYER);
        projectileLayer.repaint();

        animateProjectile(proj, label);
    }

    /* animate projectile movement */
    public void animateProjectile(Projectile proj, JLabel label) {
        int speed = 10; // pixels per frame
        int delay = 30; // ms (for 30 FPS)

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            int newX = label.getX() + speed;
            label.setLocation(newX, label.getY());

            // Update model position
            proj.setCol(newX / tilePanels[0][0].getWidth());

            // Check collision
            Zombie zombie = model.getZombieAtPixel(proj.getRow(), newX);
            if (zombie != null && proj.isActive()) {
                proj.move(model); // this will call onHit internally
                projectileLayer.remove(label);
                projectileLayer.repaint();
                timer.stop();
            }

            // Remove if off screen
            if (newX > projectileLayer.getWidth() || !proj.isActive()) {
                projectileLayer.remove(label);
                projectileLayer.repaint();
                timer.stop();
            }
        });

        timer.start();
    }

    // Game end screen
    // Called when game ends
    public void showGameOver(boolean victory, int level) {
        gameOverLabel.setText(victory ? "VICTORY!" : "GAME OVER!");
        gameOverLabel.setForeground(victory ? Color.GREEN : Color.RED);
        gameOverPanel.setVisible(true);

        backButton.setVisible(true);
    }

    // Called to hide game over screen
    public void hideGameOver() {
        gameOverPanel.setVisible(false);
        backButton.setVisible(false);
    }


    // Adds listener to new "Back" button
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Cleans and resets board state
    public void resetBoard() {
        if (tilePanels == null || plantLabels == null) {
            System.err.println("Board not initialized properly.");
            return;
        }

        for (int r = 0; r < tilePanels.length; r++) {
            for (int c = 0; c < tilePanels[r].length; c++) {
                JLayeredPane tile = tilePanels[r][c];
                if (tile == null) continue;

                // Remove all components EXCEPT background (layer 0)
                for (int i = tile.getComponentCount() - 1; i >= 0; i--) {
                    Component comp = tile.getComponent(i);
                    Integer layer = (Integer) tile.getLayer(comp);
                    if (layer != 0) {
                        tile.remove(comp);
                    }
                }

                // Create and re-add an empty plant label to layer 1
                JLabel plantLabel = new JLabel();
                plantLabel.setBounds(0, 0, 100, 100);
                tile.add(plantLabel, Integer.valueOf(1));
                plantLabels[r][c] = plantLabel;

                tile.revalidate();
                tile.repaint();
            }
        }
    }



    // For cherrybomb visuals
    public void showBoomEffect(int row, int col) {
        int tileSize = 100; 
        int centerX = col * tileSize + tileSize / 2;
        int centerY = row * tileSize + tileSize / 2;

        Boom explosion = new Boom(centerX, centerY);
        layeredPane.add(explosion, JLayeredPane.POPUP_LAYER);
        layeredPane.repaint();
    }

}



    




    



