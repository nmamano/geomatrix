/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.Point;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import java.util.HashSet;
import java.util.Set;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class AreaController extends BusinessController {
    
    private Set<Point> area1Vertexs;
    private Set<Point> area2Vertexs;
    private Set<Point> area3Vertexs;
    public AreaController(JAXBDataController data) {
        super(data);
        this.area1Vertexs = new HashSet<Point>();
        this.area2Vertexs = new HashSet<Point>();
        this.area3Vertexs = new HashSet<Point>();
    }
    
    public boolean areaContainsVertex(int areaNumber, java.awt.Point vertex) {
        Point point = new Point(vertex.x, vertex.y);
        return getAreaVertexs(areaNumber).contains(point);  
    }

    public void removeVertexFromArea(int areaNumber, java.awt.Point vertex) {
        Point point = new Point(vertex.x, vertex.y);
        getAreaVertexs(areaNumber).remove(point);
    }

    public void addVertexToArea(int areaNumber, java.awt.Point vertex) {
        Point point = new Point(vertex.x, vertex.y);
        getAreaVertexs(areaNumber).add(point);
    }

    private Set<Point> getAreaVertexs(int areaNumber) {
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        if (areaNumber == 1) return area1Vertexs;
        if (areaNumber == 2) return area2Vertexs;
        return area3Vertexs;
    }

    public HashSet<java.awt.Point> getVertexs(int i) {
        HashSet<java.awt.Point> vertexs = new HashSet<java.awt.Point>();
        for (Point p : getAreaVertexs(i)) {
            vertexs.add(new java.awt.Point(p.x, p.y));
        }
        return vertexs;
    }
    
}
