
package geomatrix.gridplane;

import geomatrix.utils.Axis;
import geomatrix.utils.Interval;

/**
 *
 * @author Nil
 */
public class VerticalSegment extends Segment implements Comparable {

    public VerticalSegment(int x, int y1, int y2) {
        this(x, new Interval(y1, y2));
    }
    
    public VerticalSegment(int x, Interval yInterval) {
        super(x, yInterval, Axis.Vertical);
    }

    /**
     * @return the x
     */
    public int getX() {
        return super.fixed;
    }

    /**
     * @return the yInterval
     */
    public Interval getYInterval() {
        return super.range;
    }
    
}
