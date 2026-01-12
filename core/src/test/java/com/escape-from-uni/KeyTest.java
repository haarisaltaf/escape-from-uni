package com.escapefromuni;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.Key;
import com.badlogic.gdx.math.Vector2;

public class KeyTest {

    private Key Key = new Key(new Vector2 (128, 256));
    private Key KeyNegativeX = new Key(new Vector2 (-128, 256));
    private Key KeyNegativeY = new Key(new Vector2 (128, -256));
    private Key KeyLowerEdgeX = new Key(new Vector2 (0, 256));
    private Key KeyLowerEdgeY = new Key(new Vector2 (128, 0));
    private Key KeyUpperEdgeX = new Key(new Vector2 (8192, 256));
    private Key KeyUpperEdgeY = new Key(new Vector2 (128, 8192));
    private Key KeyLowerErroniousX = new Key(new Vector2 (-1, 256));
    private Key KeyLowerErroniousY = new Key(new Vector2 (128, -1));
    private Key KeyUpperErroniousX = new Key(new Vector2 (8193, 256));
    private Key KeyUpperErroniousY = new Key(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, Key.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, KeyNegativeX.position);
        
        assertEquals(positionDefault, KeyNegativeY.position);

        assertEquals(positionDefault, KeyLowerErroniousX.position);
        assertEquals(positionDefault, KeyLowerErroniousY.position);
        assertEquals(positionDefault, KeyUpperErroniousX.position);
        assertEquals(positionDefault, KeyUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, KeyLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, KeyLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, KeyUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, KeyUpperEdgeY.position);
    }
 }
