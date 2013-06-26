/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.business.controllers.RectangleIteratorController;
import geomatrix.gridplane.Line;
import geomatrix.business.controllers.GeomatrixController;
import geomatrix.business.controllers.CellIteratorController;
import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Rectangle;
import geomatrix.gridplane.Segment;
import geomatrix.utils.Direction;
import geomatrix.gridplane.Vector;
import geomatrix.utils.Axis;
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


/**
 *
 * @author Nil
 */
public class MapPanel extends JPanel {
    
    private MainFrame mainFrame;
    private GeomatrixController areaController;
    
    private int selectedAreaNumber;
  
    private PresentationArea area1;
    private PresentationArea area2;
    private PresentationArea area3;

    private int gridWidth = 20;
    private int gridHeight = 20;
    
    private static final int CELL_PIXEL_LENGTH = 20;
    private static final int VERTEX_PIXEL_RADIUS = 9;
    private static final int ITERATION_MARK_PIXEL_RADIUS = 8;
    private static final Color ITERATION_MARK_COLOR = Color.ORANGE;
    private static final Color ITERATION_RECTANGLE_COLOR = Color.BLACK;
    
    private static final Color AREA_1_COLOR = Color.RED;
    private static final Color AREA_2_COLOR = Color.BLUE;
    private static final Color AREA_3_COLOR = Color.GREEN;
    private static final Color BACKGROUND_GRID_COLOR = Color.white;
    private static final Color COLOR_GRID = Color.decode("#EEEEEE");
    private static final Color OVERLAPPED_LINES_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_LINES_COLOR = Color.BLACK;
    private static final Color OVERLAPPED_CELLS_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_CELLS_COLOR = Color.BLACK;
    
    private Set<Cell> iteredCells;
    private boolean ignoreInput;
    
    private RectangleIterationPanel rectangleIterationPanel;
    
    /**
     * Initialization.
     * All areas have no vertexs.
     * All areas are displayed.
     * Area 1 is selected.
     * @param swingController 
     */
    public MapPanel(SwingController swingController) {
        
        setIgnoreInput(false);
        
        setPreferredSize(new Dimension(gridWidth*CELL_PIXEL_LENGTH,
                                       gridHeight*CELL_PIXEL_LENGTH));
        
        this.areaController = swingController.getBusinessController(GeomatrixController.class);
        selectedAreaNumber = 1;
        
        area1 = new PresentationArea(true, AREA_1_COLOR);
        area2 = new PresentationArea(false, AREA_2_COLOR);
        area3 = new PresentationArea(false, AREA_3_COLOR);
        
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

    private void paintVertexs(Graphics2D g) {
        for (int i = 0; i < gridWidth; ++i) {
            for (int j = 0; j < gridHeight; ++j) {
                paintVertex(new GridPoint(i, j), g);
            }
        }
    }
    
    private void paintVertex(GridPoint point, Graphics2D g) {
        Pixel coordinates = findCoordinates(point);
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
    
    private void paintSphere(Pixel center, Color color, int radius, Graphics2D g) {
        g.setColor(color);
        for (int i = 0; i <= radius; ++i) {
            g.drawOval(center.x - i/2, center.y - i/2, i, i);
        }
    }
    
    private List<Color> getColors(GridPoint point) {
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
        return Math.max(this.getWidth() / gridWidth,
                        this.getHeight() / gridHeight);
    }

    private Pixel findCoordinates(GridPoint p) {
        return new Pixel(p.x*cellSize(), p.y*cellSize());
    }
    
    private Pixel findCoordinates(Cell cell) {
        Pixel center = new Pixel(cell.x*cellSize(), cell.y*cellSize());
        center.x += CELL_PIXEL_LENGTH/2;
        center.y += CELL_PIXEL_LENGTH/2;
        return center;
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
        if (line.axis == Axis.Vertical) {
            GridPoint lineOrigin = new GridPoint(line.fixedCoordinate, 0);
            int xValue = findCoordinates(lineOrigin).x;
            g.drawLine(xValue, 0, xValue, getHeight());
        }
        else {
            GridPoint lineOrigin = new GridPoint(0, line.fixedCoordinate);
            int yValue = findCoordinates(lineOrigin).y;
            g.drawLine(0, yValue, getWidth(), yValue);
        }
    }

    private void paintAreas(Graphics2D g) {
        paintContainedCells(g);
        paintEdges(g);
    }

    private void paintContainedCells(Graphics2D g) {
        List<Cell> area1ContainedCells, area2ContainedCells, area3ContainedCells;
        if (area1.isDisplayed) {
            area1ContainedCells = areaController.getContainedCells(area1.vertexs);
        }
        else {
            area1ContainedCells = new ArrayList<Cell>();
        }
        if (area2.isDisplayed) {
            area2ContainedCells = areaController.getContainedCells(area2.vertexs);
        }
        else {
            area2ContainedCells = new ArrayList<Cell>();
        }
        if (area3.isDisplayed) {
            area3ContainedCells = areaController.getContainedCells(area3.vertexs);
        }
        else {
            area3ContainedCells = new ArrayList<Cell>();
        }
        paintCells(area1ContainedCells, area2ContainedCells, area3ContainedCells, g);
    }

    private void paintCells(List<Cell> area1ContainedCells,
            List<Cell> area2ContainedCells, List<Cell> area3ContainedCells, Graphics2D g) {
        
        Map<Cell, Color> cellColors = findCellColors(
                area1ContainedCells, area2ContainedCells, area3ContainedCells);
        
        for (Cell cell : cellColors.keySet()) {
            paintCell(cell, cellColors.get(cell), g);
        }
    }

    private Map<Cell, Color> findCellColors(List<Cell> area1cells,
            List<Cell> area2cells, List<Cell> area3cells) {
        
        Map<Cell, Color> cellColors = new HashMap<Cell, Color>();
        for (Cell cell : area1cells) {
            cellColors.put(cell, area1.color);
        }
        for (Cell cell : area2cells) {
            if (cellColors.containsKey(cell)) {
                cellColors.put(cell, OVERLAPPED_CELLS_COLOR);
            }
            else {
                cellColors.put(cell, area2.color);
            }
        }
        for (Cell cell : area3cells) {
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

    private void paintCell(Cell cell, Color color, Graphics2D g) {
        g.setColor(color);
        Pixel topLeft = findCoordinates(new GridPoint(cell.x, cell.y));
        Pixel center = new Pixel(topLeft.x + cellSize()/2, topLeft.y + cellSize()/2);
        
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

    private void updateMenusThatRequireValidAreaActivation(int modifiedAreaNumber) {
        if (areaController.isValidArea(getArea(modifiedAreaNumber).vertexs)) {
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
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.union(destinationAreaVertexs,
                                                      otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }
    
    void intersectionArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.intersection(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void differenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.difference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void symmetricDifferenceArea(int destinationAreaNumber, int otherAreaNumber) {
        assert(destinationAreaNumber != otherAreaNumber);
        Set<GridPoint> destinationAreaVertexs = getArea(destinationAreaNumber).vertexs;
        Set<GridPoint> otherAreaVertexs = getArea(otherAreaNumber).vertexs;
        assert(areaController.isValidArea(destinationAreaVertexs) &&
               areaController.isValidArea(otherAreaVertexs));
        
        getArea(destinationAreaNumber).vertexs = areaController.symmetricDifference(
                destinationAreaVertexs, otherAreaVertexs);
        
        updateMenusThatRequireValidAreaActivation(destinationAreaNumber);
        repaint();
    }

    void showBoundingRectangle(int areaNumber, boolean enable) {
        getArea(areaNumber).isBoundingRectangleDisplayed = enable;
        
        repaint();
    }

    private void paintBoundingRectangles(Graphics2D g) {
        Collection<Segment> area1BoundingRectangleEdges, area2BoundingRectangleEdges,
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

    private void paintSegments(Collection<Segment> area1Segments,
            Collection<Segment> area2Segments, Collection<Segment> area3Segments,
            Graphics2D g) {

        //TO REFACTOR
//        for (Vector segment : lineColors.keySet()) {
//            paintSegment(segment, lineColors.get(segment), g);
//        }
    }

    private void paintSegment(Segment segment, Color color, Graphics2D g) {
        g.setColor(color);
        Pair<GridPoint, GridPoint> endPoints = segment.getEndpoints();
        Pixel pixelEndPoint1 = findCoordinates(endPoints.first);
        Pixel pixelEndPoint2 = findCoordinates(endPoints.second);
        g.drawLine(pixelEndPoint1.x, pixelEndPoint1.y, pixelEndPoint2.x, pixelEndPoint2.y);
    }

    private Map<Vector, Color> findUnitarySegmentColors(
            List<Vector> area1Segments, List<Vector> area2Segments,
            List<Vector> area3Segments) {
        
        Map<Vector, Color> segmentColors = new HashMap<Vector, Color>();
        for (Vector segment : area1Segments) {
            segmentColors.put(segment, area1.color);
        }
        for (Vector segment : area2Segments) {
            if (segmentColors.containsKey(segment)) {
                segmentColors.put(segment, OVERLAPPED_LINES_COLOR);
            }
            else {
                segmentColors.put(segment, area2.color);
            }
        }
        for (Vector segment : area3Segments) {
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

    private void advance(GridPoint point, Direction direction) {
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
        translateArea(areaNumber, gridWidth, 0);
    }

    void reflectVertical(int areaNumber) {
        assert(areaController.isValidArea(getArea(areaNumber).vertexs));
        getArea(areaNumber).vertexs = areaController.reflectVertical(
                getArea(areaNumber).vertexs);
        
        translateArea(areaNumber, gridWidth, 0); //reallocate in visible area
        
        repaint();
    }

    void reflectHorizontal(int areaNumber) {
        assert(areaController.isValidArea(getArea(areaNumber).vertexs));
        getArea(areaNumber).vertexs = areaController.reflectHorizontal(
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
            Pixel inGridCenter = findCoordinates(point);
            paintSphere(inGridCenter, ITERATION_MARK_COLOR, ITERATION_MARK_PIXEL_RADIUS, g);
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
            return new GridPoint((pixel.x+CELL_PIXEL_LENGTH/2)/cellSize(),
                                 (pixel.y+CELL_PIXEL_LENGTH/2)/cellSize());
        }

    }   
    
}
