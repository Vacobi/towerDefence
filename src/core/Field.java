package core;

import events.CellEvent;
import events.CellListener;
import events.WaveListener;
import monster.Monster;
import projectile.Projectile;
import projectile.ProjectilesContainer;
import road.Road;
import tower.TowersContainer;
import tower.Tower;
import utils.Position;

import java.util.*;

public class Field implements WaveListener, CellListener {
    private final Set<Cell> cells;
    private final Road road;
    private final TowersContainer towers;
    private final ProjectilesContainer projectiles;
    private Wave wave;
    private Timer updateTimer;
    private static final int UPDATE_RATE = 24;
    private static final int UPDATE_PERIOD_MS = 1000 / UPDATE_RATE;

    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;

    public Field() {
        road = new Road();
        cells = new HashSet<>();
        initializeCells();

        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer();
        wave = null;
    }

    public Field(String path) {
        road = new Road(path);
        cells = new HashSet<>();
        initializeCells();

        towers = new TowersContainer(this);
        projectiles = new ProjectilesContainer();
        wave = null;
    }

    private void initializeCells() {
        Set<Position> roadPositions = new HashSet<>();
        road.getRoadCells().forEach(cell -> roadPositions.add(cell.position()));

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Position pos = new Position(i, j);
                if (!roadPositions.contains(pos)) {
                    Cell cell = new Cell(pos);
                    cell.addListener(this);
                    cells.add(cell);
                }
            }
        }
    }

    public void startUpdates(UpdateFieldController controller) {
        updateTimer = new Timer("FieldUpdateTimer", true);
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!controller.shouldContinue()) {
                    stopUpdates();
                    return;
                }

                long currentTick = System.currentTimeMillis();
                wave.spawnMonsters(currentTick);
                wave.updateMonsters(currentTick);

                towersShoot(currentTick);
                updateProjectiles(currentTick);
            }
        }, 0, UPDATE_PERIOD_MS);
    }

    public void stopUpdates() {
        updateTimer.cancel();
        updateTimer.purge();
    }

    public Road getRoad() {
        return road;
    }

    public void setWave(Wave wave) {
        if (wave == null) {
            throw new IllegalArgumentException("Wave can not be null");
        }

        this.wave = wave;
        wave.addListener(this);
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
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            if (!projectile.active()) {
                iterator.remove();
            } else {
                projectile.update(currentTick);
            }
        }
    }

    public Set<Cell> getCells() {
        return new HashSet<>(cells);
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public List<Projectile> getProjectiles() {
        return new LinkedList<>(projectiles.getProjectiles());
    }

    public Optional<AbstractCell> cellAt(Position position) {
        for (Cell cell : cells) {
            if (cell.position().equals(position)) {
                return Optional.of(cell);
            }
        }

        for (RoadCell roadCell : road.getRoadCells()) {
            if (roadCell.position().equals(position)) {
                return Optional.of(roadCell);
            }
        }

        return Optional.empty();
    }

    @Override
    public void onMonsterDeath(Monster monster) {
        ;
    }

    @Override
    public void onMonsterReachedEnd(Monster monster) {
        ;
    }

    @Override
    public void onWaveEnd(Wave wave) {

        projectiles.clearProjectiles();
        stopUpdates();
    }

    @Override
    public void onTowerBuilt(CellEvent event) {
        towers.addTower(event.getTower());
    }
}
