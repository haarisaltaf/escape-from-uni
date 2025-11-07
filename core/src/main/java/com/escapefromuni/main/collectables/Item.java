package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;

public abstract class Item extends Collectable {
    public Item(Vector2 position,String itemType) {
        super(position);
        this.itemType = itemType;
    }
    public Item(Vector2 position,String imageDir,String itemType){
        super(position,imageDir);
        this.itemType = itemType;
    }
    public final String itemType;
}
