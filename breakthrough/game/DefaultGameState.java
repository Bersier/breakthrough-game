package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

/**
 * The default implementation for GameState. Not optimized, but big-Oh optimal. Simple. Safe.
 * <p>
 * Created on 7/23/2014.
 */
class DefaultGameState extends AbstractBigState {

    DefaultGameState(Color[][] board) {
        super(board);
    }

    DefaultGameState(GameState state) {
        super(state);
    }

    @Override
    public Color getColorAt(int i, int j) {
        return board[i][j];
    }

    @Override
    public GameState after(WhiteMove move) {

        // reverse the board
        final Color[][] board = reverse(this.board);

        // apply the reverse of the move on the reversed board, which is equivalent to the original move
        board[inverse(move.i)]   [inverse(move.j)]    = Color.None;
        board[inverse(move.newi)][inverse(move.newj)] = Color.Black;

        // wrap the board
        return new DefaultGameState(board);
    }

    private static Color[][] reverse(Color[][] board) {
        final int size = board.length;
        final Color[][] reverse = new Color[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                reverse[size-1 - i][size-1 - j] = board[i][j].opposite();
            }
        }
        return reverse;
    }
}
