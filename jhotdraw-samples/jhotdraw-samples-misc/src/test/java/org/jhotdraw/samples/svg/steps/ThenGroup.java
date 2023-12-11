package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.figure.CompositeFigure;

public class ThenGroup extends Stage<ThenGroup> {

    @ExpectedScenarioState
    DrawingView view;

    public ThenGroup a_group_is_created() {

        CompositeFigure group = (CompositeFigure) view.getDrawing().getChildren().iterator().next();

        System.out.println("Group: " + group);
        System.out.println("Group children figures: " + group.getChildren());
        System.out.println("View drawing: " + view.getDrawing());
        System.out.println("View drawing children: " + view.getDrawing().getChildren());
        System.out.println("Child count: " + group.getChildCount());

        assert group.includes(view.getSelectedFigures().iterator().next());

        return self();
    }

}
