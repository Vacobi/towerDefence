package core;

import events.GameEvent;
import events.GameListener;
import events.WaveEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void waveStarts() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        game.startWave();
        expectedEvents.add(Event.WAVE_START);

        assertEquals(expectedEvents, events);
        assertEquals(0, playerWins);
        assertEquals(0, playerLoses);
        assertEquals(1, waveStarts);
        assertEquals(0, waveEnds);
        assertEquals(0, waveChanges);
    }

    @Test
    void waveEnds() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        WaveEvent waveEvent = new WaveEvent(game.getField().getWave());
        waveEvent.setWave(game.getField().getWave());

        game.onWaveEnd(waveEvent);
        expectedEvents.add(Event.WAVE_END);
        expectedEvents.add(Event.WAVE_CHANGE);

        assertEquals(2, game.getField().getWave().getNumber());
        assertEquals(expectedEvents, events);
        assertEquals(0, playerWins);
        assertEquals(0, playerLoses);
        assertEquals(0, waveStarts);
        assertEquals(1, waveEnds);
        assertEquals(1, waveChanges);
    }

    @Test
    void monsterDeath() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        WaveEvent waveEvent = new WaveEvent(game.getField().getWave());

        int goldBefore = game.getPlayer().getBankAccount().getGold();
        for (int i = 0; i < 1000; i++) {
            game.onMonsterDeath(waveEvent);
        }
        int goldAfter = game.getPlayer().getBankAccount().getGold();

        assertTrue(goldAfter > goldBefore);
        assertEquals(expectedEvents, events);
        assertEquals(0, playerWins);
        assertEquals(0, playerLoses);
        assertEquals(0, waveStarts);
        assertEquals(0, waveEnds);
        assertEquals(0, waveChanges);
    }

    @Test
    void monsterReachedEnd() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        WaveEvent waveEvent = new WaveEvent(game.getField().getWave());
        waveEvent.setWave(game.getField().getWave());

        int livesBefore = game.getPlayer().getLives();
        game.onMonsterReachedEnd(waveEvent);
        int livesAfter = game.getPlayer().getLives();

        assertEquals(livesBefore - 1, livesAfter);
        assertEquals(expectedEvents, events);
        assertEquals(0, playerWins);
        assertEquals(0, playerLoses);
        assertEquals(0, waveStarts);
        assertEquals(0, waveEnds);
        assertEquals(0, waveChanges);
    }

    @Test
    void playerLoses() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        WaveEvent waveEvent = new WaveEvent(game.getField().getWave());
        waveEvent.setWave(game.getField().getWave());

        int totalLives = game.getPlayer().getLives();
        for (int i = 0; i < totalLives; i++) {
            game.onMonsterReachedEnd(waveEvent);
        }
        expectedEvents.add(Event.PLAYER_LOSE);

        assertEquals(0, game.getPlayer().getLives());
        assertEquals(expectedEvents, events);
        assertEquals(0, playerWins);
        assertEquals(1, playerLoses);
        assertEquals(0, waveStarts);
        assertEquals(0, waveEnds);
        assertEquals(0, waveChanges);
    }

    @Test
    void playerWins() {
        Game game = new Game();
        game.addGameListener(new GameListenerImpl());

        WaveEvent waveEvent = new WaveEvent(game.getField().getWave());
        waveEvent.setWave(game.getField().getWave());

        int livesBefore = game.getPlayer().getLives();
        int totalWaves = game.getTotalWavesCount();
        for (int i = 0; i < totalWaves; i++) {
            game.onWaveEnd(waveEvent);
            expectedEvents.add(Event.WAVE_END);
            if (i != totalWaves - 1) {
                expectedEvents.add(Event.WAVE_CHANGE);
            }
        }
        expectedEvents.add(Event.PLAYER_WIN);

        assertEquals(livesBefore, game.getPlayer().getLives());
        assertEquals(expectedEvents, events);
        assertEquals(1, playerWins);
        assertEquals(0, playerLoses);
        assertEquals(0, waveStarts);
        assertEquals(totalWaves, waveEnds);
        assertEquals(totalWaves - 1, waveChanges);
    }

    private enum Event {PLAYER_WIN, PLAYER_LOSE, WAVE_START, WAVE_END, WAVE_CHANGE}
    private List<Event> events;
    private List<Event> expectedEvents;

    private int playerWins;
    private int playerLoses;
    private int waveStarts;
    private int waveEnds;
    private int waveChanges;
    @BeforeEach
    void setUp() {
        events = new LinkedList<>();
        expectedEvents = new LinkedList<>();
        playerWins = 0;
        playerLoses = 0;
        waveStarts = 0;
        waveEnds = 0;
        waveChanges = 0;
    }

    private class GameListenerImpl implements GameListener {

        @Override
        public void onPlayerWin(GameEvent event) {
            events.add(Event.PLAYER_WIN);
            playerWins++;
        }

        @Override
        public void onPlayerLose(GameEvent event) {
            events.add(Event.PLAYER_LOSE);
            playerLoses++;
        }

        @Override
        public void onWaveStart(GameEvent event) {
            events.add(Event.WAVE_START);
            waveStarts++;
        }

        @Override
        public void onWaveEnd(GameEvent event) {
            events.add(Event.WAVE_END);
            waveEnds++;
        }

        @Override
        public void onWaveChange(GameEvent event) {
            events.add(Event.WAVE_CHANGE);
            waveChanges++;
        }
    }
}