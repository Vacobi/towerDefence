package core;

import economic.Accountant;
import events.GameListener;
import events.WaveListener;
import factory.WaveFactory;
import monster.Monster;
import utils.GameState;

import java.util.ArrayList;
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
        currentWaveNumber = 1;

        field = new Field();
        changeWave();
        field.setWave(wave);
        gameState = GameState.WAITING_WAVE_START;

        Builder builder = new Builder(field);
        player = new Player(builder);
        accountant = new Accountant(player.getBankAccount());

        listeners = new ArrayList<>();

        creditGoldBeforeWave(wave);
    }

    private void creditGoldBeforeWave(Wave wave) {
        accountant.creditGoldBeforeWave(wave);
    }

    public void startWave() {
        if (gameState != GameState.WAITING_WAVE_START) {
            return;
        }

        changeWave();
        freezePlayer();

        this.gameState = GameState.WAVE_IN_PROGRESS;
        field.updateEntitiesLoop(this);

        processEndOfWave();
    }

    private void changeWave() {
        wave = waveFactory.createWave(currentWaveNumber, field);
        wave.addListener(this);
        field.setWave(wave);
    }

    private void processEndOfWave() {
        if (gameState != GameState.END) {
            this.gameState = GameState.WAITING_WAVE_START;
            determineWin();
            currentWaveNumber++;
            unfreezePlayer();
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

    private void determineLose() {
        if (player.getLives() <= 0) {
            listeners.forEach((GameListener l) -> {
                l.onPlayerLose(player);
            });

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
}
