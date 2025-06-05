package core;

import economic.Accountant;
import events.GameEvent;
import events.GameListener;
import events.WaveEvent;
import events.WaveListener;
import factory.WaveFactory;
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

    //------------------------------------------------------------------------------------------------------------------

    private void changeWave() {
        wave = waveFactory.createWave(++currentWaveNumber, field);
        wave.addListener(this);

        field.setWave(wave);

        accountant.creditGoldBeforeWave(wave);

        fireWaveChange();
    }

    public void startWave() {
        if (!canStartWave()) {
            return;
        }

        freezePlayer();

        this.gameState = GameState.WAVE_IN_PROGRESS;
        field.startUpdates(this);

        fireWaveStart();
    }

    public boolean canStartWave() {
        return gameState == GameState.WAITING_WAVE_START && wave.getNumber() <= WAVES_COUNT && !player.frozen();
    }

    private void freezePlayer() {
        player.freeze(true);
    }

    private void unfreezePlayer() {
        player.freeze(false);
    }

    private void determineWin() {
        if (currentWaveNumber >= WAVES_COUNT) {
            gameState = GameState.END;

            firePlayerWin();
        }
    }

    private void determineLose() {
        if (player.getLives() <= 0) {
            gameState = GameState.END;

            firePlayerLose();
        }
    }

    @Override
    public boolean shouldContinue() {
        return gameState != GameState.END;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Field getField() {
        return field;
    }

    public Player getPlayer() {
        return player;
    }

    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    public void addGameListener(GameListener listener) {
        listeners.add(listener);
    }

    public void removeGameListener(GameListener listener) {
        listeners.remove(listener);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMonsterDeath(WaveEvent event) {
        accountant.creditGoldForMonsterKill();
    }

    @Override
    public void onMonsterReachedEnd(WaveEvent event) {
        player.reduceLives(1);

        determineLose();
    }

    @Override
    public void onWaveEnd(WaveEvent event) {
        if (gameState != GameState.END) {
            fireWaveEnd();
            determineWin();
        }

        if (gameState != GameState.END) {
            this.gameState = GameState.WAITING_WAVE_START;
            unfreezePlayer();
            changeWave();
        }
    }

    public int getTotalWavesCount() {
        return WAVES_COUNT;
    }

    //------------------------------------------------------------------------------------------------------------------

    private void fireWaveChange() {
        GameEvent event = new GameEvent(this);
        event.setWave(wave);

        listeners.forEach(l -> l.onWaveChange(event));
    }

    private void fireWaveStart() {
        GameEvent event = new GameEvent(this);
        event.setWave(wave);

        listeners.forEach(l -> l.onWaveStart(event));
    }

    private void fireWaveEnd() {
        GameEvent event = new GameEvent(this);
        event.setWave(wave);

        listeners.forEach(l -> l.onWaveEnd(event));
    }

    private void firePlayerLose() {
        GameEvent event = new GameEvent(this);
        event.setPlayer(getPlayer());

        listeners.forEach(l -> l.onPlayerLose(event));
    }

    private void firePlayerWin() {
        GameEvent event = new GameEvent(this);
        event.setPlayer(getPlayer());

        listeners.forEach(l -> l.onPlayerWin(event));
    }
}
