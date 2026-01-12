package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.BinocularsItem;
import com.badlogic.gdx.math.Vector2;

public class BinocularsItemTest {

    private BinocularsItem BinocularsItem = new BinocularsItem(new Vector2 (128, 256));
    private BinocularsItem BinocularsItemNegativeX = new BinocularsItem(new Vector2 (-128, 256));
    private BinocularsItem BinocularsItemNegativeY = new BinocularsItem(new Vector2 (128, -256));
    private BinocularsItem BinocularsItemLowerEdgeX = new BinocularsItem(new Vector2 (0, 256));
    private BinocularsItem BinocularsItemLowerEdgeY = new BinocularsItem(new Vector2 (128, 0));
    private BinocularsItem BinocularsItemUpperEdgeX = new BinocularsItem(new Vector2 (8192, 256));
    private BinocularsItem BinocularsItemUpperEdgeY = new BinocularsItem(new Vector2 (128, 8192));
    private BinocularsItem BinocularsItemLowerErroniousX = new BinocularsItem(new Vector2 (-1, 256));
    private BinocularsItem BinocularsItemLowerErroniousY = new BinocularsItem(new Vector2 (128, -1));
    private BinocularsItem BinocularsItemUpperErroniousX = new BinocularsItem(new Vector2 (8193, 256));
    private BinocularsItem BinocularsItemUpperErroniousY = new BinocularsItem(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, BinocularsItem.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, BinocularsItemNegativeX.position);
        
        assertEquals(positionDefault, BinocularsItemNegativeY.position);

        assertEquals(positionDefault, BinocularsItemLowerErroniousX.position);
        assertEquals(positionDefault, BinocularsItemLowerErroniousY.position);
        assertEquals(positionDefault, BinocularsItemUpperErroniousX.position);
        assertEquals(positionDefault, BinocularsItemUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, BinocularsItemLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, BinocularsItemLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, BinocularsItemUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, BinocularsItemUpperEdgeY.position);
    }
 }
