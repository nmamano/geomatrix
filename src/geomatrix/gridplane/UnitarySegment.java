/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.gridplane;

import geomatrix.utils.Axis;

/**
 *
 * @author Nil
 */
public class UnitarySegment {
    public GridPoint topLeftEndPoint;
    public Axis axis;

    public UnitarySegment(GridPoint endPoint1, Axis axis) {
        this.topLeftEndPoint = endPoint1;
        this.axis = axis;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.topLeftEndPoint != null ? this.topLeftEndPoint.hashCode() : 0);
        hash = 37 * hash + (this.axis != null ? this.axis.hashCode() : 0);
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
        final UnitarySegment other = (UnitarySegment) obj;
        if (this.topLeftEndPoint != other.topLeftEndPoint && (this.topLeftEndPoint == null || !this.topLeftEndPoint.equals(other.topLeftEndPoint))) {
            return false;
        }
        if (this.axis != other.axis && (this.axis == null || !this.axis.equals(other.axis))) {
            return false;
        }
        return true;
    }
    
}
