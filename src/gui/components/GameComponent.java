package gui.components;

import javax.swing.*;
import java.awt.*;

public abstract class GameComponent<T> extends JComponent {
    protected final T model;

    public GameComponent(T model, int width, int height) {
        this.model = model;
        setSize(width, height);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected abstract void paintComponent(Graphics g);
}
