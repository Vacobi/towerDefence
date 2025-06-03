package projectile;

import java.util.*;
import java.util.function.Consumer;

public class ProjectilesContainer implements Iterable<Projectile> {
    private final List<Projectile> projectiles;

    public ProjectilesContainer() {
        projectiles = new LinkedList<>();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Iterator<Projectile> iterator() {
        return projectiles.iterator();
    }

    @Override
    public void forEach(Consumer<? super Projectile> action) {
        Iterable.super.forEach(action);
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<Projectile> getProjectiles() {
        return new LinkedList<>(projectiles);
    }

    public void clearProjectiles() {
        projectiles.clear();
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void addProjectiles(List<Projectile> projectiles) {
        this.projectiles.addAll(projectiles);
    }

    public void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }
}
