package org.jhotdraw.samples.svg;

import junit.framework.TestCase;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.HashMap;

public class PathToolTest extends TestCase {

    PathTool pathTool;

    protected void setUp() throws Exception {
        super.setUp();
        HashMap<AttributeKey<?>, Object> attributes = new HashMap<>();
        pathTool = new PathTool(new SVGPathFigure(), new SVGBezierFigure(), attributes);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        pathTool = null;
    }


    public void testCreatePath() {
        //Setup

        //Run test

        //Verify resul

    }

    public void testFinishCreation() {

    }
}