package projectile.behavior;

import collision.HitboxParameters;
import monster.Monster;
import projectile.ExplosiveProjectile;

import java.util.HashSet;
import java.util.Set;

public class ExplosiveBehavior implements ProjectileBehavior<ExplosiveProjectile> {

    private ExplosiveProjectile projectile;

    public ExplosiveBehavior(ExplosiveProjectile projectile) {
        this.projectile = projectile;
    }

    public ExplosiveBehavior() {
        this(null);
    }

    @Override
    public void applyEffect(long currentTick) {
        if (projectile == null) {
            throw new IllegalStateException("Projectile not set");
        }

        if (!projectile.active()) {
            throw new IllegalStateException("Explosive Projectile should be active");
        }

        Set<Monster> monsters = new HashSet<>(projectile.getField().getWave().getAliveMonsters());

        if (collidesAtLeastWithOne(monsters)) {
            explode();

            damageCollideableMonsters(monsters);

            projectile.deactivate();
        }
    }

    private boolean collidesAtLeastWithOne(Set<Monster> monsters) {
        for (Monster monster : monsters) {
            if (projectile.collidesWith(monster)) {
                return true;
            }
        }

        return false;
    }

    private void explode() {
        projectile.updateHitboxParameters(
                new HitboxParameters(
                        projectile.getRadius() * 2, // diameter in the Hitbox
                        projectile.getRadius() * 2, // diameter in the Hitbox
                        projectile.getHitbox().getHitboxParameters().angle()
                )
        );
        projectile.setExploded();
    }

    private void damageCollideableMonsters(Set<Monster> monsters) {
        for (Monster monster : monsters) {
            if (projectile.collidesWith(monster)) {
                monster.applyDamage(projectile.getDamage());
            }
        }
    }

    @Override
    public ExplosiveBehavior clone() {
        return new ExplosiveBehavior();
    }

    @Override
    public void setProjectile(ExplosiveProjectile projectile) {
        if (this.projectile != null) {
            throw new IllegalStateException("Projectile already set");
        }

        this.projectile = projectile;
    }

    @Override
    public ExplosiveProjectile getProjectile() {
        return projectile;
    }
}
