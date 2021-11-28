package screen;

import engine.Cooldown;
import engine.Core;
import engine.DesignSetting;
import engine.DrawManager.SpriteType;
import java.awt.event.KeyEvent;

public class ShipScreen extends Screen{

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    private SpriteType newSpriteType;

    private final DesignSetting designSetting;
    private int cursor=0;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width  Screen width.
     * @param height Screen height.
     * @param fps    Screen frame per second.
     * @param designSetting designSetting
     */
    public ShipScreen(int width, int height, int fps, DesignSetting designSetting) {
        super(width, height, fps);
        this.designSetting = designSetting;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
        this.returnCode = 1;
        this.cursor = designSetting.designIndexOf(designSetting.getShipType());
    }


    @Override
    public int run() {
        super.run();

        return this.returnCode;
    }

    protected final void update() {
        super.update();

        draw();

        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT)
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                prevItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                nextItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                designSetting.setShipType(designSetting.getDesignList().get(cursor).getKey());
                designSetting.setShipSize(designSetting.getSizeList().get(cursor).getKey(), designSetting.getSizeList().get(cursor).getValue());
            }
            if (inputManager.isKeyDown(KeyEvent.VK_ESCAPE))
                this.isRunning = false;
        }

    }

    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawShipCustomMenu(this);
        drawManager.drawDesigns(this, this.cursor, designSetting);
        drawManager.completeDrawing(this);
    }

    private void nextItem(){
        int next = cursor;
        if(next == designSetting.getDesignList().size()-1)
            next = 0;
        else
            next++;

        if(designSetting.getDesignList().get(next).getValue())
            cursor = next;
    }

    private void prevItem(){
        int prev = cursor;
        if(prev == 0)
            prev = designSetting.getDesignList().size()-1;
        else
            prev--;

        if(designSetting.getDesignList().get(prev).getValue())
            cursor = prev;
    }

}
