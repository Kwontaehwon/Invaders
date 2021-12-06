package entity;

import engine.DrawManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import entity.Bullet;
import engine.*;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    Bullet bullet;
    @BeforeEach
    void setUp(){
        bullet = new Bullet(1,2,3,4);
    }

    @Test
    void setSprite(){
        bullet.setSpeed(0,-6);
        bullet.setSprite();
        assertEquals(DrawManager.SpriteType.Bullet1, bullet.getSprite());
        bullet.setSpeed(0,-8);
        bullet.setSprite();
        assertEquals(DrawManager.SpriteType.Bullet2, bullet.getSprite());
    }

    @Test
    void setSpeed(){
        bullet.setSpeed(5,8);
        assertEquals(5,bullet.getSpeedX());
        assertEquals(8,bullet.getSpeedY());
    }

    @Test
    void update(){
        bullet.update(false);
        assertEquals(4,bullet.getPositionX());
        assertEquals(6,bullet.getPositionY());
        bullet.update(true);
        assertEquals(4,bullet.getPositionX());
        assertEquals(7,bullet.getPositionY());
    }
}