package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;

import java.util.List;

/**
 * Instances of this interface represent states of a Breakthrough game in progress.
 * The player whose turn it is always plays white.
 * Thus, the board gets reversed each turn, so as to remain in canonical form.
 * They should be immutable.
 * They do not store whether someone won.
 * They can assume they are being used properly.
 * <p>
 * Created on 7/19/2014.
 */
public interface Game {

    /**
     * @param i first board coordinate
     * @param j second board coordinate
     * @return the color of the pawn at that position, or None if there is no pawn there
     */
    Color at(int i, int j);

    /**
     * @return the size of one side of the square board
     */
    int size();

    /**
     * Returns a new game state which represents the board after the move has been executed.
     * The pawn color on that board is reversed, as it is always white's turn.
     *
     * Does not check whether the given move is legal.
     *
     * @param move the move to be executed
     * @return the resulting board.
     */
    Game after(WhiteMove move);

    /**
     * The game as seen in a mirror.
     * As the game has a lateral symmetry, the two games are effectively the same.
     *
     * @return a game where row i becomes row (size-1 - i)
     */
    Game mirror();

    /**
     * @return whether the given move is legal for the player whose turn it is (white),
     * ignoring winning conditions
     */
    boolean isLegal(WhiteMove move);

    /**
     * @return all legal moves, shuffled
     */
    List<WhiteMove> legalMoves();

    /**
     * @return all possible gameStates resulting from a legal move, shuffled
     */
    List<Game> futures();

    /**
     * Assumes that the given board is the result of legal play.
     *
     * @return whether the player who just played (Black) won in this state.
     */
    boolean hasWinner();

    /**
     * @param color the color of the pawns that are to be counted
     * @return how many pawns of that color are on the board
     */
    int count(Color color);

    /**
     * @param colorsInverted if true, flips the roles of black and white in the representation
     * @return an ASCII representation of the game state
     */
    String toString(boolean colorsInverted);

    /**
     * @return whether the other GameState represents the same game state
     */
    @Override
    boolean equals(Object other);

    @Override
    int hashCode();

    /**
     * @return an ASCII representation of the game state
     */
    @Override
    String toString();
}
