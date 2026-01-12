package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.SpeedCollectable;
import com.badlogic.gdx.math.Vector2;

public class SpeedCollectableTest {

    private SpeedCollectable speedCollectable = new SpeedCollectable(new Vector2 (128, 256));
    private SpeedCollectable speedCollectableNegativeX = new SpeedCollectable(new Vector2 (-128, 256));
    private SpeedCollectable speedCollectableNegativeY = new SpeedCollectable(new Vector2 (128, -256));
    private SpeedCollectable speedCollectableLowerEdgeX = new SpeedCollectable(new Vector2 (0, 256));
    private SpeedCollectable speedCollectableLowerEdgeY = new SpeedCollectable(new Vector2 (128, 0));
    private SpeedCollectable speedCollectableUpperEdgeX = new SpeedCollectable(new Vector2 (8192, 256));
    private SpeedCollectable speedCollectableUpperEdgeY = new SpeedCollectable(new Vector2 (128, 8192));
    private SpeedCollectable speedCollectableLowerErroniousX = new SpeedCollectable(new Vector2 (-1, 256));
    private SpeedCollectable speedCollectableLowerErroniousY = new SpeedCollectable(new Vector2 (128, -1));
    private SpeedCollectable speedCollectableUpperErroniousX = new SpeedCollectable(new Vector2 (8193, 256));
    private SpeedCollectable speedCollectableUpperErroniousY = new SpeedCollectable(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, speedCollectable.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, speedCollectableNegativeX.position);
        
        assertEquals(positionDefault, speedCollectableNegativeY.position);

        assertEquals(positionDefault, speedCollectableLowerErroniousX.position);
        assertEquals(positionDefault, speedCollectableLowerErroniousY.position);
        assertEquals(positionDefault, speedCollectableUpperErroniousX.position);
        assertEquals(positionDefault, speedCollectableUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, speedCollectableLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, speedCollectableLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, speedCollectableUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, speedCollectableUpperEdgeY.position);
    }
 }
