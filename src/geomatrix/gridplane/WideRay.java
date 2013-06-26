
package geomatrix.gridplane;

import geomatrix.gridplane.Cell;
import geomatrix.utils.Direction;

/**
 *
 * @author Nil
 */
public class WideRay {
    public Cell origin;
    public Direction direction;

    public WideRay(Cell origin, Direction direction) {
        this.origin = origin;
        this.direction = direction;
    }

    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.origin != null ? this.origin.hashCode() : 0);
        hash = 37 * hash + (this.direction != null ? this.direction.hashCode() : 0);
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
        final WideRay other = (WideRay) obj;
        if (this.origin != other.origin && (this.origin == null || !this.origin.equals(other.origin))) {
            return false;
        }
        if (this.direction != other.direction) {
            return false;
        }
        return true;
    }
    
    
}
