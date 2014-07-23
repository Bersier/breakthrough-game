package breakthrough.game;

import breakthrough.Color;
import breakthrough.Move;

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
    public GameState after(Move move) {
        final DefaultGameState result = new DefaultGameState(this.board);
        final Color[][] board = result.board;
        final Color pawnColor = board[move.i][move.j];
        board[move.i][move.j] = Color.none;
        board[move.newi(pawnColor)][move.newj(pawnColor)] = pawnColor;
        return result;
    }

    private static Color[][] copyBoard(Color[][] board) {
        final int size = board.length;
        final Color[][] copy = new Color[size][];
        for (int i = 0; i < size; i++) {
            final Color[] row = board[i];
            if (row.length != size) {
                throw new IllegalArgumentException("Passed 2D array is not a legal board!");
            }
            copy[i] = Arrays.copyOf(row, size);
        }
        return copy;
    }
}
