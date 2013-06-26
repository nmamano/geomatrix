/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.events;

import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Line;
import java.awt.Color;
import java.util.Collection;
import manticore.Event;

/**
 *
 * @author Nil
 */
public class areaModified implements Event {

    public int areaID;
    public Collection<Cell> containedCells;
    public Collection<GridPoint> vertex;
    public Collection<Line> invalidLines;

    public areaModified(int areaID, Collection<Cell> containedCells, Collection<GridPoint> vertex, Collection<Line> invalidLines) {
        this.areaID = areaID;
        this.containedCells = containedCells;
        this.vertex = vertex;
        this.invalidLines = invalidLines;
    }
    
}
