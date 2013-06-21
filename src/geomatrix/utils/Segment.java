
package geomatrix.utils;

import java.awt.Point;

/**
 *
 * @author Nil
 */
public class Segment {
    public Point endPoint1, endPoint2;

    public Segment(Point endPoint1, Point endPoint2) {
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.endPoint1 != null ? this.endPoint1.hashCode() : 0);
        hash = 89 * hash + (this.endPoint2 != null ? this.endPoint2.hashCode() : 0);
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
        if (this.endPoint1 != other.endPoint1 && (this.endPoint1 == null || !this.endPoint1.equals(other.endPoint1))) {
            return false;
        }
        if (this.endPoint2 != other.endPoint2 && (this.endPoint2 == null || !this.endPoint2.equals(other.endPoint2))) {
            return false;
        }
        return true;
    }

    public Direction getDirection() {
        if (endPoint1.x == endPoint2.x) {
            if (endPoint1.y < endPoint2.y) return Direction.S;
            return Direction.N;
        }
        if (endPoint1.x < endPoint2.x) return Direction.E;
        return Direction.W;
    }
    
    
}
