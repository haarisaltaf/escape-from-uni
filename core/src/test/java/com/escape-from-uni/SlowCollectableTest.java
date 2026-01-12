package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.SlowCollectable;
import com.badlogic.gdx.math.Vector2;

public class SlowCollectableTest {

    private SlowCollectable SlowCollectable = new SlowCollectable(new Vector2 (128, 256));
    private SlowCollectable SlowCollectableNegativeX = new SlowCollectable(new Vector2 (-128, 256));
    private SlowCollectable SlowCollectableNegativeY = new SlowCollectable(new Vector2 (128, -256));
    private SlowCollectable SlowCollectableLowerEdgeX = new SlowCollectable(new Vector2 (0, 256));
    private SlowCollectable SlowCollectableLowerEdgeY = new SlowCollectable(new Vector2 (128, 0));
    private SlowCollectable SlowCollectableUpperEdgeX = new SlowCollectable(new Vector2 (8192, 256));
    private SlowCollectable SlowCollectableUpperEdgeY = new SlowCollectable(new Vector2 (128, 8192));
    private SlowCollectable SlowCollectableLowerErroniousX = new SlowCollectable(new Vector2 (-1, 256));
    private SlowCollectable SlowCollectableLowerErroniousY = new SlowCollectable(new Vector2 (128, -1));
    private SlowCollectable SlowCollectableUpperErroniousX = new SlowCollectable(new Vector2 (8193, 256));
    private SlowCollectable SlowCollectableUpperErroniousY = new SlowCollectable(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, SlowCollectable.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, SlowCollectableNegativeX.position);
        
        assertEquals(positionDefault, SlowCollectableNegativeY.position);

        assertEquals(positionDefault, SlowCollectableLowerErroniousX.position);
        assertEquals(positionDefault, SlowCollectableLowerErroniousY.position);
        assertEquals(positionDefault, SlowCollectableUpperErroniousX.position);
        assertEquals(positionDefault, SlowCollectableUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, SlowCollectableLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, SlowCollectableLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, SlowCollectableUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, SlowCollectableUpperEdgeY.position);
    }
 }
