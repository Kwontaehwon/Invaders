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

public class PauseScreen extends Screen{
    /** Container of game states and settings */
    GameStatus gameStatus;

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    /** Index of focused menu */
    int cursor;

    /** Index of menus */
    public static int CONTINUE = 0;
    public static int MAIN_MENU = 1;
    public static int RESTART = 2;
    public static int SAVE = 3;
    public static int MUSIC = 4;
    public static int SOUND = 5;
    public static int MUSIC_DOWN = 6;
    public static int MUSIC_UP = 7;
    public static int SOUND_DOWN = 8;
    public static int SOUND_UP = 9;


    /**
     * Constructor, establishes the properties of the screen.
     * @param width Screen width.
     * @param height Screen height.
     * @param fps
     * @param gameStatus
     */
    public PauseScreen(int width, int height, int fps, GameStatus gameStatus) {
        super(width, height, fps);

        this.returnCode = Core.PLAY;
        this.cursor = CONTINUE;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        this.gameStatus = gameStatus;
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run(){
        super.run();
        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
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

            if (this.cursor == CONTINUE && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.returnCode = Core.PLAY;
                this.isRunning = false;
            }
            if (this.cursor == MAIN_MENU && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.returnCode = Core.MAIN_MENU;
                Core.flag_main = true;
                this.isRunning = false;
            }
            if (this.cursor == RESTART && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.returnCode = Core.RESTART;
                Core.flag_restart = true;
                this.isRunning = false;
            }
            if (this.cursor == SAVE && inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                this.saveSave();
                this.selectionCooldown.reset();
            }
            if (this.cursor == MUSIC_DOWN && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_LEFT))) {
                backgroundMusic.decrease();
                this.selectionCooldown.reset();
            }
            if (this.cursor == MUSIC_UP && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                backgroundMusic.increase();
                this.selectionCooldown.reset();
            }
            if (this.cursor == SOUND_DOWN && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                effectSound.decrease();
                this.selectionCooldown.reset();
            }
            if (this.cursor == SOUND_UP && (inputManager.isKeyDown(KeyEvent.VK_SPACE) ||
                                            inputManager.isKeyDown(KeyEvent.VK_RIGHT))) {
                effectSound.increase();
                this.selectionCooldown.reset();
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (cursor == SOUND)
            this.cursor = CONTINUE;
        else if (cursor == MUSIC_DOWN)
            this.cursor = SOUND_DOWN;
        else if (cursor == MUSIC_UP)
            this.cursor = SOUND_UP;
        else if (cursor == SOUND_UP || cursor == SOUND_DOWN)
            this.cursor = cursor;
        else
            this.cursor++;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (cursor == CONTINUE)
            this.cursor = SOUND;
        else if (cursor == SOUND_DOWN)
            this.cursor = MUSIC_DOWN;
        else if (cursor == SOUND_UP)
            this.cursor = MUSIC_UP;
        else if (cursor == MUSIC_DOWN || cursor == MUSIC_UP)
            this.cursor = cursor;
        else
            this.cursor--;
    }

    /**
     * Shifts the focus to the right menu item.
     */
    private void rightMenuItem(){
        if (this.cursor == SOUND)
            this.cursor = SOUND_UP;
        else if (this.cursor == SOUND_DOWN)
            this.cursor = SOUND;
        else if (this.cursor == MUSIC)
            this.cursor = MUSIC_UP;
        else if (this.cursor == MUSIC_DOWN)
            this.cursor = MUSIC;
    }

    /**
     * Shifts the focus to the left menu item.
     */
    private void leftMenuItem(){
        if (this.cursor == MUSIC)
            this.cursor = MUSIC_DOWN;
        else if (this.cursor == MUSIC_UP)
            this.cursor = MUSIC;
        else if (this.cursor == SOUND)
            this.cursor = SOUND_DOWN;
        else if (this.cursor == SOUND_UP)
            this.cursor = SOUND;
    }

    /**
     * save all of the status in the game excepts entity's positions.
     */
    private void saveSave() {
        try {
            List<String> gameStatusList = new ArrayList<>();
            String state = gameStatus.getStates().getLevel() +
                    ", " + gameStatus.getStates().getScore() +
                    ", " + gameStatus.getStates().getLivesRemaining() +
                    ", " + gameStatus.getStates().getBulletsShot() +
                    ", " + gameStatus.getStates().getShipsDestroyed() +
                    ", " + gameStatus.getStates().getBoomTimes() +
                    ", " + gameStatus.getStates().getSkillCool()[0] +
                    ", " + gameStatus.getStates().getSkillCool()[1] +
                    ", " + gameStatus.getStates().getSkillCool()[2] +
                    ", " + gameStatus.getStates().getSkillCool()[3] +
                    ", " + gameStatus.getStates().getUltimateTimes();
            String settings = gameStatus.getSettings().getFormationWidth() +
                    ", " + gameStatus.getSettings().getFormationHeight() +
                    ", " + gameStatus.getSettings().getBaseSpeed() +
                    ", " + gameStatus.getSettings().getShootingFrecuency();

            gameStatusList.add(state);
            gameStatusList.add(settings);
            gameStatusList.add(Boolean.toString(gameStatus.getBonus()));
            Core.getFileManager().saveSaves(gameStatusList);
        } catch (IOException e) {
            logger.warning("Couldn't load saves!");
        }
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawPauseTitle(this);
        drawManager.drawPauseMenu(this, this.cursor);

        drawManager.completeDrawing(this);
    }
}
