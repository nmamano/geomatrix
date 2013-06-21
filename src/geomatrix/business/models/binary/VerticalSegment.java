
package geomatrix.business.models.binary;

import geomatrix.utils.Direction;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;

/**
 *
 * @author Nil
 */
public class VerticalSegment implements Comparable {
    public int x;
    public Interval yInterval;
    
    /**
     * Constructor from x and y values.
     * Pre: rangeEnd1 != rangeEnd2
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
    public GridPoint getOtherEndpoint(GridPoint endpoint) {
        Pair<GridPoint, GridPoint> endpoints = getEndpoints();
        assert(endpoint.equals(endpoints.first) || endpoint.equals(endpoints.second)) :
                "Endpoints: " + endpoints.first.toString() + ", " +
                endpoints.second.toString() + "\n" + "Parameter: " +
                endpoint.toString();
        
        if (endpoint.equals(endpoints.first)) return endpoints.second;
        else return endpoints.first;
    }
    
    /**
     * Returns the endpoints of the edge.
     * @return a pair with both endpoints.
     */
    public Pair<GridPoint, GridPoint> getEndpoints() {
        return new Pair<GridPoint, GridPoint>(new GridPoint(x,yInterval.low), 
               new GridPoint(x,yInterval.high));
    }
    
    /**
     * Returns whether the segment other is contained in this.
     * pre: this and other have the same direction (vertical or horizontal)
     * @param other the segment to be tested if it is contained.
     * @return true if other is contained in this. false otherwise.
     */
    public boolean contains(VerticalSegment other) {
        assert(this.getClass().equals(other.getClass()));
        return other.x == this.x && this.yInterval.contains(other.yInterval);
    }
 
    /**
     * Returns whether the segment 'segment' and this intersect.
     * pre: this and other have different directions (vertical and horizontal)
     * @param other
     * @return 
     */
    public boolean intersects(VerticalSegment other) {
        assert(! this.getClass().equals(other.getClass()));
        return other.yInterval.contains(x) && yInterval.contains(other.x);
    }
    
    /**
     * Returns whether the wide ray 'ray' intersects with this edge.
     * pre: ray propagates to east or west.
     * @param ray the ray whose intersection with this edge is to be checked.
     * @return true if the wide ray 'ray' intersects with this edge. false
     * otherwise.
     */
    public boolean intersects(WideRay ray) {
        assert(ray.direction.equals(Direction.E) || ray.direction.equals(Direction.W));
        if (ray.direction.equals(Direction.E)) {
            return x > ray.origin.x && yInterval.low <= ray.origin.y &&
                                       yInterval.high > ray.origin.y;
        }
        else {
            return x <= ray.origin.x && yInterval.low <= ray.origin.y &&
                                        yInterval.high > ray.origin.y;
        }
    }
    
    /**
     * Given that this segment and 'segment' intersect, returns the grid point
     * where they intersect.
     * pre: this and other intersect
     * @param other
     * @return 
     */
    public GridPoint getIntersection(VerticalSegment other) {
        assert(intersects(other));
        return new GridPoint(x, other.x);
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
        VerticalSegment other = (VerticalSegment) o;
        if (this.x < other.x) return -1;
        else if (this.x > other.x) return 1;
        return 0;
    }
    
    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.x;
        hash = 79 * hash + (this.yInterval != null ? this.yInterval.hashCode() : 0);
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
