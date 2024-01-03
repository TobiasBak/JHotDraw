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
import org.jhotdraw.samples.svg.action.commands.CombinePathsCommand;
import org.jhotdraw.samples.svg.action.commands.PathCommand;
import org.jhotdraw.samples.svg.action.commands.SplitPathsCommand;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;
import org.jhotdraw.util.ResourceBundleUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.util.*;

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
    private PathCommand combineCommand = new CombinePathsCommand();
    private PathCommand splitCommand = new SplitPathsCommand();

    @FeatureEntryPoint(value = "Split Action")
    public SplitAction(DrawingEditor editor) {
        this(editor, new SVGPathFigure());
        labels.configureAction(this, ID);
    }

    @FeatureEntryPoint(value = "Split Action")
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

        if (canUngroup()) {
            final CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
            final LinkedList<Figure> ungroupedPaths = new LinkedList<Figure>();

            splitCommand.execute(view, group, ungroupedPaths);

            UndoableEdit edit = new AbstractUndoableEdit() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getPresentationName() {
                    return labels.getTextProperty("edit.splitPath");
                }

                @Override
                public void redo() throws CannotRedoException {
                    super.redo();
                    splitCommand.execute(view, group, ungroupedPaths);
                }

                @Override
                public void undo() throws CannotUndoException {
                    super.undo();
                    combineCommand.execute(view, group, ungroupedPaths);
                }
            };
            fireUndoableEditHappened(edit);
        }
    }
}
