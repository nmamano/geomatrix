
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Point;
import geomatrix.business.models.binary.VerticalSegment;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 * Represents a vertical edge of an area.
 * @author Nil
 */
class VerticalEdge extends VerticalSegment implements Comparable {

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

    /**
     * Returns whether the left wide ray 'ray' intersects with this edge.
     * @param ray the ray whose intersection with this edge is to be tested.
     * @return true if 'ray' intersects with this edge. 
     */
    boolean intersects(LeftWideRay ray) {
        return x <= ray.origin.x && yInterval.low <= ray.origin.y && yInterval.high > ray.origin.y;
    }

    /**
     * Returns whether the right wide ray 'ray' intersects with this edge.
     * @param ray the ray whose intersection with this edge is to be tested.
     * @return true if 'ray' intersects with this edge. 
     */
    boolean intersects(RightWideRay ray) {
        return x > ray.origin.x && yInterval.low <= ray.origin.y && yInterval.high > ray.origin.y;
    }
    
    /**
     * Compares this VerticalEdge with the specified edge for order.
     * Vertical edges are sorted by their x coordinate.
     * Returns -1, 0, or 1 as this object is less than, equal to, or greater
     * than the specified edge.
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Object o) {
        VerticalEdge other = (VerticalEdge) o;
        if (this.x < other.x) return -1;
        else if (this.x > other.x) return 1;
        return 0;
    }

}
