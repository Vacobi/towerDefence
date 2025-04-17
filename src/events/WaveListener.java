package events;

import monster.Monster;

public interface WaveListener {
    void onMonsterDeath(Monster monster);

    void onMonsterReachedEnd(Monster monster);
}
