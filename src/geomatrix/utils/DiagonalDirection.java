
package geomatrix.utils;

/**
 * Cardinal directions: N (North), S (South), E (East), W (West).
 * 
 * @author alvaro
 */
public enum DiagonalDirection {

    NW(0), NE(1), SE(2), SW(3);
    
    private final int direction;

    private DiagonalDirection(int direction) {
        this.direction = direction;
    }

    /**
     * Returns the immediate orientation to the left of the caller.
     * @param None
     * @return The orientation of the left of the implicit parameter
     */
    public DiagonalDirection rotateLeft() {
	return DiagonalDirection.values()[(this.direction + 3) % 4];
    }
    
    /**
     * Returns the immediate orientation to the right of the caller.
     * @param None
     * @return The orientation of the right of the implicit parameter
     */
    public DiagonalDirection rotateRight() {
        return DiagonalDirection.values()[(this.direction + 1) % 4];
    }

    /**
     * Returns the opposite orientation of the caller.
     * @param None
     * @return The orientation in front of the caller.
     */
    public DiagonalDirection complementary() {
        return DiagonalDirection.values()[(this.direction + 2) % 4];
    }
    
    @Override
    public String toString() {
        if (this == NW) return "NW";
        else if (this == NE) return "NE";
        else if (this == SW) return "SW";
        else return "SE";
    }
}
