package events;

import core.Player;
import core.Wave;

import java.util.EventObject;

public class GameEvent extends EventObject {

    Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    Wave wave;

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public Wave getWave() {
        return wave;
    }

    public GameEvent(Object source) {
        super(source);
    }
}
