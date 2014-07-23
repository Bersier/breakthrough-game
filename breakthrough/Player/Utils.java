package breakthrough.player;

import breakthrough.Color;
import breakthrough.Max;
import breakthrough.Move;
import breakthrough.ValueFunction;
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
     * @return a best move of the given color according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
    public static Max<Move> bestMove(GameState state, Color color, ValueFunction<GameState> value) {
        final List<Move> moves = state.legalMoves(color);
        Move best = moves.get(0);
        double bestValue = value.at(state.after(best));
        for (Move move : moves) {
            final double moveValue = value.at(state.after(move));
            if (moveValue > bestValue) {
                best = move;
                bestValue = moveValue;
            }
        }
        return new Max<Move>(best, bestValue);
    }

}
