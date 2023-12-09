package org.jhotdraw.samples.svg.action.commands;

import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.CompositeFigure;
import org.jhotdraw.draw.figure.Figure;
import java.util.List;

public interface PathCommand {
    void execute(DrawingView view, CompositeFigure group, List<Figure> figures);
}
