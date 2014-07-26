package breakthrough;

@FunctionalInterface
public interface ValueFunction<Obs> {

    /**
     * @return the value of the given observation
     */
	double at(Obs o);
}
