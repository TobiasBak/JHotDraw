package org.jhotdraw.samples.svg.gui;

import org.jetbrains.annotations.NotNull;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.tool.TextAreaCreationTool;
import org.jhotdraw.draw.tool.TextCreationTool;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class FontBarSelectionComponentDisplayer extends SelectionComponentDisplayer {
    public FontBarSelectionComponentDisplayer(@NotNull DrawingEditor editor, JComponent component) {
        super(editor, component);
    }

    @Override
    public void updateVisibility() {
        JComponent component = getComponent();
        if (component == null) {
            dispose();
            return;
        }

        boolean visible = editorHasActiveView()
                && (visibleBecauseOfTool() || selectedFiguresContainsText(editor.getActiveView().getSelectedFigures()));

        component.setVisible(visible);

        // The following is needed to trick BoxLayout
        Dimension preferredSize = visible ? null : new Dimension(0, 0);
        component.setPreferredSize(preferredSize);

        component.revalidate();
    }

    private boolean editorHasActiveView() {
        return editor != null && editor.getActiveView() != null;
    }

    private boolean visibleBecauseOfTool() {
        return isVisibleIfCreationTool && textToolIsActive();
    }

    private boolean textToolIsActive() {
        return editor.getTool() instanceof TextCreationTool || editor.getTool() instanceof TextAreaCreationTool;
    }

    private boolean selectedFiguresContainsText(Collection<Figure> figures) {
        for (Figure f : figures) {
            if (f instanceof TextHolderFigure) {
                return true;
            }
            if (f instanceof CompositeFigure && selectedFiguresContainsText(((CompositeFigure) f).getChildren())) {
                return true;
            }
        }
        return false;
    }
}
