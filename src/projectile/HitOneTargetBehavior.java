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
