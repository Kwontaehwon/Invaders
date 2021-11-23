package skill;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager;

import java.awt.*;
//무적
public class Skill1 extends Skill{

    private final int SKILL_COOLDOWN = 15 * 1000      ;  //쿨타임

    private final int DURATION_COOLDOWN = 5 * 1000; //지속시간

    private int currentSkillCooldown ;

    private Cooldown skillCooldown;

    private Cooldown duration;

    private boolean activation;

    private boolean open; //열려있는지 체크

    public Skill1(int level,int currentSkillCooldown) {
        super(0, 0, 8*2, 8*2, Color.white);

        this.activation = false;
        //load된 쿨타임을 적용하기위해
        this.currentSkillCooldown = currentSkillCooldown;
        this.skillCooldown = Core.getCooldown(currentSkillCooldown * 1000);
        this.duration = Core.getCooldown(DURATION_COOLDOWN);
        this.spriteType =  DrawManager.SpriteType.Skill1;
        if(level >= 2) this.open = true;
        else this.open = false;

    }

    //활성화
    public void startActivate(){
        activation = true;
        duration.reset();
        //활성화하면 다시 쿨타임이 15초부터 돌기때문에
        this.skillCooldown = Core.getCooldown(SKILL_COOLDOWN);
    }
    //활성화 중, true면 활성화중.
    public boolean checkActivate(){ return activation; }


    //지속시간 체크
    public boolean checkDuration() {
        if(duration.checkFinished()) {
            this.activation = false;
            this.logger.info("The duration of Skill1 is over." );
            return activation;
        }
        else {
            return activation;
        }
    }
    // 스킬쿨타임 체크 끝났나?
    public boolean checkCoolTime(){
        if(this.skillCooldown.checkFinished()) {
            this.logger.info("Skill1 was used. ");
            return true; 
        }
        else {
            this.logger.info("Skill1 Cooldown left");
            return false;
        }
    }

    public void startCoolTime(){
        this.skillCooldown.reset();
    }

    //스킬 남은시간을 돌려줌
    public int returnSkillCoolTime(){
        // -인 경우를 처리해주기위해.
        if(this.skillCooldown.getDuration() - this.skillCooldown.passedCooldown() < 0) return 0;
        else return this.skillCooldown.getDuration() - this.skillCooldown.passedCooldown();
    }

    //pause한 시간만큼 시간을 시작시간에 더해줌.
    public void pause(long time){
        this.skillCooldown.pause(time);
        this.duration.pause(time);
    }
    public boolean checkOpen() { return this.open; }

}
