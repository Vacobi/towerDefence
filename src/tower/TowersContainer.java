package tower;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class TowersContainer implements Iterable<Tower> {
    private final Set<Tower> towers;

    public TowersContainer() {
        towers = new HashSet<>();
    }

    //------------------------------------------------------------------------------------------------------------------

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
