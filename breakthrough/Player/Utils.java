package breakthrough.player;

import breakthrough.*;
import breakthrough.game.Game;

import java.util.List;
import java.util.Random;

/**
 * Utility class for players.
 * <p>
 * Provides generally useful methods for players.
 * <p>
 * Created on 7/20/2014.
 */
public class Utils {

    final static Random random = new Random();

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

    private static Max<WhiteMove> bestMove(Game state, List<WhiteMove> moves, ValueFunction<Game> value) {//todo
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

    public static Max<WhiteMove> usuallyBestMove(Game state, ValueFunction<Game> value) {
        final List<WhiteMove> moves = state.legalMoves();
        Max<WhiteMove> best = bestMove(state, moves, value);

        if(random.nextDouble() < (1 - best.value)/16) {
            final WhiteMove randomMove = moves.get(random.nextInt(moves.size()));
            best = new Max<WhiteMove>(randomMove, best.value);//todo broken (value is not the true value)
        }

        return best;
    }
}
