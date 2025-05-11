package core;

import events.MonsterListener;
import events.WaveListener;
import monster.Monster;

import java.util.*;
import java.util.function.Consumer;

public class Wave implements Iterable<Monster>, MonsterListener {

    private final Queue<Monster> monstersToSpawn;
    private final long spawnDelay;
    private final int number;
    private final List<WaveListener> listeners;

    private final Set<Monster> aliveMonsters;
    private long lastSpawnTime;

    public Wave(Queue<Monster> monstersToSpawn, long spawnDelay, int number) {
        this.number = number;
        this.monstersToSpawn = monstersToSpawn;
        this.spawnDelay = spawnDelay;
        aliveMonsters = new HashSet<>();
        listeners = new LinkedList<>();
        lastSpawnTime = System.currentTimeMillis() - spawnDelay;
    }

    public int getNumber() {
        return number;
    }

    public boolean hasEnded() {
        return aliveMonsters.isEmpty() && monstersToSpawn.isEmpty();
    }

    public void spawnMonsters(long currentTick) {
        if (monstersToSpawn.isEmpty()) {
            return;
        }

        if (currentTick - lastSpawnTime >= spawnDelay) {
            Monster monster = monstersToSpawn.poll();
            monster.addListener(this);
            aliveMonsters.add(monster);
            lastSpawnTime = currentTick;
        }
    }

    public void updateMonsters(long currentTick) {
        Iterator<Monster> iterator = aliveMonsters.iterator();
        while (iterator.hasNext()) {
            Monster monster = iterator.next();
            monster.move(currentTick);

            if (!monster.stillInWave()) {
                iterator.remove();
            }
        }
    }

    public Set<Monster> getAliveMonsters() {
        return aliveMonsters;
    }

    // - /////////////////////////////////////////////////////////////

    public void addListener(WaveListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WaveListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onMonsterDeath(Monster monster) {
        listeners.forEach(l -> l.onMonsterDeath(monster));
    }

    @Override
    public void onMonsterReachedEnd(Monster monster) {
        listeners.forEach(l -> l.onMonsterReachedEnd(monster));
    }

    @Override
    public Iterator<Monster> iterator() {
        return aliveMonsters.iterator();
    }

    @Override
    public void forEach(Consumer<? super Monster> action) {
        Iterable.super.forEach(action);
    }
}
