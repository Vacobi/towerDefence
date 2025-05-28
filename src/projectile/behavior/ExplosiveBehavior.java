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
        for (Monster monster : monsters) {
            if (projectile.collidesWith(monster)) {
                if (!projectile.isExploded()) {
                    projectile.updateHitboxParameters(
                            new HitboxParameters(
                                    projectile.getRadius(),
                                    projectile.getRadius(),
                                    projectile.getHitbox().getHitboxParameters().angle()
                            )
                    );
                    projectile.setExploded();
                }
                monster.applyDamage(projectile.getDamage());
            }
        }

        if (projectile.isExploded()) {
            projectile.deactivate();
        }
    }

    @Override
    public ProjectileBehavior clone() {
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
