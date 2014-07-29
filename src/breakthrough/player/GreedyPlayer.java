package breakthrough.player;

import static java.lang.Math.sin;
import static java.lang.Math.PI;

import breakthrough.Color;
import breakthrough.heuristic.WinningHeuristic;

/**
 * Greedy {@link Player}.
 * Gives first priority to winning moves, and second priority to moves with highest count advantage.
 * <p/>
 * Created on 7/23/2014.
 */
public class GreedyPlayer extends HeuristicPlayer {

    public GreedyPlayer() {
        super(new WinningHeuristic(
                state -> normalizeToUtility(state.count(Color.White), state.count(Color.Black))
        ));
    }

    /**
     * @param whiteCount number of white pawns on the board
     * @param blackCount number of black pawns on the board
     * @return a utility estimation
     */
    private static double normalizeToUtility(double whiteCount, double blackCount) {

        // the use of the sine is to get a smooth function with zero derivative at the end-points
        return sin(PI/2 * (blackCount - whiteCount)/(blackCount + whiteCount));
    }
}
