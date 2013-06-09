
package geomatrix.business.models.binary;

import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 * Represents a horizontal segment.
 * @author Nil
 */
public class HorizontalSegment extends VerticalSegment {
    public int y;
    public Interval xInterval;

    /**
     * Constructor from x and y values.
     * Pre: x1 != x2
     * @param y
     * @param x1
     * @param x2 
     */
    public HorizontalSegment(int y, int x1, int x2) {
        this(y, new Interval(x1, x2));
    }
    
    public HorizontalSegment(int y, Interval xInterval) {
        super(y, xInterval);
        this.y = y;
        this.xInterval = xInterval;
    }

    /**
     * Given an endpoint of the edge, returns the other endpoint.
     * Pre: 'endpoint' is an endpoint of this edge.
     * @param endpoint the endpoint of the edge.
     * @return the other endpoint of the edge.
     */
    @Override
    public Point getOtherEndpoint(Point endpoint) {
        return super.getOtherEndpoint(endpoint.revert()).revert();
    }
    
    /**
     * Returns the endpoints of the edge.
     * @return a pair with both endpoints.
     */
    @Override
    public Pair<Point, Point> getEndpoints() {
        Pair<Point, Point> result = super.getEndpoints();
        result.first.revert();
        result.second.revert();
        return result;
    }
    
    /**
     * Given that this segment and 'segment' intersect, returns the grid point
     * where they intersect.
     * @param segment
     * @return 
     */
    @Override
    public Point getIntersection(VerticalSegment segment) {
        return super.getIntersection(segment).revert();
    }
       
}
