package engine;

public class Sound {
    public Audio shootingSound;
    public Audio destroyedEnemySound;
    public Audio hitEnemySound;
    public Audio getItemSound;
    public Audio dropItemSound;
    public Audio boomingSound;
    public Audio deathSound;
    public Audio roundStartSound;
    public Audio countDownSound;
    public Audio shipDeathSound;
    public Audio roundEndSound;
    public Audio getCoinSound;
    public Audio getPowerUpSound;
    public Audio skill1Sound;
    public Audio skill2Sound;
    public Audio skill3Sound;
    public Audio skill4Sound;
    public Audio skillUnlockSound;
    public Audio recoverySound;
    public Audio ultimateSound;

    public Sound(){
        shootingSound = new Audio("res/shootingSound.wav", false);
        destroyedEnemySound = new Audio("res/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("res/hitEnemySound.wav", false);
        getItemSound = new Audio("res/getItemSound.wav", false);
        getCoinSound = new Audio("res/getCoin.wav", false);
        getPowerUpSound = new Audio("res/getPowerUp.wav",false);
        dropItemSound = new Audio("res/dropItemSound.wav", false);
        boomingSound = new Audio("res/boomingSound.wav", false);
        deathSound = new Audio("res/deathSound.wav", false);
        roundStartSound = new Audio("res/roundStart.wav", false);
        countDownSound = new Audio("res/countdown.wav", false);   // 0.9s
        shipDeathSound = new Audio("res/shipDeathSound.wav", false); // 1.398s
        roundEndSound = new Audio("res/roundEndSound.wav", false);  // 2.757s
        skill1Sound = new Audio("res/skill1.wav", false);
        skill2Sound = new Audio("res/skill2.wav", false);
        skill3Sound = new Audio("res/skill3.wav", false);
        skill4Sound = new Audio("res/skill4.wav", false);
        skillUnlockSound = new Audio("res/skillUnlock.wav", false);
        recoverySound = new Audio("res/recovery.wav", false);
        ultimateSound = new Audio("res/ultimate.wav", false);
    }

    public void increase(){
        this.shootingSound.increase();
        this.destroyedEnemySound.increase();
        this.hitEnemySound.increase();
        this.getItemSound.increase();
        this.dropItemSound.increase();
        this.boomingSound.increase();
        this.deathSound.increase();
        this.roundStartSound.increase();
        this.countDownSound.increase();
        this.shipDeathSound.increase();
        this.roundEndSound.increase();
        this.getCoinSound.increase();
        this.getPowerUpSound.increase();
        this.skill1Sound.increase();
        this.skill2Sound.increase();
        this.skill3Sound.increase();
        this.skill4Sound.increase();
        this.skillUnlockSound.increase();
        this.recoverySound.increase();
        this.ultimateSound.increase();
    }

    public void decrease(){
        this.shootingSound.decrease();
        this.destroyedEnemySound.decrease();
        this.hitEnemySound.decrease();
        this.getItemSound.decrease();
        this.dropItemSound.decrease();
        this.boomingSound.decrease();
        this.deathSound.decrease();
        this.roundStartSound.decrease();
        this.countDownSound.decrease();
        this.shipDeathSound.decrease();
        this.roundEndSound.decrease();
        this.getCoinSound.decrease();
        this.getPowerUpSound.decrease();
        this.skill1Sound.decrease();
        this.skill2Sound.decrease();
        this.skill3Sound.decrease();
        this.skill4Sound.decrease();
        this.skillUnlockSound.decrease();
        this.recoverySound.decrease();
        this.ultimateSound.decrease();
    }
}
