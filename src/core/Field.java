package core;

import projectile.Projectile;
import projectile.ProjectilesContainer;
import road.Road;
import tower.TowersContainer;
import tower.Tower;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Field {
    private final Set<Cell> cells;
    private final Road road;
    private final TowersContainer towers;
    private final ProjectilesContainer projectiles;
    private Wave wave;
    private Timer updateTimer;
    private static final int UPDATE_RATE = 25;
    private static final int UPDATE_PERIOD_MS = 1000 / UPDATE_RATE;

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

    public void updateEntitiesLoop(UpdateFieldController controller) {
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (wave.hasEnded() || !controller.shouldContinue()) {
                    stopUpdates();
                    return;
                }

                long currentTick = System.currentTimeMillis();
                wave.updateMonsters(currentTick);
                wave.spawnMonsters(currentTick);

                updateProjectiles(currentTick);
                towersShoot(currentTick);
            }
        }, 0, UPDATE_PERIOD_MS);
    }

    public void stopUpdates() {
        updateTimer.cancel();
        updateTimer.purge();
        updateTimer = new Timer("FieldUpdateTimer", true);
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

    protected void towersShoot(long currentTick) {
        towers.forEach((Tower tower) -> {
            Set<Projectile> shots = tower.shoot(currentTick);
            projectiles.addProjectiles(shots);
        });
    }

    protected void updateProjectiles(long currentTick) {
        projectiles.forEach((Projectile projectile) -> {
            projectile.update(currentTick);
        });
    }

    public Set<Cell> getCells() {
        return cells;
    }
}
