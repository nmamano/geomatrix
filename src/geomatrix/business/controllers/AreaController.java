/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.GridPoint;
import geomatrix.business.models.binary.Rectangle;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import geomatrix.presentation.swing.GridCell;
import geomatrix.utils.Line;
import geomatrix.utils.Segment;
import java.awt.Point;
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
        Set<GridPoint> vertexs = pointsToGridPoints(points);
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
    
    private Set<GridPoint> pointsToGridPoints(Collection<Point> points) {
        Set<GridPoint> gridPoints = new HashSet<GridPoint>();
        for (Point p : points) {
            gridPoints.add(new GridPoint(p.x, p.y));
        }
        return gridPoints;
    }
    
    private Set<Point> gridPointsToPoints(Collection<GridPoint> gridPoints) {
        Set<Point> points = new HashSet<Point>();
        for (GridPoint p : gridPoints) {
            points.add(new Point(p.x, p.y));
        }
        return points;
    }

    public Set<Point> union(Set<Point> area1Vertexs, Set<Point> area2Vertexs) {
        
        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = new Geomatrix(area1Vertexs);
        Geomatrix area2 = new Geomatrix(area2Vertexs);
        area1.union(area2);
        return gridPointsToPoints(area1.getVertexs());    
    }
        
    public Set<Point> intersection(Set<Point> area1Vertexs, Set<Point> area2Vertexs) {

        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = new Geomatrix(area1Vertexs);
        Geomatrix area2 = new Geomatrix(area2Vertexs);
        area1.intersection(area2);
        return gridPointsToPoints(area1.getVertexs()); 
    }

    public Set<Point> difference(Set<Point> area1Vertexs, Set<Point> area2Vertexs) {

        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = new Geomatrix(area1Vertexs);
        Geomatrix area2 = new Geomatrix(area2Vertexs);
        area1.difference(area2);
        return gridPointsToPoints(area1.getVertexs()); 
    }

    public Set<Point> symmetricDifference(Set<Point> area1Vertexs, Set<Point> area2Vertexs) {

        assert(isValidArea(area1Vertexs) && isValidArea(area2Vertexs));
        
        Geomatrix area1 = new Geomatrix(area1Vertexs);
        Geomatrix area2 = new Geomatrix(area2Vertexs);
        area1.symmetricDifference(area2);
        return gridPointsToPoints(area1.getVertexs()); 
    }

    public List<Segment> getBoundingRectangleEdges(Set<Point> vertexs) {
        assert(isValidArea(vertexs));
        
        Geomatrix area = new Geomatrix(vertexs);
        Rectangle r = area.getBoundingRectangle();
        return getEdges(r);
        
    }

    private List<Segment> getEdges(Rectangle r) {
        List<Segment> edges = new ArrayList<Segment>();
        edges.add(new Segment(new Point(r.topLeft.x, r.topLeft.y), new Point(r.topLeft.x, r.bottomRight.y)));
        edges.add(new Segment(new Point(r.topLeft.x, r.topLeft.y), new Point(r.bottomRight.x, r.topLeft.y)));
        edges.add(new Segment(new Point(r.topLeft.x, r.bottomRight.y), new Point(r.bottomRight.x, r.bottomRight.y)));
        edges.add(new Segment(new Point(r.bottomRight.x, r.topLeft.y), new Point(r.bottomRight.x, r.bottomRight.y)));
        return edges;
    }

    public Set<Point> translate(Set<Point> vertexs, int xTranslate, int yTranslate) {

        assert(isValidArea(vertexs));
        
        Geomatrix area = new Geomatrix(vertexs);
        area.translation(new GridPoint(xTranslate, yTranslate));
        return gridPointsToPoints(area.getVertexs()); 
    }

    public Set<Point> rotate90Degrees(Set<Point> vertexs) {

        assert(isValidArea(vertexs));
        
        Geomatrix area = new Geomatrix(vertexs);
        area.rotation(90);
        return gridPointsToPoints(area.getVertexs());
    }

    public Set<Point> reflectVertical(Set<Point> vertexs) {

        assert(isValidArea(vertexs));
        
        Geomatrix area = new Geomatrix(vertexs);
        area.verticalReflection();
        return gridPointsToPoints(area.getVertexs());
    }

    public Set<Point> reflectHorizontal(Set<Point> vertexs) {

        assert(isValidArea(vertexs));
        
        Geomatrix area = new Geomatrix(vertexs);
        area.horizontalReflection();
        return gridPointsToPoints(area.getVertexs());
    }
}
