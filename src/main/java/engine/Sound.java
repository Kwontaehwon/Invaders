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

    public Sound(){
        shootingSound = new Audio("src/main/resources/shootingSound.wav", false);
        destroyedEnemySound = new Audio("src/main/resources/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("src/main/resources/hitEnemySound.wav", false);
        getItemSound = new Audio("src/main/resources/getItemSound.wav", false);
        dropItemSound = new Audio("src/main/resources/dropItemSound.wav", false);
        boomingSound = new Audio("src/main/resources/boomingSound.wav", false);
        deathSound = new Audio("src/main/resources/deathSound.wav", false);
        roundStartSound = new Audio("src/main/resources/roundStart.wav", false);
        countDownSound = new Audio("src/main/resources/countdown.wav", false);   // 0.9s
        shipDeathSound = new Audio("src/main/resources/shipDeathSound.wav", false); // 1.398s
        roundEndSound = new Audio("src/main/resources/roundEndSound.wav", false);  // 2.757s
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
    }
}
