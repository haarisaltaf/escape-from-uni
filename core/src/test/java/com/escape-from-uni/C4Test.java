package com.escapefromuni.collectables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.C4;
import com.badlogic.gdx.math.Vector2;

public class C4Test {

    private C4 C4 = new C4(new Vector2 (128, 256));
    private C4 C4NegativeX = new C4(new Vector2 (-128, 256));
    private C4 C4NegativeY = new C4(new Vector2 (128, -256));
    private C4 C4LowerEdgeX = new C4(new Vector2 (0, 256));
    private C4 C4LowerEdgeY = new C4(new Vector2 (128, 0));
    private C4 C4UpperEdgeX = new C4(new Vector2 (8192, 256));
    private C4 C4UpperEdgeY = new C4(new Vector2 (128, 8192));
    private C4 C4LowerErroniousX = new C4(new Vector2 (-1, 256));
    private C4 C4LowerErroniousY = new C4(new Vector2 (128, -1));
    private C4 C4UpperErroniousX = new C4(new Vector2 (8193, 256));
    private C4 C4UpperErroniousY = new C4(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, C4.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, C4NegativeX.position);
        
        assertEquals(positionDefault, C4NegativeY.position);

        assertEquals(positionDefault, C4LowerErroniousX.position);
        assertEquals(positionDefault, C4LowerErroniousY.position);
        assertEquals(positionDefault, C4UpperErroniousX.position);
        assertEquals(positionDefault, C4UpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, C4LowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, C4LowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, C4UpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, C4UpperEdgeY.position);
    }
 }
