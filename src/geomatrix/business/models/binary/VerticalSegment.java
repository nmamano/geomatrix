
package geomatrix.business.models.binary;

import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 * Represents a vertical segment.
 * @author Nil
 */
public class VerticalSegment {
    public int x;
    public Interval yInterval;

    /**
     * Constructor from x and y values.
     * Pre: y1 != y2
     * @param x
     * @param y1
     * @param y2 
     */
    public VerticalSegment(int x, int y1, int y2) {
        this(x, new Interval(y1, y2));
    }
    
    public VerticalSegment(int x, Interval yInterval) {
        this.x = x;
        this.yInterval = yInterval;
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
        return new Pair<Point, Point>(new Point(x,yInterval.low), 
               new Point(x,yInterval.high));
    }
    
    /**
     * Returns whether the vertical segment other is contained in this.
     * @param other the vertical segment to be tested if it is contained.
     * @return true if other is contained in this. false otherwise.
     */
    public boolean contains(VerticalSegment other) {
        return other.x == this.x && this.yInterval.contains(other.yInterval);
    }
 
    /**
     * Returns whether the horizontal segment 'segment' and this intersect.
     * @param segment
     * @return 
     */
    public boolean intersects(HorizontalSegment segment) {
        return segment.xInterval.contains(x) && yInterval.contains(segment.y);
    }
    
    /**
     * Given that this segment and 'segment' intersect, returns the grid point
     * where they intersect.
     * @param segment
     * @return 
     */
    public Point getIntersection(HorizontalSegment segment) {
        assert(intersects(segment));
        return new Point(x, segment.y);
    }
    
    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.x;
        hash = 89 * hash + (this.yInterval != null ? this.yInterval.hashCode() : 0);
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
        final VerticalSegment other = (VerticalSegment) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.yInterval != other.yInterval && (this.yInterval == null || !this.yInterval.equals(other.yInterval))) {
            return false;
        }
        return true;
    }


    
    
}
