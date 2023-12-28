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
    public void setEditor(DrawingEditor newEditor) {
        if (displayer != null) {
            displayer.dispose();
            displayer = null;
        }
        super.setEditor(newEditor);

        if (newEditor == null) {
            return;
        }

        displayer = new FontBarSelectionComponentDisplayer(editor, this);
    }

    @Override
    @FeatureEntryPoint(value = "FontToolBar")
    protected JComponent createDisclosedComponent(int state) {
        JPanel basePanel = createBasePanel();

        boolean noEditorSet = editor == null;
        if (noEditorSet) {
            return basePanel;
        }

        int textFieldColumnWidth;
        int textFieldGridWidth;

        ExpansionState expansionState = ExpansionState.getExpansionState(state);
        switch (expansionState) {
            case HALF:
                textFieldGridWidth = 2;
                textFieldColumnWidth = 2;
                break;
            case FULL:
                textFieldGridWidth = 3;
                textFieldColumnWidth = 12;
                break;
            default:
                return null;
        }

        addFontChooserElements(textFieldColumnWidth, textFieldGridWidth, basePanel);

        return basePanel;
    }

    private static JPanel createBasePanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 8));
        p.setLayout(new GridBagLayout());
        return p;
    }

    private void addFontChooserElements(int textFieldColumnWidth, int textFieldGridWidth, JPanel p) {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");

        addTextFieldForFontName(textFieldColumnWidth, textFieldGridWidth, labels, p);

        addFontChooserButton(labels, p);

        // Font size field with slider
        JPanel p2 = new JPanel(new GridBagLayout());
        p2.setOpaque(false);

        addControlElementsForFontSize(labels, p2);
        addPanel2(p, p2);

        // Font style buttons
        JButton boldButton = ButtonFactory.createFontStyleBoldButton(editor, labels, disposables);
        addButton(boldButton, "first", null, p);

        JButton italicButton = ButtonFactory.createFontStyleItalicButton(editor, labels, disposables);
        addButton(italicButton, "middle", null, p);

        JButton underlineButton = ButtonFactory.createFontStyleUnderlineButton(editor, labels, disposables);
        addButton(underlineButton, "last", GridBagConstraints.WEST, p);
    }

    private void addControlElementsForFontSize(ResourceBundleUtil labels, JPanel p2) {
        addFontSizeField(labels, p2);

        JAttributeSlider sizeSlider = createSizeSlider();
        addPopupButtonForSizeSlider(sizeSlider, labels, p2);
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

        p.add(faceField, createGridBagConstraint(gridWidth, 0, 0, null, null, null));
    }

    private static void addPanel2(JPanel p, JPanel p2) {
        GridBagConstraints gbc = createGridBagConstraint(2, 0, 1, null, null, GridBagConstraints.BOTH);
        p.add(p2, gbc);
    }

    private void addPopupButtonForSizeSlider(JAttributeSlider sizeSlider, ResourceBundleUtil labels, JPanel p2) {
        JPopupButton sizePopupButton = new JPopupButton();
        sizePopupButton.add(sizeSlider);
        labels.configureToolBarButton(sizePopupButton, "attribute.fontSize");
        sizePopupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(sizePopupButton));
        sizePopupButton.setPopupAnchor(SOUTH_EAST);
        disposables.add(new SelectionComponentRepainter(editor, sizePopupButton));
        GridBagConstraints gbc = createGridBagConstraint(1, 2, 1, 3, null, GridBagConstraints.NONE);
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
        disposables.add(new FigureAttributeEditorHandler<>(FONT_SIZE, sizeField, editor));

        GridBagConstraints gbc = createGridBagConstraint(2, 0, 1, 3, null, GridBagConstraints.HORIZONTAL);
        gbc.weightx = 1f;
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

    private static GridBagConstraints createGridBagConstraint(int gridWidth, int gridX, int gridY, Integer topInset, Integer anchor, Integer fill) {
        if (topInset == null) {
            topInset = 0;
        }

        if (anchor == null) {
            anchor = GridBagConstraints.FIRST_LINE_START;
        }

        if (fill == null) {
            fill = GridBagConstraints.HORIZONTAL;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.insets = new Insets(topInset, 0, 0, 0);
        gbc.anchor = anchor;
        gbc.fill = fill;
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
