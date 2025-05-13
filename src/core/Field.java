package core;

import projectile.Projectile;
import projectile.ProjectilesContainer;
import road.Road;
import tower.TowersContainer;
import tower.Tower;
import utils.Position;

import java.util.*;

public class Field {
    private final Set<Cell> cells;
    private final Road road;
    private final TowersContainer towers;
    private final ProjectilesContainer projectiles;
    private Wave wave;
    private Timer updateTimer;
    private static final int UPDATE_RATE = 25;
    private static final int UPDATE_PERIOD_MS = 1000 / UPDATE_RATE;

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    public Field() {
        road = new Road();
        cells = new HashSet<>();
        initializeCells();

        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer(this);
        wave = null;
    }

    public Field(String path) {
        road = new Road(path);
        cells = new HashSet<>();
        initializeCells();

        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer(this);
        wave = null;
    }

    private void initializeCells() {
        Set<Position> roadPositions = new HashSet<>();
        road.getRoadCells().forEach(cell -> roadPositions.add(cell.position()));

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position pos = new Position(i, j);
                if (!roadPositions.contains(pos)) {
                    cells.add(new Cell(pos));
                }
            }
        }
    }

    public void startUpdates(UpdateFieldController controller) {
        updateTimer = new Timer();
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
            List<Projectile> shots = tower.shoot(currentTick);
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
