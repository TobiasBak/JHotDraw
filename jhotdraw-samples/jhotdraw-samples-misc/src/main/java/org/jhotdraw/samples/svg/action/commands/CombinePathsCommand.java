package org.jhotdraw.samples.svg.action.commands;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.awt.geom.AffineTransform;
import java.util.*;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;

public class CombinePathsCommand implements PathCommand {
    @Override
    public void execute(DrawingView view, CompositeFigure group, List<Figure> figures) {
        Drawing drawing = view.getDrawing();
        int groupIndex = drawing.indexOf(figures.get(0));
        combinePaths(view, group, figures, groupIndex);
    }

    public void combinePaths(DrawingView view, CompositeFigure group, Collection<Figure> figures, int groupIndex) {
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(groupIndex, group);
        group.willChange();
        group.basicRemoveAllChildren();

        AffineTransform tx = figures.iterator().next().get(TRANSFORM);
        verifyTransformsAreTheSame(figures, tx);

        setAttributesOnGroupFigures(group, figures);

        group.set(TRANSFORM, tx);

        for (Figure f : figures) {
            SVGPathFigure path = (SVGPathFigure) f;
            // In case the transforms are different, we flatten it in the figures.
            if (tx == null) {
                path.flattenTransform();
            }
            List<Figure> children = new LinkedList<Figure>(path.getChildren());
            path.basicRemoveAllChildren();
            for (Figure child : children) {
                SVGBezierFigure bez = (SVGBezierFigure) child;
                child.willChange();
                group.basicAdd(child);
            }
        }

        group.changed();
        view.addToSelection(group);
    }

    void setAttributesOnGroupFigures(CompositeFigure group, Collection<Figure> figures) {
        for (Map.Entry<AttributeKey<?>, Object> entry : figures.iterator().next().getAttributes().entrySet()) {
            group.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
        }
    }

    void verifyTransformsAreTheSame(Collection<Figure> figures, AffineTransform tx) {
        for (Figure f : figures) {
            AffineTransform ftx = f.get(TRANSFORM);
            if (ftx == tx || ftx != null && tx != null && ftx.equals(tx)) {
            } else {
                tx = null;
                break;
            }
        }
    }

    void addChildrenToTheGroup (CompositeFigure group, Collection<Figure> figures, AffineTransform tx) {
        for (Figure f : figures) {
            SVGPathFigure path = (SVGPathFigure) f;
            // In case the transforms are different, we flatten it in the figures.
            if (tx == null) {
                path.flattenTransform();
            }
            List<Figure> children = new LinkedList<Figure>(path.getChildren());
            System.out.println("Children: " + children);
            path.basicRemoveAllChildren();
            for (Figure child : children) {
                SVGBezierFigure bez = (SVGBezierFigure) child;
                child.willChange();
                group.basicAdd(child);
                System.out.println("Child: " + child);
            }
        }
    }
}
