package events;

import core.Wave;
import monster.Monster;

import java.util.EventObject;

public class WaveEvent extends EventObject {

    Monster monster;

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public Monster getMonster() {
        return monster;
    }

    Wave wave;

    public void setWave(Wave wave) {
        this.wave = wave;
    }

    public Wave getWave() {
        return wave;
    }

    public WaveEvent(Object source) {
        super(source);
    }
}
