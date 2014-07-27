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
    private boolean colorsInverted = false;

    /**
     * Unsafe! Does not make a copy of the array argument.
     *
     * @param board the array that will be used by this {@link Game} to represent the board
     */
    InversionGame(Color[][] board) {
        super(board);
    }

    InversionGame(Game state) {
        super(state);
    }

    @Override
    public Color at(int i, int j) {
        final Color color = board[get(i)][get(j)];
        return colorsInverted ? color.dual() : color;
    }

    private void removePawnAt(int i, int j) {
        board[get(i)][get(j)] = Color.None;
    }

    private void putPawnAt(int i, int j) {
        board[get(i)][get(j)] = colorsInverted ? Color.Black : Color.White;
    }

    private int get(int i) {
        return colorsInverted ? inverse(i) : i;
    }

    @Override
    public Game after(WhiteMove move) {
        final InversionGame nextState = new InversionGame(Utils.copy(board));
        nextState.colorsInverted = colorsInverted;
        nextState.removePawnAt(move.i   , move.j   );
        nextState.putPawnAt   (move.newi, move.newj);
        nextState.colorsInverted = !colorsInverted;
        return nextState;
    }

    @Override
    public Game mirror() {
        return new InversionGame(mirror(board));
    }
}
