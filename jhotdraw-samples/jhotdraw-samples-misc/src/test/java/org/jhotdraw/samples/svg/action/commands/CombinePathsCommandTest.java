package org.jhotdraw.samples.svg.action.commands;

import junit.framework.TestCase;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;


public class CombinePathsCommandTest extends TestCase {

    CombinePathsCommand command;

    public void setUp() throws Exception {
        super.setUp();
        command = new CombinePathsCommand();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        command = null;
    }

    public void testVerifyTransformsAreNotTheSameWithTwoRectangles(){
        // Setup
        Collection<Figure> figures = new ArrayList<>();
        figures.add(new SVGRectFigure(0, 0, 50, 50));
        figures.add(new SVGRectFigure(0, 0, 50, 50));
        AffineTransform tx = figures.iterator().next().get(TRANSFORM);

        // Run the test
        command.verifyTransformsAreTheSame(figures, tx);

        // Assert
        assertNull(tx);
    }

    public void testVerifyTransformsAreNotTheSameWithEmptyPathFigure() {
        // Setup
        Collection<Figure> figures = new ArrayList<>();
        figures.add(new SVGPathFigure(true));
        figures.add(new SVGRectFigure(0, 0, 50, 50));
        AffineTransform tx = figures.iterator().next().get(TRANSFORM);

        // Run the test
        command.verifyTransformsAreTheSame(figures, tx);

        // Assert
        assertNull(tx);
    }

    public void testSetAttributesOnGroupFiguresTwoFigures() {
        // Setup
        CompositeFigure group = new SVGPathFigure(true);
        Collection<Figure> figures = new ArrayList<>();
        SVGRectFigure figure1 = new SVGRectFigure(0, 0, 50, 50);
        SVGRectFigure figure2 = new SVGRectFigure(0, 0, 50, 50);
        figures.add(figure1);
        figures.add(figure2);

        // Run the test
        command.setAttributesOnGroupFigures(group, figures);

        // Assert
        CompositeFigure expected = new SVGPathFigure(true);
        System.out.println(group.getAttributes());
        System.out.println(expected.getAttributes());
        assertEquals(expected.getAttributes(), group.getAttributes());
    }

    public void testSetAttributesOnGroupFiguresEmptyFigure() {
        // Setup
        CompositeFigure group = new SVGPathFigure(true);
        Collection<Figure> figures = new ArrayList<>();
        SVGPathFigure figure1 = new SVGPathFigure(true);
        SVGRectFigure figure2 = new SVGRectFigure(0, 0, 50, 50);
        figures.add(figure1);
        figures.add(figure2);

        // Run the test
        command.setAttributesOnGroupFigures(group, figures);

        // Assert
        CompositeFigure expected = new SVGPathFigure(true);
        assertEquals(expected.getAttributes(), group.getAttributes());
    }

    public void testSetAttributesOnGroupWithNonDefaultAttributes() {
        // Setup
        CompositeFigure group = new SVGPathFigure(true);
        Collection<Figure> figures = new ArrayList<>();
        SVGRectFigure figure1 = new SVGRectFigure(0, 0, 50, 50);
        SVGRectFigure figure2 = new SVGRectFigure(0, 0, 50, 50);
        figures.add(figure1);
        figures.add(figure2);

        // Run the test
        command.setAttributesOnGroupFigures(group, figures);

        // Assert
        CompositeFigure expected = new SVGPathFigure(true);
        for (Map.Entry<AttributeKey<?>, Object> entry : figures.iterator().next().getAttributes().entrySet()) {
            expected.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
            if (entry.getValue() != null && entry.getValue().equals(0)) {
                expected.set((AttributeKey<Object>) entry.getKey(), 0);
            }
        }
        assertNotSame(expected.getAttributes(), group.getAttributes());
    }

    public void testAddChildrenToTheGroupTwoRectangles() {
        // Setup
        CompositeFigure group = new SVGPathFigure(true);
        Collection<Figure> figures = new ArrayList<>();
        Figure figure1 = new SVGPathFigure();
        Figure figure2 = new SVGPathFigure();
        figures.add(figure1);
        figures.add(figure2);
        AffineTransform tx = figures.iterator().next().get(TRANSFORM);

        // Run the test
        command.addChildrenToTheGroup(group, figures, tx);

        // Assert
        CompositeFigure expected = new SVGPathFigure(true);
        Figure figure3 = new SVGBezierFigure();
        Figure figure4 = new SVGBezierFigure();
        expected.basicAdd(figure3);
        expected.basicAdd(figure4);

        assertTrue(expected.getChildren().size() == group.getChildren().size());
    }
}