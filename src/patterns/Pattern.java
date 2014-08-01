package patterns;

import breakthrough.Color;
import breakthrough.Board;
import breakthrough.Move;

import java.util.List;

/*
 * A pattern matches certain boards.
 * For each cell of the board, it either
 *     puts no restriction on it (don't care, X)
 *     should not contain a defendent's pawn (blank, )
 *     should contain an attacker's pawn O>)
 *     
 * must implement hashcode() and equals()
 */
interface Pattern {
	
	// checks whether the given board contains this pattern
	boolean match(Board<Move> board);
	
	// returns white for attacker (iff N is odd), black for defender
	Color getBeginner();
	
	// returns the size of the board
	int getSize();
	
	// returns the number of turns after which the attacker is supposed to win
	int getN();
	
	// a pattern is good if the attacker always wins in this position in N turns
	Ternar isGood();
	
	// returns all the patterns directly smaller than this (this is their sup)
	List<Pattern> getLessers();
	
	// number of pawns of this color, according to the color scheme above
	int count(Color color);
	
	// returns the pattern in a color list form
	List<Color> getList();
	
	// returns the color at (i, j)
	Color getColorAt(int i, int j);
	
	// returns the grid of the board representing the pattern
	Color[][] getGrid();
	
	// print the pattern
	void print();
	
}
