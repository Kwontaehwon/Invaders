package skill;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;
import entity.Entity;
import java.util.logging.Logger;

import java.awt.*;

abstract class Skill extends Entity {

    private Cooldown skillCooldown;

    private Cooldown duration;

    private boolean activation;

    private boolean open; //열려있는지 체크

    protected Logger logger;


    public Skill(int positionX, int positionY, int width, int height, Color color) {
        super(0, 0, 8*2, 8*2, Color.white);
        this.logger = Core.getLogger();
    }

    abstract void startActivate();

    abstract boolean checkDuration();

    abstract boolean checkCoolTime();

    abstract void startCollTime();

    abstract boolean checkOpen();
}
