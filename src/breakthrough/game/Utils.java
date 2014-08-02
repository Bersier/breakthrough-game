package breakthrough.game;

import breakthrough.Color;

import java.util.Arrays;

/**
 * Utilities for classes in the game package.
 *
 * Created on 7/20/2014.
 */
interface Utils {

    static Color[][] copy(Color[][] board) {
        final int size = board.length;
        final Color[][] copy = new Color[size][];
        for (int i = 0; i < size; i++) {
            copy[i] = Arrays.copyOf(board[i], size);
        }
        return copy;
    }

    static Color[][] reverse(Color[][] board) {
        final int size = board.length;
        final Color[][] reverse = new Color[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                reverse[dual(size, i)][dual(size, j)] = board[i][j].dual();
            }
        }
        return reverse;
    }

    static Color[][] getBoard(Game game) {
        return new DefaultGame(game).board;
    }

    /**
     * @return the reversed coordinate
     */
    static int dual(int size, int i) {
        return size-1 - i;
    }
}
