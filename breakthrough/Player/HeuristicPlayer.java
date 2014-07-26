package breakthrough.player;

import breakthrough.Max;
import breakthrough.ValueFunction;
import breakthrough.WhiteMove;
import breakthrough.game.GameState;
import breakthrough.heuristic.Heuristic;

import java.text.DecimalFormat;

/**
 * Simple heuristic player. Always plays a best move of its given heuristic.
 * <p>
 * Created on 7/20/2014.
 */
public class HeuristicPlayer implements Player {

    private final ValueFunction<GameState> heuristic;
    private double lastValue = 0;

    public HeuristicPlayer(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public WhiteMove play(GameState current) {
        final Max<WhiteMove> bestMove = Utils.bestMove(current, heuristic);
        lastValue = bestMove.value;
        return bestMove.argmax;
    }

    @Override
    public String talk() {
        final DecimalFormat f = new DecimalFormat("00");
        final double confidence = (lastValue + 1)/2;
        return "I feel " + f.format(confidence * 100) + "% confident about winning.";
    }
}
