package com.escapefromuni;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import com.escapefromuni.main.collectables.TimeStop;
import com.badlogic.gdx.math.Vector2;

public class TimeStopTest {

    private TimeStop TimeStop = new TimeStop(new Vector2 (128, 256));
    private TimeStop TimeStopNegativeX = new TimeStop(new Vector2 (-128, 256));
    private TimeStop TimeStopNegativeY = new TimeStop(new Vector2 (128, -256));
    private TimeStop TimeStopLowerEdgeX = new TimeStop(new Vector2 (0, 256));
    private TimeStop TimeStopLowerEdgeY = new TimeStop(new Vector2 (128, 0));
    private TimeStop TimeStopUpperEdgeX = new TimeStop(new Vector2 (8192, 256));
    private TimeStop TimeStopUpperEdgeY = new TimeStop(new Vector2 (128, 8192));
    private TimeStop TimeStopLowerErroniousX = new TimeStop(new Vector2 (-1, 256));
    private TimeStop TimeStopLowerErroniousY = new TimeStop(new Vector2 (128, -1));
    private TimeStop TimeStopUpperErroniousX = new TimeStop(new Vector2 (8193, 256));
    private TimeStop TimeStopUpperErroniousY = new TimeStop(new Vector2 (128, 8193));
    

    @Test
    void testCollectablePosition() {
        Vector2 position = new Vector2 (128, 256);
        assertEquals(position, TimeStop.position);
        // 
        Vector2 positionDefault = new Vector2 (0, 0);
        assertEquals(positionDefault, TimeStopNegativeX.position);
        
        assertEquals(positionDefault, TimeStopNegativeY.position);

        assertEquals(positionDefault, TimeStopLowerErroniousX.position);
        assertEquals(positionDefault, TimeStopLowerErroniousY.position);
        assertEquals(positionDefault, TimeStopUpperErroniousX.position);
        assertEquals(positionDefault, TimeStopUpperErroniousY.position);

        Vector2 positionLowerEdgeX = new Vector2 (0, 256);
        assertEquals(positionLowerEdgeX, TimeStopLowerEdgeX.position);
        Vector2 positionLowerEdgeY = new Vector2 (128, 0);
        assertEquals(positionLowerEdgeY, TimeStopLowerEdgeY.position);
        Vector2 positionUpperEdgeX = new Vector2 (8192, 256);
        assertEquals(positionUpperEdgeX, TimeStopUpperEdgeX.position);
        Vector2 positionUpperEdgeY = new Vector2 (128, 8192);
        assertEquals(positionUpperEdgeY, TimeStopUpperEdgeY.position);
    }
 }
