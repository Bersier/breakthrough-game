package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;
import commons.Pair;
import commons.Thunk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class GameTest {

    private static Random random = new Random();

    private final Game refG;
    private final Game game;
    private final int  size;

    public GameTest(Game refG, Game game) {
        this.refG = refG;
        this.game = game;
        this.size = refG.size();
    }

    /**
     * This is useless, but had to add it to stop JUnit from complaining.
     */
    @Parameterized.Parameters
    public static Collection<Game[]> data() {
        return Arrays.asList(new Game[][]{new Game[]{getRefG(2), getRefG(2)}});
    }

    static Collection<Game[]> getTestParams(Function<Game, Game> constructor) {
        return getGamesToTest(size -> constructor.apply(getRefG(size)));
    }

    private static Collection<Game[]> getGamesToTest(Function<Integer, Game> newGame) {
        return getTestGamePairs(newGame).stream()
                .map(pair -> new Game[]{ pair.first, pair.second })
                .collect(Collectors.toList());
    }

    private static Collection<Pair<Game, Game>> getTestGamePairs(Function<Integer, Game> newGame) {
        final Collection<Pair<Game, Game>> toTest = new ArrayList<>();
        for (Integer size : new Integer[]{2, 4, 7, 8}) {
            for (int i = 0; i < 8; i++) {
                Game game = newGame.apply(size);
                Game refG = getRefG(size);
                do {
                    toTest.add(new Pair<>(game, refG));
                    final WhiteMove move = refG.legalMoves().get(0);
                    game = game.after(move);
                    refG = refG.after(move);
                } while (! refG.hasWinner());
                toTest.add(new Pair<>(game, refG));
            }
        }
        return toTest;
    }

    private static Game getRefG(Integer size) {
        return Utils.newGame(size);
    }

    private static void sometimes(Thunk t) {
        if (random.nextInt(8) == 0) {
            t.eval();
        }
    }

    @Test
    public void testAt() throws Exception {
        final int i = random.nextInt(size);
        final int j = random.nextInt(size);

        assertEquals(
                refG.at(i, j),
                game.at(i, j)
        );
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(
                refG.size(),
                game.size()
        );
    }

    @Test
    public void testMirror() throws Exception {
        assertEquals(
                refG.mirror(),
                game.mirror()
        );
    }

    @Test
    public void testIsLegal() throws Exception {
        final int i = random.nextInt(size);
        final int j = random.nextInt(size - 1);
        final WhiteMove.Direction dir = WhiteMove.Direction.values()[random.nextInt(3)];

        final WhiteMove move = new WhiteMove(i, j, dir);

        assertEquals(
                refG.isLegal(move),
                game.isLegal(move)
        );
    }

    @Test
    public void testLegalMoves() throws Exception {
        sometimes(() -> assertEquals(
                new HashSet<WhiteMove>(refG.legalMoves()),
                new HashSet<WhiteMove>(game.legalMoves())
        ));
    }

    @Test
    public void testFutures() throws Exception {
        sometimes(() -> assertEquals(
                new HashSet<Game>(refG.futures()),
                new HashSet<Game>(game.futures())
        ));
    }

    @Test
    public void testHasWinner() throws Exception {
        assertEquals(
                refG.hasWinner(),
                game.hasWinner()
        );
    }

    @Test
    public void testCount() throws Exception {
        final Color color = Color.values()[random.nextInt(3)];
        assertEquals(
                refG.count(color),
                game.count(color)
        );
    }

    @Test
    public void testDual() throws Exception {
        sometimes(() -> assertEquals(
                refG.dual(),
                game.dual()
        ));
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(
                refG.equals(getRefG(size)),
                game.equals(getRefG(size))
        );
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(
                refG.hashCode(),
                game.hashCode()
        );
    }
}
