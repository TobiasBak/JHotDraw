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

        System.out.println("Group: " + group);
        System.out.println("Group children figures: " + group.getChildren());
        System.out.println("View drawing: " + view.getDrawing());
        System.out.println("View drawing children: " + view.getDrawing().getChildren());

        splitCommand.execute(view, group, ungroupedPaths);
        System.out.println("View drawing after split: " + view.getDrawing());
        System.out.println("View drawing children after split: " + view.getDrawing().getChildren());

        return self();
    }

    public WhenSplit the_group_is_selected() {
        view.selectAll();

        System.out.println("View selected figures: " + view.getSelectedFigures());

        return self();
    }


}
