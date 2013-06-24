/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.GridPoint;
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
    
}
