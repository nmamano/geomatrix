
package geomatrix.business.models.binary;

/**
 * Represents a rectangle of the grid.
 * It is represented by the coordinates of two grid points:
 * topLeft: the grid point where the left vertical edge and the top horizontal
 * edge coincide.
 * bottomRight: the grid point where the right vertical edge and the bottom
 * horizontal edge coincide.
 * @author Nil
 */
public class Rectangle {
    Point topLeft;
    Point bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Returns whether a rectangle is consistent.
     * For a rectangle to be consistent, the coordinates of topLeft must be lower
     * than the coordinates of bottomRight (thus ensuring it is not empty).
     * @return true if the rectangle is consistent. false otherwise.
     */
    boolean valid() {
        return topLeft.x < bottomRight.x && topLeft.y < bottomRight.y;
    }
    
    /**
     * In netbeans we trust.
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.topLeft != null ? this.topLeft.hashCode() : 0);
        hash = 79 * hash + (this.bottomRight != null ? this.bottomRight.hashCode() : 0);
        return hash;
    }

    /**
     * In netbeans we trust.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rectangle other = (Rectangle) obj;
        if (this.topLeft != other.topLeft && (this.topLeft == null || !this.topLeft.equals(other.topLeft))) {
            return false;
        }
        if (this.bottomRight != other.bottomRight && (this.bottomRight == null || !this.bottomRight.equals(other.bottomRight))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + topLeft.toString() + ", " + bottomRight.toString() + ']';
    }
    
    
    
}
