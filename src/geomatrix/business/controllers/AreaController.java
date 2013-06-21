/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.GridPoint;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import geomatrix.presentation.swing.GridCell;
import geomatrix.utils.Line;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class AreaController extends BusinessController {
    
    public AreaController(JAXBDataController data) {
        super(data);

    }

    /**
     * Returns all the lines in the plane such that they have a senar number of
     * 'points' in them. In a valid area, there are none of those.
     * @param points
     * @return 
     */
    public List<Line> getInvalidLines(Set<Point> points) {
        Set<GridPoint> vertexs = new HashSet<GridPoint>();
        for (Point p : points) {
            vertexs.add(new GridPoint(p.x, p.y));
        }
        return Geomatrix.getInvalidLines(vertexs);
    }

    public boolean isValidArea(Set<Point> points) {
        return getInvalidLines(points).isEmpty();
    }

    public List<GridCell> getContainedCells(Set<Point> points) {
        List<GridCell> containedCells = new ArrayList<GridCell>();
        List<Point> vertexs = new ArrayList<Point> (points);
        if (isValidArea(points)) {
            Geomatrix area = new Geomatrix(vertexs);
            for (Cell cell : area) {
                containedCells.add(new GridCell(cell.x, cell.y));
            }
        }
        return containedCells;        
    }
    
}
