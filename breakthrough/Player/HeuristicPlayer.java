package breakthrough.player;

import breakthrough.Max;
import breakthrough.Move;
import breakthrough.ValueFunction;
import breakthrough.WhiteMove;
import breakthrough.game.GameState;

import java.text.DecimalFormat;

/**
 * Simple heuristic player. Always plays a best move of its given heuristic.
 * <p>
 * Created on 7/20/2014.
 */
public class HeuristicPlayer implements Player {

    private final ValueFunction<GameState> heuristic;
    private Double confidence = .5;

    public HeuristicPlayer(ValueFunction<GameState> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public WhiteMove play(GameState current) {
        final Max<WhiteMove> bestMove = Utils.bestMove(current, heuristic);
        confidence = bestMove.value;
        return bestMove.argmax;
    }

    @Override
    public String talk() {
        final DecimalFormat f = new DecimalFormat("00");
        return "I feel " + f.format(confidence * 100) + "% confident about winning.";
    }
}
