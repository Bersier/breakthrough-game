package breakthrough.player;

import breakthrough.Max;
import breakthrough.ValueFunction;
import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

import java.text.DecimalFormat;

/**
 * Simple heuristic player. Always plays a best move of its given {@link Heuristic}.
 * <p>
 * Created on 7/20/2014.
 */
public class HeuristicPlayer implements Player {

    private final ValueFunction<Game> heuristic;
    private double lastValue = 0;

    public HeuristicPlayer(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * @return one of the moves judged best with uniform probability
     */
    @Override
    public WhiteMove play(Game current) {
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
