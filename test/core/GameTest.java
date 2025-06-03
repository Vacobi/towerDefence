package core;

import events.GameEvent;
import events.GameListener;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameTest implements GameListener {

    private static int win = 0;
    private static int lose = 0;

//    @Test
//    void waveEndsByPlayerLose() throws InterruptedException {
//        Game game = new Game();
//        game.addGameListener(this);
//
//        game.startWave();
//
//        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
//
//        System.out.println(game.getField().getWave().getAliveMonsters());
//        assertEquals(1, lose);
//    }

//    @Test
//    void waveEndsByPlayerWin() throws InterruptedException {
//        Game game = new Game();
//        game.addGameListener(this);
//
//        game.startWave();
//
//        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
//
//        game.getField().getWave().getAliveMonsters().forEach(monster -> {
//            monster.applyDamage(100);
//        });
//
//        game.startWave();
//
//        Thread.sleep(TimeUnit.MINUTES.toMillis(10));
//
//        System.out.println(game.getField().getWave().getAliveMonsters());
//        assertEquals(1, lose);
//    }


    @Override
    public void onPlayerWin(GameEvent event) {
        win++;
    }

    @Override
    public void onPlayerLose(GameEvent event) {
        lose++;
    }

    @Override
    public void onWaveStart(GameEvent event) {

    }

    @Override
    public void onWaveEnd(GameEvent event) {

    }

    @Override
    public void onWaveChange(GameEvent event) {

    }
}