package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.action.commands.CombinePathsCommand;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.ArrayList;
import java.util.List;

public class GivenAGroup extends Stage<GivenAGroup> {

    @ScenarioState
    CombinePathsCommand combineCommand = new CombinePathsCommand();

    @ProvidedScenarioState
    DrawingView view = new DefaultDrawingView();

    public GivenAGroup a_group() {
        view.setDrawing(new DefaultDrawing());
        Drawing drawing = view.getDrawing();

        List<Figure> inputFigures = new ArrayList<>();
        inputFigures.add(new SVGPathFigure(true));
        inputFigures.add(new SVGPathFigure(true));
        inputFigures.add(new SVGPathFigure(true));

        drawing.addAll(inputFigures);

        view.selectAll();

        CompositeFigure group = (CompositeFigure) new SVGPathFigure(true);

        combineCommand.execute(view, group, drawing.sort(view.getSelectedFigures()));

        System.out.println("Drawingview group: " + view.getSelectedFigures());

        return self();
    }

}
