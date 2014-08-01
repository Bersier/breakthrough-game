package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;
import breakthrough.WhiteMove.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static breakthrough.game.Utils.*;

/**
 * Provides default implementations where possible.
 * <p>
 * Created on 7/20/2014.
 */
abstract class AbstractGame implements Game {

    @Override
    public boolean isLegal(WhiteMove move) {

        // check that the initial position of the move is in bounds
        if (! (  move.i >= 0   &&   move.i < size()   &&   move.j >= 0   &&   move.j < size()  )) {
            throw new ObviouslyIllegalException("Initial position out of bounds!");
        }

        // get the destination position
        final int newj = move.newj;
        final int newi = move.newi;

        // if it is out of bounds column-wise, the initial position was on the win column
        if (newj >= size()) {
            throw new ObviouslyIllegalException("Initial position is on win column!");
        }

        // make sure there is a pawn of the right color at the initial position
        if (at(move.i, move.j) != Color.White) {
            return false;
        }

        // if it is out of bounds row-wise...
        if (newi < 0 || newi >= size()) {
            return false;
        }

        // if the destination position is occupied by a pawn of same color...
        if (at(newi, newj) == Color.White) {
            return false;
        }

        // if the move is straight and the destination position is occupied...
        if (move.isStraight() && at(newi, newj) != Color.None) {
            return false;
        }

        // if none of the previous conditions matches, then the move is legal
        return true;
    }

    @Override
    public List<WhiteMove> legalMoves() {
        final int size = size();
        final List<WhiteMove> list = new ArrayList<WhiteMove>(size * size / 2);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                for (Direction dir : Direction.values()) {
                    final WhiteMove move = new WhiteMove(i, j, dir);
                    if (isLegal(move)) {
                        list.add(move);
                    }
                }
            }
        }
        Collections.shuffle(list);
        return list;
    }

    @Override
    public List<Game> futures() {
        return legalMoves().stream().map(this::after).collect(Collectors.toList());
    }

    @Override
    public boolean hasWinner() {

        // check whether there is a black pawn on the winning column
        final int winningColumn = 0;
        for (int i = 0; i < size(); i++) {
            if (at(i, winningColumn) == Color.Black) {
                return true;
            }
        }

        // check whether there are no more pawns of opposite color
        return count(Color.White) == 0;
    }

    @Override
    public int count(Color color) {
        int count = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                count += (at(i, j) == color) ? 1 : 0;
            }
        }
        return count;
    }

    @Override
    public Game dual() {
        return gameState(reverse(getBoard(this)));
    }

    @Override
    public boolean equals(Object other) {
        final Game otherState = (Game) other;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (otherState.at(i, j) != at(i, j)) {
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

        ASCIIBoardRepresentation(Game state) {
            final int size = state.size();

            appendLn("\n");
            leftSpace(); horizontalBorder(size);
            for (int i = 0; i < size; i++) {
                leftSpace();
                for (int j = 0; j < size; j++) {
                    append("|");
                    pawn(state.at(i, j));
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
                case White:
                    append(" O ");
                    break;
                case Black:
                    append(" X ");
                    break;
                case None:
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
