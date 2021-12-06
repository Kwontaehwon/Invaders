import engine.*;
import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship ship;
    Set<Boom> booms;
    int boomTimes;
    @BeforeEach
    void setUp() {
        DesignSetting designSetting = new DesignSetting(DrawManager.SpriteType.Ship);
        ship = new Ship(0, 0, designSetting.getSizeX(), designSetting.getSizeY(), designSetting.getShipType());
        booms = new HashSet<Boom>();
        int boomTimes = 1;
    }

    @Test
    void move() {
        ship.moveRight();
        assertEquals(2,ship.getPositionX());
        ship.moveLeft();
        assertEquals(0,ship.getPositionX());
    }

    @Test
    void boomShoot() {
        assertEquals(true,ship.boomShoot(booms));
        boomTimes--;
        assertEquals(false,ship.boomShoot(booms));

    }


    @Test
    void destroy() {
        this.ship.destroy();
        assertEquals(true,ship.isDestroyed());
    }


    @Test
    void getShootingCoolDown() {
        assertEquals(750,ship.getShootingCoolDown());
        ship.setShootingCoolDown(450);
        assertEquals(450,ship.getShootingCoolDown());
    }

    @Test
    void getBulletSpeed() {
        assertEquals(-6,ship.getBulletSpeed());
        ship.setBulletSpeed(-8);
        assertEquals(-8,ship.getBulletSpeed());
    }
}