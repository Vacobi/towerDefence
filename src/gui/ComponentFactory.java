package gui;

import core.AbstractCell;
import core.Cell;
import core.RoadCell;
import monster.Monster;
import projectile.LaserProjectile;
import projectile.PlainProjectile;
import tower.Tower;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ComponentFactory {
    private static final Map<Class<?>, Function<?, GameComponent<?>>> creators = new HashMap<>();

    private static final int towerSize = AbstractCell.getSize();

    static {
        creators.put(Cell.class,
                (Function<Cell, GameComponent<?>>) c -> new CellComponent(c, c.getSize(), c.getSize()));
        creators.put(RoadCell.class,
                (Function<RoadCell, GameComponent<?>>) c -> new RoadCellComponent(c, c.getSize(), c.getSize()));
        creators.put(Monster.class,
                (Function<Monster, GameComponent<?>>) m -> new MonsterComponent(m, 450, 450));
        creators.put(PlainProjectile.class,
                (Function<PlainProjectile, GameComponent<?>>) p -> new PlainProjectileComponent(p, 450, 450));
        creators.put(LaserProjectile.class,
                (Function<LaserProjectile, GameComponent<?>>) l -> new LaserProjectileComponent(l, 450, 450));
        creators.put(Tower.class,
                (Function<Tower<?>, GameComponent<?>>) t -> new TowerComponent(t, towerSize, towerSize));
    }

    @SuppressWarnings("unchecked")
    public static <M> GameComponent<M> create(M model) {
        @SuppressWarnings("unchecked")
        Function<M, GameComponent<?>> func = (Function<M, GameComponent<?>>) creators.get(model.getClass());
        if (func == null) {
            for (Map.Entry<Class<?>, Function<?, GameComponent<?>>> entry : creators.entrySet()) {
                if (entry.getKey().isAssignableFrom(model.getClass())) {
                    func = (Function<M, GameComponent<?>>) entry.getValue();
                    break;
                }
            }
        }
        if (func != null) {
            return (GameComponent<M>) func.apply(model);
        }
        throw new IllegalArgumentException("No creator for model: " + model.getClass());
    }
}
