package events;

public interface MonsterListener {
    void onMonsterDeath(MonsterEvent event);

    void onMonsterReachedEnd(MonsterEvent event);
}
