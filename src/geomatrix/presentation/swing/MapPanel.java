/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.utils.Line;
import geomatrix.business.controllers.AreaController;
import geomatrix.utils.Direction;
import geomatrix.utils.Segment;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import manticore.presentation.SwingController;


/**
 *
 * @author Nil
 */
public class MapPanel extends JPanel {
    
    private MainFrame mainFrame;
    private AreaController areaController;
    
    private int selectedAreaNumber;
  
    private PresentationArea area1;
    private PresentationArea area2;
    private PresentationArea area3;

    private static final int GRID_WIDTH = 20;
    private static final int GRID_DEPTH = 20;
    
    private static final int PREFERRED_CELL_PIXEL_LENGTH = 20;
    private static final int VERTEX_PIXEL_RADIUS = 9;
    
    private static final Color AREA_1_COLOR = Color.RED;
    private static final Color AREA_2_COLOR = Color.BLUE;
    private static final Color AREA_3_COLOR = Color.GREEN;
    private static final Color BACKGROUND_GRID_COLOR = Color.white;
    private static final Color COLOR_GRID = Color.decode("#EEEEEE");
    private static final Color OVERLAPPED_LINES_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_LINES_COLOR = Color.BLACK;
    private static final Color OVERLAPPED_CELLS_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_CELLS_COLOR = Color.BLACK;
    
    /**
     * Initialization.
     * All areas have no vertexs.
     * All areas are displayed.
     * Area 1 is selected.
     * @param swingController 
     */
    public MapPanel(SwingController swingController) {
        
        setPreferredSize(new Dimension(GRID_WIDTH*PREFERRED_CELL_PIXEL_LENGTH,
                                       GRID_DEPTH*PREFERRED_CELL_PIXEL_LENGTH));
        
        this.areaController = swingController.getBusinessController(AreaController.class);
        selectedAreaNumber = 1;
        
        area1 = new PresentationArea(true, AREA_1_COLOR);
        area2 = new PresentationArea(true, AREA_2_COLOR);
        area3 = new PresentationArea(true, AREA_3_COLOR);
        
        this.addMouseListener(new SelectGridPointListener());
        repaint();
        
    }

    /**
     * This coupling is made so that mapPanel can tell the main frame when the
     * areas are valid/unvalid so that it can enable/disable the menu
     * options that require the areas to be valid.
     * @param mainFrame 
     */
    void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_GRID_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Graphics2D g2D = (Graphics2D) g;
        drawGrid(g2D);
        paintVertexs(g2D);
        paintInvalidLines(g2D);
        paintAreas(g2D);
        paintBoundingRectangles(g2D);
    }

    private void paintVertexs(Graphics2D g) {
        for (int i = 0; i < GRID_DEPTH; ++i) {
            for (int j = 0; j < GRID_WIDTH; ++j) {
                paintPoint(new Point(i, j), g);
            }
        }
    }
    
    private void paintPoint(Point point, Graphics2D g) {
        Point coordinates = findCoordinates(point);
        List<Color> colors = getColors(point);
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
    
    private void paintSphere(Point center, Color color, int radius, Graphics2D g) {
        g.setColor(color);
        for (int i = 0; i <= radius; ++i) {
            g.drawOval(center.x - i/2, center.y - i/2, i, i);
        }
    }
    
    private List<Color> getColors(Point point) {
        List<Color> colors = new ArrayList<Color>();
        if (area1.containsVertex(point) && area1.isDisplayed) colors.add(area1.color);
        if (area2.containsVertex(point) && area2.isDisplayed) colors.add(area2.color);
        if (area3.containsVertex(point) && area3.isDisplayed) colors.add(area3.color);
        return colors;
    }
      
    private void drawGrid(Graphics2D g) {        
        g.setColor(COLOR_GRID);
        
        for(int i = 0; i < getWidth(); i += cellSize())
            g.drawLine(i, 0, i, getHeight());
        
        for(int i = 0; i < getHeight(); i += cellSize())
            g.drawLine(0, i, getWidth(), i);
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
    
    private int cellSize() {
        return Math.max(this.getWidth() / GRID_WIDTH,
                        this.getHeight() / GRID_DEPTH);
    }

    private Point findCoordinates(Point p) {
        return new Point(p.x*cellSize(), p.y*cellSize());
    }

    private PresentationArea getArea(int areaNumber) {
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        if (areaNumber == 1) return area1;
        if (areaNumber == 2) return area2;
        return area3;
    }

    private void paintInvalidLines(Graphics2D g) {
        List<Line> area1InvalidLines, area2InvalidLines, area3InvalidLines;
        if (area1.isDisplayed) {
            area1InvalidLines = areaController.getInvalidLines(area1.vertexs);
        }
        else {
            area1InvalidLines = new ArrayList<Line>();
        }
        if (area2.isDisplayed) {
            area2InvalidLines = areaController.getInvalidLines(area2.vertexs);
        }
        else {
            area2InvalidLines = new ArrayList<Line>();
        }
        if (area3.isDisplayed) {
            area3InvalidLines = areaController.getInvalidLines(area3.vertexs);
        }
        else {
            area3InvalidLines = new ArrayList<Line>();
        }
        paintLines(area1InvalidLines, area2InvalidLines, area3InvalidLines, g);
    }

    private void paintLines(List<Line> area1Lines, List<Line> area2Lines,
            List<Line> area3Lines, Graphics2D g) {
        
        Map<Line, Color> lineColors = findLineColors(
                area1Lines, area2Lines, area3Lines);
        
        for (Line line : lineColors.keySet()) {
            paintLine(line, lineColors.get(line), g);
        }
    }

    private Map<Line, Color> findLineColors(List<Line> area1Lines,
            List<Line> area2Lines, List<Line> area3Lines) {
        
        Map<Line, Color> lineColors = new HashMap<Line, Color>();
        for (Line line : area1Lines) {
            lineColors.put(line, area1.color);
        }
        for (Line line : area2Lines) {
            if (lineColors.containsKey(line)) {
                lineColors.put(line, OVERLAPPED_LINES_COLOR);
            }
            else {
                lineColors.put(line, area2.color);
            }
        }
        for (Line line : area3Lines) {
            if (lineColors.containsKey(line)) {
                if (lineColors.get(line) == OVERLAPPED_LINES_COLOR) {
                    lineColors.put(line, DOUBLY_OVERLAPPED_LINES_COLOR);
                }
                else {
                    lineColors.put(line, OVERLAPPED_LINES_COLOR);
                }
            }
            else {
                lineColors.put(line, area3.color);
            }
        }
        return lineColors;
    }

    private void paintLine(Line line, Color color, Graphics2D g) {
        g.setColor(color);
        if (line.vertical) {
            Point lineOrigin = new Point(line.fixedCoordinate, 0);
            int xValue = findCoordinates(lineOrigin).x;
            g.drawLine(xValue, 0, xValue, getHeight());
        }
        else {
            Point lineOrigin = new Point(0, line.fixedCoordinate);
            int yValue = findCoordinates(lineOrigin).y;
            g.drawLine(0, yValue, getWidth(), yValue);
        }
    }

    private void paintAreas(Graphics2D g) {
        paintContainedCells(g);
        paintEdges(g);
    }

    private void paintContainedCells(Graphics2D g) {
        List<GridCell> area1ContainedCells, area2ContainedCells, area3ContainedCells;
        if (area1.isDisplayed) {
            area1ContainedCells = areaController.getContainedCells(area1.vertexs);
        }
        else {
            area1ContainedCells = new ArrayList<GridCell>();
        }
        if (area2.isDisplayed) {
            area2ContainedCells = areaController.getContainedCells(area2.vertexs);
        }
        else {
            area2ContainedCells = new ArrayList<GridCell>();
        }
        if (area3.isDisplayed) {
            area3ContainedCells = areaController.getContainedCells(area3.vertexs);
        }
        else {
            area3ContainedCells = new ArrayList<GridCell>();
        }
        paintCells(area1ContainedCells, area2ContainedCells, area3ContainedCells, g);
    }

    private void paintCells(List<GridCell> area1ContainedCells,
            List<GridCell> area2ContainedCells, List<GridCell> area3ContainedCells, Graphics2D g) {
        
        Map<GridCell, Color> cellColors = findCellColors(
                area1ContainedCells, area2ContainedCells, area3ContainedCells);
        
        for (GridCell cell : cellColors.keySet()) {
            paintCell(cell, cellColors.get(cell), g);
        }
    }

    private Map<GridCell, Color> findCellColors(List<GridCell> area1cells,
            List<GridCell> area2cells, List<GridCell> area3cells) {
        
        Map<GridCell, Color> cellColors = new HashMap<GridCell, Color>();
        for (GridCell cell : area1cells) {
            cellColors.put(cell, area1.color);
        }
        for (GridCell cell : area2cells) {
            if (cellColors.containsKey(cell)) {
                cellColors.put(cell, OVERLAPPED_CELLS_COLOR);
            }
            else {
                cellColors.put(cell, area2.color);
            }
        }
        for (GridCell cell : area3cells) {
            if (cellColors.containsKey(cell)) {
                if (cellColors.get(cell) == OVERLAPPED_CELLS_COLOR) {
                    cellColors.put(cell, DOUBLY_OVERLAPPED_CELLS_COLOR);
                }
                else {
                    cellColors.put(cell, OVERLAPPED_CELLS_COLOR);
                }
            }
            else {
                cellColors.put(cell, area3.color);
            }
        }
        return cellColors;
    }

    private void paintCell(GridCell cell, Color color, Graphics2D g) {
        g.setColor(color);
        Point topLeft = findCoordinates(new Point(cell.x, cell.y));
        Point center = new Point(topLeft.x + cellSize()/2, topLeft.y + cellSize()/2);
        
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

    private void paintEdges(Graphics2D g) {
        //edges are not painted for now. It is not necessary.
    }

    private void updateSetOperationMenusActivation(int modifiedAreaNumber) {
        if (areaController.isValidArea(getArea(modifiedAreaNumber).vertexs)) {
            mainFrame.enableSetOperations(modifiedAreaNumber, true);
        }
        else {
            mainFrame.enableSetOperations(modifiedAreaNumber, false);
        }
    }
        
    void resetArea(int areaNumber) {
        getArea(areaNumber).vertexs = new HashSet<Point>();
        
        updateSetOperationMenusActivation(areaNumber);
        repaint();
    }

    void cloneArea(int destinationAreaNumber, int toBeClonedAreaNumber) {
        assert(destinationAreaNumber != toBeClonedAreaNumber);
        
        getArea(destinationAreaNumber).vertexs = new HashSet<Point>();
        for (Point vertex : getArea(toBeClonedAreaNumber).vertexs) {
            getArea(destinationAreaNumber).vertexs.add(vertex);
        }
        
        updateSetOperationMenusActivation(destinationAreaNumber);
        repaint();
    }

    void unionArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<Point> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<Point> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.union(destinationAreaVertexs,
                                                      otherAreaVertexs);
        
        updateSetOperationMenusActivation(destinationAreaNumber);
        repaint();
    }
    
    void intersectionArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<Point> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<Point> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.intersection(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateSetOperationMenusActivation(destinationAreaNumber);
        repaint();
    }

    void differenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<Point> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<Point> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.difference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateSetOperationMenusActivation(destinationAreaNumber);
        repaint();
    }

    void symmetricDifferenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<Point> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<Point> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.symmetricDifference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateSetOperationMenusActivation(destinationAreaNumber);
        repaint();
    }

    void showBoundingRectangle(int areaNumber, boolean enable) {
        getArea(areaNumber).isBoundingRectangleDisplayed = enable;
        
        repaint();
    }

    private void paintBoundingRectangles(Graphics2D g) {
        List<Segment> area1BoundingRectangleEdges, area2BoundingRectangleEdges,
                area3BoundingRectangleEdges;
        
        if (shouldPaintBoundingRectangle(1)) {
            area1BoundingRectangleEdges = areaController.getBoundingRectangleEdges(area1.vertexs);
        }
        else {
            area1BoundingRectangleEdges = new ArrayList<Segment>();
        }
        if (shouldPaintBoundingRectangle(2)) {
            area2BoundingRectangleEdges = areaController.getBoundingRectangleEdges(area2.vertexs);
        }
        else {
            area2BoundingRectangleEdges = new ArrayList<Segment>();
        }
        if (shouldPaintBoundingRectangle(3)) {
            area3BoundingRectangleEdges = areaController.getBoundingRectangleEdges(area3.vertexs);
        }
        else {
            area3BoundingRectangleEdges = new ArrayList<Segment>();
        }
        paintSegments(area1BoundingRectangleEdges, area2BoundingRectangleEdges, area3BoundingRectangleEdges, g);
    }
        
    private boolean shouldPaintBoundingRectangle(int areaNumber) {
        return getArea(areaNumber).isDisplayed &&
               getArea(areaNumber).isBoundingRectangleDisplayed &&
               areaController.isValidArea(getArea(areaNumber).vertexs);
    }

    private void paintSegments(List<Segment> area1Segments,
            List<Segment> area2Segments, List<Segment> area3Segments, Graphics2D g) {
        
        List<Segment> area1UnitarySegments = breakDownToUnitarySegments(area1Segments);
        List<Segment> area2UnitarySegments = breakDownToUnitarySegments(area2Segments);
        List<Segment> area3UnitarySegments = breakDownToUnitarySegments(area3Segments);
        Map<Segment, Color> lineColors = findUnitarySegmentColors(
                area1Segments, area2Segments, area3Segments);
        
        for (Segment segment : lineColors.keySet()) {
            paintSegment(segment, lineColors.get(segment), g);
        }
    }

    private void paintSegment(Segment segment, Color color, Graphics2D g) {
        g.setColor(color);
        Point gridEndPoint1 = findCoordinates(segment.endPoint1);
        Point gridEndPoint2 = findCoordinates(segment.endPoint2);
        g.drawLine(gridEndPoint1.x, gridEndPoint1.y, gridEndPoint2.x, gridEndPoint2.y);
    }

    private List<Segment> breakDownToUnitarySegments(List<Segment> segments) {
        List<Segment> unitarySegments = new ArrayList<Segment>();
        
        for (Segment segment : segments) {
            Direction direction = segment.getDirection();
            Point point = segment.endPoint1;
            while (! point.equals(segment.endPoint2)) {
                Point next = (Point) point.clone();
                advance(next, direction);
                unitarySegments.add(new Segment(point, next));
                point = (Point) next.clone();
            }
        }
        
        return unitarySegments;
    }

    private Map<Segment, Color> findUnitarySegmentColors(
            List<Segment> area1Segments, List<Segment> area2Segments,
            List<Segment> area3Segments) {
        
        Map<Segment, Color> segmentColors = new HashMap<Segment, Color>();
        for (Segment segment : area1Segments) {
            segmentColors.put(segment, area1.color);
        }
        for (Segment segment : area2Segments) {
            if (segmentColors.containsKey(segment)) {
                segmentColors.put(segment, OVERLAPPED_LINES_COLOR);
            }
            else {
                segmentColors.put(segment, area2.color);
            }
        }
        for (Segment segment : area3Segments) {
            if (segmentColors.containsKey(segment)) {
                if (segmentColors.get(segment) == OVERLAPPED_LINES_COLOR) {
                    segmentColors.put(segment, DOUBLY_OVERLAPPED_LINES_COLOR);
                }
                else {
                    segmentColors.put(segment, OVERLAPPED_LINES_COLOR);
                }
            }
            else {
                segmentColors.put(segment, area3.color);
            }
        }
        return segmentColors;
    }

    private void advance(Point point, Direction direction) {
        if (direction == Direction.N) --point.y;
        else if (direction == Direction.S) ++point.y;
        else if (direction == Direction.W) --point.x;
        else ++point.x;
    }

    void translateArea(int areaNumber, int xTranslate, int yTranslate) {
        assert(areaController.isValidArea(getArea(areaNumber).vertexs));
        
        getArea(areaNumber).vertexs = areaController.translate(
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
        assert(areaController.isValidArea(getArea(areaNumber).vertexs));
        assert(degrees == 90 || degrees == 180 || degrees == 270);
        
        rotate90AndReallocate(areaNumber);
        //rotating 180 equals rotating 90 twice
        if (degrees > 90) rotate90AndReallocate(areaNumber);
        //rotating 270 equals rotating 90 three times
        if (degrees > 180) rotate90AndReallocate(areaNumber);

        repaint();
    }

    private void rotate90AndReallocate(int areaNumber) {
        getArea(areaNumber).vertexs = areaController.rotate90Degrees(
                getArea(areaNumber).vertexs);
        translateArea(areaNumber, GRID_WIDTH, 0);
    }
            
    private class SelectGridPointListener extends MouseAdapter {

        public SelectGridPointListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point clickedVertex = getClosestVertex(e.getPoint());
            
            PresentationArea selectedArea = getArea(selectedAreaNumber);
            if (selectedArea.containsVertex(clickedVertex)) {
                selectedArea.removeVertexFromArea(clickedVertex);
            }
            else {
                selectedArea.addVertexToArea(clickedVertex);
            }
            
            updateSetOperationMenusActivation(selectedAreaNumber);
            
            repaint();
        }

        private Point getClosestVertex(Point point) {
            return new Point((point.x+PREFERRED_CELL_PIXEL_LENGTH/2)/cellSize(),
                             (point.y+PREFERRED_CELL_PIXEL_LENGTH/2)/cellSize());
        }

    }   
    
}
