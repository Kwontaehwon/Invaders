package engine;

public class Sound {
    public Audio shootingSound;
    public Audio destroyedEnemySound;
    public Audio hitEnemySound;
    public Audio getItemSound;
    public Audio dropItemSound;
    public Audio boomingSound;
    public Audio deathSound;
    public Audio roundStartSouond;
    public Audio countDownSound;
    public Audio shipDeathSound;
    public Audio roundEndSound;

    public Sound(){
        shootingSound = new Audio("res/shootingSound.wav", false);
        destroyedEnemySound = new Audio("res/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("res/hitEnemySound.wav", false);
        getItemSound = new Audio("res/getItemSound.wav", false);
        dropItemSound = new Audio("res/dropItemSound.wav", false);
        boomingSound = new Audio("res/boomingSound.wav", false);
        deathSound = new Audio("res/deathSound.wav", false);
        roundStartSouond = new Audio("res/roundStart.wav", false);
        countDownSound = new Audio("res/countdown.wav", false);   // 0.9s
        shipDeathSound = new Audio("res/shipDeathSound.wav", false); // 1.398s
        roundEndSound = new Audio("res/roundEndSound.wav", false);  // 2.757s
    }

    public void increase(){
        this.shootingSound.increase();
        this.destroyedEnemySound.increase();
        this.hitEnemySound.increase();
        this.getItemSound.increase();
        this.dropItemSound.increase();
        this.boomingSound.increase();
        this.deathSound.increase();
        this.roundStartSouond.increase();
        this.countDownSound.increase();
        this.shipDeathSound.increase();
        this.roundEndSound.increase();
    }

    public void decrease(){
        this.shootingSound.decrease();
        this.destroyedEnemySound.decrease();
        this.hitEnemySound.decrease();
        this.getItemSound.decrease();
        this.dropItemSound.decrease();
        this.boomingSound.decrease();
        this.deathSound.decrease();
        this.roundStartSouond.decrease();
        this.countDownSound.decrease();
        this.shipDeathSound.decrease();
        this.roundEndSound.decrease();
    }
}
