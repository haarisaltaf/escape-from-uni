package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;

public abstract class Item extends Collectable {
    public Item(Vector2 position,String itemType) {
        super(position);
        this.itemType = itemType;
        this.exists = true;
    }
    public Item(Vector2 position,String imageDir,String itemType){
        super(position,imageDir);
        this.itemType = itemType;
        this.exists = true;
    }

    public void setAsNotExist() {
        this.exists = false;
    }

    public boolean doesExist() {
        return exists;
    }

    public final String itemType;
}
