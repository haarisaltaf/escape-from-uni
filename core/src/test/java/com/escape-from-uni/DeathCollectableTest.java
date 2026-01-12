package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.DeathCollectable;
import com.badlogic.gdx.math.Vector2;

public class DeathCollectableTest {

    private DeathCollectable DeathCollectable = new DeathCollectable(new Vector2 (128, 256));
    private DeathCollectable DeathCollectableNegativeX = new DeathCollectable(new Vector2 (-128, 256));
    private DeathCollectable DeathCollectableNegativeY = new DeathCollectable(new Vector2 (128, -256));
    private DeathCollectable DeathCollectableLowerEdgeX = new DeathCollectable(new Vector2 (0, 256));
    private DeathCollectable DeathCollectableLowerEdgeY = new DeathCollectable(new Vector2 (128, 0));
    private DeathCollectable DeathCollectableUpperEdgeX = new DeathCollectable(new Vector2 (8192, 256));
    private DeathCollectable DeathCollectableUpperEdgeY = new DeathCollectable(new Vector2 (128, 8192));
    private DeathCollectable DeathCollectableLowerErroniousX = new DeathCollectable(new Vector2 (-1, 256));
    private DeathCollectable DeathCollectableLowerErroniousY = new DeathCollectable(new Vector2 (128, -1));
    private DeathCollectable DeathCollectableUpperErroniousX = new DeathCollectable(new Vector2 (8193, 256));
    private DeathCollectable DeathCollectableUpperErroniousY = new DeathCollectable(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, DeathCollectable.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, DeathCollectableNegativeX.position);
        
        assertEquals(positionDefault, DeathCollectableNegativeY.position);

        assertEquals(positionDefault, DeathCollectableLowerErroniousX.position);
        assertEquals(positionDefault, DeathCollectableLowerErroniousY.position);
        assertEquals(positionDefault, DeathCollectableUpperErroniousX.position);
        assertEquals(positionDefault, DeathCollectableUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, DeathCollectableLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, DeathCollectableLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, DeathCollectableUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, DeathCollectableUpperEdgeY.position);
    }
 }
