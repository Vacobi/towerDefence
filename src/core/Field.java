package core;

import projectile.Projectile;
import projectile.ProjectilesContainer;
import road.Road;
import tower.TowersContainer;
import tower.Tower;

import java.util.HashSet;
import java.util.Set;

public class Field {
    private final Set<Cell> cells;
    private final Road road;
    private final TowersContainer towers;
    private final ProjectilesContainer projectiles;
    private Wave wave;

    public Field() {
        cells = new HashSet<>();
        road = new Road();
        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer(this);
        wave = null;
    }

    public Field(String path) {
        cells = new HashSet<>();
        road = new Road(path);
        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer(this);
        wave = null;
    }

    public void updateEntitiesLoop(UpdateFieldLoopController updateFieldLoopController) {
        while (!wave.hasEnded() && updateFieldLoopController.shouldContinue()) {
            long currentTick = System.currentTimeMillis();
            wave.updateMonsters(currentTick);
            wave.spawnMonsters(currentTick);
            updateProjectiles(currentTick);
            towersShoot(currentTick);
        }
    }

    public Road getRoad() {
        return road;
    }

    public void setWave(Wave wave) {
        if (wave == null) {
            throw new IllegalArgumentException("Wave can not be null");
        }

        this.wave = wave;
    }

    public Wave getWave() {
        return wave;
    }

    public void towersShoot(long currentTick) {
        towers.forEach((Tower tower) -> {
            Set<Projectile> shots = tower.shoot(currentTick);
            projectiles.addProjectiles(shots);
        });
    }

    public void updateProjectiles(long currentTick) {
        projectiles.forEach((Projectile projectile) -> {
            projectile.update(currentTick);
        });
    }

    public Set<Cell> getCells() {
        return cells;
    }
}
