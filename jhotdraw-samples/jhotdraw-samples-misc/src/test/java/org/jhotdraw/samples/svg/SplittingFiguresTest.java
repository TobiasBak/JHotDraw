package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.jhotdraw.samples.svg.steps.GivenAGroup;
import org.jhotdraw.samples.svg.steps.ThenSplit;
import org.jhotdraw.samples.svg.steps.WhenSplit;
import org.junit.Test;

public class SplittingFiguresTest extends ScenarioTest<GivenAGroup, WhenSplit, ThenSplit> {

    @Test
    public void testSplitting() {
        given().a_group();

        when().the_group_is_selected().and().the_group_is_split();

        then().the_group_is_split();
    }
}
