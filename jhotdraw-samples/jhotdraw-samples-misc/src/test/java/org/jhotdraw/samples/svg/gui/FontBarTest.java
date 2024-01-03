package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.figure.TextAreaFigure;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.samples.svg.SVGDrawingPanel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.swing.*;
import java.awt.*;

import static org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE;

public class FontBarTest {
    private static FontToolBar fontToolBar;
    private static SVGDrawingPanel drawingPanel;
    private static final String ITALIC_BUTTON_TOOL_TIP_TEXT = "Italic";
    private static final String UNDERLINE_BUTTON_TEXT = "Underline";
    private static final String BOLD_BUTTON_TEXT = "Bold";


    @BeforeAll
    public static void setUpClass() {
        drawingPanel = new SVGDrawingPanel();
        drawingPanel.setEditor(new DefaultDrawingEditor());

        findFontToolBar(drawingPanel);

        if (fontToolBar == null) {
            throw new RuntimeException("Could not find fontToolBar");
        }
    }

    @BeforeEach
    public void setUp() {
        drawingPanel.getDrawing().removeAllChildren();
    }

    private static boolean findFontToolBar(JComponent component) {
        for (Component c : component.getComponents()) {
            if (c instanceof FontToolBar) {
                fontToolBar = (FontToolBar) c;
                return true;
            }
            if (c instanceof JComponent) {
                boolean found = findFontToolBar((JComponent) c);
                if (found) {
                    return true;
                }
            }
        }
        return false;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testItalicButtonMakesTextItalic(int expansionState) {
        JPanel panel = (JPanel) fontToolBar.createDisclosedComponent(expansionState);
        JButton italicButton = getButton(panel, ITALIC_BUTTON_TOOL_TIP_TEXT);

        TextAreaFigure textAreaFigure = addSelectedTextAreaToDrawing();

        boolean isItalic = textAreaFigure.getFont().isItalic();
        if (isItalic) {
            throw new RuntimeException("Font is already italic");
        }
        italicButton.doClick();

        Assertions.assertTrue(textAreaFigure.getFont().isItalic());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testUnderlineButtonMakesTextUnderlined(int expansionState) {
        JPanel panel = (JPanel) fontToolBar.createDisclosedComponent(expansionState);
        JButton underlineButton = getButton(panel, UNDERLINE_BUTTON_TEXT);

        TextAreaFigure textAreaFigure = addSelectedTextAreaToDrawing();

        boolean isUnderlined = textAreaFigure.get(FONT_UNDERLINE);
        if (isUnderlined) {
            throw new RuntimeException("Font is already underlined");
        }

        underlineButton.doClick();

        Assertions.assertTrue(textAreaFigure.get(FONT_UNDERLINE));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testBoldButtonMakesTextBold(int expansionState) {
        JPanel panel = (JPanel) fontToolBar.createDisclosedComponent(expansionState);
        JButton boldButton = getButton(panel, BOLD_BUTTON_TEXT);

        TextAreaFigure textAreaFigure = addSelectedTextAreaToDrawing();

        boolean isBold = textAreaFigure.getFont().isBold();
        if (isBold) {
            throw new RuntimeException("Font is already bold");
        }

        boldButton.doClick();

        Assertions.assertTrue(textAreaFigure.getFont().isBold());
    }



    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testFontNameTextFieldChangesFontName(int state) {
        JPanel panel = (JPanel) fontToolBar.createDisclosedComponent(state);
        JTextField fontNameTextField =  getAttributeTextField(panel, "font");

        TextAreaFigure textAreaFigure = addSelectedTextAreaToDrawing();

        String fontName = textAreaFigure.getFont().getFamily();
        if (fontName.equals("Arial")) {
            fontName = "Times New Roman";
        } else {
            fontName = "Arial";
        }

        fontNameTextField.setText(fontName);
        Assertions.assertEquals(fontName, textAreaFigure.getFont().getFamily());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void testFontSizeTextFieldChangesFontSize(int state) {
        JPanel panel = (JPanel) fontToolBar.createDisclosedComponent(state);
        JTextField fontSizeTextField =  getAttributeTextField(panel, "size");

        TextAreaFigure textAreaFigure = addSelectedTextAreaToDrawing();

        int fontSize = textAreaFigure.getFont().getSize();
        fontSizeTextField.setText(Integer.toString(fontSize + 1));
        Assertions.assertEquals(fontSize + 1, textAreaFigure.getFont().getSize());
    }


    private static TextAreaFigure addSelectedTextAreaToDrawing() {
        return addSelectedTextAreaToDrawing(drawingPanel);
    }

    public static TextAreaFigure addSelectedTextAreaToDrawing(SVGDrawingPanel drawingPanel) {
        TextAreaFigure textAreaFigure = new TextAreaFigure();
        drawingPanel.getDrawing().add(textAreaFigure);

        drawingPanel.getView().selectAll();
        return textAreaFigure;
    }

    public static JAttributeTextField getAttributeTextField(JPanel panel, String toolTipText) {
        for (Component c : panel.getComponents()) {
            if (!(c instanceof JAttributeTextField)) {
                if (c instanceof JPanel) {
                    try {
                        return getAttributeTextField((JPanel) c, toolTipText);
                    } catch (RuntimeException e) {
                        continue;
                    }
                }
                continue;
            }
            JAttributeTextField attributeTextField = (JAttributeTextField) c;
            if (attributeTextField.getToolTipText() != null && attributeTextField.getToolTipText().toLowerCase().contains(toolTipText)) {
                return attributeTextField;
            }
        }

        throw new RuntimeException("Could not find " + toolTipText + " attribute text field");
    }

    private static JButton getButton(JPanel panel, String toolTipText) {
        for (Component c : panel.getComponents()) {
            if (!(c instanceof JButton)) {
                continue;
            }
            JButton button = (JButton) c;
            if (button.getToolTipText() != null && button.getToolTipText().contains(toolTipText)) {
                return button;
            }
        }

        throw new RuntimeException("Could not find " + toolTipText + " button");
    }
}
