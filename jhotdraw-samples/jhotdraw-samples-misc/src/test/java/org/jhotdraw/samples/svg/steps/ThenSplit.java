package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;

public class ThenSplit extends Stage<ThenSplit> {

    @ExpectedScenarioState
    DrawingView view;

    public ThenSplit the_group_is_split() {
        Drawing drawing = view.getDrawing();

        System.out.println("Drawing: " + drawing);
        System.out.println("Drawing children: " + drawing.getChildren());

        assert view.getSelectedFigures().size() == 3;
        return self();
    }
}
