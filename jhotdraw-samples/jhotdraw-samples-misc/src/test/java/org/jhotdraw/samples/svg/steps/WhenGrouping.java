package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;

import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import static org.assertj.core.api.Assertions.assertThat;
import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.samples.svg.action.commands.CombinePathsCommand;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.ArrayList;
import java.util.List;

public class WhenGrouping extends Stage<WhenGrouping> {
    @ScenarioState
    CombinePathsCommand combineCommand = new CombinePathsCommand();

    @ExpectedScenarioState
    List<Figure> inputFigures = new ArrayList<>();

    @ProvidedScenarioState
    DrawingView view = new DefaultDrawingView();

    public WhenGrouping the_figures_are_grouped() {
        Drawing drawing = view.getDrawing();
        CompositeFigure group = (CompositeFigure) new SVGPathFigure(true);

        combineCommand.execute(view, group, drawing.sort(view.getSelectedFigures()));

        return self();
    }

    public WhenGrouping the_figures_are_selected () {
        view.setDrawing(new DefaultDrawing());
        Drawing drawing = view.getDrawing();

        assertThat(inputFigures).isNotEmpty();
        drawing.addAll(inputFigures);

        view.selectAll();

        return self();
    }

}
