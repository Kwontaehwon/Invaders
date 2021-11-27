package entity;

import engine.DrawManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {
    Bullet bullet;
    Ship ship;
    int BULLET_SPEED = 4;
    int speedX;
    int speedY;

    @BeforeEach
    void setUp() {
        int xPos = (int) (Math.random() * 500);
        int yPos = (int) (Math.random() * 200) + 300;
        int xPos1 = (int) (Math.random() * 500);
        int yPos1 = (int) (Math.random() * 300);
        ship = new Ship(xPos, yPos, DrawManager.SpriteType.Ship);
        int difX = xPos-xPos1;
        int difY = yPos-yPos1;
        int divideNum = 200;
        if(difX>200) divideNum = (int) (difX*0.95);
        else if(difX<-200) divideNum = (int) (difX* -0.95);
        speedX = difX*BULLET_SPEED/divideNum;
        speedY = difY*BULLET_SPEED/divideNum;
        bullet = new Bullet(xPos1, yPos1, speedX, speedY);
    }


    @Test
    void setSprite() {
        bullet.setSprite();
        assertEquals(speedY < 0, bullet.getSpriteType() == DrawManager.SpriteType.Bullet);
        assertEquals(speedY > 0, bullet.getSpriteType() == DrawManager.SpriteType.EnemyBullet);
    }

    @Test
    void setSpeed() {
        bullet.setSpeed(0);
        assertEquals(0, bullet.getSpeedY());
        bullet.setSpeed(101);
        assertEquals(101, bullet.getSpeedY());
    }

    @Test
    void update() {
        int a=0;
        boolean result;
        int x,y;
        do{
            x=bullet.positionX;
            y=bullet.positionY;
            bullet.update(false);
            result = bullet.getPositionX()==x+speedX;
            result = bullet.getPositionY()==y+speedY && result;
            ++a;
        }
        while(a<30 && result);

        assertTrue(result);
    }

    @Test
    void targetOneTime() {
        assertTrue(target());
    }
    @Test
    void targetManyTimes() {
        int c=0;
        int result = 0;
        int TIMES = 50;
        while(c<TIMES){
            setUp();
            if(target())
                result++;
            c++;
        }
        assertEquals(TIMES, result);
    }

    boolean target(){
        int a=0;

        do{
            bullet.update(false);
            ++a;
        }
        while(!checkCollision(ship, bullet) && a<90);   // 90번 update 동안에 안 부딪히면 false
        return a<90;
    }

    boolean checkCollision(final Entity a, final Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }
}