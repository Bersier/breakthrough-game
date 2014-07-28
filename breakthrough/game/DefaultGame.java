package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

/**
 * The default implementation for {@link Game}. Not optimized, but big-Oh optimal. Simple. Safe.
 * <p>
 * Created on 7/23/2014.
 */
class DefaultGame extends TwoDArrayGame {

    /**
     * Unsafe! Does not make a copy of the array argument.
     *
     * @param board the array that will be used by this {@link Game} to represent the board
     */
    DefaultGame(Color[][] board) {
        super(board);
    }

    DefaultGame(Game state) {
        super(state);
    }

    @Override
    public Color at(int i, int j) {
        return board[i][j];
    }

    @Override
    public Game after(WhiteMove move) {

        // reverse the board
        final Color[][] board = Utils.reverse(this.board);

        // apply the reverse of the move on the reversed board, which is equivalent to the original move
        board[dual(move.i)]   [dual(move.j)]    = Color.None;
        board[dual(move.newi)][dual(move.newj)] = Color.Black;

        // wrap the board
        return new DefaultGame(board);
    }

    @Override
    public Game mirror() {
        return new DefaultGame(mirror(board));
    }
}
