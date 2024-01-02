package org.jhotdraw.samples.svg.figures;

import junit.framework.TestCase;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.geom.BezierPath;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class SVGPathFigureTest extends TestCase {

    SVGPathFigure svgPathFigure;

    protected void setUp() throws Exception {
        super.setUp();
        svgPathFigure = new SVGPathFigure();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        svgPathFigure = null;
    }

    public void testContains() {
        //Setup
        Point2D.Double p = new Point2D.Double(0, 0);
        SVGBezierFigure figure = new SVGBezierFigure(true);
        figure.addNode((new BezierPath.Node(new Point2D.Double(0, 0))));
        svgPathFigure.add(figure);

        //Run the test
        boolean result = svgPathFigure.contains(p);

        //Verify the results
        assertTrue(result);
    }


    public void testDrawFigure() {
        //Setup
        Graphics2D graphic = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();

        //Run the test
        svgPathFigure.drawFigure(graphic);
        AffineTransform transform = graphic.getTransform();
        Paint paint = graphic.getPaint();
        Stroke stroke = graphic.getStroke();

        //Verify the results
        assertTrue(transform != null && paint != null && stroke != null);
    }

    public void testGetDrawingArea() {
        //Setup
        DefaultDrawingView view = new DefaultDrawingView();
        DefaultDrawing drawing = new DefaultDrawing();
        drawing.add(svgPathFigure);
        view.setDrawing(drawing);

        //Run the test
        Rectangle2D.Double drawingArea = svgPathFigure.getDrawingArea();

        //Verify the results
        assertNotNull(drawingArea);
    }
}