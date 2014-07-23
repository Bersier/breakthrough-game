package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

import java.util.Arrays;

/**
 * <p/>
 * Created on 7/23/2014.
 */
public abstract class AbstractDefaultGameState extends AbstractGameState {

    private final int size;
    private final Color[][] board;

    AbstractDefaultGameState(Color[][] board) {
        this.size = board.length;
        this.board = copyBoard(board);
    }

    AbstractDefaultGameState(GameState state) {
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
