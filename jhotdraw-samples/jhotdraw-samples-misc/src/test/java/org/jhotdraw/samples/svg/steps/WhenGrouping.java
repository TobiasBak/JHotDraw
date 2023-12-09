package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.annotation.ProvidedScenarioState;


import com.tngtech.jgiven.annotation.ScenarioState;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;



import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.samples.svg.action.CombineAction;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.List;

public class WhenGrouping extends Stage<WhenGrouping> {
    @ScenarioState
    CombineAction combineAction = new CombineAction(new DefaultDrawingEditor());

    @ExpectedScenarioState
    List<Figure> inputFigures;

    @ProvidedScenarioState
    List<Figure> selectedFigures;

    @ProvidedScenarioState
    CompositeFigure group;

    public WhenGrouping the_figures_are_grouped() {
        System.out.println("Selected figures " + selectedFigures);
        assertThat(selectedFigures).isNotEmpty();
        group = (CompositeFigure) new SVGPathFigure(true);
        System.out.println("Grouping " + group);
//        combineAction.addingFiguresToGroup(group, selectedFigures);

        return self();
    }

    public WhenGrouping the_figures_are_selected () {
        assertThat(inputFigures).isNotEmpty();
        selectedFigures = inputFigures;

        return self();
    }




}
