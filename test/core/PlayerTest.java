package core;

import economic.BankAccount;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projectile.Projectile;
import tower.Tower;
import tower.TowersCatalogue;
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
}