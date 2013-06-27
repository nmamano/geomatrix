/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.gridplane;

import geomatrix.utils.Axis;
import geomatrix.utils.Direction;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Nil
 */
public class Segment implements Comparable {
    public int fixed;
    public Interval range;
    Axis axis;

    public Segment(int fixed, Interval range, Axis axis) {
        this.fixed = fixed;
        this.range = range;
        this.axis = axis;
    }

    public Pair<GridPoint, GridPoint> getEndpoints() {
        GridPoint endPoint1, endPoint2;
        if (axis == Axis.Vertical) {
            endPoint1 = new GridPoint(fixed, range.low);
            endPoint2 = new GridPoint(fixed, range.high);
        }
        else {
            endPoint1 = new GridPoint(range.low, fixed);
            endPoint2 = new GridPoint(range.high, fixed);
        }
        return new Pair<GridPoint, GridPoint>(endPoint1, endPoint2);
    }

    public GridPoint getOtherEndpoint(GridPoint endpoint) {
        Pair<GridPoint, GridPoint> endpoints = getEndpoints();
        if (endpoint.equals(endpoints.first)) return endpoints.second;
        else return endpoints.first;
    }
    
    public boolean contains(Segment other) {
        if (axis != other.axis) return false;
        if (fixed != other.fixed) return false;
        return this.range.contains(other.range);
    }
    
    public boolean intersects(Segment other) {
        if (axis == other.axis) return false;
        return other.range.contains(fixed) && range.contains(other.fixed);
    }
    
    public boolean intersects(WideRay ray) {
        if (! beyondRayOrigin(ray)) return false;
        
        Interval intervalExtendedRayWidth = new Interval(range.low - 1, range.high);       
        if (ray.direction.getAxis() == Axis.Horizontal) {
            return intervalExtendedRayWidth.contains(ray.origin.y);
        }
        else {
            return intervalExtendedRayWidth.contains(ray.origin.x);
        }
    }
    
    public GridPoint getIntersection(Segment other) {
        assert(intersects(other));
        if (axis == Axis.Vertical) return new GridPoint(fixed, other.fixed);
        else return new GridPoint(other.fixed, fixed);
    }
    
    public Collection<UnitarySegment> breakIntoUnitarySegments() {
        Collection<UnitarySegment> unitarySegments = new ArrayList<UnitarySegment>();
        
        for (int i = range.low; i < range.high; ++i) {
            GridPoint topLeftEndPoint;
            if (axis == Axis.Vertical) topLeftEndPoint = new GridPoint(fixed, i);
            else topLeftEndPoint = new GridPoint(i, fixed);
            unitarySegments.add(new UnitarySegment(topLeftEndPoint, axis));
        }
        return unitarySegments;
    }
    
    private boolean beyondRayOrigin(WideRay ray) {
        if (axis == ray.direction.getAxis()) return false;
        
        if (ray.direction == Direction.E) return fixed > ray.origin.x;
        else if (ray.direction == Direction.W) return fixed <= ray.origin.x;
        else if (ray.direction == Direction.S) return fixed > ray.origin.y;
        else return fixed <= ray.origin.y;
    }

    @Override
    public int compareTo(Object obj) {
        Segment other = (Segment) obj;
        assert (axis == other.axis);
        if (this.fixed < other.fixed) return -1;
        else if (this.fixed > other.fixed) return 1;
        return 0;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.fixed;
        hash = 79 * hash + (this.range != null ? this.range.hashCode() : 0);
        hash = 79 * hash + (this.axis != null ? this.axis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Segment other = (Segment) obj;
        if (this.fixed != other.fixed) {
            return false;
        }
        if (this.range != other.range && (this.range == null || !this.range.equals(other.range))) {
            return false;
        }
        if (this.axis != other.axis) {
            return false;
        }
        return true;
    }

}
