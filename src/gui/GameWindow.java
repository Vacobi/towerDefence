package gui;

import core.AbstractCell;
import core.Game;
import events.GameEvent;
import events.GameListener;
import tower.Tower;
import projectile.Projectile;
import tower.TowersCatalogue;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements GameListener {
    private final GameWidgetPanel gameWidgetPanel;
    private final InfoPanel infoPanel;
    private final JButton startButton;

    private final Game game;

    public GameWindow(Game game) {
        super("Tower Defense");
        this.game = game;
        game.addGameListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        TowersCatalogue catalogue = game.getPlayer().getBuilder().getTowersCatalogue();
        CataloguePanel cataloguePanel = new CataloguePanel(catalogue, game);

        gameWidgetPanel = new GameWidgetPanel(game, AbstractCell.getSize(), cataloguePanel);

        startButton = createStartButton();
        add(startButton, BorderLayout.NORTH);

        infoPanel = new InfoPanel(game);
        add(infoPanel, BorderLayout.SOUTH);

        cataloguePanel.addPropertyChangeListener("selectedPrototype", evt -> {
            Tower<? extends Projectile> prototype = (Tower<? extends Projectile>) evt.getNewValue();
            gameWidgetPanel.setSelectedPrototype(prototype);
        });
        add(cataloguePanel, BorderLayout.EAST);

        add(new JScrollPane(gameWidgetPanel), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createStartButton() {
        JButton startButton = new JButton("Начать Волну");
        startButton.addActionListener(e -> {
            game.startWave();
            gameWidgetPanel.startUpdateTimer();
        });

        return startButton;
    }

    //------------------------------------------------------------------------------------------------------------------

    private void processWaveStartButtonEnable() {
        startButton.setEnabled(game.canStartWave());
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

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onPlayerWin(GameEvent event) {
        showEndDialog("Вы выиграли!");
        processWaveStartButtonEnable();
    }

    @Override
    public void onPlayerLose(GameEvent event) {
        showEndDialog("Вы проиграли!");
        processWaveStartButtonEnable();
    }

    @Override
    public void onWaveStart(GameEvent event) {
        processWaveStartButtonEnable();
    }

    @Override
    public void onWaveEnd(GameEvent event) {
        processWaveStartButtonEnable();
    }

    @Override
    public void onWaveChange(GameEvent event) {
        processWaveStartButtonEnable();
    }
}
