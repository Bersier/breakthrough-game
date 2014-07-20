package breakthrough.gameState;

import breakthrough.Color;
import breakthrough.Move;

import java.util.List;

/**
 * Instances of this interface represent states of a Breakthrough game in progress.
 * They should be immutable.
 * They do not store who's turn it is or whether someone won, nor do they take these into account.
 * So they assume they are being used properly.
 *
 * Created on 7/19/2014.
 */
public interface GameState {

    /**
     * @param i first board coordinate
     * @param j second board coordinate
     * @return the color of the pawn at that position, or None if there is no pawn there
     */
    Color getColorAt(int i, int j);

    /**
     * @return the size of one side of the square board
     */
    int size();

    /**
     * Does not check whether the given move is legal.
     *
     * @param move the move to be executed
     * @return the resulting board.
     */
    GameState after(Move move);

    /**
     * @return whether the given move is legal for the player of the given color,
     * ignoring winning conditions
     */
    boolean isLegal(Move move, Color pawnColor);

    /**
     * @return all legal moves for the given color, shuffled
     */
    List<Move> legalMoves(Color color);

    /**
     * @return all possible gameStates resulting from a legal move of the given color, shuffled
     */
    List<GameState> futures(Color color);

    /**
     * Assumes that the given board is the result of legal play.
     *
     * @return the winner in this state. If neither Black nor White have won, return None.
     */
    Color getWinner();

    /**
     * @return whether the player of the given color wins in this game state
     */
    public boolean isWinner(Color color);

    /**
     * @param color the color of the pawn that are to be counted
     * @return how many pawns of that color are on the board
     */
    int count(Color color);

    /**
     * @return an ASCII representation of the game state
     */
    @Override
    String toString();
}
