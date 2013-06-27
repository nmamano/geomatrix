/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.gridplane.UnitarySegment;
import geomatrix.business.controllers.RectangleIteratorController;
import geomatrix.gridplane.Line;
import geomatrix.business.controllers.GeomatrixController;
import geomatrix.business.controllers.CellIteratorController;
import geomatrix.business.events.AreaModifiedEvent;
import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.gridplane.Segment;
import geomatrix.utils.Direction;
import geomatrix.gridplane.Vector;
import geomatrix.utils.Axis;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import manticore.Debug;
import manticore.presentation.SwingController;
import manticore.presentation.annotation.Listen;


/**
 *
 * @author Nil
 */
public class MapPanel extends JPanel {

    private GeomatrixController geomatrixController;

    private int gridWidth;
    private int gridHeight;
    
    private static final int STARTING_GRID_WIDTH = 20;
    private static final int STARTING_GRID_HEIGHT = 20;
    
    private static final int CELL_PIXEL_LENGTH = 20;
    
    private static final Color BACKGROUND_GRID_COLOR = Color.white;
    private static final Color GRID_COLOR = Color.decode("#EEEEEE");
    private static final Color OVERLAPPED_LINES_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_LINES_COLOR = Color.BLACK;

    
    //??
    private boolean ignoreInput;   
    private RectangleIterationPanel rectangleIterationPanel;
    
    //the new data structs
    Combinations[][] cells;
    Combinations[][] verticalUnitarySegments;
    Combinations[][] horizontalUnitarySegments;
    Combinations[][] vertexes;
    private Set<Cell> iteredCells;
    
    /**
     * Initialization.
     * All areas have no vertexs.
     * All areas are displayed.
     * Area 1 is selected.
     * @param swingController 
     */
    public MapPanel(SwingController swingController) {
        
        //??
        //setIgnoreInput(false);
        
        //??
        //setPreferredSize(new Dimension(gridWidth*CELL_PIXEL_LENGTH,
        //                               gridHeight*CELL_PIXEL_LENGTH));
        
        this.geomatrixController = swingController.getBusinessController(GeomatrixController.class);

        this.addMouseListener(new SelectGridPointListener());
        setGridSize(STARTING_GRID_WIDTH, STARTING_GRID_HEIGHT);
        repaint();
        
    }
    
    private void setGridSize(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        cells = new Combinations[gridWidth][gridHeight];
        verticalUnitarySegments = new Combinations[gridWidth+1][gridHeight];
        horizontalUnitarySegments = new Combinations[gridWidth][gridHeight+1];
        vertexes = new Combinations[gridWidth+1][gridHeight+1];
        iteredCells = new HashSet<Cell>();
        setDataStructsEmpty();
    }
    
    private void setDataStructsEmpty() {
        for (int i = 0; i < gridWidth; ++i) {
            for (int j = 0; j < gridHeight; ++j) {
                cells[i][j] = Combinations.None;
                verticalUnitarySegments[i][j] = Combinations.None;
                horizontalUnitarySegments[i][j] = Combinations.None;
                vertexes[i][j] = Combinations.None;
            }
        }
        for (int i = 0; i < gridWidth; ++i) {
            horizontalUnitarySegments[i][gridHeight] = Combinations.None;
            vertexes[i][gridHeight] = Combinations.None;
        }
        for (int j = 0; j < gridHeight; ++j) {
            horizontalUnitarySegments[gridWidth][j] = Combinations.None;
            vertexes[gridWidth][j] = Combinations.None;            
        }
        vertexes[gridWidth][gridHeight] = Combinations.None;
    }

    @Listen(AreaModifiedEvent.class)
    public void areaModified(AreaModifiedEvent evt) {    
        updateDataStructs(evt);
        repaint();
    }

    private void updateDataStructs(AreaModifiedEvent evt) {
        updateCells(evt.containedCells, evt.areaID);
        updateUnitarySegments(evt.invalidLines, evt.areaID);
        updateVertexes(evt.vertexes, evt.areaID);
    }
    
    private void updateCells(Collection<Cell> containedCells, int areaID) {
            for (Cell cell : containedCells) {
            cells[cell.x][cell.y] = cells[cell.x][cell.y].add(areaID);
        }    
    }
    
    private void updateUnitarySegments(Collection<Line> invalidLines, int areaID) {
        for (Line line : invalidLines) {
            if (line.axis == Axis.Vertical) {
                Segment segment = new Segment(line.fixedCoordinate, new Interval(0, gridHeight), Axis.Vertical);
                Collection<UnitarySegment> unitarySegments = segment.breakIntoUnitarySegments();
                for (UnitarySegment unitarySegment : unitarySegments) {
                    verticalUnitarySegments[unitarySegment.topLeftEndPoint.x][unitarySegment.topLeftEndPoint.y].add(areaID);
                }
            }
            else {
                Segment segment = new Segment(line.fixedCoordinate, new Interval(0, gridWidth), Axis.Horizontal);
                Collection<UnitarySegment> unitarySegments = segment.breakIntoUnitarySegments();
                for (UnitarySegment unitarySegment : unitarySegments) {
                    horizontalUnitarySegments[unitarySegment.topLeftEndPoint.x][unitarySegment.topLeftEndPoint.y].add(areaID);
                }                
            }
        }    
    }
    
    private void updateVertexes(Collection<GridPoint> points, int areaID) {
        for (GridPoint point : points) {
            vertexes[point.x][point.y] = vertexes[point.x][point.y].add(areaID);
        }  
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_GRID_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Graphics2D g2D = (Graphics2D) g;
        Drawer.drawGrid(g2D, getWidth(), getHeight());
        paintAreas(g2D);
        paintInvalidLines(g2D);
        paintVertexs(g2D);
        try {
            paintIteredCells(g2D);
        }
        catch (java.util.ConcurrentModificationException e) {
            //ignore
            //this causes the automatic iteration display to disappear
            //at times while iterating, but it's better than a exception pop up
        }
        paintIteratingRectangle(g2D);
    }
    
    private void paintAreas(Graphics2D g) {
        for (int i = 0; i < gridWidth; ++i) {
            for (int j = 0; j < gridHeight; ++j) {
                Drawer.paintCell(new Cell(i,j), cells[i][j].getColor(), g);
            }
        }
    }
    
    private void paintVertexs(Graphics2D g) {
        for (int i = 0; i < gridWidth+1; ++i) {
            for (int j = 0; j < gridHeight+1; ++j) {
                Drawer.paintVertex(new GridPoint(i, j), vertexes[i][j].getColors(), g);
            }
        }
    }

    private void paintInvalidLines(Graphics2D g) {
        for (int i = 0; i < gridWidth; ++i) {
            for (int j = 0; j < gridHeight+1; ++j) {
                UnitarySegment segment = new UnitarySegment(new GridPoint(i, j), Axis.Horizontal);
                Drawer.paintUnitarySegment(segment, horizontalUnitarySegments[i][j].getColor(), g);
            }
        } 
        for (int i = 0; i < gridWidth+1; ++i) {
            for (int j = 0; j < gridHeight; ++j) {
                UnitarySegment segment = new UnitarySegment(new GridPoint(i, j), Axis.Vertical);
                Drawer.paintUnitarySegment(segment, verticalUnitarySegments[i][j].getColor(), g);
            }
        } 
    }
        
    /**
     * Sets the indicated area as selected.
     * If it was not displayed, it is displayed.
     * @param areaNumber the number of the area to be selected
     */
    void setSelected(int areaNumber) {
        
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        selectedAreaNumber = areaNumber;
        try {
            setDisplayed(areaNumber, true);
        }
        catch (HideSelectedAreaException e) {
            //this can't happen because the shouldBeDisplayed parameter is true.
            assert(false);
        }
    }

    /**
     * Displays or hides the indicated area according to 'shouldBeDisplayed'.
     * If the indicated area is already in the indicated state, nothing is done.
     * If the indicated area is selected, an exception is thrown.
     * @param areaNumber the number of the area to be displayed or hidden.
     * @param shouldBeDisplayed the boolean indicating if the area has to be
     * displayed or not.
     */
    void setDisplayed(int areaNumber, boolean shouldBeDisplayed)
        throws HideSelectedAreaException {
        
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        if (areaNumber == selectedAreaNumber && ! shouldBeDisplayed) {
            throw new HideSelectedAreaException();
        }
        getArea(areaNumber).isDisplayed = shouldBeDisplayed;
       
        repaint();
    }
    


    private PresentationArea getArea(int areaNumber) {
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        if (areaNumber == 1) return area1;
        if (areaNumber == 2) return area2;
        return area3;
    }

    private void updateMenusThatRequireValidAreaActivation(int modifiedAreaNumber) {
        if (geomatrixController.isValidArea(getArea(modifiedAreaNumber).vertexs)) {
            mainFrame.enableOperationsThatRequireValidArea(modifiedAreaNumber, true);
        }
        else {
            mainFrame.enableOperationsThatRequireValidArea(modifiedAreaNumber, false);
        }
    }
        
    void resetArea(int areaNumber) {
        getArea(areaNumber).vertexs = new HashSet<GridPoint>();
        
        updateMenusThatRequireValidAreaActivation(areaNumber);
        repaint();
    }

    void cloneArea(int destinationAreaNumber, int toBeClonedAreaNumber) {
        assert(destinationAreaNumber != toBeClonedAreaNumber);
        
        getArea(destinationAreaNumber).vertexs = new HashSet<GridPoint>();
        for (GridPoint vertex : getArea(toBeClonedAreaNumber).vertexs) {
            getArea(destinationAreaNumber).vertexs.add(vertex);
        }
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void unionArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(geomatrixController.isValidArea(destinationAreaVertexs) &&
               geomatrixController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = geomatrixController.union(destinationAreaVertexs,
                                                      otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }
    
    void intersectionArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(geomatrixController.isValidArea(destinationAreaVertexs) &&
               geomatrixController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = geomatrixController.intersection(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void differenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(geomatrixController.isValidArea(destinationAreaVertexs) &&
               geomatrixController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = geomatrixController.difference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void symmetricDifferenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(geomatrixController.isValidArea(destinationAreaVertexs) &&
               geomatrixController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = geomatrixController.symmetricDifference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }
   
    private boolean shouldPaintBoundingRectangle(int areaNumber) {
        return getArea(areaNumber).isDisplayed &&
               getArea(areaNumber).isBoundingRectangleDisplayed &&
               geomatrixController.isValidArea(getArea(areaNumber).vertexs);
    }

    void translateArea(int areaNumber, int xTranslate, int yTranslate) {
        assert(geomatrixController.isValidArea(getArea(areaNumber).vertexs));
        
        getArea(areaNumber).vertexs = geomatrixController.translate(
                getArea(areaNumber).vertexs, xTranslate, yTranslate);
        
        repaint();
    }

    /**
     * This method might give trouble if the grid is not squared.
     * Because of the reallocation. A rotated area will not necessarily fit
     * in the grid once rotated if its not squared.
     * @param areaNumber
     * @param degrees 
     */
    void rotateArea(int areaNumber, int degrees) {
        assert(geomatrixController.isValidArea(getArea(areaNumber).vertexs));
        assert(degrees == 90 || degrees == 180 || degrees == 270);
        
        rotate90AndReallocate(areaNumber);
        //rotating 180 equals rotating 90 twice
        if (degrees > 90) rotate90AndReallocate(areaNumber);
        //rotating 270 equals rotating 90 three times
        if (degrees > 180) rotate90AndReallocate(areaNumber);

        repaint();
    }

    private void rotate90AndReallocate(int areaNumber) {
        getArea(areaNumber).vertexs = geomatrixController.rotate90Degrees(
                getArea(areaNumber).vertexs);
        translateArea(areaNumber, gridWidth, 0);
    }

    void reflectVertical(int areaNumber) {
        assert(geomatrixController.isValidArea(getArea(areaNumber).vertexs));
        getArea(areaNumber).vertexs = geomatrixController.reflectVertical(
                getArea(areaNumber).vertexs);
        
        translateArea(areaNumber, gridWidth, 0); //reallocate in visible area
        
        repaint();
    }

    void reflectHorizontal(int areaNumber) {
        assert(geomatrixController.isValidArea(getArea(areaNumber).vertexs));
        getArea(areaNumber).vertexs = geomatrixController.reflectHorizontal(
                getArea(areaNumber).vertexs);
        
        translateArea(areaNumber, 0, gridHeight); //reallocate in visible area
        
        repaint();
    }

    void setGridSize(int xSize, int ySize) {
        int gridWidthOffset = xSize - gridWidth;
        int gridHeightOffset = ySize - gridHeight;
        gridWidth = xSize;
        gridHeight = ySize;
        mainFrame.setSize(mainFrame.getWidth()+gridWidthOffset*CELL_PIXEL_LENGTH,
                          mainFrame.getHeight()+gridHeightOffset*CELL_PIXEL_LENGTH);
        mainFrame.repaint();
        repaint();
    }

    void cellIteration(int areaNumber) {
        CellIteratorController cellController = new CellIteratorController(getArea(areaNumber).vertexs);
        CellIterationPanel cellIterationPanel = new CellIterationPanel(cellController, this);
        cellIterationPanel.setLocation(mainFrame.getLocation().x + mainFrame.getWidth(), mainFrame.getLocation().y);
        
        setIgnoreInput(true);
        cellIterationPanel.setVisible(true);
    }

    void paintIteredCells(Graphics2D g) {
        if (iteredCells == null) return;
        for (Cell point : iteredCells) {
            Drawer.paintIteredCell(point, g);
        }
    }

    void setIterationPoints(Set<Cell> iteredCells) {
        this.iteredCells = iteredCells;
    }

    public final void setIgnoreInput(boolean shouldIgnore) {
        ignoreInput = shouldIgnore;
    }

    void rectangleIteration(int areaNumber) {
        RectangleIteratorController rectangleController = new RectangleIteratorController(getArea(areaNumber).vertexs);
        rectangleIterationPanel = new RectangleIterationPanel(rectangleController, this);
        rectangleIterationPanel.setLocation(mainFrame.getLocation().x + mainFrame.getWidth(), mainFrame.getLocation().y);
        
        setIgnoreInput(true);
        rectangleIterationPanel.setVisible(true);
    }

    private void paintIteratingRectangle(Graphics2D g) {
        if (rectangleIterationPanel != null && rectangleIterationPanel.isVisible()
                && ! iteredCells.isEmpty()) {
            GridPoint topLeft = rectangleIterationPanel.lastIteratedPoint;
            GridPoint bottomRight = rectangleIterationPanel.lastIteratedPoint;
            bottomRight.x += rectangleIterationPanel.rectangleWidth;
            bottomRight.y += rectangleIterationPanel.rectangleHeight;

            Rectangle rectangle = new Rectangle(topLeft, bottomRight);
            for (Segment segment : rectangle.getEdges()) {
                paintSegment(segment, ITERATION_RECTANGLE_COLOR, g);
            }
        }
    }


        
    private class SelectGridPointListener extends MouseAdapter {

        public SelectGridPointListener() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (ignoreInput) return;
            
            Pixel clickedPixel = new Pixel(e.getX(), e.getY());
            GridPoint clickedVertex = getClosestVertex(clickedPixel);
            
            PresentationArea selectedArea = getArea(selectedAreaNumber);
            if (selectedArea.containsVertex(clickedVertex)) {
                selectedArea.removeVertexFromArea(clickedVertex);
            }
            else {
                selectedArea.addVertexToArea(clickedVertex);
            }
            
            updateMenusThatRequireValidAreaActivation(selectedAreaNumber);
            
            repaint();
        }

        private GridPoint getClosestVertex(Pixel pixel) {
            return new GridPoint((pixel.x+CELL_PIXEL_LENGTH/2)/CELL_PIXEL_LENGTH,
                                 (pixel.y+CELL_PIXEL_LENGTH/2)/CELL_PIXEL_LENGTH);
        }

    }
    
    private static class Drawer {
        
        private static final int VERTEX_PIXEL_RADIUS = 9;
        private static final int ITERATION_MARK_PIXEL_RADIUS = 8;
        private static final Color ITERATION_MARK_COLOR = Color.ORANGE;
        private static final Color ITERATION_RECTANGLE_COLOR = Color.BLACK;
    
        public static void paintCell(Cell cell, Color color, Graphics2D g) {
            if (color == null) return;
            g.setColor(color);
            Pixel topLeft = findCoordinates(new GridPoint(cell.x, cell.y));
            Pixel center = new Pixel(topLeft.x + CELL_PIXEL_LENGTH/2, topLeft.y + CELL_PIXEL_LENGTH/2);

            int i = center.x;
            int j = center.y;
            int size = 0;
            g.drawRect(i, j, 1, 1);
            for (; i > topLeft.x; --i) {            
                g.drawRect(i, j, size, size);
                --j;
                size += 2;
            }
        }
            
        public static void paintVertex(GridPoint point, List<Color> colors, Graphics2D g) {
            Pixel coordinates = findCoordinates(point);
            if (colors.size() == 1) {
                paintSphere(coordinates, colors.get(0), VERTEX_PIXEL_RADIUS, g);
            }
            else if (colors.size() == 2) {
                paintSphere(coordinates, colors.get(0), VERTEX_PIXEL_RADIUS+1, g);
                paintSphere(coordinates, colors.get(1), (VERTEX_PIXEL_RADIUS+1)/2, g);
            }
            else if (colors.size() == 3) {
                paintSphere(coordinates, colors.get(0), VERTEX_PIXEL_RADIUS+2, g);
                paintSphere(coordinates, colors.get(1), (VERTEX_PIXEL_RADIUS+2)*2/3, g);
                paintSphere(coordinates, colors.get(2), (VERTEX_PIXEL_RADIUS+2)/3, g);
            }
        }

        public static void paintIteredCell(Cell point, Graphics2D g) {
            Pixel inGridCenter = findCoordinates(new GridPoint(point.x, point.y));
            paintSphere(inGridCenter, ITERATION_MARK_COLOR, ITERATION_MARK_PIXEL_RADIUS, g);
        }
               
        public static void drawGrid(Graphics2D g, int width, int height) {        
            g.setColor(GRID_COLOR);

            for(int i = 0; i < width; i += CELL_PIXEL_LENGTH)
                g.drawLine(i, 0, i, height);

            for(int i = 0; i < height; i += CELL_PIXEL_LENGTH)
                g.drawLine(0, i, width, i);
        }
        
        private static void paintUnitarySegment(UnitarySegment segment, Color color, Graphics2D g) {
            g.setColor(color);
            GridPoint endPoint1, endPoint2;
            endPoint1 = segment.topLeftEndPoint;
            if (segment.axis == Axis.Horizontal) endPoint2 = new GridPoint(endPoint1.x + 1, endPoint1.y);
            else endPoint2 = new GridPoint(endPoint1.x, endPoint1.y + 1);

            Pixel pixelEndPoint1 = findCoordinates(endPoint1);
            Pixel pixelEndPoint2 = findCoordinates(endPoint2);
            
            g.drawLine(pixelEndPoint1.x, pixelEndPoint1.y, pixelEndPoint2.x, pixelEndPoint2.y);
            g.drawLine(segment.topLeftEndPoint.x, segment.topLeftEndPoint.y, WIDTH, WIDTH);
        }
            
        private static void paintSphere(Pixel center, Color color, int radius, Graphics2D g) {
            g.setColor(color);
            for (int i = 0; i <= radius; ++i) {
                g.drawOval(center.x - i/2, center.y - i/2, i, i);
            }
        }
        
        private static Pixel findCoordinates(GridPoint p) {
            return new Pixel(p.x*CELL_PIXEL_LENGTH, p.y*CELL_PIXEL_LENGTH);
        }
        
        private Pixel findCoordinates(Cell cell) {
            Pixel center = new Pixel(cell.x*CELL_PIXEL_LENGTH, cell.y*CELL_PIXEL_LENGTH);
            center.x += CELL_PIXEL_LENGTH/2;
            center.y += CELL_PIXEL_LENGTH/2;
            return center;
        }

    }
    
}
