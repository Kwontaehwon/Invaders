package engine;

import engine.DrawManager.SpriteType;
import java.util.ArrayList;
import java.util.Map;

public class DesignSetting {

    private SpriteType shipType;

    private static ArrayList<Map.Entry<SpriteType, Boolean>> designList = new ArrayList<>();

    public DesignSetting(SpriteType shipType){
        this.shipType = shipType;

        designList.add(Map.entry(SpriteType.Ship, true));
        designList.add(Map.entry(SpriteType.NewShipDesign, false));
    }

    public SpriteType getShipType(){ return shipType; }

    public void setShipType(SpriteType type){
        this.shipType = type;
    }

    public ArrayList<Map.Entry<SpriteType, Boolean>> getDesignList() {
        return designList;
    }

    public void setDesignAchieved(SpriteType sprite, Boolean value){
        for(Map.Entry<SpriteType, Boolean> entry :designList){
            if(entry.getKey()==sprite) {
                entry.setValue(value);
                return;
            }
        }
    }

    public int designIndexOf(SpriteType sprite){
        int index=0;
        for(Map.Entry<SpriteType, Boolean> entry : designList){
            if(entry.getKey() == sprite)
                return index;
            index++;
        }
        return -1;
    }

}
