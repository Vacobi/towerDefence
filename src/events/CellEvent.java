package events;

import projectile.Projectile;
import tower.Tower;

import java.util.EventObject;

public class CellEvent extends EventObject {

    Tower<? extends Projectile> tower;

    public void setTower(Tower<? extends Projectile> tower) {
        this.tower = tower;
    }

    public Tower<? extends Projectile> getTower() {
        return tower;
    }

    public CellEvent(Object source) {
        super(source);
    }
}
