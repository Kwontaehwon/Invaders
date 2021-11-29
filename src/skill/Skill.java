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

    private boolean open;

    protected Logger logger;

    /**
     * Constructor, establishes the skill's generic properties.
     *
     * @param positionX Initial position of the entity in the X axis.
     *
     * @param positionY Initial position of the entity in the Y axis.
     *
     * @param width Width of the entity.
     *
     * @param height Height of the entity.
     *
     * @param color Color of the entity.
     */
    public Skill(int positionX, int positionY, int width, int height, Color color) {
        super(0, 0, 8*2, 8*2, Color.white);
        this.logger = Core.getLogger();
    }

    abstract void startActivate();

    abstract boolean checkDuration();

    abstract boolean checkCoolTime();

    abstract void startCoolTime();

    abstract boolean checkOpen();
}
