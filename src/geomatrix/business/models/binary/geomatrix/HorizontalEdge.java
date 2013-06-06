
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.HorizontalSegment;
import geomatrix.utils.Interval;

/**
 * Represents an horizontal edge of an area.
 * @author Nil
 */
class HorizontalEdge extends HorizontalSegment {

    /**
     * Constructor from x and y values.
     * Pre: x1 != x2
     * @param y
     * @param x1
     * @param x2 
     */
    public HorizontalEdge(int y, int x1, int x2) {
        super(y, x1, x2);
    }

    public HorizontalEdge(int y, Interval xInterval) {
        super(y, xInterval);
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
