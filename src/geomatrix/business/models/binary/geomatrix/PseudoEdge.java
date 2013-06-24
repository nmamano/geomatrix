/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.GridPoint;
import geomatrix.business.models.binary.Rectangle;
import geomatrix.utils.Interval;
import geomatrix.utils.Segment;
import java.awt.Point;

/**
 *
 * @author Nil
 */
class PseudoEdge extends Segment {

    public PseudoEdge(GridPoint endPoint1, GridPoint endPoint2) {
        super(new Point(endPoint1.x, endPoint1.y), new Point(endPoint2.x, endPoint2.y));
    }

    Rectangle getConflictingArea(Rectangle rectangle) {
        GridPoint bottomRight, topLeft;
        if (isVerticalSegment()) {
            bottomRight = new GridPoint(getX(), getInterval().high);
            topLeft = new GridPoint(getX() - rectangle.getWidth() + 1,
                    getInterval().low - rectangle.getHeight() + 1);
        }
        else {
            bottomRight = new GridPoint(getInterval().high, getY());
            topLeft = new GridPoint(
                    getInterval().low - rectangle.getWidth() + 1,
                    getY() - rectangle.getHeight() + 1);
        }
        return new Rectangle(topLeft, bottomRight);
    }

    int getX() {
        assert(isVerticalSegment());
        return endPoint1.x;
    }

    boolean isVerticalSegment() {
        return endPoint1.x == endPoint2.x;
    }

    int getY() {
        assert(!isVerticalSegment());
        return endPoint1.y;
    }

    Interval getInterval() {
        if (isVerticalSegment()) {
            return new Interval(endPoint1.y, endPoint2.y);
        }
        else {
            return new Interval(endPoint1.x, endPoint2.x);
        }
    }
    
}
