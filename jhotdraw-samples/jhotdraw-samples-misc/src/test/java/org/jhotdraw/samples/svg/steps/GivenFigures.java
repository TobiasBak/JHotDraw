package org.jhotdraw.samples.svg.steps;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGEllipseFigure;
import org.jhotdraw.samples.svg.figures.SVGRectFigure;


import java.util.ArrayList;

import java.util.List;

public class GivenFigures extends Stage<GivenFigures> {

    @ProvidedScenarioState
    List<Figure> inputFigures = new ArrayList<>();

    public GivenFigures an_ellipse() {
        inputFigures.add(new SVGEllipseFigure(0, 0, 50, 50));
        System.out.println("Input figures at ellipse " + inputFigures);
        return self();
    }

    public GivenFigures an_rectangle() {
        inputFigures.add(new SVGRectFigure(0, 0, 50, 50));
        System.out.println("Input figures at rec " + inputFigures);
        return self();
    }

}
