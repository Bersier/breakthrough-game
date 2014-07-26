package breakthrough.player;

import static java.lang.Math.sin;
import static java.lang.Math.PI;

import breakthrough.Color;
import breakthrough.ValueFunction;
import breakthrough.game.GameState;
import breakthrough.heuristic.Heuristic;
import breakthrough.heuristic.WinningHeuristic;

/**
 * Greedy Player.
 * Gives first priority to winning moves, and second priority to moves with highest count advantage.
 * <p/>
 * Created on 7/23/2014.
 */
public class GreedyPlayer extends HeuristicPlayer {//todo next: composition of partial value functions?

    public GreedyPlayer() {
        super(new WinningHeuristic(
                state -> normalizeToUtility(state.count(Color.White), state.count(Color.Black))
        ));
        /*super(
                state -> state.hasWinner() ?
                        1 : normalizeToUtility(state.count(Color.White), state.count(Color.Black))
        );*/
    }

    private static double normalizeToUtility(double whiteCount, double blackCount) {
        return sin(PI/2 * (whiteCount - blackCount)/(whiteCount + blackCount));
    }
}
