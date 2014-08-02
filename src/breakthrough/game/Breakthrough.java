package breakthrough.game;

import breakthrough.Color;
import breakthrough.Move;
import breakthrough.player.Player;

/**
 * <p>
 * Created on 8/1/2014.
 */
public interface Breakthrough {

    /**
     * Play a Breakthrough game with the given players.
     *
     * @param white the first player
     * @param black the second player
     * @param size the size of one side of the square board on which is to be played
     * @return the color of the winner
     */
    static Color play(Player white, Player black, int size) {
        final Game start = gameStart(white, black, size);
        final Player winner = play(white, black, start);
        return gameClose(white, black, winner);
    }

    /**
     * Play a Breakthrough game with the given players, showing its progress.
     *
     * @param white the first player
     * @param black the second player
     * @param size the size of one side of the square board on which is to be played
     * @return the color of the winner
         */
    static Color showPlay(Player white, Player black, int size) {
        final Game start = gameStart(white, black, size);
        final Player winner = showPlay(white, black, start, Color.White);
        return gameClose(white, black, winner);
    }

    /**
     * @return a new game state of the given size for a game that is about to start
     */
    static Game newGame(int size) {
        return new DefaultGame(startingBoard(size));
    }

    /**
     * @return a game state corresponding to the given 2D Color array
     */
    static Game gameState(Color[][] board) {
        checkSize(board);
        return new DefaultGame(Utils.copy(board));
    }

    /**
     * Play a Breakthrough game with the given players from the given state.
     *
     * @param active the player who is about to play
     * @param passive the player who just played
     * @param state the current state of the Breakthrough game
     * @return the winner
     */
    static Player play(Player active, Player passive, Game state) {

        // check whether there is a winner
        if (state.hasWinner()) {
            return passive;
        }

        // continue the game, switching player roles
        return play(passive, active, getNextState(active, state));
    }

    static Game gameStart(Player white, Player black, int size) {
        final Game start = newGame(size);
        white.gameStart(start, true);
        black.gameStart(start, false);
        return start;
    }

    static Color gameClose(Player white, Player black, Player winner) {
        winner.gameOver(true);
        if (winner == white) {
            black.gameOver(false);
            return Color.White;
        }
        else {
            white.gameOver(false);
            return Color.Black;
        }
    }

    /**
     * Play a Breakthrough game with the given players from the given state, showing its progress.
     *
     * @param active the player who is about to play
     * @param passive the player who just played
     * @param state the current state of the Breakthrough game
     * @return the winner
     */
    static Player showPlay(Player active, Player passive, Game state, Color activeColor) {

        // check whether there is a winner
        if (state.hasWinner()) {

            // print out the last position and the winner
            System.out.println(gameToString(state, activeColor));
            System.out.println("The winner is: " + passive);

            return passive;
        }

        final Game nextState = getNextState(active, state);

        // print out the board and what the player has to say
        System.out.println(gameToString(state, activeColor));
        System.out.println(activeColor + " is playing: " + active.talk());

        // continue the game, switching player roles
        return showPlay(passive, active, nextState, activeColor.dual());
    }

    static String gameToString(Game game, Color activeColor) {
        final Game toShow = activeColor == Color.Black ? game.dual() : game;
        return toShow.toString();
    }

    static Game getNextState(Player active, Game state) {

        // ask player whose turn it is for its next move
        final Move move = active.play(state);

        // check the move is legal
        if (! state.isLegal(move)) {
            throw new RuntimeException("Cheating attempt by " + active + " detected!");
        }

        return state.after(move);
    }

    /**
     * @return a 2D array representation for a game of the given size that is about to start
     */
    static Color[][] startingBoard(int size) {
        checkSize(size);

        final Color[][] board = new Color[size][size];
        final int noOfPawnColumns = 1 + size / 7;

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

    static void checkSize(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size must be at least 2!");
        }
        if (size >= 32768) {
            throw new IllegalArgumentException("Board size may not be above 32767!");
        }
    }

    static void checkSizesAreEqual(int size, int rowLength) {
        if (rowLength != size) {
            throw new IllegalArgumentException("Passed 2D array is not square!");
        }
    }

    static void checkSize(Color[][] board) {
        final int size = board.length;
        checkSize(size);
        for (int i = 0; i < size; i++) {
            final int rowLength = board[i].length;
            checkSizesAreEqual(size, rowLength);
        }
    }
}
