import engine.*;
import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    Item bonus1,bonus2,bonus3,shooting,bulletSpeed;

    @BeforeEach
    void setUp() {
        bonus1 = new Item(0,0,16,16,  DrawManager.SpriteType.BonusScoreItem1,6 );
        bonus2 = new Item(0,0,16,16,  DrawManager.SpriteType.BonusScoreItem2,6 );
        bonus3 = new Item(0,0,16,16,  DrawManager.SpriteType.BonusScoreItem3,6 );
        shooting = new Item(0,0,16,16,  DrawManager.SpriteType.ShootingCoolItem,3 );
        bulletSpeed = new Item(0,0,16,16,  DrawManager.SpriteType.BulletSpeedItem,2 );
    }

    @Test
    void getPointValue() {
        assertAll(
                () -> assertEquals(50,bonus1.getPointValue()),
                () -> assertEquals(100,bonus2.getPointValue()),
                () -> assertEquals(300,bonus3.getPointValue()),
                () -> assertEquals(0,shooting.getPointValue()),
                () -> assertEquals(0,bulletSpeed.getPointValue())
        );
    }
}