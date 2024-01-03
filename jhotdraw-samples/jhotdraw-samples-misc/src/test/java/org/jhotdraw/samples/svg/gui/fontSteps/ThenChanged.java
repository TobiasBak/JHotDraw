package org.jhotdraw.samples.svg.gui.fontSteps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.figure.TextAreaFigure;

public class ThenChanged extends Stage<ThenChanged> {
    @ExpectedScenarioState
    String fontName;

    @ExpectedScenarioState
    TextAreaFigure textAreaFigure;

    public ThenChanged the_font_is_changed_in_the_drawing() {
        System.out.println("Font name: " + textAreaFigure.getFont().getFamily());
        System.out.println("Expected font name: " + fontName);

        assert textAreaFigure.getFont().getFamily().equals(fontName);

        return self();
    }
}
