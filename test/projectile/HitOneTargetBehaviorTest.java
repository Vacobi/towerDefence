package projectile;

import core.Field;
import factory.MonsterFactory;
import factory.ProjectileFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class HitOneTargetBehaviorTest {
    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    private final MonsterFactory monsterFactory = new MonsterFactory();
    private final Position monsterPosition = new Position(100, 100);
    private final MovingMonsterStrategy strategy = new PlainRoadMoving(monsterPosition, field, 10);

    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    @Nested
    class HitMonsterTest {

        @Test
        void collidesWithMonster() {
            Monster monster = monsterFactory.createMonster(strategy);
            Projectile projectile = projectileFactory.createMovingProjectile(monster.getPosition(), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = monster.getHealth() - projectile.getDamage();

            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertFalse(projectile.active());
        }

        @Test
        void notCollidesWithMonster() {
            MovingMonsterStrategy notCollidingStrategy = new PlainRoadMoving(new Position(0, 0), field, 10);
            Monster monster = monsterFactory.createMonster(notCollidingStrategy);
            Projectile projectile = projectileFactory.createMovingProjectile(new Position(100, 100), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = monster.getHealth();

            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertTrue(projectile.active());
        }

        @Test
        void monsterDies() {
            Monster monster = monsterFactory.createMonster(strategy);
            Projectile projectile = projectileFactory.createMovingProjectile(monster.getPosition(), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = 0;

            monster.applyDamage(monster.getHealth() - projectile.getDamage());
            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertFalse(projectile.active());
        }
    }
}