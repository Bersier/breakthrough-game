package breakthrough;

import breakthrough.gameState.GameState;
import breakthrough.player.Player;

/**
 * Created on 7/20/2014.
 *
 * @author Stephane Bersier
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
    public static Color play(Player white, Player black, int size) {
        return play(white, black, newGameState(size), Color.white);
    }

    /**
     * Play a Breakthrough game with the given players from the given state.
     *
     * @param active the player who is about to play
     * @param passive the player who just played
     * @param current the current state of the Breakthrough game
     * @param turn the color of the player who is about to play
     * @return the winner's color
     */
    private static Color play(Player active, Player passive, GameState current, Color turn) {
        final Color previous = turn.opposite();

        // check whether there is a winner
        if (current.isWinner(previous)) {
            return previous;
        }

        // ask player whose turn it is for its next move
        final Move move = active.play(current);

        // check the move is legal
        if (! current.isLegal(move, turn)) {
            throw new RuntimeException("Cheating attempt by " + turn + " detected!");
        }

        // play the move
        return play(passive, active, current.after(move), previous);
    }

    /**
     * @return return a new game state of the given size for a game that is about to start
     */
    public static GameState newGameState(int size) {
        return null;//todo
    }
}
