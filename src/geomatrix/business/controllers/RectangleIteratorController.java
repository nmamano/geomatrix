/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Nil
 */
public class RectangleIteratorController {
    Geomatrix geomatrix;
    Rectangle rectangle;
    Iterator<GridPoint> iterator;
    private static final int STARTING_WIDTH = 4;
    private static final int STARTING_HEIGHT = 4;
    
    public RectangleIteratorController(Set<GridPoint> vertexs) {
        geomatrix = Geomatrix.buildGeomatrixFromPoints(vertexs);
        setRectangle(STARTING_WIDTH, STARTING_HEIGHT);
        iterator = geomatrix.iterator(rectangle);
    }
        
    public void reset() {
        iterator = geomatrix.iterator(rectangle);
    }

    public boolean hasMore() {
        return iterator.hasNext();
    }

    public GridPoint getNext() {
        return iterator.next();
    }

    public final void setRectangle(int width, int height) {
        rectangle = new Rectangle(new GridPoint(0,0), new GridPoint(width,height));
    }
    
}
