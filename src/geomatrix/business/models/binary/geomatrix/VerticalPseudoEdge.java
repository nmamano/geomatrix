/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.utils.Interval;
import geomatrix.gridplane.Vector;
import geomatrix.gridplane.VerticalSegment;
import java.awt.Point;

/**
 *
 * @author Nil
 */
class VerticalPseudoEdge extends VerticalSegment {

    public VerticalPseudoEdge(int x, Interval yInterval) {
        super(x, yInterval);
    }

    Rectangle getConflictingArea(Rectangle rectangle) {
        GridPoint bottomRight, topLeft;
        bottomRight = new GridPoint(getX(), getYInterval().high);
        topLeft = new GridPoint(getX() - rectangle.getWidth() + 1,
                getYInterval().low - rectangle.getHeight() + 1);

        return new Rectangle(topLeft, bottomRight);
    }

}
