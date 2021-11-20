package skill;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;

import java.awt.*;
//적총알느려짐
public class Skill3 extends Skill {

    private final int SKILL_COOLDOWN = 15 * 1000 ; //30seconds

    private final int DURATION_COOLDOWN = 5 * 1000; //3초동안 지속

    private Cooldown skillCooldown;

    private Cooldown duration;

    private boolean activation;

    private boolean open; //열려있는지 체크

    public Skill3(int level) {
        super(0, 0, 8*2, 8*2, Color.white);

        this.activation = false;
        this.skillCooldown = Core.getCooldown(SKILL_COOLDOWN);
        this.duration = Core.getCooldown(DURATION_COOLDOWN);
        this.spriteType =  DrawManager.SpriteType.Skill3;
        if(level >= 4) this.open = true;
        else this.open = false;

    }

    //활성화
    public void startActivate(){
        activation = true;
        duration.reset();
    }
    //활성화 중, true면 활성화중.
    public boolean checkActivate(){ return activation; }

    //지속시간 체크
    public boolean checkDuration() {
        if(duration.checkFinished()) {
            this.activation = false;
            this.logger.info("The duration of Skill3 is over." );
            return activation;
        }
        else {
            return activation;
        }
    }
    // 스킬쿨타임 체크 끝났나?
    public boolean checkCoolTime(){
        if(this.skillCooldown.checkFinished()) {
            this.logger.info("Skill3 was used. ");
            return true;
        }
        else {
            this.logger.info("Skill3 Cooldown left");
            return false;
        }
    }
    // 스킬쿨타임 다시시작.
    public void startCollTime(){
        this.skillCooldown.reset();
    }

    public boolean checkOpen() { return this.open; }

    public int returnCoolTime(){

        return this.SKILL_COOLDOWN/1000 - this.skillCooldown.passedCooldown();
    }
}
