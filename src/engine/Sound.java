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

    /**
     * Constructor, adds Audio.
     */
    public Sound(){
        shootingSound = new Audio("res/shootingSound.wav", false);
        destroyedEnemySound = new Audio("res/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("res/hitEnemySound.wav", false);
        getItemSound = new Audio("res/getItemSound.wav", false);
        dropItemSound = new Audio("res/dropItemSound.wav", false);
        boomingSound = new Audio("res/boomingSound.wav", false);
        deathSound = new Audio("res/deathSound.wav", false);
        roundStartSound = new Audio("res/roundStart.wav", false);
        countDownSound = new Audio("res/countdown.wav", false);   // 0.9s
        shipDeathSound = new Audio("res/shipDeathSound.wav", false); // 1.398s
        roundEndSound = new Audio("res/roundEndSound.wav", false);  // 2.757s
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
    }
}
