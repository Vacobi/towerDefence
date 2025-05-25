package events;

import core.Wave;
import monster.Monster;

public interface WaveListener {
    void onMonsterDeath(Monster monster);

    void onMonsterReachedEnd(Monster monster);

    void onWaveEnd(Wave wave);
}
