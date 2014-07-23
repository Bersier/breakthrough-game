package breakthrough;

public enum Color {
	
	White(0), Black(1), None(-1);
	
	public final int inc;
	
	private Color(int inc) {
		this.inc = inc;
	}

	public Color opposite() {
		switch(this) {
		case White:
			return Black;
		case Black:
			return White;
		}
		return None;
	}
}
