package breakthrough.player;

import breakthrough.*;
import breakthrough.game.Game;

import java.util.List;

/**
 * Utility class for players.
 * <p>
 * Provides generally useful methods for players.
 * <p>
 * Created on 7/20/2014.
 */
public class Utils {

    /**
     * Plays randomly.
     * Chooses a legal move uniformly at random.
     */
    public static final Player RandomPlayer = new HeuristicPlayer(board -> 0);

    /**
     * @return a best move for white, according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
    public static Max<WhiteMove> bestMove(Game state, ValueFunction<Game> value) {
        final List<WhiteMove> moves = state.legalMoves();
        WhiteMove best = moves.get(0);
        double bestValue = value.at(state.after(best));
        for (WhiteMove move : moves) {
            final double moveValue = value.at(state.after(move));
            if (moveValue > bestValue) {
                best = move;
                bestValue = moveValue;
            }
        }
        return new Max<WhiteMove>(best, bestValue);
    }
}
