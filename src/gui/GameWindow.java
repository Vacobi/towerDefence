package gui;

import core.AbstractCell;
import core.Game;
import core.Player;
import core.Wave;
import events.GameListener;
import tower.Tower;
import projectile.Projectile;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements GameListener {
    private final GameWidgetPanel gameWidgetPanel;
    private final InfoPanel infoPanel;

    private final Game game;

    public GameWindow(Game game) {
        super("Tower Defense");
        this.game = game;
        game.addGameListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameWidgetPanel = new GameWidgetPanel(game, AbstractCell.getSize());

        TowersCatalogue catalogue = new TowersCatalogue(game.getField());
        CataloguePanel cataloguePanel = new CataloguePanel(catalogue);

        gameWidgetPanel.setCataloguePanel(cataloguePanel);

        JButton startBtn = new JButton("Start Wave");
        startBtn.addActionListener(e -> {
            game.startWave();
            gameWidgetPanel.startUpdateTimer();
        });
        add(startBtn, BorderLayout.NORTH);

        infoPanel = new InfoPanel(game);
        add(infoPanel, BorderLayout.SOUTH);

        cataloguePanel.addPropertyChangeListener("selectedPrototype", evt -> {
            Tower<? extends Projectile> prototype = (Tower<? extends Projectile>) evt.getNewValue();
            gameWidgetPanel.setSelectedPrototype(prototype);
        });

        add(new JScrollPane(gameWidgetPanel), BorderLayout.CENTER);
        add(cataloguePanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void onPlayerWin(Player player) {
        showEndDialog("Вы выиграли!");
    }

    @Override
    public void onPlayerLose(Player player) {
        showEndDialog("Вы проиграли!");
    }

    @Override
    public void onWaveStart(Wave wave) {
        ;
    }

    @Override
    public void onWaveEnd(Wave wave) {
        ;
    }

    private void showEndDialog(String message) {
        int wave = game.getCurrentWaveNumber();
        String fullMessage = message + "\nТекущая волна: " + wave;

        String[] options = {"Начать заново", "Выйти"};
        int choice = JOptionPane.showOptionDialog(
                this,
                fullMessage,
                "Игра окончена",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            restartGame();
        } else {
            System.exit(0);
        }

        setVisible(true);
    }

    private void restartGame() {
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            Game newGame = new Game();
            new GameWindow(newGame);
        });
    }
}
