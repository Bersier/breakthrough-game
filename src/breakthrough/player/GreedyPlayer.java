package breakthrough.player;

import static breakthrough.player.Utils.normalizeToUtility;
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
}
