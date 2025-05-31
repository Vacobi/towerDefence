package core;

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
    public void onPlayerWin(Player player) {
        win++;
    }

    @Override
    public void onPlayerLose(Player player) {
        lose++;
    }

    @Override
    public void onWaveStart(Wave wave) {

    }

    @Override
    public void onWaveEnd(Wave wave) {

    }

    @Override
    public void onWaveChange(Wave wave) {

    }
}