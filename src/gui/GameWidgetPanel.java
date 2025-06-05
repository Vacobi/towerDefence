package gui;

import core.*;
import events.WaveEvent;
import events.WaveListener;
import gui.components.ComponentFactory;
import gui.components.GameComponent;
import gui.components.MonsterComponent;
import projectile.Projectile;
import tower.Tower;
import utils.Position;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class GameWidgetPanel extends JLayeredPane implements WaveListener {
    private static final Integer ROAD_LAYER = 100;
    private static final Integer MONSTER_LAYER = 200;
    private static final Integer PROJECTILE_LAYER = 300;
    private static final Integer TOWER_LAYER = 400;

    private final Game game;
    private final int cellSize;
    private Tower<? extends Projectile> selectedPrototype;
    private CataloguePanel cataloguePanel;

    private Timer uiUpdateTimer;

    public GameWidgetPanel(Game game, int cellSize, CataloguePanel cataloguePanel) {
        this.game = game;
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(
                game.getField().getWidth() * cellSize,
                game.getField().getHeight() * cellSize
        ));
        initField();
        this.cataloguePanel = cataloguePanel;
    }

    private void initField() {
        Field field = game.getField();
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                Position pos = new Position(i, j);
                GameComponent<AbstractCell> cell = ComponentFactory.create(field.cellAt(pos).get());
                cell.setBounds(pos.getX() * cellSize, pos.getY() * cellSize, cellSize, cellSize);
                add(cell, ROAD_LAYER);

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        onCellClicked(pos);
                    }
                });
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private void onCellClicked(Position pos) {
        if (game.getPlayer().frozen()) {
            return;
        }

        Optional<AbstractCell> optCell = game.getField().cellAt(pos);
        if (optCell.isEmpty()) return;

        AbstractCell abstractCell = optCell.get();

        if (abstractCell instanceof Cell cell) {
            if (cell.hasTower()) {
                showUpgradeDialog(cell.getTower());
                return;
            }

            if (selectedPrototype != null) {
                boolean built = game.getPlayer().placeTower(selectedPrototype, cell);
                if (built) {
                    GameComponent<Tower<?>> towerComponent = ComponentFactory.create(cell.getTower());
                    towerComponent.setBounds(pos.getX() * cellSize, pos.getY() * cellSize, cellSize, cellSize);
                    add(towerComponent, TOWER_LAYER);

                    revalidate();
                    repaint();
                    selectedPrototype = null;
                    cataloguePanel.cancelSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Не удалось построить башню: недостаточно золота или ячейка недоступна.");
                }
            }
        }
    }

    private void showUpgradeDialog(Tower<?> tower) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new TowerUpgradeDialog(topFrame, tower, game.getPlayer()).setVisible(true);

        revalidate();
        repaint();
    }

    public void setSelectedPrototype(Tower<? extends Projectile> prototype) {
        this.selectedPrototype = prototype;
    }

    public void startUpdateTimer() {
        if (uiUpdateTimer != null && uiUpdateTimer.isRunning()) return;

        game.getField().getWave().addListener(this);
        int delay = 1000 / 60;
        uiUpdateTimer = new Timer(delay, e -> refreshDynamicComponents());
        uiUpdateTimer.start();
    }

    private void stopUpdateTimer() {
        uiUpdateTimer.stop();
    }

    private void refreshDynamicComponents() {
        Arrays.stream(getComponentsInLayer(MONSTER_LAYER)).forEach(this::remove);
        Arrays.stream(getComponentsInLayer(PROJECTILE_LAYER)).forEach(this::remove);

        new LinkedList<>(game.getField().getWave().getAliveMonsters()).forEach(monster -> {
            MonsterComponent component = (MonsterComponent) ComponentFactory.create(monster);
            add(component, MONSTER_LAYER);
        });

        new LinkedList<>(game.getField().getProjectiles()).forEach(projectile -> {
            GameComponent<?> component = ComponentFactory.create(projectile);
            add(component, PROJECTILE_LAYER);
        });

        revalidate();
        repaint();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMonsterDeath(WaveEvent event) {
        ;
    }

    @Override
    public void onMonsterReachedEnd(WaveEvent event) {
        ;
    }

    @Override
    public void onWaveEnd(WaveEvent event) {
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {}
        stopUpdateTimer();
        refreshDynamicComponents();
    }
}
