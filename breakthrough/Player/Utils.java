package breakthrough.player;

import breakthrough.*;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

import java.util.ArrayList;
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
    public static Max<WhiteMove> bestMove(Game state, Heuristic value) {
        return bestMove(state, state.legalMoves(), value);
    }

    /**
     * @param state on which the moves are played
     * @param utility used determine which moves are best, by evaluating the resulting states
     * @return the first best move among the given moves
     */
    public static Max<WhiteMove> bestMove(Game state, List<WhiteMove> moves, Heuristic utility) {
        WhiteMove best = moves.get(0);
        double bestValue = utility.at(state.after(best));
        for (WhiteMove move : moves) {
            final double moveValue = utility.at(state.after(move));
            if (moveValue > bestValue) {
                best = move;
                bestValue = moveValue;
            }
        }
        return new Max<WhiteMove>(best, bestValue);
    }

    public static Max<WhiteMove> usuallyBestMove(Game state, Heuristic value) {
        return usuallyBestMove(state, state.legalMoves(), value);
    }

    public static Max<WhiteMove> usuallyBestMove(Game state, List<WhiteMove> moves, Heuristic value) {
        Max<WhiteMove> best = bestMove(state, moves, value);

        if(random.nextDouble() < (1 - best.value)/16) {
            final WhiteMove randomMove = moves.get(random.nextInt(moves.size()));
            best = new Max<WhiteMove>(randomMove, best.value);//todo broken (value is not the true value, leading to wrong talk, but is necessary for learning)
        }

        return best;
    }

    /**
     * @return the last 'width' columns of the board, as a list, ordered lexicographically by (-j,i)
     */
    public static List<Color> getList(Game board, int width) {
        final int size = board.size();
        final List<Color> list = new ArrayList<Color>(size*width);
        for(int j = size-1; j >= Math.max(0, size-width); j--) {
            for(int i = 0; i < size; i++) {
                list.add(board.at(i, j));
            }
        }
        return list;
    }
}
