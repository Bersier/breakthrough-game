package breakthrough.game;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * <p>
 * Created on 7/30/2014.
 */
@RunWith(Parameterized.class)
public class InversionGameTest extends GameTest {

    public InversionGameTest(Game refG, Game game) {
    super(refG, game);
    }

    @Parameterized.Parameters
    public static Collection<Game[]> data() {
        return getTestParams(InversionGame::new);
    }
}
