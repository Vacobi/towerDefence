package monster;

import collision.CollisionObject;
import collision.HitboxParameters;
import events.MonsterListener;
import utils.CoordinatesConverter;
import utils.Position;

import java.util.ArrayList;
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
        this.health = health;
        this.strategy = strategy;
        listeners = new ArrayList<>();
    }

    public void addListener(MonsterListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MonsterListener listener) {
        listeners.remove(listener);
    }

    public void move(long currentTick) {
        if (strategy.monsterReachedEnd()) {
            return;
        }

        strategy.moveMonster(currentTick);
        updateHitboxPosition(
                CoordinatesConverter.centerToLeftTop(
                        strategy.currentPosition(),
                        getHitbox().getHitboxParameters().width(),
                        getHitbox().getHitboxParameters().height()
                )
        );

        if (strategy.monsterReachedEnd()) {
            monsterReachedEnd();
        }
    }

    public void applyDamage(int damage) {
        health = Math.max(health - damage, 0);

        if (health == 0) {
            monsterDied();
        }
    }

    private void monsterDied() {
        for(MonsterListener listener : listeners) {
            listener.onMonsterDeath(this);
        }
    }

    private void monsterReachedEnd() {
        for(MonsterListener listener : listeners) {
            listener.onMonsterReachedEnd(this);
        }
    }

    public Position getPosition() {
        return strategy.currentPosition();
    }

    public int getHealth() {
        return health;
    }
}
