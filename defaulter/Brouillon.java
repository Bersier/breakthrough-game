package defaulter;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Brouillon {

	public static int p(Object s) {
		System.out.println(s);
		return 0;
	}
	
	public static Object doit() {
		return null;
	}
	
	public static void main(String[] args) {
		/*final byte mask = (byte)(255);
		byte a = (byte)-128;
		byte b = (byte)1;
		a = (byte)(a-b);
		p(""+a);*/
		p(Direction.straight.ordinal());
	}
}
