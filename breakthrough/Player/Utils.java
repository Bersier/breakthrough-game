package breakthrough.player;

import breakthrough.*;
import breakthrough.game.GameState;

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
     * @return a best move for white, according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
    public static Max<WhiteMove> bestMove(GameState state, ValueFunction<GameState> value) {
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
