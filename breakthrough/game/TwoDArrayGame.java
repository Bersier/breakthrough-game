package breakthrough.game;

import breakthrough.Color;

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
                this.board[i][j] = state.at(i, j);
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
    int dual(int i) {
        return Utils.dual(size, i);
    }

    static Color[][] mirror(Color[][] board) {
        final int size = board.length;
        final Color[][] copy = new Color[size][];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(board[Utils.dual(size, i)], size);
        }
        return copy;
    }
}
