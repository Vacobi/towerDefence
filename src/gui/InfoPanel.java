package gui;

import core.Game;
import core.Player;
import core.Wave;
import events.GameListener;
import events.PlayerListener;

import javax.swing.*;

public class InfoPanel extends JPanel implements GameListener, PlayerListener {
    private final JPanel status = new JPanel();
    private final JLabel goldLabel = new JLabel();
    private final JLabel waveLabel = new JLabel();
    private final JLabel livesLabel = new JLabel();

    private final Game game;

    public InfoPanel(Game game) {
        status.add(new JLabel("Gold: ")); status.add(goldLabel);
        status.add(new JLabel("Wave: ")); status.add(waveLabel);
        status.add(new JLabel("Lives: ")); status.add(livesLabel);
        add(status);

        this.game = game;
        game.addGameListener(this);

        game.getPlayer().addListener(this);

        updateInfo();
    }

    public void updateInfo() {
        updateGoldLabel();
        updateWaveLabel();
        updateLivesLabel();
    }

    private void updateGoldLabel() {
        goldLabel.setText(String.valueOf(game.getPlayer().getBankAccount().getGold()));
    }

    private void updateWaveLabel() {
        waveLabel.setText(String.valueOf(game.getCurrentWaveNumber()));
    }

    private void updateLivesLabel() {
        livesLabel.setText(String.valueOf(game.getPlayer().getLives()));
    }

    @Override
    public void onPlayerWin(Player player) {
        ;
    }

    @Override
    public void onPlayerLose(Player player) {
        ;
    }

    @Override
    public void onWaveStart(Wave wave) {

    }

    @Override
    public void onWaveEnd(Wave wave) {
        ;
    }

    @Override
    public void onWaveChange(Wave wave) {
        updateWaveLabel();
    }

    @Override
    public void onPlayerLostLive(Player player) {
        updateLivesLabel();
    }

    @Override
    public void onChangedPlayerGoldCount(Player player) {
        updateGoldLabel();
    }
}
