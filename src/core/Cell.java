package core;

import events.CellEvent;
import events.CellListener;
import exception.CellAlreadyHasTower;
import projectile.Projectile;
import tower.Tower;
import utils.Position;

import java.util.LinkedList;
import java.util.List;

public class Cell extends AbstractCell implements Cloneable {
    private Tower tower;

    private List<CellListener> listeners;

    public Cell(Position position) {
        super(position);

        listeners = new LinkedList<>();
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        if (this.tower != null) {
            throw new CellAlreadyHasTower(this);
        }

        this.tower = tower;

        fireTowerBuilt(tower);
    }

    private void fireTowerBuilt(Tower<? extends Projectile> tower) {
        CellEvent event = new CellEvent(this);
        event.setTower(tower);

        listeners.forEach(l -> l.onTowerBuilt(event));
    }

    public boolean canPlaceTower() {
        return tower == null;
    }

    @Override
    public Cell clone() {
        return new Cell(position());
    }

    public boolean hasTower() {
        return tower != null;
    }

    public void addListener(CellListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CellListener listener) {
        listeners.remove(listener);
    }
}
