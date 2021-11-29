package engine;

import engine.DrawManager.SpriteType;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class DesignSetting {

    private SpriteType shipType;

    private int sizeX;
    private int sizeY;

    private static ArrayList<SimpleEntry<SpriteType, Boolean>> designList = new ArrayList<>();
    private static ArrayList<SimpleEntry<Integer, Integer>> sizeList = new ArrayList<>();

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

    public SpriteType getShipType(){ return shipType; }

    public int getSizeX(){return sizeX;}
    public int getSizeY(){return sizeY;}

    public void setShipType(SpriteType type){
        this.shipType = type;
    }

    public void setShipSize(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public ArrayList<SimpleEntry<Integer, Integer>> getSizeList() {
        return sizeList;
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
