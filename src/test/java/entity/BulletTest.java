package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    Bullet a;
    @BeforeEach
    void setUp(){
        a = new Bullet(1,2,3,4);
    }

    @Test
    @DisplayName("Assert True")
    void TestTrue(){
        assertEquals(1, a.getPositionX());
    }

    @Test
    @DisplayName("Assert False")
    void TestFalse(){
        assertEquals(2, a.getPositionX());
    }
}