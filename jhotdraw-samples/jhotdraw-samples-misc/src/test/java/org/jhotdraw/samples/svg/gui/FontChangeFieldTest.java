package org.jhotdraw.samples.svg.gui;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.gui.fontSteps.GivenDrawing;
import org.jhotdraw.samples.svg.gui.fontSteps.ThenChanged;
import org.jhotdraw.samples.svg.gui.fontSteps.WhenChanging;
import org.junit.Test;

public class FontChangeFieldTest extends ScenarioTest<GivenDrawing, WhenChanging, ThenChanged> {

    @Test
    public void testGrouping() {
        given().a_drawing();
        when().a_new_font_is_set_in_the_field();
        then().the_font_is_changed_in_the_drawing();
    }
}
