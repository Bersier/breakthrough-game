package breakthrough.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class GameTest {

    private final Game game;

    public GameTest(Game game) {
        this.game = game;
    }

    @Parameterized.Parameters
    public static Collection<Game[]> data() {
        return Arrays.asList(new Game[][] {{Utils.newGameState(8)}, {Utils.newGameState(4)}});
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAt() throws Exception {

    }

    @Test
    public void testSize() throws Exception {

    }

    @Test
    public void testAfter() throws Exception {

    }

    @Test
    public void testMirror() throws Exception {

    }

    @Test
    public void testIsLegal() throws Exception {

    }

    @Test
    public void testLegalMoves() throws Exception {

    }

    @Test
    public void testFutures() throws Exception {
        assertEquals("futures must agree with legalMoves",
                game.legalMoves().stream().map(game::after).collect(Collectors.toSet()),
                new HashSet<Game>(game.futures())
        );
    }

    @Test
    public void testHasWinner() throws Exception {

    }

    @Test
    public void testCount() throws Exception {

    }

    @Test
    public void testDual() throws Exception {

    }

    @Test
    public void testEquals() throws Exception {

    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(1, 1);
    }
}