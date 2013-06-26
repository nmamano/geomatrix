/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Line;
import geomatrix.utils.Axis;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Nil
 */
public class PointSet {
    HashSet<GridPoint> points;
    
    public Collection<Line> getInvalidLines() {
        HashSet<Line> lines = new HashSet<Line>();
        
        for (GridPoint point : points) {
            Line verticalLine = new Line(point.x, Axis.Vertical);
            if (lines.contains(verticalLine))
                lines.remove(verticalLine);
            else lines.add(verticalLine);
            
            Line horizontalLine = new Line(point.y, Axis.Horizontal);
            if (lines.contains(horizontalLine))
                lines.remove(horizontalLine);
            else lines.add(horizontalLine);
        }

        return lines;        
    }
    
    public boolean isValidArea() {
        return getInvalidLines().isEmpty();
    }
    
    public Geomatrix getGeomatrix() {
        return Geomatrix.buildGeomatrixFromPoints(points);
    }
}
