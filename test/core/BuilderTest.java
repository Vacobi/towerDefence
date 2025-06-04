package core;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projectile.Projectile;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import tower.TowersCatalogue;
import utils.BuildingResponse;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static asserts.TestAsserts.assertBuildingResponsesEquals;
import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    Builder builder = new Builder(field);

    @Nested
    class Build {
        @Test
        void buildTowerOnFreeCell() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int balance = 50;
            Optional<Integer> optionalTowerCost = towersCatalogue.getPrice(typicalTower);
            int towerCost = optionalTowerCost.get();
            int expectedChange = balance - towerCost;
            BuildingResponse expectedBuilt = new BuildingResponse(expectedChange, true);

            BuildingResponse actualBuilt = builder.buildTower(typicalTower, cell, balance);

            assertBuildingResponsesEquals(expectedBuilt, actualBuilt);
            assertFalse(cell.canPlaceTower());
        }

        @Test
        void buildTowerOnFreeCellWithChange() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int balance = 50;
            Optional<Integer> optionalTowerCost = towersCatalogue.getPrice(typicalTower);
            int towerCost = optionalTowerCost.get();
            int expectedChange = balance - towerCost;
            BuildingResponse expectedBuilt = new BuildingResponse(expectedChange, true);

            BuildingResponse actualBuilt = builder.buildTower(typicalTower, cell, balance);

            assertBuildingResponsesEquals(expectedBuilt, actualBuilt);
            assertFalse(cell.canPlaceTower());
        }

        @Test
        void towersWasNotBuild() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int balance = 50;
            BuildingResponse expectedBuilt = new BuildingResponse(balance, false);

            builder.buildTower(typicalTower, cell, balance);

            BuildingResponse actualBuilt = builder.buildTower(typicalTower, cell, balance);

            assertBuildingResponsesEquals(expectedBuilt, actualBuilt);
            assertFalse(cell.canPlaceTower());
        }

        @Test
        void buildTwoSameTypicalTowersOnDifferentCells() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Cell differentCell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int balance = 50;
            Optional<Integer> optionalTowerCost = towersCatalogue.getPrice(typicalTower);
            int towerCost = optionalTowerCost.get();
            int expectedChange = balance - towerCost;
            BuildingResponse expectedBuilt = new BuildingResponse(expectedChange, true);

            builder.buildTower(typicalTower, cell, balance);

            BuildingResponse actualBuilt = builder.buildTower(typicalTower, differentCell, balance);

            assertBuildingResponsesEquals(expectedBuilt, actualBuilt);
            assertFalse(differentCell.canPlaceTower());
        }

        @Test
        void notEnoughGold() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int balance = 10;
            BuildingResponse expectedBuilt = new BuildingResponse(balance, false);

            BuildingResponse actualBuilt = builder.buildTower(typicalTower, cell, balance);

            assertBuildingResponsesEquals(expectedBuilt, actualBuilt);
            assertTrue(cell.canPlaceTower());
        }
    }

    @Nested
    class Upgrade {

        @Test
        void upgradeTower() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();

            int balance = 100;
            BuildingResponse buildingResponse = builder.buildTower(typicalTower, cell, balance);
            balance = buildingResponse.change();
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            Optional<Integer> optionalLevelOfCharacteristicBeforeUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelBeforeUpgrade = optionalLevelOfCharacteristicBeforeUpgrade.get();

            Optional<Integer> optionalUpgradeCost = towersCatalogue.getUpgradePrice(upgradableCharacteristic, levelBeforeUpgrade + 1);
            int upgradeCost = optionalUpgradeCost.get();

            int expectedChange = balance - upgradeCost;

            BuildingResponse expectedUpgrade = new BuildingResponse(expectedChange, true);

            //----------------------------------------------------------------------------------------------------------

            BuildingResponse actualUpgrade = builder.upgradeTower(tower, upgradableCharacteristic, balance);

            Optional<Integer> optionalLevelOfCharacteristicAfterUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelAfterUpgrade = optionalLevelOfCharacteristicAfterUpgrade.get();

            //----------------------------------------------------------------------------------------------------------

            assertEquals(levelBeforeUpgrade + 1, levelAfterUpgrade);
            assertBuildingResponsesEquals(expectedUpgrade, actualUpgrade);
        }

        @Test
        void upgradeCancelledByTower() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();

            int balance = 100;
            BuildingResponse buildingResponse = builder.buildTower(typicalTower, cell, balance);
            balance = buildingResponse.change();
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;
            BuildingResponse upgradeResponse = builder.upgradeTower(tower, upgradableCharacteristic, balance);
            balance = upgradeResponse.change();
            upgradeResponse = builder.upgradeTower(tower, upgradableCharacteristic, balance);
            balance = upgradeResponse.change();
            BuildingResponse expectedUpgrade = new BuildingResponse(balance, false);

            //----------------------------------------------------------------------------------------------------------

            BuildingResponse actualUpgrade = builder.upgradeTower(tower, upgradableCharacteristic, balance);

            //----------------------------------------------------------------------------------------------------------

            assertBuildingResponsesEquals(expectedUpgrade, actualUpgrade);
        }

        @Test
        void notEnoughGoldToUpgrade() {
            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();

            int balance = 100;
            builder.buildTower(typicalTower, cell, balance);
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            int balanceToUpgrade = 10;
            BuildingResponse expectedUpgrade = new BuildingResponse(balanceToUpgrade, false);

            //----------------------------------------------------------------------------------------------------------

            BuildingResponse actualUpgrade = builder.upgradeTower(tower, upgradableCharacteristic, balanceToUpgrade);

            //----------------------------------------------------------------------------------------------------------

            assertBuildingResponsesEquals(expectedUpgrade, actualUpgrade);
        }
    }
}