package breakthrough.player;

import breakthrough.Color;
import breakthrough.game.GameState;

/**
 * Provides default implementations where possible.
 * <p>
 * Created on 7/23/2014.
 */
public abstract class AbstractPlayer implements Player {

    @Override
    public void gameStart(GameState initialState, boolean youStart) {}

    @Override
    public void gameOver(boolean youWon) {}

    @Override
    public String talk() {
        return "";
    }
}
