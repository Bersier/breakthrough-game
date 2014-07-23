package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

import java.util.Arrays;

/**
 * The default implementation for GameState. Not optimized, but big-Oh optimal. Simple. Safe.
 * <p>
 * Created on 7/23/2014.
 */
class DefaultGameState extends AbstractGameState {

    private final int size;
    private final Color[][] board;

    DefaultGameState(Color[][] board) {
        this.size = board.length;
        this.board = copyBoard(board);
    }

    DefaultGameState(GameState state) {
        this.size = state.size();
        this.board = new Color[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = state.getColorAt(i, j);
            }
        }
    }

    @Override
    public Color getColorAt(int i, int j) {
        return board[i][j];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public GameState after(WhiteMove move) {

        // reverse the board
        final Color[][] board = reverse(this.board);

        // apply the reverse of the move on the reversed board, which is equivalent to the original move
        board[size-1 - move.i]   [size-1 - move.j]    = Color.None;
        board[size-1 - move.newi][size-1 - move.newj] = Color.Black;

        // wrap the board
        return new InversionGameState(board);
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

    static Color[][] copyBoard(Color[][] board) {
        final int size = board.length;
        final Color[][] copy = new Color[size][];
        for (int i = 0; i < size; i++) {
            final Color[] row = board[i];
            Game.checkSizesAreEqual(row.length, size);
            copy[i] = Arrays.copyOf(row, size);
        }
        return copy;
    }
}
