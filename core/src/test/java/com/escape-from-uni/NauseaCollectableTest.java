package com.escapefromuni;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.NauseaCollectable;
import com.badlogic.gdx.math.Vector2;

public class NauseaCollectableTest {

    private NauseaCollectable NauseaCollectable = new NauseaCollectable(new Vector2 (128, 256));
    private NauseaCollectable NauseaCollectableNegativeX = new NauseaCollectable(new Vector2 (-128, 256));
    private NauseaCollectable NauseaCollectableNegativeY = new NauseaCollectable(new Vector2 (128, -256));
    private NauseaCollectable NauseaCollectableLowerEdgeX = new NauseaCollectable(new Vector2 (0, 256));
    private NauseaCollectable NauseaCollectableLowerEdgeY = new NauseaCollectable(new Vector2 (128, 0));
    private NauseaCollectable NauseaCollectableUpperEdgeX = new NauseaCollectable(new Vector2 (8192, 256));
    private NauseaCollectable NauseaCollectableUpperEdgeY = new NauseaCollectable(new Vector2 (128, 8192));
    private NauseaCollectable NauseaCollectableLowerErroniousX = new NauseaCollectable(new Vector2 (-1, 256));
    private NauseaCollectable NauseaCollectableLowerErroniousY = new NauseaCollectable(new Vector2 (128, -1));
    private NauseaCollectable NauseaCollectableUpperErroniousX = new NauseaCollectable(new Vector2 (8193, 256));
    private NauseaCollectable NauseaCollectableUpperErroniousY = new NauseaCollectable(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, NauseaCollectable.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, NauseaCollectableNegativeX.position);
        
        assertEquals(positionDefault, NauseaCollectableNegativeY.position);

        assertEquals(positionDefault, NauseaCollectableLowerErroniousX.position);
        assertEquals(positionDefault, NauseaCollectableLowerErroniousY.position);
        assertEquals(positionDefault, NauseaCollectableUpperErroniousX.position);
        assertEquals(positionDefault, NauseaCollectableUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, NauseaCollectableLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, NauseaCollectableLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, NauseaCollectableUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, NauseaCollectableUpperEdgeY.position);
    }
 }
