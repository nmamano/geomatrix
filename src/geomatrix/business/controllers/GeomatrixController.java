/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import geomatrix.gridplane.Line;
import geomatrix.gridplane.Segment;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class GeomatrixController extends BusinessController {
    
    public GeomatrixController(JAXBDataController data) {
        super(data);

    }

    /**
     * Returns all the lines in the plane such that they have a senar number of
     * 'points' in them. In a valid area, there are none of those.
     * @param points
     * @return 
     */
    public List<Line> getInvalidLines(Set<GridPoint> points) {
        return Geomatrix.getInvalidLines(points);
    }

    public boolean isValidArea(Set<GridPoint> points) {
        return getInvalidLines(points).isEmpty();
    }

    public List<Cell> getContainedCells(Set<GridPoint> points) {
        List<Cell> containedCells = new ArrayList<Cell>();
        if (isValidArea(points)) {
            Geomatrix area = Geomatrix.buildGeomatrixFromPoints(points);
            for (Cell cell : area) {
                containedCells.add(new Cell(cell.x, cell.y));
            }
        }
        return containedCells;        
    }

    public Set<GridPoint> union(Set<GridPoint> area1Vertexs, Set<GridPoint> area2Vertexs) {      
        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = Geomatrix.buildGeomatrixFromPoints(area1Vertexs);
        Geomatrix area2 = Geomatrix.buildGeomatrixFromPoints(area2Vertexs);
        area1.union(area2);
        return new HashSet(area1.getVertexs());    
    }
        
    public Set<GridPoint> intersection(Set<GridPoint> area1Vertexs, Set<GridPoint> area2Vertexs) {
        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = Geomatrix.buildGeomatrixFromPoints(area1Vertexs);
        Geomatrix area2 = Geomatrix.buildGeomatrixFromPoints(area2Vertexs);
        area1.intersection(area2);
        return new HashSet(area1.getVertexs()); 
    }

    public Set<GridPoint> difference(Set<GridPoint> area1Vertexs, Set<GridPoint> area2Vertexs) {
        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = Geomatrix.buildGeomatrixFromPoints(area1Vertexs);
        Geomatrix area2 = Geomatrix.buildGeomatrixFromPoints(area2Vertexs);
        area1.difference(area2);
        return new HashSet(area1.getVertexs()); 
    }

    public Set<GridPoint> symmetricDifference(Set<GridPoint> area1Vertexs, Set<GridPoint> area2Vertexs) {
        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = Geomatrix.buildGeomatrixFromPoints(area1Vertexs);
        Geomatrix area2 = Geomatrix.buildGeomatrixFromPoints(area2Vertexs);
        area1.symmetricDifference(area2);
        return new HashSet(area1.getVertexs()); 
    }

    public Collection<Segment> getBoundingRectangleEdges(Set<GridPoint> vertexs) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = Geomatrix.buildGeomatrixFromPoints(vertexs);
        Rectangle r = area.getBoundingRectangle();
        return r.getEdges();
        
    }

    public Set<GridPoint> translate(Set<GridPoint> vertexs, int xTranslate, int yTranslate) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = Geomatrix.buildGeomatrixFromPoints(vertexs);
        area.translation(new GridPoint(xTranslate, yTranslate));
        return new HashSet(area.getVertexs()); 
    }

    public Set<GridPoint> rotate90Degrees(Set<GridPoint> vertexs) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = Geomatrix.buildGeomatrixFromPoints(vertexs);
        area.rotation(90);
        return new HashSet(area.getVertexs());
    }

    public Set<GridPoint> reflectVertical(Set<GridPoint> vertexs) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = Geomatrix.buildGeomatrixFromPoints(vertexs);
        area.verticalReflection();
        return new HashSet(area.getVertexs());
    }

    public Set<GridPoint> reflectHorizontal(Set<GridPoint> vertexs) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = Geomatrix.buildGeomatrixFromPoints(vertexs);
        area.horizontalReflection();
        return new HashSet(area.getVertexs());
    }
}
