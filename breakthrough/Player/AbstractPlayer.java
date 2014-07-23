package breakthrough.player;

import breakthrough.Color;
import breakthrough.game.GameState;

/**
 * Provides default implementations where possible.
 * <p>
 * Created on 7/23/2014.
 */
public abstract class AbstractPlayer implements Player {

    private Color myColor;

    protected Color myColor() {
        return myColor;
    }

    @Override
    public void gameStart(GameState initialState, Color yourColor) {
        gameStart(yourColor);
    }

    @Override
    public void gameStart(Color yourColor) {
        if (myColor != null) {
            throw new IllegalStateException("Start method got already called!");
        }
        myColor = yourColor;
    }

    @Override
    public void gameOver(boolean youWon) {}

    @Override
    public String talk() {
        return "";
    }
}
