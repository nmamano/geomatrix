/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.business.controllers.AreaController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import javax.swing.JPanel;
import manticore.presentation.SwingController;

/**
 *
 * @author Nil
 */
public class MapPanel extends JPanel {
    
    private AreaController areaController;
    
    private int selectedAreaNumber;
    
    private static final int GRID_WIDTH = 24;
    private static final int GRID_DEPTH = 24;
    
    private static final int CELL_SIDE_PIXEL_LENGTH = 16;
    private static final int VERTEX_PIXEL_DIAMETER = 4;
    
    private PresentationArea area1;
    private PresentationArea area2;
    private PresentationArea area3;

    private static final Color AREA_1_COLOR = Color.RED;
    private static final Color AREA_2_COLOR = Color.BLUE;
    private static final Color AREA_3_COLOR = Color.GREEN;
    private static final Color BACKGROUND_GRID_COLOR = Color.white;
    private static final Color COLOR_GRID = Color.decode("#EEEEEE");

    /**
     * Initialization.
     * All areas have no vertexs.
     * All areas are displayed.
     * Area 1 is selected.
     * @param swingController 
     */
    public MapPanel(SwingController swingController) {
        
        setPreferredSize(new Dimension(GRID_WIDTH*CELL_SIDE_PIXEL_LENGTH,
                                       GRID_DEPTH*CELL_SIDE_PIXEL_LENGTH));
        
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
        if (area1.isDisplayed) paintArea(area1, g2D);
        if (area2.isDisplayed) paintArea(area2, g2D);
        if (area3.isDisplayed) paintArea(area3, g2D);
    }

    private void drawGrid(Graphics2D g) {        
        g.setColor(COLOR_GRID);
        
        for(int i = 0; i < getWidth(); i += cellSize())
            g.drawLine(i, 0, i, getHeight());
        
        for(int i = 0; i < getHeight(); i += cellSize())
            g.drawLine(0, i, getWidth(), i);
    }
    
    void paintArea(PresentationArea area, Graphics2D g) {
        for (Point p : area.vertexs) {
            displayPoint(p, area.color, g);
        }
    }

    private void displayPoint(Point p, Color color, Graphics2D g) {
        Point coordinates = findCoordinates(p);
        g.setColor(color);
        g.drawOval(coordinates.x - VERTEX_PIXEL_DIAMETER/2,
                   coordinates.y - VERTEX_PIXEL_DIAMETER/2,
                   VERTEX_PIXEL_DIAMETER, VERTEX_PIXEL_DIAMETER);
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
            return new Point((point.x+CELL_SIDE_PIXEL_LENGTH/2)/cellSize(),
                             (point.y+CELL_SIDE_PIXEL_LENGTH/2)/cellSize());
        }

    }   
    
}
