/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.events;

import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Line;
import geomatrix.gridplane.UnitarySegment;
import java.awt.Color;
import java.util.Collection;
import manticore.Event;

/**
 *
 * @author Nil
 */
public class AreaModifiedEvent implements Event {

    public int areaID;
    public boolean valid;
    public Collection<Cell> containedCells;
    public Collection<GridPoint> vertexes;
    public Collection<Line> invalidLines;

    public AreaModifiedEvent(int areaID, boolean valid, Collection<Cell> containedCells, Collection<GridPoint> vertex, Collection<Line> invalidLines) {
        this.areaID = areaID;
        this.valid = valid;
        this.containedCells = containedCells;
        this.vertexes = vertex;
        this.invalidLines = invalidLines;
    }

    
}
