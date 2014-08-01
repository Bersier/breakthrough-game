package breakthrough.game;

import breakthrough.Color;

import java.util.Stack;

/**
* <p>
* Created on 8/1/2014.
*/
public class ASCIIBoardRepresentation {

    private final StringBuilder builder = new StringBuilder();
    private final String string;

    public ASCIIBoardRepresentation(int size, Stack<Color> stack) {//todo remove redundancy
        appendLn("\n");
        leftSpace(); horizontalBorder(size);
        for(int i = 0; i < size; i++) {
            if(stack.isEmpty()) {
                break;
            }
            leftSpace();
            for(int j = 0; j < size; j++) {
                append("|");
                pawn(stack.pop());
            }
            appendLn("|");
            leftSpace(); horizontalBorder(size);
        }
        appendLn("\n");

        this.string = builder.toString();
    }

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
