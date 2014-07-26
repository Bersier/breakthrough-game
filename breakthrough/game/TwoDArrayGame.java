package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

import java.util.Arrays;

/**
 * Common code for Game implementations that represent the board as a 2D array.
 * <p/>
 * Created on 7/23/2014.
 */
abstract class TwoDArrayGame extends AbstractGame {

    final int size;
    final Color[][] board;

    /**
     * Unsafe! Does not make a copy of the array argument.
     *
     * @param board the array that will be used by this {@link Game} to represent the board
     */
    TwoDArrayGame(Color[][] board) {
        this.size = board.length;
        this.board = board;
    }

    TwoDArrayGame(Game state) {
        this.size = state.size();
        this.board = new Color[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = state.getColorAt(i, j);
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * @return the equivalent coordinate on the reverse board
     */
    int inverse(int i) {
        return size-1 - i;
    }

    static Color[][] mirror(Color[][] board) {
        final int size = board.length;
        final Color[][] copy = new Color[size][];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(board[size-1 - i], size);
        }
        return copy;
    }
}
