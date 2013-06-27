/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.events.AreaDisplayedEvent;
import geomatrix.business.events.AreaSelectedEvent;
import geomatrix.business.events.AreasModifiedEvent;
import geomatrix.business.models.binary.geomatrix.Geomatrix;
import geomatrix.business.models.binary.geomatrix.PointSet;
import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.HorizontalSegment;
import geomatrix.gridplane.Line;
import geomatrix.gridplane.Rectangle;
import geomatrix.gridplane.VerticalSegment;
import geomatrix.presentation.swing.AreaElements;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class GeomatrixController extends BusinessController {
    
    private int selectedID;
    private PointSet[] points;
    private boolean[] showBoundingRectangle;
    private boolean[] showAreas;
    
    public GeomatrixController(JAXBDataController data) {
        super(data);
        
        points = new PointSet[4]; //position 0 wasted
        showBoundingRectangle = new boolean[4]; //position 0 wasted
        showAreas = new boolean[4]; //position 0 wasted
        
        showAreas[1] = true;
        selectedID = 1;
        
        for (int i = 1; i < 4; ++i) {
            points[i] = new PointSet(new HashSet<GridPoint>());
        }
    }

    public void setSelected(int areaID) {
        selectedID = areaID;
        notifyAreaSelected(areaID);
        notifyAreasModified();
    }

    public void setDisplayed(int areaID, boolean displayed) {
        showAreas[areaID] = displayed;
        notifyAreasModified(); 
    }

    public void cloneArea(int originalID, int cloneID) {
        points[cloneID] = points[originalID].copy();
        notifyAreasModified();
    }
    
    public void unionArea(int originalID, int unitedID) {
        Geomatrix original = Geomatrix.buildGeomatrixFromPoints(points[originalID].points);
        Geomatrix other = Geomatrix.buildGeomatrixFromPoints(points[unitedID].points);
        original.union(other);
        points[originalID].setPoints(original.getVertexs());
        notifyAreasModified();
    }
       
    public void resetArea(int areaID) {
        points[areaID].setPoints(new HashSet<GridPoint>());
        notifyAreasModified();
    }
    
    public void differenceArea(int originalID, int otherID) {
        Geomatrix original = Geomatrix.buildGeomatrixFromPoints(points[originalID].points);
        Geomatrix other = Geomatrix.buildGeomatrixFromPoints(points[otherID].points);
        original.difference(other);
        points[originalID].setPoints(original.getVertexs());
        notifyAreasModified();
    }

    public void intersectionArea(int originalID, int otherID) {
        Geomatrix original = Geomatrix.buildGeomatrixFromPoints(points[originalID].points);
        Geomatrix other = Geomatrix.buildGeomatrixFromPoints(points[otherID].points);
        original.intersection(other);
        points[originalID].setPoints(original.getVertexs());
        notifyAreasModified();
    }
    
    public void symmetricDifferenceArea(int originalID, int otherID) {
        Geomatrix original = Geomatrix.buildGeomatrixFromPoints(points[originalID].points);
        Geomatrix other = Geomatrix.buildGeomatrixFromPoints(points[otherID].points);
        original.symmetricDifference(other);
        points[originalID].setPoints(original.getVertexs());
        notifyAreasModified();
    }
    
    public void rectangleDecomposition(int areaID) {
        throw new UnsupportedOperationException("No algorithm has been found for this problem yet.");
    }

    public void showBoundingRectangle(int areaID, boolean show) {
        showBoundingRectangle[areaID] = show;
        notifyAreasModified();
    }
    
    public void translateArea(int areaID, int xTranslate, int yTranslate) {
        Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);
        geomatrix.translation(new GridPoint(xTranslate, yTranslate));
        points[areaID].setPoints(geomatrix.getVertexs());
        notifyAreasModified();
    }

    public void rotateArea(int areaID, int degrees, int gridWidth, int gridHeight) {
        Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);
        if (degrees == 90) {
            geomatrix.rotation(90);
            geomatrix.translation(new GridPoint(0, -gridHeight)); //reallocation to visible zone
        }
        else if (degrees == 180) {
            geomatrix.rotation(180);
            geomatrix.translation(new GridPoint(gridWidth, -gridHeight)); //reallocation to visible zone
        }
        else {
            geomatrix.rotation(270);
            geomatrix.translation(new GridPoint(gridWidth, 0)); //reallocation to visible zone
        }
        points[areaID].setPoints(geomatrix.getVertexs());
        notifyAreasModified();
    }

    public void reflectVertical(int areaID, int gridHeight) {
        Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);
        
        geomatrix.verticalReflection();
        geomatrix.translation(new GridPoint(0, -gridHeight));
        
        points[areaID].setPoints(geomatrix.getVertexs());
        notifyAreasModified();
    }

    public void reflectHorizontal(int areaID, int gridWidth) {
        Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);

        geomatrix.horizontalReflection();
        geomatrix.translation(new GridPoint(gridWidth, 0));
        
        points[areaID].setPoints(geomatrix.getVertexs());
        notifyAreasModified();
    }
    

    private void notifyAreaSelected(int areaID) {
        notify(new AreaSelectedEvent(areaID));
    }  
    
    private void notifyAreasModified() {
        AreaElements area1 = computeAreaElements(1);
        AreaElements area2 = computeAreaElements(2);
        AreaElements area3 = computeAreaElements(3);
        notify(new AreasModifiedEvent(area1, area2, area3));
    }

    private AreaElements computeAreaElements(int ID) {
        if (showAreas[ID]) {
            List<VerticalSegment> verticalSegments = computeVerticalSegments(ID);
            List<HorizontalSegment> horizontalSegments = computeHorizontalSegments(ID);
            return new AreaElements(points[ID].isValidArea(), points[ID].getContainedCells(), points[ID].points, points[ID].getInvalidLines(), verticalSegments, horizontalSegments);
        }
        else {
            return new AreaElements(points[ID].isValidArea(), new ArrayList<Cell>(), new ArrayList<GridPoint>(), new ArrayList<Line>(), new ArrayList<VerticalSegment>(), new ArrayList<HorizontalSegment>());
        }
    }

    List<VerticalSegment> computeVerticalSegments(int areaID) {
        List<VerticalSegment> verticalSegments = new ArrayList<VerticalSegment>();
        if (points[areaID].isValidArea() && showBoundingRectangle[areaID]) {
            Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);
            Rectangle r = geomatrix.getBoundingRectangle();
            verticalSegments.add(r.getLeftEdge());
            verticalSegments.add(r.getRightEdge());
        }
        return verticalSegments;
    }
    
    List<HorizontalSegment> computeHorizontalSegments(int areaID) {
        List<HorizontalSegment> horizontalSegments = new ArrayList<HorizontalSegment>();
        if (points[areaID].isValidArea() && showBoundingRectangle[areaID]) {
            Geomatrix geomatrix = Geomatrix.buildGeomatrixFromPoints(points[areaID].points);
            Rectangle r = geomatrix.getBoundingRectangle();
            horizontalSegments.add(r.getBottomEdge());
                horizontalSegments.add(r.getTopEdge());
        }
        return horizontalSegments;
    }















    
    
    
}
