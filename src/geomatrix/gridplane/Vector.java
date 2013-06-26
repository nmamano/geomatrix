
package geomatrix.gridplane;

import geomatrix.utils.Direction;
import java.awt.Point;

/**
 *
 * @author Nil
 */
public class Vector {
    public GridPoint origin;
    public int length;
    public Direction direction;

    public Vector(GridPoint origin, int length, Direction direction) {
        this.origin = origin;
        this.length = length;
        this.direction = direction;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.origin != null ? this.origin.hashCode() : 0);
        hash = 29 * hash + this.length;
        hash = 29 * hash + (this.direction != null ? this.direction.hashCode() : 0);
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
        final Vector other = (Vector) obj;
        if (this.origin != other.origin && (this.origin == null || !this.origin.equals(other.origin))) {
            return false;
        }
        if (this.length != other.length) {
            return false;
        }
        if (this.direction != other.direction) {
            return false;
        }
        return true;
    }

    public GridPoint getTerminalPoint() {
        GridPoint terminalPoint = origin.copy();
        if (direction == Direction.N) terminalPoint.y -= length;
        else if (direction == Direction.S) terminalPoint.y += length;
        else if (direction == Direction.W) terminalPoint.x -= length;
        else terminalPoint.x += length;
        return terminalPoint;
    }

}
