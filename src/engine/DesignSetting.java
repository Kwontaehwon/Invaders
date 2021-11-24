package engine;

import engine.DrawManager.SpriteType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class DesignSetting {

    private SpriteType shipType;

    private static ArrayList<SimpleEntry<SpriteType, Boolean>> designList = new ArrayList<>();

    public DesignSetting(SpriteType shipType){
        this.shipType = shipType;

        designList.add(new SimpleEntry<>(SpriteType.Ship, true));
        designList.add(new SimpleEntry<>(SpriteType.NewShipDesign, false));
    }

    public SpriteType getShipType(){ return shipType; }

    public void setShipType(SpriteType type){
        this.shipType = type;
    }

    public ArrayList<SimpleEntry<SpriteType, Boolean>> getDesignList() {
        return designList;
    }

    public void setDesignAchieved(SpriteType sprite, Boolean value){
        for(SimpleEntry<SpriteType, Boolean> entry :designList){
            if(entry.getKey()==sprite) {
                entry.setValue(value);
                return;
            }
        }
    }

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
