package org.jhotdraw.samples.svg;

import junit.framework.TestCase;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import java.util.HashMap;

public class RectangleToolTest extends TestCase {

    SVGRectFigure rectangleTool;

    protected void setUp() throws Exception {
        super.setUp();
        rectangleTool = new SVGRectFigure();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        rectangleTool = null;
    }


    public void testCreatePath() {
        // Setup

        // Run test

        // Verify result

    }

    public void testFinishCreation() {

    }
}
