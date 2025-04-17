package events;

import monster.Monster;

public interface MonsterListener {
    void onMonsterDeath(Monster monster);

    void onMonsterReachedEnd(Monster monster);
}
