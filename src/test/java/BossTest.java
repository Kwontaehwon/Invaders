import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import engine.*;


import static org.junit.jupiter.api.Assertions.*;

class BossTest {

    Boss boss;

    @BeforeEach
    void setUp() {
        boss = new Boss();
    }

    @Test
    void destroy() {
        assertAll(
                () -> { boss.destroy();
                    assertEquals(false,boss.isDestroyed());
                },
                () -> {
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    boss.destroy();
                    assertEquals(true,boss.isDestroyed());
                }
        );
    }

    @Test
    void isDestroyed() {
        assertEquals(300,boss.getPointValue());
    }
}