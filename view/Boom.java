package view;

import javax.swing.*;
import java.awt.*;

public class Boom extends JComponent {
    private int x, y;
    private int size = 0;
    private int maxSize = 100;
    private float alpha = 1f;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
        setBounds(x - maxSize / 2, y - maxSize / 2, maxSize, maxSize);

        Timer animationTimer = new Timer(30, e -> {
            size += 10;
            alpha -= 0.1f;
            if (alpha <= 0f) {
                ((Timer) e.getSource()).stop();
                Container parent = getParent();
                if (parent != null) parent.remove(this);
                parent.repaint();
            }
            repaint();
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(255, 69, 0)); // reddish-orange
        g2.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
        g2.dispose();
    }
}
