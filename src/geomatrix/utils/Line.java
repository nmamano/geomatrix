/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.utils;

/**
 *
 * @author Nil
 */
public class Line {
    public boolean vertical; //if false, the line is horizontal
    public int fixedCoordinate;

    public Line(boolean vertical, int fixedCoordinate) {
        this.vertical = vertical;
        this.fixedCoordinate = fixedCoordinate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.vertical ? 1 : 0);
        hash = 97 * hash + this.fixedCoordinate;
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
        if (this.vertical != other.vertical) {
            return false;
        }
        if (this.fixedCoordinate != other.fixedCoordinate) {
            return false;
        }
        return true;
    }
    
}
