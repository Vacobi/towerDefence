package monster;

import core.Field;
import events.MonsterEvent;
import events.MonsterListener;
import factory.MonsterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import road.RoadSegment;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {

    Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
            .toAbsolutePath()
            .normalize();
    Field field = new Field(path.toString());

    MonsterFactory factory = new MonsterFactory();

    @Nested
    class ApplyDamageTest implements MonsterListener{

        private int monsterReachedEnd;
        private int monsterDeath;
        @BeforeEach
        void setUp() {
            monsterReachedEnd = 0;
            monsterDeath = 0;
        }

        @Test
        void applyDamageToDeath() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);

            monster.applyDamage(monster.getHealth());

            assertEquals(0, monster.getHealth());
            assertFalse(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(1, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Test
        void applyMoreDamageThanHealth() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);

            monster.applyDamage(monster.getHealth() + 10);

            assertEquals(0, monster.getHealth());
            assertFalse(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(1, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Test
        void applyDamageButAlive() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);

            int fullHealth = monster.getHealth();
            int damage = fullHealth - 10;
            monster.applyDamage(damage);

            assertEquals(fullHealth - damage, monster.getHealth());
            assertTrue(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(0, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Test
        void zeroDamage() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);

            int fullHealth = monster.getHealth();
            monster.applyDamage(0);

            assertEquals(fullHealth, monster.getHealth());
            assertTrue(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(0, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Test
        void negativeDamage() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);

            int fullHealth = monster.getHealth();
            assertThrows(IllegalArgumentException.class, () -> monster.applyDamage(-1));

            assertEquals(fullHealth, monster.getHealth());
            assertTrue(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(0, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Override
        public void onMonsterDeath(MonsterEvent event) {
            monsterDeath++;
        }

        @Override
        public void onMonsterReachedEnd(MonsterEvent event) {
            monsterReachedEnd++;
        }
    }

    @Nested
    class Move implements MonsterListener{ // Все тесты в стратегии перемещения

        private int monsterReachedEnd;
        private int monsterDeath;
        @BeforeEach
        void setUp() {
            monsterReachedEnd = 0;
            monsterDeath = 0;
        }

        @Test
        void monsterReachedEnd() {
            int speed = 10;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);
            int fullHealth = monster.getHealth();

            // initialize first tick
            long tickTime = System.currentTimeMillis();
            monster.move(tickTime);
            tickTime += TimeUnit.SECONDS.toMillis(100);

            RoadSegment roadSegment = field.getRoad().getRoadSegments().get(0);
            Position positionOfRoadStart = roadSegment.getStart();
            Position expectedPosition = positionOfRoadStart.move(roadSegment.getDirection(), roadSegment.getLength());

            //----------------------------------------------------------------------------------------------------------

            monster.move(tickTime);

            //----------------------------------------------------------------------------------------------------------

            assertEquals(fullHealth, monster.getHealth());
            assertEquals(expectedPosition, monster.getPosition());
            assertTrue(monster.isAlive());
            assertTrue(monster.hasReachedEnd());
            assertEquals(0, monsterDeath);
            assertEquals(1, monsterReachedEnd);
        }

        @Test
        void monsterDoesntMove() {
            int speed = 0;
            PlainRoadMoving movingStrategy = new PlainRoadMoving(field, speed);

            Monster monster = factory.createMonster(movingStrategy);
            monster.addListener(this);
            int fullHealth = monster.getHealth();

            // initialize first tick
            long tickTime = System.currentTimeMillis();
            monster.move(tickTime);
            tickTime += TimeUnit.SECONDS.toMillis(100);

            RoadSegment roadSegment = field.getRoad().getRoadSegments().get(0);
            Position positionOfRoadStart = roadSegment.getStart();

            //----------------------------------------------------------------------------------------------------------

            monster.move(tickTime);

            //----------------------------------------------------------------------------------------------------------

            assertEquals(fullHealth, monster.getHealth());
            assertEquals(positionOfRoadStart, monster.getPosition());
            assertTrue(monster.isAlive());
            assertFalse(monster.hasReachedEnd());
            assertEquals(0, monsterDeath);
            assertEquals(0, monsterReachedEnd);
        }

        @Override
        public void onMonsterDeath(MonsterEvent event) {
            monsterDeath++;
        }

        @Override
        public void onMonsterReachedEnd(MonsterEvent event) {
            monsterReachedEnd++;
        }
    }
}