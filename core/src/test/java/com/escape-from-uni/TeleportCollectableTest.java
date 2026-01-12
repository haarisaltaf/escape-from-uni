package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.TeleportCollectable;
import com.badlogic.gdx.math.Vector2;

public class TeleportCollectableTest {

    private TeleportCollectable TeleportCollectable = new TeleportCollectable(new Vector2 (128, 256));
    private TeleportCollectable TeleportCollectableNegativeX = new TeleportCollectable(new Vector2 (-128, 256));
    private TeleportCollectable TeleportCollectableNegativeY = new TeleportCollectable(new Vector2 (128, -256));
    private TeleportCollectable TeleportCollectableLowerEdgeX = new TeleportCollectable(new Vector2 (0, 256));
    private TeleportCollectable TeleportCollectableLowerEdgeY = new TeleportCollectable(new Vector2 (128, 0));
    private TeleportCollectable TeleportCollectableUpperEdgeX = new TeleportCollectable(new Vector2 (8192, 256));
    private TeleportCollectable TeleportCollectableUpperEdgeY = new TeleportCollectable(new Vector2 (128, 8192));
    private TeleportCollectable TeleportCollectableLowerErroniousX = new TeleportCollectable(new Vector2 (-1, 256));
    private TeleportCollectable TeleportCollectableLowerErroniousY = new TeleportCollectable(new Vector2 (128, -1));
    private TeleportCollectable TeleportCollectableUpperErroniousX = new TeleportCollectable(new Vector2 (8193, 256));
    private TeleportCollectable TeleportCollectableUpperErroniousY = new TeleportCollectable(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, TeleportCollectable.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, TeleportCollectableNegativeX.position);
        
        assertEquals(positionDefault, TeleportCollectableNegativeY.position);

        assertEquals(positionDefault, TeleportCollectableLowerErroniousX.position);
        assertEquals(positionDefault, TeleportCollectableLowerErroniousY.position);
        assertEquals(positionDefault, TeleportCollectableUpperErroniousX.position);
        assertEquals(positionDefault, TeleportCollectableUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, TeleportCollectableLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, TeleportCollectableLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, TeleportCollectableUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, TeleportCollectableUpperEdgeY.position);
    }
 }
