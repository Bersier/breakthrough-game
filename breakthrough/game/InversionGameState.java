package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

import java.util.Arrays;

/**
 * Similar to DefaultGameState, but handles game state inversions lazily.
 * <p>
 * Created on 7/23/2014.
 */
class InversionGameState extends AbstractGameState {

    private final int size;
    private final Color[][] board;

    /**
     * whether black and white are inverted
     */
    private boolean invertedColors = false;

    InversionGameState(Color[][] board) {
        this.size = board.length;
        this.board = DefaultGameState.copyBoard(board);
    }

    InversionGameState(GameState state) {
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
        final InversionGameState nextState = new InversionGameState(board);
        nextState.invertedColors =   invertedColors;
        nextState.removePawnAt(move.i   , move.j   );
        nextState.putPawnAt   (move.newi, move.newj);
        nextState.invertedColors = ! invertedColors;
        return nextState;
    }
}
