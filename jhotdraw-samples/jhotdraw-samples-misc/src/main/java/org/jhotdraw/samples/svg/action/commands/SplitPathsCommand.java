package org.jhotdraw.samples.svg.action.commands;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SplitPathsCommand implements PathCommand {
    @Override
    public void execute(DrawingView view, CompositeFigure group, List<Figure> figures) {
        Drawing drawing = view.getDrawing();
        final int[] ungroupedPathsIndices = new int[group.getChildCount()];
        final int[] ungroupedPathsChildCounts = new int[group.getChildCount()];

        setUngroupedPathsAndAddPath(group, figures, drawing, ungroupedPathsIndices, ungroupedPathsChildCounts);

        splitPath(view, group, figures, ungroupedPathsIndices, ungroupedPathsChildCounts);
    }

    private void setUngroupedPathsAndAddPath(CompositeFigure group, List<Figure> figures, Drawing drawing, int[] ungroupedPathsIndices, int[] ungroupedPathsChildCounts) {
        int i = 0;
        int index = drawing.indexOf(group);
        for (Figure f : group.getChildren()) {
            SVGPathFigure path = new SVGPathFigure(true);
            for (Map.Entry<AttributeKey<?>, Object> entry : group.getAttributes().entrySet()) {
                path.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
            }
            figures.add(path);
            ungroupedPathsIndices[i] = index + i;
            ungroupedPathsChildCounts[i] = 1;
            i++;
        }
    }

    public void splitPath(DrawingView view, CompositeFigure group, List<Figure> ungroupedPaths, int[] ungroupedPathsIndices, int[] ungroupedPathsChildCounts) {
        view.clearSelection();
        group.basicRemoveAllChildren();
        view.getDrawing().remove(group);

        SVGPathFigure pathFigure = (SVGPathFigure) group;

        pathFigure.flattenTransform();

        addGroupedPathsToUngroupedPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);

        view.addToSelection(ungroupedPaths);
    }

    private void addGroupedPathsToUngroupedPath(DrawingView view, CompositeFigure group, List<Figure> ungroupedPaths, int[] ungroupedPathsIndices, int[] ungroupedPathsChildCounts) {
        Iterator<Figure> groupedFigures = new LinkedList<Figure>(group.getChildren()).iterator();
        for (int i = 0; i < ungroupedPaths.size(); i++) {
            CompositeFigure path = (CompositeFigure) ungroupedPaths.get(i);
            view.getDrawing().add(ungroupedPathsIndices[i], path);
            path.willChange();
            for (int j = 0; j < ungroupedPathsChildCounts[i]; j++) {
                Figure child = groupedFigures.next();
                child.willChange();
                path.basicAdd(child);
            }
            path.changed();
        }
    }
}
