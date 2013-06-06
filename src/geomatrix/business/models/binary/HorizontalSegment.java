
package geomatrix.business.models.binary;

import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 * Represents a horizontal segment.
 * @author Nil
 */
public class HorizontalSegment {
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
        this.y = y;
        this.xInterval = xInterval;
    }

    /**
     * Given an endpoint of the edge, returns the other endpoint.
     * Pre: 'endpoint' is an endpoint of this edge.
     * @param endpoint the endpoint of the edge.
     * @return the other endpoint of the edge.
     */
    public Point getOtherEndpoint(Point endpoint) {
        Pair<Point, Point> endpoints = getEndpoints();
        assert(endpoint == endpoints.first || endpoint == endpoints.second);
        if (endpoint == endpoints.first) return endpoints.second;
        else return endpoints.first;
    }
    
    /**
     * Returns the endpoints of the edge.
     * @return a pair with both endpoints.
     */
    public Pair<Point, Point> getEndpoints() {
        return new Pair<Point, Point>(new Point(y,xInterval.low), 
               new Point(y,xInterval.high));
    }
    
    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.y;
        hash = 89 * hash + (this.xInterval != null ? this.xInterval.hashCode() : 0);
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
        final HorizontalSegment other = (HorizontalSegment) obj;
        if (this.y != other.y) {
            return false;
        }
        if (this.xInterval != other.xInterval && (this.xInterval == null || !this.xInterval.equals(other.xInterval))) {
            return false;
        }
        return true;
    }


    
    
}
