package org.jhotdraw.samples.svg.gui.fontSteps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.TextAreaFigure;

import javax.swing.*;

import static org.jhotdraw.samples.svg.gui.FontBarTest.getAttributeTextField;

public class WhenChanging extends Stage<WhenChanging> {

    @ExpectedScenarioState
    JPanel panel;

    @ExpectedScenarioState
    TextAreaFigure textAreaFigure;

    @ProvidedScenarioState
    String fontName;

    public WhenChanging a_new_font_is_set_in_the_field() {
        fontName = textAreaFigure.getFont().getFamily();
        if (fontName.equals("Arial")) {
            fontName = "Times New Roman";
        } else {
            fontName = "Arial";
        }

        JTextField fontNameTextField =  getAttributeTextField(panel, "font");

        fontNameTextField.setText(fontName);

        return self();
    }

    public WhenChanging an_invalid_font_is_set_in_the_input_field() {
        fontName = textAreaFigure.getFont().getFamily();


        JTextField fontNameTextField =  getAttributeTextField(panel, "font");

        fontNameTextField.setText("invalid font name");

        return self();
    }

    public WhenChanging the_existing_font_name_is_written_in_the_field() {
        fontName = textAreaFigure.getFont().getFamily();


        JTextField fontNameTextField =  getAttributeTextField(panel, "font");

        fontNameTextField.setText(fontName);

        return self();
    }
}
