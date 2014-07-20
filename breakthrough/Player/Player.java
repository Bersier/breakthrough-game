package breakthrough.player;

import breakthrough.Color;
import breakthrough.gameState.GameState;
import breakthrough.Move;

public interface Player {

    /**
     * Tell this player that a new game has started with the given initial state.
     * And that it is assigned the given color. White is assumed to start.
     */
    void start(GameState initialState, Color yourColor);

    /**
     * Tell this player that a new game has started, and that it is assigned the given color.
     */
    void start(Color yourColor);

    /**
     * Tell this player that it won.
     */
	void youWin();

    /**
     * Tell this player that it lost.
     */
	void youLoose();

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
