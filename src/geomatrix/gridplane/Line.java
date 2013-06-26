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
public class Line {
    public int fixedCoordinate;
    public Axis axis;

    public Line(int fixedCoordinate, Axis axis) {
        this.fixedCoordinate = fixedCoordinate;
        this.axis = axis;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.fixedCoordinate;
        hash = 29 * hash + (this.axis != null ? this.axis.hashCode() : 0);
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
        final Line other = (Line) obj;
        if (this.fixedCoordinate != other.fixedCoordinate) {
            return false;
        }
        if (this.axis != other.axis) {
            return false;
        }
        return true;
    }

}
