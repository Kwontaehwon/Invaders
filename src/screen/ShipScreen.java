package screen;

import engine.Cooldown;
import engine.Core;
import engine.DesignSetting;
import engine.DrawManager.SpriteType;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ShipScreen extends Screen{

    private ArrayList<SpriteType> designList = new ArrayList<>(Arrays.asList(SpriteType.Ship, SpriteType.NewShipDesign));
    //TODO 디자인 제한하고 해금 기능 만들기
    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;
    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;


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
    }


    @Override
    public int run() {
        super.run();

        return this.returnCode;
    }

    protected final void update() {
        super.update();

        if(cursor==0)
            designSetting.setShipType(SpriteType.Ship);
        else if(cursor==1)
            designSetting.setShipType(SpriteType.NewShipDesign);
        else
            designSetting.setShipType(SpriteType.Ship);

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
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE))
                this.isRunning = false;
        }

    }

    public void draw(){
        drawManager.initDrawing(this);
        drawManager.drawShipCustomMenu(this);
        drawManager.drawDesigns(this, designList, this.cursor);
        //TODO 잠긴 색깔 구분하기
        drawManager.completeDrawing(this);
    }

    private void nextItem(){
        if(this.cursor == designList.size()-1)
            cursor = 0;
        else
            cursor++;
    }

    private void prevItem(){
        if(this.cursor == 0)
            cursor = designList.size()-1;
        else
            cursor--;
    }

}
