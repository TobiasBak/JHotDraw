package org.jhotdraw.samples.svg.gui.fontSteps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.figure.TextAreaFigure;
import org.jhotdraw.samples.svg.SVGDrawingPanel;
import org.jhotdraw.samples.svg.gui.FontToolBar;

import javax.swing.*;
import java.awt.*;

import static org.jhotdraw.samples.svg.gui.FontBarTest.addSelectedTextAreaToDrawing;

public class GivenDrawing extends Stage<GivenDrawing> {
    @ProvidedScenarioState
    JPanel panel;

    @ProvidedScenarioState
    SVGDrawingPanel drawingPanel;

    @ProvidedScenarioState
    TextAreaFigure textAreaFigure;

    public GivenDrawing a_drawing() {
        drawingPanel = new SVGDrawingPanel();
        drawingPanel.setEditor(new DefaultDrawingEditor());

        FontToolBar fontToolBar = findFontToolBar(drawingPanel);

        panel = (JPanel) fontToolBar.createDisclosedComponent(1);
        return self();
    }

    public GivenDrawing some_selected_text() {
        textAreaFigure = addSelectedTextAreaToDrawing(drawingPanel);

        return self();
    }

    private static FontToolBar findFontToolBar(JComponent component) {
        FontToolBar fontToolBar = null;
        for (Component c : component.getComponents()) {
            if (c instanceof FontToolBar) {
                fontToolBar = (FontToolBar) c;
                return fontToolBar;
            }
            if (c instanceof JComponent) {
                fontToolBar = findFontToolBar((JComponent) c);
                if (fontToolBar != null) {
                    return fontToolBar;
                }
            }
        }
        return fontToolBar;
    }

}
