
package geomatrix.utils;

/**
 * Cardinal directions: N (North), S (South), E (East), W (West).
 * 
 * @author alvaro
 */
public enum Direction {

    N(0), E(1), S(2), W(3);
    
    private final int direction;

    private Direction(int direction) {
        this.direction = direction;
    }

    /**
     * Returns the immediate orientation to the left of the caller.
     * @param None
     * @return The orientation of the left of the implicit parameter
     */
    public Direction rotateLeft() {
	return Direction.values()[(this.direction + 3) % 4];
    }
    
    /**
     * Returns the immediate orientation to the right of the caller.
     * @param None
     * @return The orientation of the right of the implicit parameter
     */
    public Direction rotateRight() {
        return Direction.values()[(this.direction + 1) % 4];
    }

    /**
     * Returns the opposite orientation of the caller.
     * @param None
     * @return The orientation in front of the caller.
     */
    public Direction complementary() {
        return Direction.values()[(this.direction + 2) % 4];
    }
    
    public Axis getAxis() {
        if (this == N || this == S) return Axis.Vertical;
        else return Axis.Horizontal;
    }
    
    @Override
    public String toString() {
        if (this == N) return "N";
        else if (this == E) return "E";
        else if (this == W) return "W";
        else return "S";
    }
}
