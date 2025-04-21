package projectile;

import monster.Monster;

public class HitOneTargetBehavior implements ProjectileBehavior {

    private final Projectile projectile;

    public HitOneTargetBehavior(Projectile projectile) {
        this.projectile = projectile;
    }

    @Override
    public void applyEffect(long currentTick) {
        for (Monster monster : projectile.getField().getWave().getAliveMonsters()) {
            if (projectile.collidesWith(monster)) {
                monster.applyDamage(projectile.getDamage());
                projectile.deactivate();
                return;
            }
        }
    }
}
