package core;

import economic.BankAccount;
import events.BankAccountEvent;
import events.BankAccountListener;
import events.PlayerListener;
import projectile.Projectile;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import utils.BuildingResponse;

import java.util.LinkedList;
import java.util.List;

public class Player implements BankAccountListener {
    private final Builder builder;
    private final BankAccount bankAccount;
    private boolean frozen;
    private int lives;

    private final List<PlayerListener> listeners;

    public Player(Builder builder) {
        this.builder = builder;
        this.bankAccount = new BankAccount();
        this.bankAccount.addListener(this);
        frozen = false;
        lives = 5;

        listeners = new LinkedList<>();
    }

    public void reduceLives(int lives) {
        this.lives -= lives;

        listeners.forEach(listener -> listener.onPlayerLostLive(this));
    }

    public boolean placeTower(Tower<? extends Projectile> typicalTower, Cell cell) {
        if (frozen) {
            return false;
        }

        BuildingResponse response = builder.buildTower(typicalTower, cell, bankAccount.getGold());
        bankAccount.setGold(response.change());
        return response.built();
    }

    public boolean upgradeTower(Tower<? extends Projectile> tower, TowerUpgradableCharacteristic towerUpgrade) {
        if (frozen) {
            return false;
        }

        BuildingResponse response = builder.upgradeTower(tower,towerUpgrade, bankAccount.getGold());
        bankAccount.setGold(response.change());
        return response.built();
    }

    public boolean canBuildTower(Tower<? extends Projectile> tower, Cell cell) {
        return !frozen && builder.canBuildTower(tower, cell, bankAccount.getGold());
    }

    public boolean canBuildTower(Tower<? extends Projectile> tower) {
        return !frozen && builder.enoughGoldToBuild(tower, bankAccount.getGold());
    }

    public boolean enoughGoldToBuild(Tower<? extends Projectile> tower) {
        return builder.enoughGoldToBuild(tower, bankAccount.getGold());
    }

    public boolean canUpgradeTower(Tower<? extends Projectile> tower, TowerUpgradableCharacteristic upgrade) {
        return !frozen && builder.canUpgradeTower(tower, upgrade, bankAccount.getGold());
    }

    public boolean enoughGoldTUpgrade(Tower<? extends Projectile> tower, TowerUpgradableCharacteristic upgrade) {
        return builder.enoughGoldToUpgrade(tower, upgrade, bankAccount.getGold());
    }

    // - /////////////////////////////////////////////////////////////
    public void addListener(PlayerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PlayerListener listener) {
        listeners.remove(listener);
    }

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

    @Override
    public void onGoldCountChange(BankAccountEvent event) {
        listeners.forEach(listener -> listener.onChangedPlayerGoldCount(this));
    }

    public Builder getBuilder() {
        return builder;
    }
}
