package breakthrough;

/**
 * Instances of this class represent moves in Breakthrough.
 */
public final class Move {

    /** start coordinates */
	final int i, j;

    /** direction of the move */
	private final Direction dir;
	
	Move(int i, int j, Direction dir) {
		this.i = i;
		this.j = j;
		this.dir = dir;
	}

	Move(int i, int j, int dir) {
		this.i = i;
		this.j = j;
        this.dir = Direction.values()[dir + 1];
	}

    /**
     * @return whether the move is straight
     */
	boolean isStraight() {
		return dir == Direction.straight;
	}

    /**
     * @param color the color of the player making the move
     * @return the i-coordinate after making this move
     */
	int newi(Color color) {
		return i + (1 - 2*color.ordinal())*(dir.ordinal()-1);
	}

    /**
     * @param color the color of the player making the move
     * @return the j-coordinate after making this move
     */
	int newj(Color color) {
		return j + (1 - 2*color.ordinal());
	}
	
}
