package core;

import economic.Accountant;
import events.GameListener;
import events.WaveListener;
import factory.WaveFactory;
import monster.Monster;
import utils.GameState;

import java.util.LinkedList;
import java.util.List;

public class Game implements WaveListener, UpdateFieldController {
    private GameState gameState;
    private Wave wave;
    private final Field field;
    private final Player player;
    private final Accountant accountant;
    private final List<GameListener> listeners;
    private final WaveFactory waveFactory;

    private final int WAVES_COUNT = 10;
    private int currentWaveNumber;

    public Game() {
        this.waveFactory = new WaveFactory();
        currentWaveNumber = 0;

        field = new Field();
        gameState = GameState.WAITING_WAVE_START;

        Builder builder = new Builder(field);
        player = new Player(builder);
        accountant = new Accountant(player.getBankAccount());

        listeners = new LinkedList<>();

        changeWave();
    }

    public void startWave() {
        if (!canStartWave()) {
            return;
        }

        freezePlayer();

        this.gameState = GameState.WAVE_IN_PROGRESS;
        field.startUpdates(this);

        listeners.forEach((GameListener l) -> l.onWaveStart(wave));
    }

    public boolean canStartWave() {
        return gameState == GameState.WAITING_WAVE_START && wave.getNumber() <= WAVES_COUNT && !player.frozen();
    }

    private void changeWave() {
        wave = waveFactory.createWave(++currentWaveNumber, field);
        wave.addListener(this);
        field.setWave(wave);
        accountant.creditGoldBeforeWave(wave);

    private void processEndOfWave() {
        if (gameState != GameState.END) {
            this.gameState = GameState.WAITING_WAVE_START;
            unfreezePlayer();
            Wave endedWave = wave;
            listeners.forEach((GameListener l) -> l.onWaveEnd(endedWave));
            determineWin();
            changeWave();
        }
    }

    private void determineWin() {
        if (currentWaveNumber >= WAVES_COUNT) {
            listeners.forEach((GameListener l) -> {
                l.onPlayerWin(player);
            });

            gameState = GameState.END;
        }
    }

    private void freezePlayer() {
        player.freeze(true);
    }

    private void unfreezePlayer() {
        player.freeze(false);
    }

    @Override
    public void onMonsterDeath(Monster monster) {
        accountant.creditGoldForMonsterKill();
    }

    @Override
    public void onMonsterReachedEnd(Monster monster) {
        player.reduceLives(1);

        determineLose();
    }

    @Override
    public void onWaveEnd(Wave wave) {
        processEndOfWave();
    }

    private void determineLose() {
        if (player.getLives() <= 0) {
            listeners.forEach((GameListener l) -> l.onPlayerLose(player));

            gameState = GameState.END;
        }
    }

    @Override
    public boolean shouldContinue() {
        return gameState != GameState.END;
    }

    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }

    public Field getField() {
        return field;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }
}
