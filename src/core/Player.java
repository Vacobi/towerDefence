package core;

import economic.BankAccount;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import utils.BuildingResponse;

public class Player {
    private final Builder builder;
    private final BankAccount bankAccount;
    private boolean frozen;
    private int lives;

    public Player(Builder builder) {
        this.builder = builder;
        this.bankAccount = new BankAccount();
        frozen = false;
        lives = 10;
    }

    public void reduceLives(int lives) {
        this.lives -= lives;
    }

    public boolean placeTower(Class<? extends Tower> tower, Cell cell) {
        BuildingResponse response = builder.buildTower(tower, cell, bankAccount.gold);
        if (frozen) {
            return false;
        }

        bankAccount.setGold(response.change());
        return response.built();
    }

    public boolean upgradeTower(Tower tower, TowerUpgradableCharacteristic towerUpgrade) {
        if (frozen) {
            return false;
        }

        BuildingResponse response = builder.upgradeTower(tower,towerUpgrade, bankAccount.gold);
        bankAccount.setGold(response.change());
        return response.built();
    }

    // - /////////////////////////////////////////////////////////////
    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public boolean frozen() {
        return this.frozen;
    }

    public void freeze(boolean frozen) {
        this.frozen = frozen;
    }

    public int getLives() {
        return this.lives;
    }
}
