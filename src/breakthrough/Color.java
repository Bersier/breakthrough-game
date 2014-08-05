package breakthrough;

/**
 * The colors used in Breakthrough.
 * Used for board squares, players, pawns, ...
 */
public enum Color {

    /**
     * By default, color of the player who just played.
     */
    Black(1),

    /**
     * Color of an empty square.
     */
    None(-1),

    /**
     * By default, color of the player who is about to play.
     */
    White(0);


    @Deprecated
	public final int inc;
	
	private Color(int inc) {
		this.inc = inc;
	}

    /**
     * @return the dual of this color; None is its own dual.
     */
	public Color dual() {
		return Color.values()[2 - ordinal()];
	}
}
