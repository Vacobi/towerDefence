package core;

import events.MonsterEvent;
import events.MonsterListener;
import events.WaveEvent;
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

    //------------------------------------------------------------------------------------------------------------------

    public boolean hasEnded() {
        return aliveMonsters.isEmpty() && monstersToSpawn.isEmpty();
    }

    public void spawnMonsters(long currentTick) {
        if (monstersToSpawn.isEmpty()) {
            return;
        }

        if (passedSpawnDelayTime(currentTick)) {
            Monster monster = monstersToSpawn.poll();
            monster.addListener(this);
            aliveMonsters.add(monster);
            lastSpawnTime = currentTick;
        }
    }

    private boolean passedSpawnDelayTime(long currentTick) {
        return currentTick - lastSpawnTime >= spawnDelay;
    }

    public void updateMonsters(long currentTick) {
        for (Monster monster : new ArrayList<>(aliveMonsters)) {
            monster.move(currentTick);
        }
    }

    private void processEndOfWave() {
        if (hasEnded()) {
            fireWaveEnd();
        }
    }

    @Override
    public Iterator<Monster> iterator() {
        return aliveMonsters.iterator();
    }

    @Override
    public void forEach(Consumer<? super Monster> action) {
        Iterable.super.forEach(action);
    }

    //------------------------------------------------------------------------------------------------------------------

    public int getNumber() {
        return number;
    }

    public Set<Monster> getAliveMonsters() {
        return new HashSet<>(aliveMonsters);
    }

    public void addListener(WaveListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WaveListener listener) {
        listeners.remove(listener);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onMonsterDeath(MonsterEvent event) {
        aliveMonsters.remove(event.getMonster());

        fireMonsterDeath(event);

        processEndOfWave();
    }

    @Override
    public void onMonsterReachedEnd(MonsterEvent event) {
        aliveMonsters.remove(event.getMonster());

        fireMonsterReachedEnd(event);

        processEndOfWave();
    }

    //------------------------------------------------------------------------------------------------------------------

    private void fireMonsterReachedEnd(MonsterEvent event) {
        WaveEvent waveEvent = new WaveEvent(event.getMonster());
        waveEvent.setMonster(event.getMonster());

        listeners.forEach(l -> l.onMonsterReachedEnd(waveEvent));
    }

    public void fireMonsterDeath(MonsterEvent event) {
        WaveEvent waveEvent = new WaveEvent(event.getMonster());
        waveEvent.setMonster(event.getMonster());

        listeners.forEach(l -> l.onMonsterDeath(waveEvent));
    }

    public void fireWaveEnd() {
        WaveEvent waveEvent = new WaveEvent(this);
        waveEvent.setWave(this);

        listeners.forEach(l -> l.onWaveEnd(waveEvent));
    }
}
