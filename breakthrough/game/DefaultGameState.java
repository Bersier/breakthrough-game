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

    /**
     * whether black and white are inverted
     */
    private boolean invertedColors = false;

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
        final Color color = board[get(i)][get(j)];
        return invertedColors ? color.opposite() : color;
    }

    private void removePawnAt(int i, int j) {
        board[get(i)][get(j)] = Color.None;
    }
    private void putPawnAt(int i, int j) {
        board[get(i)][get(j)] = invertedColors ? Color.Black : Color.White;
    }

    private int get(int i) {
        return invertedColors ? size-1 - i : i;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public GameState after(WhiteMove move) {
        final DefaultGameState nextState = new DefaultGameState(board);
        nextState.invertedColors =   invertedColors;
        nextState.removePawnAt(move.i   , move.j   );
        nextState.putPawnAt   (move.newi, move.newj);
        nextState.invertedColors = ! invertedColors;
        return nextState;
    }

    private static Color[][] copyBoard(Color[][] board) {
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
