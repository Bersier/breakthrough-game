package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

/**
 * Similar to {@link DefaultGame}, but handles game state inversions lazily.
 * <p>
 * Created on 7/23/2014.
 */
class InversionGame extends TwoDArrayGame {

    /**
     * whether black and white are inverted
     */
    private boolean invertedColors = false;

    InversionGame(Color[][] board) {
        super(board);
    }

    InversionGame(Game state) {
        super(state);
    }

    @Override
    public Color getColorAt(int i, int j) {
        final Color color = board[get(i)][get(j)];
        return invertedColors ? color.dual() : color;
    }

    private void removePawnAt(int i, int j) {
        board[get(i)][get(j)] = Color.None;
    }

    private void putPawnAt(int i, int j) {
        board[get(i)][get(j)] = invertedColors ? Color.Black : Color.White;
    }

    private int get(int i) {
        return invertedColors ? inverse(i) : i;
    }

    @Override
    public Game after(WhiteMove move) {
        final InversionGame nextState = new InversionGame(board);
        nextState.invertedColors =   invertedColors;
        nextState.removePawnAt(move.i   , move.j   );
        nextState.putPawnAt   (move.newi, move.newj);
        nextState.invertedColors = ! invertedColors;
        return nextState;
    }
}