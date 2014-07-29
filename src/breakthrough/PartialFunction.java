package breakthrough;

import java.util.function.Function;

/**
 * <p>
 * Created on 7/26/2014.
 */
public interface PartialFunction<A, B> extends Function<A, B> {

    boolean isDefinedAt(A a);

    default Function<A, B> els(Function<A, B> other) {
        return a -> isDefinedAt(a) ? apply(a) : other.apply(a);
    }

    default PartialFunction<A, B> els(PartialFunction<A, B> other) {
        return new PartialFunction<A, B>() {
            @Override
            public boolean isDefinedAt(A a) {
                return PartialFunction.this.isDefinedAt(a) || other.isDefinedAt(a);
            }

            @Override
            public B apply(A a) {
                return PartialFunction.this.isDefinedAt(a) ?
                             PartialFunction.this.apply(a) : other.apply(a);
            }
        };
    }
}
