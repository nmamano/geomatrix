
package geomatrix.business.models.binary;

/**
 * Represents a grid point.
 * @author Nil
 */
public class GridPoint {
    public int x;
    public int y;

    public GridPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static float distance(GridPoint p1, GridPoint p2) {
        float xOffset = Math.abs(p1.x - p2.x);
        //-1 because squares are 1 unit wide
        float yOffset = Math.abs(p1.y - p2.y);
        return (float) Math.sqrt(xOffset*xOffset + yOffset*yOffset);
    }
    
    /**
     * Returns a point with the inverted coordinates of this.
     * @return a point with the inverted coordinates of this.
     */
    public GridPoint revert() {
        return new GridPoint(y, x);
    }
    
    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + x;
        hash = 37 * hash + y;
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
        final GridPoint other = (GridPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ')';
    }
    
    
    
}
