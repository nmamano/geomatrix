
package geomatrix.business.models.binary;

import geomatrix.utils.Interval;

/**
 * Represents a vertical segment.
 * @author Nil
 */
public class VerticalSegment {
    int x;
    Interval yInterval;

    public VerticalSegment(int x, int yLow, int yHigh) {
        this.x = x;
        this.yInterval = new Interval(yLow, yHigh);
    }
    
    public VerticalSegment(int x, Interval yInterval) {
        this.x = x;
        this.yInterval = yInterval;
    }

    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.x;
        hash = 89 * hash + (this.yInterval != null ? this.yInterval.hashCode() : 0);
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
