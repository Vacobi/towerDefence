package monster;

import collision.CollisionObject;
import collision.HitboxParameters;
import events.MonsterEvent;
import events.MonsterListener;
import utils.CoordinatesConverter;
import utils.Position;

import java.util.LinkedList;
import java.util.List;

public class Monster extends CollisionObject {
    private int health;
    private final List<MonsterListener> listeners;
    private final MovingMonsterStrategy strategy;

    public Monster(HitboxParameters parameters, MovingMonsterStrategy strategy, int health) {
        super(
                CoordinatesConverter.centerToLeftTop(strategy.currentPosition(), parameters.width(), parameters.height()),
                parameters
        );

        if (health <= 0) {
            throw new IllegalArgumentException("Health must be greater than 0");
        }
        this.health = health;

        strategy.setMonster(this);
        this.strategy = strategy;

        listeners = new LinkedList<>();
    }

    //------------------------------------------------------------------------------------------------------------------

    public void move(long currentTick) {

        strategy.moveMonster(currentTick);
        updateHitboxPosition(strategy.currentPosition());

        if (strategy.monsterReachedEnd()) {
            fireMonsterReachedEnd();
        }
    }

    public void applyDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }

        health = Math.max(health - damage, 0);

        if (health == 0) {
            fireMonsterDeath();
        }
    }

    public boolean stillInWave() {
        return isAlive() && !hasReachedEnd();
    }

    public boolean hasReachedEnd() {
        return strategy.monsterReachedEnd();
    }

    public boolean isAlive() {
        return health > 0;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Position getPosition() {
        return strategy.currentPosition();
    }

    public int getHealth() {
        return health;
    }

    public void addListener(MonsterListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MonsterListener listener) {
        listeners.remove(listener);
    }

    //------------------------------------------------------------------------------------------------------------------

    private void fireMonsterDeath() {
        MonsterEvent event = new MonsterEvent(this);
        event.setMonster(this);

        listeners.forEach(l -> l.onMonsterDeath(event));
    }

    private void fireMonsterReachedEnd() {
        MonsterEvent event = new MonsterEvent(this);
        event.setMonster(this);

        listeners.forEach(l -> l.onMonsterReachedEnd(event));
    }
}
