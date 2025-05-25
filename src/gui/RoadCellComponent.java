package gui;

import core.RoadCell;

import java.awt.*;

public class RoadCellComponent extends GameComponent<RoadCell> {
    public RoadCellComponent(RoadCell model, int width, int height) {
        super(model, width, height);
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, getWidth(), getHeight());
    }
}