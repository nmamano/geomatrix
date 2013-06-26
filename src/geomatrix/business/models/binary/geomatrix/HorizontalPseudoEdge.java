/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.HorizontalSegment;
import geomatrix.gridplane.Rectangle;
import geomatrix.utils.Interval;
import geomatrix.gridplane.Vector;
import java.awt.Point;
import java.util.logging.Logger;

/**
 *
 * @author Nil
 */
class HorizontalPseudoEdge extends HorizontalSegment {

    public HorizontalPseudoEdge(int y, Interval xInterval) {
        super(y, xInterval);
    }

    Rectangle getConflictingArea(Rectangle rectangle) {
        GridPoint bottomRight, topLeft;
        bottomRight = new GridPoint(getXInterval().high, getY());
        topLeft = new GridPoint(
                getXInterval().low - rectangle.getWidth() + 1,
                getY() - rectangle.getHeight() + 1);
        return new Rectangle(topLeft, bottomRight);
    }
    
}
