//package projectile;
//
//import collision.Hitbox;
//import org.junit.jupiter.api.Test;
//import utils.Direction;
//import utils.Position;
//
//import java.awt.*;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class LinearMovingProjectileTest {
//    private LinearMovingProjectile projectile;
//
//    private int coefToLinearizeDistanceBySeconds = 1000;
//
////    @Test
////    void speedIsZero() {
////        int speed = 0;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        mp.move();
////
////        Position expPosition = new Position(1, 1);
////        Position actualPosition = mp.position();
////        assertEquals(expPosition, actualPosition);
////    }
////
////    @Test
////    void speedIsNegative() {
////        int speed = -5;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////
////        assertThrows(IllegalArgumentException.class, () -> {
////            new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////        });
////    }
////
////    @Test
////    void maxDistanceIsZero() {
////        int speed = 0;
////        int maxDistance = 0;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////
////        assertThrows(IllegalArgumentException.class, () -> {
////            new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////        });
////    }
////
////    @Test
////    void maxDistanceIsNegative() {
////        int speed = 0;
////        int maxDistance = -1;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////
////        assertThrows(IllegalArgumentException.class, () -> {
////            new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////        });
////    }
////
////    @Test
////    void speedIsPositive() {
////        int speed = 1;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(1);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(1, 2);
////        boolean expActive = true;
////        int expTraveledDistance = 1;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void directionIsWest() {
////        int speed = 1;
////        int maxDistance = 1;
////        Direction direction = Direction.WEST;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(1);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(0, 1);
////        boolean expActive = true;
////        int expTraveledDistance = 1;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void directionIsSouth() {
////        int speed = 1;
////        int maxDistance = 1;
////        Direction direction = Direction.SOUTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(1);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(1, 0);
////        boolean expActive = true;
////        int expTraveledDistance = 1;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void directionIsEast() {
////        int speed = 1;
////        int maxDistance = 1;
////        Direction direction = Direction.EAST;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(1);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(2, 1);
////        boolean expActive = true;
////        int expTraveledDistance = 1;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void traveledMaxDistance() {
////        int speed = 1;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(10);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(1, 11);
////        boolean expActive = true;
////        int expTraveledDistance = 10;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void movingAfterReachedMaxDistance() {
////        int speed = 1;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long firstMovingTime = TimeUnit.SECONDS.toMillis(10);
////        int moveDistance = (int)(firstMovingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(firstMovingTime, moveDistance);
////        long movingTime = TimeUnit.SECONDS.toMillis(1);
////        moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(1, 11);
////        boolean expActive = false;
////        int expTraveledDistance = 10;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(firstMovingTime, mp.lastMoveTime());
////    }
////
////    @Test
////    void traveledMoreThanMaxDistance() {
////        int speed = 10;
////        int maxDistance = 10;
////        Direction direction = Direction.NORTH;
////        Hitbox hitbox = new Hitbox(new Rectangle(0, 0, 2, 2), 0);
////        int damage = 15;
////        Position startPosition = new Position(1, 1);
////        LinearMovingProjectile mp = new LinearMovingProjectile(speed, maxDistance, direction, hitbox, damage, startPosition);
////
////        long movingTime = TimeUnit.SECONDS.toMillis(10);
////        int moveDistance = (int)(movingTime * speed) / coefToLinearizeDistanceBySeconds;
////        mp.move(movingTime, moveDistance);
////
////        Position expPosition = new Position(1, 11);
////        boolean expActive = true;
////        int expTraveledDistance = 10;
////        assertEquals(expPosition, mp.position());
////        assertEquals(expActive, mp.active());
////        assertEquals(expTraveledDistance, mp.totalTraveledDistance());
////        assertEquals(movingTime, mp.lastMoveTime());
////    }
//}
