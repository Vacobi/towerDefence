package tower;

import core.Field;
import exception.TowersContainerAlreadyHasOtherField;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class TowersContainer implements Iterable<Tower> {
    private final Set<Tower> towers;
    private Field field;

    public TowersContainer(Field field) {
        towers = new HashSet<>();
        this.field = field;
    }

    public void setField(Field field) {
        if (this.field != null) {
            throw new TowersContainerAlreadyHasOtherField(this);
        }

        this.field = field;
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    @Override
    public Iterator<Tower> iterator() {
        return towers.iterator();
    }

    @Override
    public void forEach(Consumer<? super Tower> action) {
        Iterable.super.forEach(action);
    }
}
