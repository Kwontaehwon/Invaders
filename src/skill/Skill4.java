package skill;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;

import java.awt.*;

public class Skill4 extends Skill {

    private final int SKILL_COOLDOWN = 15 * 1000;

    private final int DURATION_COOLDOWN = 5 * 1000;

    private int currentSkillCooldown ;

    private Cooldown skillCooldown;

    private Cooldown duration;

    private boolean activation;

    private boolean open;

    /**
     * Constructor, established the skill's properties for 3 bombs
     * @param level current level
     * @param currentSkillCooldown value of current skill cooltime
     */
    public Skill4(int level,int currentSkillCooldown) {
        super(0, 0, 8*2, 8*2, Color.white);

        this.activation = false;
        this.currentSkillCooldown = currentSkillCooldown;
        this.skillCooldown = Core.getCooldown(currentSkillCooldown * 1000);
        this.duration = Core.getCooldown(DURATION_COOLDOWN);
        this.spriteType =  DrawManager.SpriteType.Skill4;
        if(level >= 5) this.open = true;
        else this.open = false;

    }

    /**
     *for checking skill1, activate skill's cooldown
     */
    public void startActivate(){
        activation = true;
        duration.reset();
        this.skillCooldown = Core.getCooldown(SKILL_COOLDOWN);
    }

    /**
     * Check the skill is active
     * @return True if the skill is active
     */
    public boolean checkActivate(){ return activation; }


    /**
     * check the duration
     * @return True if the skill not finished
     */
    public boolean checkDuration() {
        if(duration.checkFinished()) {
            this.activation = false;
            this.logger.info("The duration of Skill4 is over." );
            return activation;
        }
        else {
            return activation;
        }
    }

    /**
     * check the skill cooltime
     * @return True if the skill can active
     */
    public boolean checkCoolTime(){
        if(this.skillCooldown.checkFinished()) {
            this.logger.info("Skill4 was used. ");
            return true;
        }
        else {
            this.logger.info("Skill4 Cooldown left");
            return false;
        }
    }

    /**
     * start skill cooltime
     */
    public void startCoolTime(){
        this.skillCooldown.reset();
    }

    /**
     * return the left time
     * @return true if skill cooltime is minus, false
     */
    public int returnSkillCoolTime(){
        if(this.skillCooldown.getDuration() - this.skillCooldown.passedCooldown() < 0) return 0;
        else return this.skillCooldown.getDuration() - this.skillCooldown.passedCooldown();
    }

    /**
     * Added added pause time
     * @param time Value of pause time
     */
    public void pause(long time){
        this.skillCooldown.pause(time);
        this.duration.pause(time);
    }

    /**
     * Check skill is open or not
     * @return True if skill is open
     */
    public boolean checkOpen() { return this.open; }

}
