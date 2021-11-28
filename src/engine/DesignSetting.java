package engine;

import engine.DrawManager.SpriteType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class DesignSetting {
    /** Sprite ship type*/
    private SpriteType shipType;
    /** List of simple entry */
    private static ArrayList<SimpleEntry<SpriteType, Boolean>> designList = new ArrayList<>();

    /**
     *Constructor, designs the ship.
     * @param shipType ship sprite type
     */
    public DesignSetting(SpriteType shipType){
        this.shipType = shipType;
        designList.add(new SimpleEntry<>(SpriteType.Ship, true));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign, false));
    }

    /**
     * Getter of ship type
     * @return current ship sprite type
     */
    public SpriteType getShipType(){ return shipType; }

    /**
     * Setter of ship type
     * @param type selected ship sprite type
     */
    public void setShipType(SpriteType type){
        this.shipType = type;
    }

    /**
     * Getter for the list of design
     * @return get design lists
     */
    public ArrayList<SimpleEntry<SpriteType, Boolean>> getDesignList() {
        return designList;
    }

    /**
     * Setter for the achieved design
     * @param sprite available sprite type
     * @param value whether this sprite can be used
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
     * Return the index of selected sprite
     * @param sprite current sprite
     * @return value of index
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
