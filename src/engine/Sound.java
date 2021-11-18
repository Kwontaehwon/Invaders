package engine;

public class Sound {
    public Audio shootingSound;
    public Audio destroyedEnemySound;
    public Audio hitEnemySound;
    public Audio getItemSound;
    public Audio dropItemSound;
    public Audio boomingSound;
    public Audio deathSound;
    public Sound(){
        shootingSound = new Audio("res/shootingSound.wav", false);
        destroyedEnemySound = new Audio("res/destroyedEnemySound.wav", false);
        hitEnemySound = new Audio("res/hitEnemySound.wav", false);
        getItemSound = new Audio("res/getItemSound.wav", false);
        dropItemSound = new Audio("res/dropItemSound.wav", false);
        boomingSound = new Audio("res/boomingSound.wav", false);
        deathSound = new Audio("res/deathSound.wav", false);
    }

    public void increase(){
        this.shootingSound.increase();
        this.destroyedEnemySound.increase();
        this.hitEnemySound.increase();
        this.getItemSound.increase();
        this.dropItemSound.increase();
        this.boomingSound.increase();
        this.deathSound.increase();
    }

    public void decrease(){
        this.shootingSound.decrease();
        this.destroyedEnemySound.decrease();
        this.hitEnemySound.decrease();
        this.getItemSound.decrease();
        this.dropItemSound.decrease();
        this.boomingSound.decrease();
        this.deathSound.decrease();
    }
}
