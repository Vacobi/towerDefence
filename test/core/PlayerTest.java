package core;

import economic.BankAccount;
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

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    Builder builder = new Builder(field);

    @Nested
    class Build {
        @Test
        void towerBuilt() {
            Player player = new Player(builder);
            int balance = 50;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            Optional<Integer> optionalTowerCost = towersCatalogue.getPrice(typicalTower);
            int towerCost = optionalTowerCost.get();

            int expectedBalance = balance - towerCost;
            boolean expectedBuilt = true;

            boolean actualBuilt = player.placeTower(typicalTower, cell);

            assertEquals(expectedBuilt, actualBuilt);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }

        @Test
        void notEnoughGoldToBuild() {
            Player player = new Player(builder);
            int initialBalance = 5;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(initialBalance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            boolean expectedBuilt = false;

            boolean actualBuilt = player.placeTower(typicalTower, cell);

            assertEquals(expectedBuilt, actualBuilt);
            assertEquals(initialBalance, player.getBankAccount().getGold());
        }

        @Test
        void buildWhenPlayerFrozen() {
            Player player = new Player(builder);
            int balance = 50;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);
            player.freeze(true);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);
            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();

            int expectedBalance = balance;
            boolean expectedBuilt = false;

            boolean actualBuilt = player.placeTower(typicalTower, cell);

            assertEquals(expectedBuilt, actualBuilt);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }
    }

    @Nested
    class Upgrade {

        @Test
        void upgradeTower() {
            Player player = new Player(builder);
            int balance = 100;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int buildCost = towersCatalogue.getAvailableTowersWithPrices().get(typicalTower);

            player.placeTower(typicalTower, cell);
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            int levelBeforeUpgrade = 1;

            Optional<Integer> optionalUpgradeCost = towersCatalogue.getUpgradePrice(upgradableCharacteristic, levelBeforeUpgrade + 1);
            int upgradeCost = optionalUpgradeCost.get();

            boolean expectedUpgrade = true;

            int expectedBalance = balance - buildCost - upgradeCost;

            //----------------------------------------------------------------------------------------------------------

            boolean actualUpgrade = player.upgradeTower(tower, upgradableCharacteristic);

            Optional<Integer> optionalLevelOfCharacteristicAfterUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelAfterUpgrade = optionalLevelOfCharacteristicAfterUpgrade.get();

            assertEquals(expectedUpgrade, actualUpgrade);
            assertEquals(levelBeforeUpgrade + 1, levelAfterUpgrade);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }

        @Test
        void towerCancelledUpgrade() {
            Player player = new Player(builder);
            int balance = 100;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int buildCost = towersCatalogue.getAvailableTowersWithPrices().get(typicalTower);

            player.placeTower(typicalTower, cell);
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            int expectedBalance = balance - buildCost;

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            int levelBeforeUpgrade = 1;

            Optional<Integer> optionalUpgradeCost = towersCatalogue.getUpgradePrice(upgradableCharacteristic, 2);
            int upgradeCost = optionalUpgradeCost.get();
            expectedBalance -= upgradeCost;

            optionalUpgradeCost = towersCatalogue.getUpgradePrice(upgradableCharacteristic, 3);
            upgradeCost = optionalUpgradeCost.get();
            expectedBalance -= upgradeCost;

            player.upgradeTower(tower, upgradableCharacteristic);
            player.upgradeTower(tower, upgradableCharacteristic);

            boolean expectedUpgrade = false;

            //----------------------------------------------------------------------------------------------------------

            boolean actualUpgrade = player.upgradeTower(tower, upgradableCharacteristic);

            Optional<Integer> optionalLevelOfCharacteristicAfterUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelAfterUpgrade = optionalLevelOfCharacteristicAfterUpgrade.get();

            assertEquals(expectedUpgrade, actualUpgrade);
            assertEquals(levelBeforeUpgrade + 2, levelAfterUpgrade);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }

        @Test
        void notEnoughGoldToUpgrade() {
            Player player = new Player(builder);
            int balance = 25;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int buildCost = towersCatalogue.getAvailableTowersWithPrices().get(typicalTower);

            player.placeTower(typicalTower, cell);
            Tower tower = cell.getTower();

            //----------------------------------------------------------------------------------------------------------

            int expectedBalance = balance - buildCost;

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            int levelBeforeUpgrade = 1;

            boolean expectedUpgrade = false;

            //----------------------------------------------------------------------------------------------------------

            boolean actualUpgrade = player.upgradeTower(tower, upgradableCharacteristic);

            Optional<Integer> optionalLevelOfCharacteristicAfterUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelAfterUpgrade = optionalLevelOfCharacteristicAfterUpgrade.get();

            assertEquals(expectedUpgrade, actualUpgrade);
            assertEquals(levelBeforeUpgrade, levelAfterUpgrade);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }

        @Test
        void upgradeTowerWhenPlayerFrozen() {
            Player player = new Player(builder);
            int balance = 100;
            BankAccount bankAccount = player.getBankAccount();
            bankAccount.setGold(balance);

            TowersCatalogue towersCatalogue = builder.getTowersCatalogue();
            Position cellPosition = new Position(0, 0);
            Cell cell = new Cell(cellPosition);

            Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
            Tower typicalTower = towers.keySet().iterator().next();
            int buildCost = towersCatalogue.getAvailableTowersWithPrices().get(typicalTower);

            player.placeTower(typicalTower, cell);
            Tower tower = cell.getTower();

            player.freeze(true);

            //----------------------------------------------------------------------------------------------------------

            TowerUpgradableCharacteristic upgradableCharacteristic = TowerUpgradableCharacteristic.DAMAGE;

            int levelBeforeUpgrade = 1;

            boolean expectedUpgrade = false;

            int expectedBalance = balance - buildCost;

            //----------------------------------------------------------------------------------------------------------

            boolean actualUpgrade = player.upgradeTower(tower, upgradableCharacteristic);

            Optional<Integer> optionalLevelOfCharacteristicAfterUpgrade = tower.getLevelOfCharacteristic(upgradableCharacteristic);
            int levelAfterUpgrade = optionalLevelOfCharacteristicAfterUpgrade.get();

            assertEquals(expectedUpgrade, actualUpgrade);
            assertEquals(levelBeforeUpgrade, levelAfterUpgrade);
            assertEquals(expectedBalance, player.getBankAccount().getGold());
        }
    }
}