
package geomatrix.gridplane;

import geomatrix.utils.Axis;
import geomatrix.utils.Interval;

public class HorizontalSegment extends Segment {

    public HorizontalSegment(int y, int x1, int x2) {
        this(y, new Interval(x1, x2));
    }
    
    public HorizontalSegment(int y, Interval xInterval) {
        super(y, xInterval, Axis.Horizontal);
    }

    public Interval getXInterval() {
        return super.range;
    }
    
    public int getY() {
        return super.fixed;
    }

}
