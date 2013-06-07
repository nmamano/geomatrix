/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Cell;

/**
 *
 * @author Nil
 */
class RightWideRay {
   public Cell origin;

    public RightWideRay(Cell origin) {
        this.origin = origin;
    }    

    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.origin != null ? this.origin.hashCode() : 0);
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
        final RightWideRay other = (RightWideRay) obj;
        if (this.origin != other.origin && (this.origin == null || !this.origin.equals(other.origin))) {
            return false;
        }
        return true;
    }
    
    
}
