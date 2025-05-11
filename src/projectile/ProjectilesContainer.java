package projectile;

import core.Field;

import java.util.*;
import java.util.function.Consumer;

public class ProjectilesContainer implements Iterable<Projectile> {
    private final List<Projectile> projectiles;
    private final Field field;

    public ProjectilesContainer(Field field) {
        projectiles = new LinkedList<>();

        this.field = field;
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

    @Override
    public Iterator<Projectile> iterator() {
        return projectiles.iterator();
    }

    @Override
    public void forEach(Consumer<? super Projectile> action) {
        Iterable.super.forEach(action);
    }
}
