
package geomatrix.utils;

/**
 * Represents an open numeric interval.
 * The interval goes from 'low' to 'high'.
 * @author Nil
 */
public class Interval {
    public int low;
    public int high;

    /**
     * Constructor.
     * Pre: end1 != end2
     * @param end1 one of the ends of the interval.
     * @param end2 the other end of the interval.
     */
    public Interval(int end1, int end2) {
        assert(end1 != end2);
        if (end1 < end2) {
            low = end1;
            high = end2;
        }
        else {
            low = end2;
            high = end1;
        }
    }

    /**
     * Returns if a number 'number' is contained in the interval.
     * @param number the number to be tested to see if it is contained.
     * @return true if number is contained in this interval.
     */
    public boolean contains(int number) {
        return low < number && high > number;
        //The ends are not contained in the interval
    }
    
    /**
     * Returns if an interval is contained in the interval.
     * @param other the interval to be tested to see if it is contained.
     * @return true if interval is contained in this interval.
     */
    public boolean contains(Interval other) {
        return low <= other.low && high >= other.high;
    }
    
    /**
     * Returns whether this interval and other overlap.
     * Two intervals overlap if there exists a point contained in both.
     * @param other the interval tested to see if it overlaps with this
     * interval.
     * @return true if this interval and other overlap.
     */
    public boolean overlaps(Interval other) {
        if (contains(other.high) || contains(other.low) ||
                other.contains(low)) return true;
        //it is not necessary to check whether other contains high
        return false;
    }
    
    /**
     * Returns the distance between this interval and other.
     * The distance is 0 if the intervals overlap, or the difference between the
     * closest ends otherwise.
     * @param other
     * @return 
     */
    public int distance(Interval other) {
        if (overlaps(other)) return 0;
        else if (low < other.low) return other.low - high;
        else return low - other.high;
    }
    
    /**
     * Returns whether an interval is consistent.
     * For an interval to be consistent, its low value must be smaller than its
     * high value.
     * @return true if the interval is consistent. false otherwise.
     */
    public boolean valid() {
        return low < high;
    }
    
    /**
     * In netbeans we trust.
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.low;
        hash = 83 * hash + this.high;
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
        final Interval other = (Interval) obj;
        if (this.low != other.low) {
            return false;
        }
        if (this.high != other.high) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + low + ".." + high + ')';
    }

    public int size() {
        return high - low;
    }
    
    
}
