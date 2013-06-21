/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.utils.Line;
import geomatrix.business.controllers.AreaController;
import geomatrix.utils.Pair;
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

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_GRID_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Graphics2D g2D = (Graphics2D) g;
        drawGrid(g2D);
        paintVertexs(g2D);
        paintArea(g2D);
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

    private void paintArea(Graphics2D g) {
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
        paintInvalidLines(area1InvalidLines, area2InvalidLines, area3InvalidLines, g);
    }

    private void paintInvalidLines(List<Line> area1InvalidLines,
            List<Line> area2InvalidLines, List<Line> area3InvalidLines, Graphics2D g) {
        
        Map<Line, Color> colorOfInvalidLines = findColorOfInvalidLines(
                area1InvalidLines, area2InvalidLines, area3InvalidLines);
        
        for (Line line : colorOfInvalidLines.keySet()) {
            paintLine(line, colorOfInvalidLines.get(line), g);
        }
    }

    private Map<Line, Color> findColorOfInvalidLines(List<Line> area1InvalidLines,
            List<Line> area2InvalidLines, List<Line> area3InvalidLines) {
        
        Map<Line, Color> colorOfInvalidLines = new HashMap<Line, Color>();
        for (Line line : area1InvalidLines) {
            colorOfInvalidLines.put(line, area1.color);
        }
        for (Line line : area2InvalidLines) {
            if (colorOfInvalidLines.containsKey(line)) {
                colorOfInvalidLines.put(line, OVERLAPPED_LINES_COLOR);
            }
            else {
                colorOfInvalidLines.put(line, area2.color);
            }
        }
        for (Line line : area3InvalidLines) {
            if (colorOfInvalidLines.containsKey(line)) {
                if (colorOfInvalidLines.get(line) == OVERLAPPED_LINES_COLOR) {
                    colorOfInvalidLines.put(line, DOUBLY_OVERLAPPED_LINES_COLOR);
                }
                else {
                    colorOfInvalidLines.put(line, OVERLAPPED_LINES_COLOR);
                }
            }
            else {
                colorOfInvalidLines.put(line, area3.color);
            }
        }
        return colorOfInvalidLines;
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
            
            repaint();
        }

        private Point getClosestVertex(Point point) {
            return new Point((point.x+PREFERRED_CELL_PIXEL_LENGTH/2)/cellSize(),
                             (point.y+PREFERRED_CELL_PIXEL_LENGTH/2)/cellSize());
        }

    }   
    
}
