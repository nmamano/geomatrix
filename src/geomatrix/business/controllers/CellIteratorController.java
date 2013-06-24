/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import java.awt.Point;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Nil
 */
public class CellIteratorController {
    Geomatrix geomatrix;
    Iterator<Cell> iterator;

    public CellIteratorController(Set<Point> vertexs) {
        geomatrix = Geomatrix.buildGeomatrixFromPoints(vertexs);
        iterator = geomatrix.iterator();
    }

    public void reset() {
        iterator = geomatrix.iterator();
    }

    public boolean hasMore() {
        return iterator.hasNext();
    }

    public Point getNext() {
        Cell next = iterator.next();
        return new Point(next.x, next.y);
    }

    
    
}
