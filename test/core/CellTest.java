package core;

import collision.HitboxParameters;
import events.CellEvent;
import events.CellListener;
import exception.CellAlreadyHasTower;
import org.junit.jupiter.api.Test;
import projectile.PlainProjectile;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import tower.DirectionalShootingStrategy;
import tower.Tower;
import tower.TowerCharacteristicsValues;
import utils.CoordinatesConverter;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static asserts.TestEventAsserts.assertCellEventEquals;
import static org.junit.jupiter.api.Assertions.*;

class CellTest implements CellListener {

    Path path = Paths.get("test", "monster", "resources", "several_segments.txt")
            .toAbsolutePath()
            .normalize();

    private final Position cellPosition = new Position(10, 10);
    private final Position projectilePosition = CoordinatesConverter.toGlobalCoordinates(cellPosition);
    private final int levelsUpgradeCount = 3;

    private final Direction mockDirection = Direction.NORTH;
    private final HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
    private final TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
    private final int speed = 10;

    private CellEvent actualCellEvent;
    private int actualCellEvents = 0;

    @Test
    public void setTower() {
        Field field = new Field(path.toString());

        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        DirectionalShootingStrategy typicalShootingStrategy = new DirectionalShootingStrategy();
        Cell cell = new Cell(cellPosition);
        cell.addListener(this);

        Tower tower = new Tower(
                cell,
                field,
                typicalShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        Position expectedPosition = new Position(10, 10);
        CellEvent expectedEvent = new CellEvent(cell);
        expectedEvent.setTower(tower);
        int expectedCellEvents = 1;

        assertEquals(tower, cell.getTower());
        assertEquals(expectedPosition, cell.position());

        assertEquals(expectedCellEvents, actualCellEvents);
        assertCellEventEquals(expectedEvent, actualCellEvent);
    }

    @Test
    public void setTowerInCellWithOtherTower() {
        Field field = new Field(path.toString());

        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        DirectionalShootingStrategy typicalShootingStrategy = new DirectionalShootingStrategy();
        Cell cell = new Cell(cellPosition);
        new Tower(
                cell,
                field,
                typicalShootingStrategy.clone(),
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile.clone(characteristics.getDamage(), characteristics.getRange())
        );

        Tower tower = new Tower(
                cell.clone(),
                field,
                typicalShootingStrategy.clone(),
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile.clone(characteristics.getDamage(), characteristics.getRange())
        );

        assertThrows(CellAlreadyHasTower.class, () -> cell.setTower(tower));
    }

    @Test
    public void towerCanBeSetWhenTowerNotSet() {
        Cell cell = new Cell(cellPosition);

        assertTrue(cell.canPlaceTower());
    }

    @Test
    public void towerCanBeSetWhenTowerAlreadySet() {
        Field field = new Field(path.toString());

        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        DirectionalShootingStrategy typicalShootingStrategy = new DirectionalShootingStrategy();
        Cell cell = new Cell(cellPosition);

        new Tower(
                cell,
                field,
                typicalShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        assertFalse(cell.canPlaceTower());
    }

    @Test
    public void toGlobalCoordinates() {
        int x = 13;
        int y = 14;
        Position cellPosition = new Position(x, y);
        Cell cell = new Cell(cellPosition);

        Position expectedGlobalPosition = new Position(
                CoordinatesConverter.toGlobalCoordinate(x) + AbstractCell.getSize() / 2,
                CoordinatesConverter.toGlobalCoordinate(y) + AbstractCell.getSize() / 2
        );

        Position actualGlobalPosition = cell.getGlobalPosition();

        assertEquals(expectedGlobalPosition, actualGlobalPosition);
    }

    @Override
    public void onTowerBuilt(CellEvent event) {
        actualCellEvent = event;
        actualCellEvents++;
    }
}