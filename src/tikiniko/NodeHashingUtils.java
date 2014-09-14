package tikiniko;

import java.util.Random;

/**
 * This hash works only for a canonical representation :(
 *
 * Created on 10/08/14.
 */
public class NodeHashingUtils {

    private NodeHashingUtils() {}

    private static final TwoBy2Matrix emptyWordHash = new TwoBy2Matrix();

    private static final TwoBy2Matrix b = new TwoBy2Matrix();
    private static final TwoBy2Matrix n = new TwoBy2Matrix();
    private static final TwoBy2Matrix w = new TwoBy2Matrix();

    private static TwoBy2Matrix hash(TwoBy2Matrix black, TwoBy2Matrix none, TwoBy2Matrix white) {
        return (b.times(black)) .plus (n.times(none)) .plus (w.times(white));
    }

    private static class TwoBy2Matrix {

        private static final Random random = new Random();

        private final int a, b;
        private final int c, d;

        TwoBy2Matrix() {
            this.a = random.nextInt(); this.b = random.nextInt();
            this.c = random.nextInt(); this.d = random.nextInt();
        }

        TwoBy2Matrix(int a, int b, int c, int d) {
            this.a = a; this.b = b;
            this.c = c; this.d = d;
        }

        public TwoBy2Matrix plus(TwoBy2Matrix m) {
            return new TwoBy2Matrix(a + m.a, b + m.b,
                                    c + m.c, d + m.d);
        }

        public TwoBy2Matrix times(TwoBy2Matrix m) {
            return new TwoBy2Matrix(a*m.a + b*m.c, a*m.b + b*m.d,
                                    c*m.a + d*m.c, c*m.b + d*m.d);
        }
    }
}
