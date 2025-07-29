package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LevelSelectView extends JFrame {
    public LevelSelectView(ActionListener levelListener) {
        setTitle("Select a Level");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with lawn green background
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(107, 142, 35)); // Lawn green

        // Title label
        JLabel titleLabel = new JLabel("Choose a Level");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 165, 0)); // PvZ orange
        titleLabel.setBounds(270, 30, 400, 50);
        mainPanel.add(titleLabel);

        // Gravestone panel
        JPanel gravestonePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(169, 169, 169));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(4));
                g2.drawRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            }
        };
        gravestonePanel.setBounds(250, 100, 300, 350);
        gravestonePanel.setOpaque(false);

        // PvZ-styled button factory
        Font buttonFont = new Font("SansSerif", Font.BOLD, 18);
        Color buttonColor = new Color(120, 72, 0);
        Color textColor = Color.WHITE;

        for (int i = 1; i <= 3; i++) {
            JButton levelButton = new JButton("Level " + i);
            levelButton.setBounds(60, 40 + (i - 1) * 70, 180, 50);
            levelButton.setFont(buttonFont);
            levelButton.setBackground(buttonColor);
            levelButton.setForeground(textColor);
            levelButton.setFocusPainted(false);
            levelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            levelButton.setActionCommand(String.valueOf(i));
            levelButton.addActionListener(levelListener);
            gravestonePanel.add(levelButton);
        }

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(90, 270, 120, 40);
        backButton.setFont(buttonFont);
        backButton.setBackground(new Color(100, 50, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        backButton.addActionListener(e -> {
            System.out.println("Back pressed.");
            dispose();
        });
        gravestonePanel.add(backButton);

        mainPanel.add(gravestonePanel);
        add(mainPanel);
        setVisible(true);
    }
}
