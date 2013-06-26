/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.utils.Interval;
import geomatrix.utils.Segment;
import java.awt.Point;

/**
 *
 * @author Nil
 */
class VerticalPseudoEdge extends Segment {

    public VerticalPseudoEdge(GridPoint endPoint1, GridPoint endPoint2) {
        super(new Point(endPoint1.x, endPoint1.y), new Point(endPoint2.x, endPoint2.y));
    }

    Rectangle getConflictingArea(Rectangle rectangle) {
        GridPoint bottomRight, topLeft;
        bottomRight = new GridPoint(getX(), getInterval().high);
        topLeft = new GridPoint(getX() - rectangle.getWidth() + 1,
                getInterval().low - rectangle.getHeight() + 1);

        return new Rectangle(topLeft, bottomRight);
    }

    int getX() {
        return endPoint1.x;
    }

    Interval getInterval() {
        return new Interval(endPoint1.y, endPoint2.y);
    }
    
}
