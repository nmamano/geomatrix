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
class HorizontalPseudoEdge extends Segment {

    public HorizontalPseudoEdge(GridPoint endPoint1, GridPoint endPoint2) {
        super(new Point(endPoint1.x, endPoint1.y), new Point(endPoint2.x, endPoint2.y));
    }

    Rectangle getConflictingArea(Rectangle rectangle) {
        GridPoint bottomRight, topLeft;
        bottomRight = new GridPoint(getInterval().high, getY());
        topLeft = new GridPoint(
                getInterval().low - rectangle.getWidth() + 1,
                getY() - rectangle.getHeight() + 1);
        return new Rectangle(topLeft, bottomRight);
    }

    int getY() {
        return endPoint1.y;
    }

    Interval getInterval() {
        return new Interval(endPoint1.x, endPoint2.x);
    }
    
}
