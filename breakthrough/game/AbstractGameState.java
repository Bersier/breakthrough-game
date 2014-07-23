package breakthrough.game;

import breakthrough.Color;
import breakthrough.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Provides default implementations where possible.
 * <p>
 * Created on 7/20/2014.
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
    public List<Move> legalMoves(Color color) {
        final int size = size();
        final List<Move> list = new ArrayList<Move>(size * size / 2);
        for (int i = 0; i < size; i++) {
            for (int j = color.inc; j < size - 1 + color.inc; j++) {
                for (int dir = -1; dir <= 1; dir++) {
                    final Move move = new Move(i, j, dir);
                    if (isLegal(move, color)) {
                        list.add(move);
                    }
                }
            }
        }
        Collections.shuffle(list);
        return list;
    }

    @Override
    public List<GameState> futures(Color color) {
        final List<Move> moves = legalMoves(color);
        final List<GameState> states = new ArrayList<GameState>(moves.size());
        for (Move move : moves) {
            states.add(after(move));
        }
        return states;
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

    /**
     * @return the column which makes the player of the given color win if they reach it
     */
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

    @Override
    public boolean equals(Object other) {
        final GameState otherState = (GameState) other;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (otherState.getColorAt(i, j) != getColorAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        // todo
        return 0;
    }

    @Override
    public String toString() {
        return new ASCIIBoardRepresentation(this).toString();
    }

    private static class ASCIIBoardRepresentation {

        private final StringBuilder builder = new StringBuilder();
        private final String string;

        ASCIIBoardRepresentation(GameState state) {
            final int size = state.size();

            appendLn("\n");
            leftSpace(); horizontalBorder(size);
            for (int i = 0; i < size; i++) {
                leftSpace();
                for (int j = 0; j < size; j++) {
                    append("|");
                    pawn(state.getColorAt(i, j));
                }
                appendLn("|");
                leftSpace(); horizontalBorder(size);
            }
            appendLn("\n");

            this.string = builder.toString();
        }

        @Override
        public String toString() {
            return string;
        }

        private void leftSpace() {
            append("\t");
        }

        private void horizontalBorder(int size) {
            for(int i=0; i<size; i++) {
                append("----");
            }
            appendLn("-");
        }

        private void pawn(Color color) {
            switch (color) {
                case white:
                    append(" O ");
                    break;
                case black:
                    append(" X ");
                    break;
                case none:
                    append("   ");
                    break;
            }
        }

        private void append(String string) {
            builder.append(string);
        }

        private void appendLn(String string) {
            builder.append(string).append("\n");
        }
    }
}
