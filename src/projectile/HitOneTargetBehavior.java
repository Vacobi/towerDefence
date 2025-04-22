package projectile;

import monster.Monster;

public class HitOneTargetBehavior implements ProjectileBehavior {

    private Projectile projectile;

    public HitOneTargetBehavior(Projectile projectile) {
        this.projectile = projectile;
    }

    public HitOneTargetBehavior() {
        ;
    }

    public void setProjectile(Projectile projectile) {
        if (this.projectile != null) {
            throw new IllegalStateException("Projectile already set");
        }

        this.projectile = projectile;
    }

    @Override
    public void applyEffect(long currentTick) {
        if (projectile == null) {
            throw new IllegalStateException("Projectile not set");
        }

        for (Monster monster : projectile.getField().getWave().getAliveMonsters()) {
            if (hitMonster(monster)) {
                return;
            }
        }
    }

    protected boolean hitMonster(Monster monster) {
        if (projectile.collidesWith(monster)) {
            monster.applyDamage(projectile.getDamage());
            projectile.deactivate();
            return true;
        }

        return false;
    }
}
