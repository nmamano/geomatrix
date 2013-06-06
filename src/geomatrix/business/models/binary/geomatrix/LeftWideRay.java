
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Cell;

/**
 * Represents a wide ray of the GridPlane that propagates the left.
 * @author Nil
 */
public class LeftWideRay {
    public Cell origin;

    public LeftWideRay(Cell origin) {
        this.origin = origin;
    }

    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.origin != null ? this.origin.hashCode() : 0);
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
        final LeftWideRay other = (LeftWideRay) obj;
        if (this.origin != other.origin && (this.origin == null || !this.origin.equals(other.origin))) {
            return false;
        }
        return true;
    }
    
}
