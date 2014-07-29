package breakthrough;

/**
 * Instances of this class represent (white) moves in Breakthrough.
 * <p/>
 * Created on 7/23/2014.
 */
public class WhiteMove {

    public static enum Direction {
        Left, Straight, Right
    }

    /** start coordinates */
    public final byte i, j;

    /** destination coordinates */
    public final byte newi, newj;

    public WhiteMove(int i, int j, Direction direction) {
        this.i    = (byte)i;
        this.j    = (byte)j;
        this.newi = (byte)(this.i + direction.ordinal()-1);
        this.newj = (byte)(this.j + 1);
    }

    public WhiteMove(int i, int j, int direction) {
        this.i    = (byte)i;
        this.j    = (byte)j;
        this.newi = (byte)(this.i + direction);
        this.newj = (byte)(this.j + 1);
    }

    /**
     * @return whether the move is straight
     */
    public boolean isStraight() {
        return i == newi;
    }

    public Direction direction() {
        return Direction.values()[newi - i + 1];
    }

    /**
     * @return the i-coordinate of the pawn before this move
     */
    public int i() {
        return i;
    }

    /**
     * @return the j-coordinate of the pawn before this move
     */
    public int j() {
        return j;
    }

    /**
     * @return the i-coordinate of the pawn after making this move
     */
    public int newi() {
        return newi;
    }

    /**
     * @return the j-coordinate of the pawn after making this move
     */
    public int newj() {
        return newj;
    }

}
