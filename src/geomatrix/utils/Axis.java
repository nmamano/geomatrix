
package geomatrix.utils;

/**
 * Cardinal directions: N (North), S (South), E (East), W (West).
 * 
 * @author alvaro
 */
public enum Axis {

    Vertical(0), Horizontal(1);
    
    private final int axis;

    private Axis(int axis) {
        this.axis = axis;
    }

    /**
     * Returns the opposite orientation of the caller.
     * @param None
     * @return The orientation in front of the caller.
     */
    public Axis complementary() {
        return Axis.values()[(this.axis + 1) % 2];
    }
    
    @Override
    public String toString() {
        if (this == Vertical) return "Vertical";
        else return "Horizontal";
    }
}
