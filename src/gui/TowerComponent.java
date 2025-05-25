package gui;

import tower.Tower;

import java.awt.*;

public class TowerComponent extends GameComponent<Tower<?>> {
    public TowerComponent(Tower<?> model, int width, int height) {
        super(model,
                width,
                height
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}