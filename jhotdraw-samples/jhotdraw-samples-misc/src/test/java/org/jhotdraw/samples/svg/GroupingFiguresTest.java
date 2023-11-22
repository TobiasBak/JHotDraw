package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.steps.GivenFigures;
import org.jhotdraw.samples.svg.steps.ThenGroup;
import org.jhotdraw.samples.svg.steps.WhenGrouping;
import org.junit.Test;

public class GroupingFiguresTest extends ScenarioTest<GivenFigures, WhenGrouping, ThenGroup> {

    @Test
    public void testGrouping() {
        given().an_ellipse().and().an_rectangle();

        when().the_figures_are_selected().and().the_figures_are_grouped();

        then().some_outcome();
    }
}
