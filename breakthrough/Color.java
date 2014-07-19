package breakthrough;

public enum Color {
	
	white(0), black(1), none(-1);
	
	public final int inc;
	
	private Color(int inc) {
		this.inc = inc;
	}

	public Color opposite() {
		switch(this) {
		case white:
			return black;
		case black:
			return white;
		}
		return none;
	}
}
