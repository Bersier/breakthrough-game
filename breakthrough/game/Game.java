package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;
import breakthrough.player.Player;

/**
 * Created on 7/20/2014.
 */
public class Game {

    /**
     * Play a Breakthrough game with the given players.
     *
     * @param white the first player
     * @param black the second player
     * @param size the size of one side of the square board on which is to be played
     * @return who won
     */
    public static Player play(Player white, Player black, int size) {
        return play(white, black, newGameState(size));
    }

    /**
     * Play a Breakthrough game with the given players from the given state.
     *
     * @param active the player who is about to play
     * @param passive the player who just played
     * @param state the current state of the Breakthrough game
     * @return the winner
     */
    public static Player play(Player active, Player passive, GameState state) {

        // check whether there is a winner
        if (state.hasWinner()) {
            return passive;
        }

        // ask player whose turn it is for its next move
        final WhiteMove move = active.play(state);

        // check the move is legal
        if (! state.isLegal(move)) {
            throw new RuntimeException("Cheating attempt by " + active + " detected!");
        }

        // continue the game, switching player roles
        return play(passive, active, state.after(move));
    }

    /**
     * @return a new game state of the given size for a game that is about to start
     */
    public static GameState newGameState(int size) {
        return new InversionGameState(startingBoard(size));
    }

    /**
     * @return a 2D array representation for a game of the given size that is about to start
     */
    private static Color[][] startingBoard(int size) {
        checkSize(size);

        final Color[][] board = new Color[size][size];
        final int noOfPawnColumns = 2 * size / 7;

        for (int i = 0; i < size; i++) {

            // put white pawns at start of row
            for (int j = 0; j < noOfPawnColumns; j++) {
                board[i][j] = Color.White;
            }

            // put no pawns in the middle
            for (int j = noOfPawnColumns; j < size - noOfPawnColumns; j++) {
                board[i][j] = Color.None;
            }

            // put black pawns at end of row
            for (int j = size - noOfPawnColumns; j < size; j++) {
                board[i][j] = Color.Black;
            }
        }
        return board;
    }

    private static void checkSize(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be at least 2!");
        }
        if (size > 128) {
            throw new IllegalArgumentException("Board size may not be above 128!");
        }
    }

    /**
     * @return a game state corresponding to the given 2D Color array
     */
    public static GameState gameState(Color[][] board) {
        checkSize(board);
        return new DefaultGameState(board);
    }

    private static void checkSize(Color[][] board) {
        final int size = board.length;
        checkSize(size);
        for (int i = 0; i < size; i++) {
            final int rowLength = board[i].length;
            checkSizesAreEqual(size, rowLength);
        }
    }

    static void checkSizesAreEqual(int size, int rowLength) {
        if (rowLength != size) {
            throw new IllegalArgumentException("Passed 2D array is not square!");
        }
    }
}
