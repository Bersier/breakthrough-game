package breakthrough;

/**
 * Instances of this interface represent a game in progress.
 * <p>
 * Only legal moves should be allowed; that is, the board should make sure no player is cheating.
 * This class only works for two-player games that are played on a square grid,
 * where only one player is allowed to play at any time, where all pawns are the same, ...
 *
 * Think of the board as a matrix, with white players initially on the left, moving to the right,
 * and the opposite for black players. As with matrices, the i-coordinate determines the row, and
 * the j-coordinate the column.
 *
 * @param <Move> the type of move to be used in this board
 */
public interface Board<Move> {

    /**
     * @return the winner in this configuration. If neither Black nor White have won, returns None.
     */
	Color getWinner();

    /**
     * Puts the board back into a start configuration.
     */
	void reset();

    /**
     * @param i first board coordinate
     * @param j second board coordinate
     * @return the color of the pawn at that position, or None if there is no pawn there
     */
	Color getColorAt(int i, int j);

    /**
     * @return the size of one side of the square board
     */
	int getSize();

    /**
     * @return whose turn is it to play, or None if one player won
     */
	Color getTurn();

    /**
     * @return a matrix corresponding to the current state of the game
     */
	Color[][] getGrid();

    /**
     * @param color the color of the pawn that are to be counted
     * @return how many pawns of that color are on the board
     */
	int count(Color color);

    /**
     * @return a copy of this board
     */
	Board<Move> copy();

    /**
     * @param move the move to be executed by the color/player whose turn it is
     */
	void move(Move move);

    /**
     * @return whether the given move is legal in the current context/game state/configuration
     */
	boolean isLegal(Move move);

    /**
     * Prints an ASCII representation of the game state
     */
	void printBoard();//todo use toString instead
}
