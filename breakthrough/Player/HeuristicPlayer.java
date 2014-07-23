package breakthrough.player;

import breakthrough.Max;
import breakthrough.Move;
import breakthrough.ValueFunction;
import breakthrough.game.GameState;

import java.text.DecimalFormat;

/**
 * Simple heuristic player. Always plays a best move of its given heuristic.
 * <p>
 * Created on 7/20/2014.
 */
public class HeuristicPlayer extends AbstractPlayer {

    private final ValueFunction<GameState> heuristic;
    private Double confidence = .5;

    public HeuristicPlayer(ValueFunction<GameState> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Move play(GameState current) {
        final Max<Move> bestMove = Utils.bestMove(current, myColor(), heuristic);
        confidence = bestMove.value;
        return bestMove.argmax;
    }

    @Override
    public String talk() {
        final DecimalFormat f = new DecimalFormat("00");
        return "I feel " + f.format(confidence * 100) + "% confident about winning.";
    }
}
