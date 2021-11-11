package engine;

import engine.DrawManager.SpriteType;

public class DesignSetting {

    private SpriteType shipType;

    public DesignSetting(SpriteType shipType){
        this.shipType = shipType;
    }

    public SpriteType getShipType(){ return shipType; }

    public void setShipType(SpriteType type){
        this.shipType = type;
    }

}
