/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.gridplane.Cell;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import geomatrix.gridplane.GridPoint;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Nil
 */
public class CellIteratorController {
    Geomatrix geomatrix;
    Iterator<Cell> iterator;

    public CellIteratorController(Set<GridPoint> vertexs) {
        geomatrix = Geomatrix.buildGeomatrixFromPoints(vertexs);
        iterator = geomatrix.iterator();
    }

    public void reset() {
        iterator = geomatrix.iterator();
    }

    public boolean hasMore() {
        return iterator.hasNext();
    }

    public Cell getNext() {
        return iterator.next();
    }

    
    
}
