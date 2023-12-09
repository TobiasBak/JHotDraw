/*
 * @(#)CombinePathsAction.java
 *
 * Copyright (c) 2006-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.action;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.CompositeFigure;
import java.util.*;
import javax.swing.undo.*;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.samples.svg.action.commands.CombinePathsCommand;
import org.jhotdraw.samples.svg.action.commands.PathCommand;
import org.jhotdraw.samples.svg.action.commands.SplitPathsCommand;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;
import org.jhotdraw.util.*;

/**
 * CombinePathsAction.
 * <p>
 * FIXME - Transforms are lost during Undo/Redo.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class CombineAction extends AbstractSelectedAction {

    private static final long serialVersionUID = 1L;
    public static final String ID = "edit.combinePaths";
    public CompositeFigure prototype;
    private ResourceBundleUtil labels
            = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
    private PathCommand combineCommand = new CombinePathsCommand();
    private PathCommand splitCommand = new SplitPathsCommand();

    @FeatureEntryPoint(value = "Combine Action")
    public CombineAction(DrawingEditor editor) {
        this(editor, new SVGPathFigure(true));
    }

    @FeatureEntryPoint(value = "Combine Action")
    public CombineAction(DrawingEditor editor, SVGPathFigure prototype) {
        super(editor);
        this.prototype = prototype;
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        labels.configureAction(this, ID);
        updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        if (getView() != null) {
            setEnabled(canGroup());
        } else {
            setEnabled(false);
        }
    }

    protected boolean canGroup() {
        boolean canCombine = getView().getSelectionCount() > 1;
        if (canCombine) {
            for (Figure f : getView().getSelectedFigures()) {
                if (!(f instanceof SVGPathFigure)) {
                    canCombine = false;
                    break;
                }
            }
        }
        return canCombine;
    }

    @FeatureEntryPoint(value = "Combine Action")
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        combineActionPerformed(e);
    }

    @FeatureEntryPoint(value = "Combine Action")
    public void combineActionPerformed(java.awt.event.ActionEvent e) {
        final DrawingView view = getView();
        Drawing drawing = view.getDrawing();

        if (canGroup()) {
            final List<Figure> ungroupedPaths = drawing.sort(view.getSelectedFigures());
            final CompositeFigure group = (CompositeFigure) prototype.clone();

            combineCommand.execute(view, group, ungroupedPaths);

            UndoableEdit edit = new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getPresentationName() {
                    return labels.getTextProperty("edit.combinePaths");
                }

                @Override
                public void redo() throws CannotRedoException {
                    super.redo();
                    combineCommand.execute(view, group, ungroupedPaths);
                }

                @Override
                public void undo() throws CannotUndoException {
                    super.undo();
                    splitCommand.execute(view, group, ungroupedPaths);
                }

                @Override
                public boolean addEdit(UndoableEdit anEdit) {
                    return super.addEdit(anEdit);
                }
            };
            fireUndoableEditHappened(edit);
        }
    }
}
