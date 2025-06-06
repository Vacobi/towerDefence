package projectile.behavior;

import monster.Monster;
import projectile.LaserProjectile;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LaserBehavior implements ProjectileBehavior<LaserProjectile> {

    private LaserProjectile projectile;

    private final long startTime;

    private final Map<Monster, Long> lastDamageTimes;

    public LaserBehavior(LaserProjectile projectile) {
        this.projectile = projectile;

        startTime = System.currentTimeMillis();

        lastDamageTimes = new HashMap<>();
    }

    //------------------------------------------------------------------------------------------------------------------

    public LaserBehavior() {
        this(null);
    }

    @Override
    public void applyEffect(long currentTick) {
        if (projectile == null) {
            throw new IllegalStateException("Projectile not set");
        }

        if (!projectile.active()) {
            throw new IllegalStateException("Laser Projectile should be active");
        }

        if (activeTimeOut(currentTick)) {
            projectile.deactivate();
            return;
        }

        List<Monster> targets = new LinkedList<>(projectile.getField().getWave().getAliveMonsters());

        for (Monster monster : targets) {
            if (projectile.collidesWith(monster) && canDamage(monster, currentTick)) {
                monster.applyDamage(projectile.getDamage());

                if (!monster.isAlive()) {
                    lastDamageTimes.remove(monster);
                } else {
                    lastDamageTimes.put(monster, currentTick);
                }
            }
        }
    }

    private boolean activeTimeOut(long currentTick) {
        return currentTick - startTime > projectile.getActiveTime();
    }

    private boolean canDamage(Monster monster, long currentTick) {
        return !lastDamageTimes.containsKey(monster) ||
                currentTick - lastDamageTimes.get(monster) > projectile.getDamageCooldown();
    }

    @Override
    public LaserBehavior clone() {
        return new LaserBehavior();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void setProjectile(LaserProjectile projectile) {
        if (this.projectile != null) {
            throw new IllegalStateException("Projectile already set");
        }

        this.projectile = projectile;
    }

    @Override
    public LaserProjectile getProjectile() {
        return projectile;
    }

    long getStartTime() {
        return startTime;
    }
}
