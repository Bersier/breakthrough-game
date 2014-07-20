package breakthrough;

/**
 * Created on 7/20/2014.
 *
 * @author Stephane Bersier
 */
abstract class AbstractGameState implements GameState {

    @Override
    public boolean isLegal(Move move, Color pawnColor) {

        // check that the initial position of the move is in bounds
        if (!(move.i >= 0 && move.i < size() && move.j >= 0 && move.j < size())) {
            throw new RuntimeException("Initial position out of bounds!");
        }

        // make sure there is a pawn of the right color at the initial position
        if (getColorAt(move.i, move.j) != pawnColor) {
            return false;
        }

        // get the destination position
        final int newj = move.newj(pawnColor);
        final int newi = move.newi(pawnColor);

        // if it is out of bounds column-wise, the initial position was on the win column
        if (newj < 0 || newj >= size()) {
            throw new RuntimeException("Initial position is on win column!");
        }

        // if it is out of bounds row-wise...
        if (newi < 0 || newi >= size()) {
            return false;
        }

        // if the destination position is occupied by a pawn of same color...
        if (getColorAt(newi, newj) == pawnColor) {
            return false;
        }

        // if the move is straight and the destination position is occupied...
        if (move.isStraight() && getColorAt(newi, newj) != Color.none) {
            return false;
        }

        // if none of the previous conditions matches, then the move is legal
        return true;
    }

    @Override
    public Color getWinner() {
        return isWinner(Color.white) ? Color.white :
               isWinner(Color.black) ? Color.black : Color.none;
    }

    @Override
    public boolean isWinner(Color color) {

        // check whether there is a pawn of the given color on its winning row
        final int j = winningColumn(color);
        for (int i = 0; i < size(); i++) {
            if (getColorAt(i, j) == color) {
                return true;
            }
        }

        // check whether there are no more pawns of opposite color
        if (count(color.opposite()) == 0) {
            return true;
        }

        return false;
    }

    protected int winningColumn(Color color) {
        return (color == Color.white) ? size()-1 : 0;
    }

    @Override
    public int count(Color color) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                count += (getColorAt(i, j) == color) ? 1 : 0;
            }
        }
        return count;
    }
}
