/*
 * @(#)StrokeToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.SliderUI;

import static org.jhotdraw.draw.AttributeKeys.FONT_FACE;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;

import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.event.SelectionComponentRepainter;
import org.jhotdraw.draw.gui.JAttributeSlider;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.draw.tool.TextAreaCreationTool;
import org.jhotdraw.draw.tool.TextCreationTool;
import org.jhotdraw.gui.JFontChooser;
import org.jhotdraw.gui.JPopupButton;
import org.jhotdraw.formatter.FontFormatter;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;

/**
 * StrokeToolBar.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FontToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private SelectionComponentDisplayer displayer;


    /**
     * Creates new instance.
     */
    @FeatureEntryPoint(value = "FontToolBar")
    public FontToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString("font.toolbar"));
        JFontChooser.loadAllFonts();
        setDisclosureStateCount(3);
    }

    @Override
    @FeatureEntryPoint(value = "FontToolBar")
    public void setEditor(DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this) {
                @Override
                public void updateVisibility() {
                    boolean newValue = editor != null
                            && editor.getActiveView() != null
                            && (isVisibleIfCreationTool && ((editor.getTool() instanceof TextCreationTool) || editor.getTool() instanceof TextAreaCreationTool)
                            || containsTextHolderFigure(editor.getActiveView().getSelectedFigures()));
                    JComponent component = getComponent();
                    if (component == null) {
                        dispose();
                        return;
                    }
                    component.setVisible(newValue);
                    // The following is needed to trick BoxLayout
                    if (newValue) {
                        component.setPreferredSize(null);
                    } else {
                        component.setPreferredSize(new Dimension(0, 0));
                    }
                    component.revalidate();
                }

                private boolean containsTextHolderFigure(Collection<Figure> figures) {
                    for (Figure f : figures) {
                        if (f instanceof TextHolderFigure) {
                            return true;
                        } else if (f instanceof CompositeFigure) {
                            if (containsTextHolderFigure(((CompositeFigure) f).getChildren())) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            };
        }
    }

    @Override
    @FeatureEntryPoint(value = "FontToolBar")
    protected JComponent createDisclosedComponent(int state) {
        JPanel basePanel = createBasePanel();

        boolean noEditorSet = editor == null;
        if (noEditorSet) {
            return basePanel;
        }

        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

        ExpansionState expansionState = ExpansionState.getExpansionState(state);
        switch (expansionState) {
            case HALF:
                addFontChooserElements(2, 2, labels, basePanel);
                break;
            case FULL:
                addFontChooserElements(12, 3, labels, basePanel);
                break;
            default:
                basePanel = null;
                break;
        }

        return basePanel;
    }

    private static JPanel createBasePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 8));
        p.setLayout(new GridBagLayout());
        return p;
    }

    private void addFontChooserElements(int textFieldColumnWidth, int textFieldGridWidth, ResourceBundleUtil labels, JPanel p) {
        addTextFieldForFontName(textFieldColumnWidth, textFieldGridWidth, labels, p);

        addFontChooserButton(labels, p);

        // Font size field with slider
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);

        addControlElementsForFontSize(labels, p2);
        addPanel2(p, p2);

        // Font style buttons
        addButton(ButtonFactory.createFontStyleBoldButton(editor, labels, disposables), "first", null, p);
        addButton(ButtonFactory.createFontStyleItalicButton(editor, labels, disposables), "middle", null, p);
        addButton(ButtonFactory.createFontStyleUnderlineButton(editor, labels, disposables), "last", GridBagConstraints.WEST, p);
    }

    private void addButton(JButton btn, String segmentPosition, Integer anchor, JPanel p) {
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));
        btn.putClientProperty("Palette.Component.segmentPosition", segmentPosition);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 0, 0, 0);
        if (anchor != null) {
            gbc.anchor = anchor;
        }
        p.add(btn, gbc);
    }

    private void addTextFieldForFontName(int columnWidth, int gridWidth, ResourceBundleUtil labels, JPanel p) {
        JAttributeTextField<Font> faceField = createFontFaceField(columnWidth, labels);
        disposables.add(new FigureAttributeEditorHandler<>(FONT_FACE, faceField, editor));

        p.add(faceField, createConstraintsForFontField(gridWidth));
    }

    private static void addPanel2(JPanel p, JPanel p2) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        p.add(p2, gbc);
    }

    private void addControlElementsForFontSize(ResourceBundleUtil labels, JPanel p2) {
        addFontSizeField(labels, p2);

        JAttributeSlider sizeSlider = createSizeSlider();
        addPopupButtonForSizeSlider(sizeSlider, labels, p2);
    }

    private void addPopupButtonForSizeSlider(JAttributeSlider sizeSlider, ResourceBundleUtil labels, JPanel p2) {
        JPopupButton sizePopupButton = new JPopupButton();
        sizePopupButton.add(sizeSlider);
        labels.configureToolBarButton(sizePopupButton, "attribute.fontSize");
        sizePopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(sizePopupButton));
        sizePopupButton.setPopupAnchor(SOUTH_EAST);
        disposables.add(new SelectionComponentRepainter(editor, sizePopupButton));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(3, 0, 0, 0);
        p2.add(sizePopupButton, gbc);
    }

    private JAttributeSlider createSizeSlider() {
        JAttributeSlider sizeSlider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 12);
        sizeSlider.setUI((SliderUI) PaletteSliderUI.createUI(sizeSlider));
        sizeSlider.setScaleFactor(1d);
        disposables.add(new FigureAttributeEditorHandler<>(FONT_SIZE, sizeSlider, editor));
        return sizeSlider;
    }

    private void addFontSizeField(ResourceBundleUtil labels, JPanel p2) {
        JAttributeTextField<Double> sizeField = new JAttributeTextField<>();
        sizeField.setColumns(1);
        sizeField.setToolTipText(labels.getString("attribute.fontSize.toolTipText"));
        sizeField.setHorizontalAlignment(JAttributeTextField.RIGHT);
        sizeField.putClientProperty("Palette.Component.segmentPosition", "first");
        sizeField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(sizeField));
        sizeField.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(0d, 1000d, 1d));
        sizeField.setHorizontalAlignment(JTextField.LEADING);
        disposables.add(new FigureAttributeEditorHandler<Double>(FONT_SIZE, sizeField, editor));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.gridwidth = 2;
        gbc.weightx = 1f;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p2.add(sizeField, gbc);
    }

    private void addFontChooserButton(ResourceBundleUtil labels, JPanel p) {
        AbstractButton btn = ButtonFactory.createFontButton(editor, FONT_FACE, labels, disposables);
        btn.setUI((PaletteButtonUI) PaletteButtonUI.createUI(btn));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        p.add(btn, gbc);
    }

    private static GridBagConstraints createConstraintsForFontField(int gridWidth) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = gridWidth;
        return gbc;
    }

    private static JAttributeTextField<Font> createFontFaceField(int columnWidth, ResourceBundleUtil labels) {
        JAttributeTextField<Font> faceField = new JAttributeTextField<>();
        faceField.setToolTipText(labels.getString("attribute.font.toolTipText"));
        faceField.setHorizontalAlignment(JAttributeTextField.RIGHT);
        faceField.putClientProperty("Palette.Component.segmentPosition", "first");
        faceField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(faceField));
        faceField.setHorizontalAlignment(JTextField.LEADING);
        faceField.setFormatterFactory(FontFormatter.createFormatterFactory());
        faceField.setColumns(columnWidth);
        return faceField;
    }

    @Override
    @FeatureEntryPoint(value = "FontToolBar")
    protected String getID() {
        return "font";
    }

    @Override
    @FeatureEntryPoint(value = "FontToolBar")
    protected int getDefaultDisclosureState() {
        return 1;
    }


    @FeatureEntryPoint(value = "Shenanigans")
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
