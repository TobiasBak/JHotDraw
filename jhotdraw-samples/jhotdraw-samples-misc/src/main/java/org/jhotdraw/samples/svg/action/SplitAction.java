/*
 * @(#)SplitPathsAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.action.AbstractSelectedAction;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.awt.geom.AffineTransform;
import java.util.*;

import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;

/**
 * SplitPathsAction.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SplitAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.splitPath";
    public CompositeFigure prototype;
    private ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

    /**
     * Creates a new instance.
     */
    @FeatureEntryPoint(value = "Split Action")
    public SplitAction(DrawingEditor editor) {
        this(editor, new SVGPathFigure());
        labels.configureAction(this, ID);
    }

    public SplitAction(DrawingEditor editor, SVGPathFigure prototype) {
        super(editor);
        this.prototype = prototype;
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(canUngroup());
        } else {
            setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        splitActionPerformed(e);
    }

    protected boolean canUngroup() {
        return getView() != null && getView().getSelectionCount() == 1
                && prototype != null
                && getView().getSelectedFigures().iterator().next().getClass().equals(
                prototype.getClass())
                && ((CompositeFigure) getView().getSelectedFigures().iterator().next()).getChildCount() > 1;
    }

    public void splitActionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        Drawing drawing = view.getDrawing();
        if (canUngroup()) {
            final CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
            final LinkedList<Figure> ungroupedPaths = new LinkedList<Figure>();
            final int[] ungroupedPathsIndices = new int[group.getChildCount()];
            final int[] ungroupedPathsChildCounts = new int[group.getChildCount()];
            int i = 0;
            int index = drawing.indexOf(group);
            for (Figure f : group.getChildren()) {
                SVGPathFigure path = new SVGPathFigure(true);
                for (Map.Entry<AttributeKey<?>, Object> entry : group.getAttributes().entrySet()) {
                    path.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
                }
                ungroupedPaths.add(path);
                ungroupedPathsIndices[i] = index + i;
                ungroupedPathsChildCounts[i] = 1;
                i++;
            }
            splitPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);
            UndoableEdit edit = new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getPresentationName() {
                    return labels.getTextProperty("edit.splitPath");
                }

                @Override
                public void redo() throws CannotRedoException {
                    super.redo();
                    splitPath(view, group, ungroupedPaths, ungroupedPathsIndices, ungroupedPathsChildCounts);
                }

                @Override
                public void undo() throws CannotUndoException {
                    super.undo();
                    combinePaths(view, group, ungroupedPaths, ungroupedPathsIndices[0]);
                }
            };
            fireUndoableEditHappened(edit);
        }
    }

    public void splitPath(DrawingView view, CompositeFigure group, List<Figure> ungroupedPaths, int[] ungroupedPathsIndices, int[] ungroupedPathsChildCounts) {
        view.clearSelection();
        Iterator<Figure> groupedFigures = new LinkedList<Figure>(group.getChildren()).iterator();
        group.basicRemoveAllChildren();
        view.getDrawing().remove(group);
        SVGPathFigure pathFigure = (SVGPathFigure) group;
        pathFigure.flattenTransform();
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
        view.addToSelection(ungroupedPaths);
    }

    public void combinePaths(DrawingView view, CompositeFigure group, Collection<Figure> figures, int groupIndex) {
        view.getDrawing().basicRemoveAll(figures);
        view.clearSelection();
        view.getDrawing().add(groupIndex, group);
        group.willChange();
        group.basicRemoveAllChildren();
        // Verify if all figures have the same transform
        AffineTransform tx = figures.iterator().next().get(TRANSFORM);
        for (Figure f : figures) {
            AffineTransform ftx = f.get(TRANSFORM);
            if (ftx == tx || ftx != null && tx != null && ftx.equals(tx)) {
            } else {
                tx = null;
                break;
            }
        }
        addingFiguresToGroup(group, figures);
        // In case all figures have the same transforms, we set it here.
        // In case the transforms are different, we set null here.
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
    public void addingFiguresToGroup(CompositeFigure group, Collection<Figure> figures) {
        for (Map.Entry<AttributeKey<?>, Object> entry : figures.iterator().next().getAttributes().entrySet()) {
            group.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
        }
    }
}
