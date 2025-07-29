import model.GameModel;
import view.GameView;
import controller.GameController;
import view.LevelSelectView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LevelSelectView(e -> {
                int selectedLevel = Integer.parseInt(e.getActionCommand());
                System.out.println("Level " + selectedLevel + " selected.");

                // Launch GameModel and GameView
                GameModel model = new GameModel(selectedLevel);
                GameView view = new GameView(model);
                model.setView(view);
                view.setModel(model);
                new GameController(model, view);

                // Close the level select screen
                JFrame source = (JFrame) SwingUtilities.getWindowAncestor((JButton) e.getSource());
                source.dispose();
            });
        });
    }
}
