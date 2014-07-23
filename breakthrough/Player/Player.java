package breakthrough.player;

import breakthrough.Color;
import breakthrough.game.GameState;
import breakthrough.Move;

public interface Player {

    /**
     * Tell this player that a new game has started with the given initial state.
     * And that it is assigned the given color. White is assumed to start.
     */
    void gameStart(GameState initialState, Color yourColor);

    /**
     * Tell this player that a new game has started, and that it is assigned the given color.
     */
    void gameStart(Color yourColor);

    /**
     * Tell this player that the game is over and whether it won.
     */
    void gameOver(boolean youWon);

    /**
     * Ask the player for a move.
     */
	Move play(GameState current);

    /**
     * @return whatever this player wants to say at that point in the game.
     * For example, it could indicate how confident it is in winning.
     */
    String talk();
}
