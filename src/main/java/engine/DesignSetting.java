package engine;

import engine.DrawManager.SpriteType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class DesignSetting {
    /** Sprite ship type*/
    private SpriteType shipType;
    /** size of ship*/
    private int sizeX;
    private int sizeY;
    /** List of simple entry */
    private static ArrayList<SimpleEntry<SpriteType, Boolean>> designList = new ArrayList<>();
    private static ArrayList<SimpleEntry<Integer, Integer>> sizeList = new ArrayList<>();

    /**
     *Constructor, designs the ship.
     * @param shipType ship sprite type
     */
    public DesignSetting(SpriteType shipType){
        this.shipType = shipType;
        sizeX = 18;
        sizeY = 16;
        designList.add(new SimpleEntry<>(SpriteType.Ship, true));
        sizeList.add(new SimpleEntry<>(18,16));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign1_1, true));
        sizeList.add(new SimpleEntry<>(16,24));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign2, true));
        sizeList.add(new SimpleEntry<>(16,16));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign3, true));
        sizeList.add(new SimpleEntry<>(17,32));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign4, true));
        sizeList.add(new SimpleEntry<>(23,32));
    }

    /**
     * Getter of ship type
     * @return current ship sprite type
     */
    public SpriteType getShipType(){ return shipType; }
    /** get size of ship*/
    public int getSizeX(){return sizeX;}
    public int getSizeY(){return sizeY;}

    /**
     * Setter of ship type
     * @param type selected ship sprite type
     */
    public void setShipType(SpriteType type){
        this.shipType = type;
    }

    public void setShipSize(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    /** get size and Desigi list */
    public ArrayList<SimpleEntry<Integer, Integer>> getSizeList() {
        return sizeList;
    }
    public ArrayList<SimpleEntry<SpriteType, Boolean>> getDesignList() {
        return designList;
    }

    /**
     * Getter for the list of design
     * @return get design lists
     */
    public void setDesignAchieved(SpriteType sprite, Boolean value){
        for(SimpleEntry<SpriteType, Boolean> entry :designList){
            if(entry.getKey()==sprite) {
                entry.setValue(value);
                return;
            }
        }
    }

    /**
     * reutrn design index
     * @param sprite
     * @return design index
     */
    public int designIndexOf(SpriteType sprite){
        int index=0;
        for(SimpleEntry<SpriteType, Boolean> entry : designList){
            if(entry.getKey() == sprite)
                return index;
            index++;
        }
        return -1;
    }

}
