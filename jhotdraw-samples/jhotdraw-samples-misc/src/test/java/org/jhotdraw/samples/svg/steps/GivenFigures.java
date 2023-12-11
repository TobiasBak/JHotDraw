package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;


import java.util.ArrayList;

import java.util.List;

public class GivenFigures extends Stage<GivenFigures> {

    @ProvidedScenarioState
    List<Figure> inputFigures = new ArrayList<>();

    public GivenFigures a_figure() {
        inputFigures.add(new SVGPathFigure(true));
        return self();
    }

}
