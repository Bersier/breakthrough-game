package breakthrough.player;

import breakthrough.WhiteMove;
import breakthrough.game.GameState;

/**
 * Instances of this interface implement Breakthrough players.
 * <p>
 * Created on 7/20/2014.
 */
public interface Player {

    /**
     * Tell this player that a new game has started with the given initial state.
     *
     * @param initialState the game state the start of the game
     * @param youStart whether this player plays first
     */
    void gameStart(GameState initialState, boolean youStart);

    /**
     * Tell this player that the game is over and whether it won.
     */
    void gameOver(boolean youWon);

    /**
     * Ask the player for a white move.
     * <p>
     * Players always play for white.
     * It is the duty of the game controller to make the needed conversions.
     */
	WhiteMove play(GameState current);

    /**
     * @return whatever this player wants to say at that point in the game.
     * For example, it could indicate how confident it is in winning.
     */
    String talk();
}
