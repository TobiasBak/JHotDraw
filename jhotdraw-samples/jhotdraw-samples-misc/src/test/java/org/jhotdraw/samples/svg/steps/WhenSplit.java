package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.action.commands.SplitPathsCommand;

import java.util.LinkedList;

public class WhenSplit extends Stage<WhenSplit> {

    @ScenarioState
    SplitPathsCommand splitCommand = new SplitPathsCommand();

    @ExpectedScenarioState
    DrawingView view;


    public WhenSplit the_group_is_split() {
        final CompositeFigure group = (CompositeFigure) view.getSelectedFigures().iterator().next();
        final LinkedList<Figure> ungroupedPaths = new LinkedList<Figure>();

        splitCommand.execute(view, group, ungroupedPaths);

        return self();
    }

    public WhenSplit the_group_is_selected() {
        view.selectAll();

        return self();
    }
}
