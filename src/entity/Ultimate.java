package entity;

import engine.DrawManager;

import java.awt.*;

public class Ultimate extends Entity{

    private int speed = -6;

    public Ultimate(int positionX, int positionY) {
        super(positionX, positionY, 100*2, 100*2, Color.white);

        this.spriteType = DrawManager.SpriteType.Ultimate;
    }

    public final void update() {
        this.positionY += this.speed;
    }
}