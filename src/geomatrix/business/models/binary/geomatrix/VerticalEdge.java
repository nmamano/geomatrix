
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Point;
import geomatrix.business.models.binary.VerticalSegment;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 * Represents a vertical edge of an area.
 * @author Nil
 */
class VerticalEdge extends VerticalSegment {

    /**
     * Constructor from x and y values.
     * Pre: y1 != y2
     * @param x
     * @param yLow
     * @param yHigh 
     */
    public VerticalEdge(int x, int y1, int y2) {
        super(x, y1, y2);
    }

    public VerticalEdge(int x, Interval yInterval) {
        super(x, yInterval);
    }
        
    @Override
    public String toString() {
        return super.toString();
    }

}
