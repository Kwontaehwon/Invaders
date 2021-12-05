package engine;

public class Sound {
    /** shooting sound */
    public Audio shootingSound;
    /** destroyed enemy sound */
    public Audio destroyedEnemySound;
    /** hit enemy sound */
    public Audio hitEnemySound;
    /** get item sound */
    public Audio getItemSound;
    /** drop item sound */
    public Audio dropItemSound;
    /** boom sound */
    public Audio boomingSound;
    /** death sound */
    public Audio deathSound;
    /** round start sound */
    public Audio roundStartSound;
    /** countdown sound */
    public Audio countDownSound;
    /** ship death sound */
    public Audio shipDeathSound;
    /** round end sound */
    public Audio roundEndSound;
    /** get coin sound*/
    public Audio getCoinSound;
    /** get powerUp item sound*/
    public Audio getPowerUpSound;
    /** use skill1 sound*/
    public Audio skill1Sound;
    /** use skill2 sound*/
    public Audio skill2Sound;
    /** use skill3 sound*/
    public Audio skill3Sound;
    /** use skill4 sound*/
    public Audio skill4Sound;
    /** skill unlock sound*/
    public Audio skillUnlockSound;
    /** recovery sound*/
    public Audio recoverySound;
    /** use ultimate sound*/
    public Audio ultimateSound;

    /**
     * Constructor, adds Audio.
     */
    public Sound(){
        shootingSound = new Audio("src/main/resources/shootingSound.wav", false);
        destroyedEnemySound = new Audio("src/main/resources/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("src/main/resources/hitEnemySound.wav", false);
        getItemSound = new Audio("src/main/resources/getItemSound.wav", false);
        getCoinSound = new Audio("src/main/resources/getCoin.wav", false);
        getPowerUpSound = new Audio("src/main/resources/getPowerUp.wav",false);
        dropItemSound = new Audio("src/main/resources/dropItemSound.wav", false);
        boomingSound = new Audio("src/main/resources/boomingSound.wav", false);
        deathSound = new Audio("src/main/resources/deathSound.wav", false);
        roundStartSound = new Audio("src/main/resources/roundStart.wav", false);
        countDownSound = new Audio("src/main/resources/countdown.wav", false);   // 0.9s
        shipDeathSound = new Audio("src/main/resources/shipDeathSound.wav", false); // 1.398s
        roundEndSound = new Audio("src/main/resources/roundEndSound.wav", false);  // 2.757s
        skill1Sound = new Audio("src/main/resources/skill1.wav", false);
        skill2Sound = new Audio("src/main/resources/skill2.wav", false);
        skill3Sound = new Audio("src/main/resources/skill3.wav", false);
        skill4Sound = new Audio("src/main/resources/skill4.wav", false);
        skillUnlockSound = new Audio("src/main/resources/skillUnlock.wav", false);
        recoverySound = new Audio("src/main/resources/recovery.wav", false);
        ultimateSound = new Audio("src/main/resources/ultimate.wav", false);
    }

    /**
     * Increase the music volume
     */
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

    /**
     * Increase the music volume
     */
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
