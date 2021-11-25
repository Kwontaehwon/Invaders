package entity;

import engine.DrawManager;

import java.awt.*;

public class LargeBoom extends Entity{

    private int speed = -6;

    public LargeBoom(int positionX, int positionY) {
        super(positionX, positionY, 100*2, 100*2, Color.white);

        this.spriteType = DrawManager.SpriteType.LargeBoom;
    }

    public final void update() {
        this.positionY += this.speed;
    }
}