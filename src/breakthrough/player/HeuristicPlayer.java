package breakthrough.player;

import breakthrough.Max;
import breakthrough.Move;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

import java.text.DecimalFormat;

import static commons.Utils.vToP;

/**
 * Simple heuristic player. Always plays a best move of its given {@link Heuristic}.
 * <p>
 * Created on 7/20/2014.
 */
public class HeuristicPlayer implements Player {

    final Heuristic heuristic;
    double expectedGain;

    public HeuristicPlayer(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public void gameStart(Game start, boolean iStart) {
        expectedGain = iStart ? 0 : heuristic.at(start);
    }

    @Override
    public void gameOver(boolean iWon) {
        expectedGain = iWon ? 1 : -1;
    }

    /**
     * @return one of the moves judged best with uniform probability
     */
    @Override
    public Move play(Game current) {
        final Max<Move> bestMove = Utils.bestMove(current, heuristic);
        expectedGain = bestMove.value;
        return bestMove.argmax;
    }

    @Override
    public String talk() {
        final DecimalFormat f = new DecimalFormat("00");
        final double confidence = vToP(expectedGain);
        return "I feel " + f.format(confidence * 100) + "% confident about winning.";
    }

    @Override
    public String toString() {
        return super.toString() + ", with heuristic:\n" + heuristic.toString();
    }
}
