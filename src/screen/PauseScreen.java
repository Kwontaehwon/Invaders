package screen;

import engine.Cooldown;
import engine.Core;
import engine.GameStatus;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static engine.Core.backgroundMusic;
import static engine.Core.effectSound;

// 추가 클래스
public class PauseScreen extends Screen{
    GameStatus gameStatus;

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;


    /**
     * Constructor, establishes the properties of the screen.
     *  @param width  Screen width.
     * @param height Screen height.
     * @param fps
     * @param gameStatus
     */
    public PauseScreen(int width, int height, int fps, GameStatus gameStatus) {
        super(width, height, fps);

        this.returnCode = 2;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        this.gameStatus = gameStatus;
    }

    public final int run(){
        super.run();

        return this.returnCode;
    }

    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }

            if (this.returnCode == 2 && inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;
            if (this.returnCode == 1 && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                Core.flag_main = true;
                this.isRunning = false;
            }
            if (this.returnCode == 4 && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                Core.flag_restart = true;
                this.isRunning = false;
            }
            if (this.returnCode == 5 && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.saveSave();
                this.selectionCooldown.reset();
            }
            if (this.returnCode == 11 && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_LEFT))) {
                backgroundMusic.decrease();
                this.selectionCooldown.reset();
            }
            if (this.returnCode == 12 && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                backgroundMusic.increase();
                this.selectionCooldown.reset();
            }
            if (this.returnCode == 13 && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                effectSound.decrease();
                this.selectionCooldown.reset();
            }
            if (this.returnCode == 14 && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                effectSound.increase();
                this.selectionCooldown.reset();
            }
            if(inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)){
                leftMenuItem();
                this.selectionCooldown.reset();
            }
            if(inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)){
                rightMenuItem();
                this.selectionCooldown.reset();
            }
        }
    }

    private void nextMenuItem() {
        if(this.returnCode == 2)
            this.returnCode = 1;
        else if(this.returnCode == 1)
            this.returnCode = 4;
        else if(this.returnCode == 6)
            this.returnCode = 15;
        else if(this.returnCode == 15)
            this.returnCode = 2;
        else if(this.returnCode == 11)
            this.returnCode = 11;
        else if(this.returnCode == 12)
            this.returnCode = 12;
        else if(this.returnCode == 13)
            this.returnCode = 13;
        else
            this.returnCode++;
    }

    private void previousMenuItem() {
        if (this.returnCode == 1)
            this.returnCode = 2;
        else if(this.returnCode == 4)
            this.returnCode = 1;
        else if(this.returnCode == 6)
            this.returnCode = 5;
        else if(this.returnCode == 15)
            this.returnCode = 6;
        else if(this.returnCode == 2)
            this.returnCode = 15;
        else if(this.returnCode == 11)
            this.returnCode = 11;
        else if(this.returnCode == 12)
            this.returnCode = 12;
        else if(this.returnCode == 13)
            this.returnCode = 13;
        else if(this.returnCode == 14)
            this.returnCode = 14;
        else
            this.returnCode--;
    }

    private void rightMenuItem(){
        if (this.returnCode == 6)
            this.returnCode = 12;
        else if (this.returnCode == 11)
            this.returnCode = 6;
        else if (this.returnCode == 15)
            this.returnCode = 14;
        else if (this.returnCode == 13)
            this.returnCode = 15;
    }

    private void leftMenuItem(){
        if (this.returnCode == 12)
            this.returnCode = 6;
        else if (this.returnCode == 6)
            this.returnCode = 11;
        else if (this.returnCode == 15)
            this.returnCode = 13;
        else if (this.returnCode == 14)
            this.returnCode = 15;
    }

    private void saveSave() {
        try {
            List<String> gameStatusList = new ArrayList<>();
            String state = Integer.toString(gameStatus.getStates().getLevel()) +
                    ", " + Integer.toString(gameStatus.getStates().getScore()) +
                    ", " + Integer.toString(gameStatus.getStates().getLivesRemaining()) +
                    ", " + Integer.toString(gameStatus.getStates().getBulletsShot()) +
                    ", " + Integer.toString(gameStatus.getStates().getShipsDestroyed()) +
                    ", " + Integer.toString(gameStatus.getStates().getBoomtimes()) +
                    ", " + Integer.toString(gameStatus.getStates().getSkillCool()[0]) +
                    ", " + Integer.toString(gameStatus.getStates().getSkillCool()[1]) +
                    ", " + Integer.toString(gameStatus.getStates().getSkillCool()[2]) +
                    ", " + Integer.toString(gameStatus.getStates().getSkillCool()[3]) +
                    ", " + Integer.toString(gameStatus.getStates().getUltimateTimes());
            String settings = Integer.toString(gameStatus.getSettings().getFormationWidth()) +
                    ", " + Integer.toString(gameStatus.getSettings().getFormationHeight()) +
                    ", " + Integer.toString(gameStatus.getSettings().getBaseSpeed()) +
                    ", " + Integer.toString(gameStatus.getSettings().getShootingFrecuency());

            gameStatusList.add(state);
            gameStatusList.add(settings);
            gameStatusList.add(Boolean.toString(gameStatus.getBonus()));
            Core.getFileManager().saveSaves(gameStatusList);
        } catch (IOException e) {
            logger.warning("Couldn't load saves!");
        }
    }

    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawPauseTitle(this);
        drawManager.drawPauseMenu(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}
