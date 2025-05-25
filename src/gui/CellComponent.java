package gui;

import core.Cell;

import java.awt.*;

public class CellComponent extends GameComponent<Cell> {
    public CellComponent(Cell model, int width, int height) {
        super(model, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(40, 180, 40));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, getWidth(), getHeight());
    }
}