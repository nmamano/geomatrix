/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.HorizontalSegment;
import geomatrix.gridplane.Line;
import geomatrix.gridplane.VerticalSegment;
import java.util.Collection;

/**
 *
 * @author Nil
 */
public class AreaElements {
    public boolean valid;
    public Collection<Cell> containedCells;
    public Collection<GridPoint> vertexes;
    public Collection<Line> lines;
    public Collection<VerticalSegment> verticalSegments;
    public Collection<HorizontalSegment> horizontalSegments;

    public AreaElements(boolean valid, Collection<Cell> containedCells, Collection<GridPoint> vertexes, Collection<Line> lines, Collection<VerticalSegment> verticalSegments, Collection<HorizontalSegment> horizontalSegments) {
        this.valid = valid;
        this.containedCells = containedCells;
        this.vertexes = vertexes;
        this.lines = lines;
        this.verticalSegments = verticalSegments;
        this.horizontalSegments = horizontalSegments;
    }

}
