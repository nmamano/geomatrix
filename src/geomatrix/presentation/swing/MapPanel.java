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
    
    private int selectedArea;
    private boolean isArea1Displayed;
    private boolean isArea2Displayed;
    private boolean isArea3Displayed;
    
    private static final int GRID_WIDTH = 30;
    private static final int GRID_DEPTH = 30;
    
    private static final int CELL_SIDE_PIXEL_LENGTH = 16;
    private static final int VERTEX_PIXEL_DIAMETER = 4;
    
    
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
        selectedArea = 1;
        isArea1Displayed = true;
        isArea2Displayed = true;
        isArea3Displayed = true;
        this.addMouseListener(new SelectGridPointListener());
        repaint();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(BACKGROUND_GRID_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        Graphics2D g2D = (Graphics2D) g;
        drawGrid(g2D);
        if (isArea1Displayed) displayArea(1, g2D);
        if (isArea2Displayed) displayArea(2, g2D);
        if (isArea3Displayed) displayArea(3, g2D);
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
        
        selectedArea = areaNumber;
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
        
        if (areaNumber == selectedArea && ! shouldBeDisplayed) {
            throw new HideSelectedAreaException();
        }
        if (areaNumber == 1) isArea1Displayed = shouldBeDisplayed;
        else if (areaNumber == 2) isArea2Displayed = shouldBeDisplayed;
        else isArea3Displayed = shouldBeDisplayed;
        
        repaint();
    }
    
    private int cellSize() {
        return Math.max(this.getWidth() / GRID_WIDTH,
                        this.getHeight() / GRID_DEPTH);
    } 

    private void displayArea(int areaNumber, Graphics2D g) {
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        HashSet<Point> vertexs = areaController.getVertexs(areaNumber);
        for (Point p : vertexs) {
            displayPoint(p, getColor(areaNumber), g);
        }
    }
    
    private Color getColor(int areaNumber) {
        assert(areaNumber >= 1 && areaNumber <= 3);
        
        if (areaNumber == 1) return AREA_1_COLOR;
        if (areaNumber == 2) return AREA_2_COLOR;
        return AREA_3_COLOR;
    }

    private void displayPoint(Point p, Color color, Graphics2D g) {
        Point coordinates = findCoordinates(p);
        g.setColor(color);
        g.drawOval(coordinates.x - VERTEX_PIXEL_DIAMETER/2,
                   coordinates.y - VERTEX_PIXEL_DIAMETER/2,
                   VERTEX_PIXEL_DIAMETER, VERTEX_PIXEL_DIAMETER);
    }

    private Point findCoordinates(Point p) {
        return new Point(p.x*cellSize(), p.y*cellSize());
    }
    
    private class SelectGridPointListener extends MouseAdapter {

        public SelectGridPointListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point clickedVertex = getClosestVertex(e.getPoint());
            if (areaController.areaContainsVertex(selectedArea, clickedVertex)) {
                areaController.removeVertexFromArea(selectedArea, clickedVertex);
            }
            else {
                areaController.addVertexToArea(selectedArea, clickedVertex);
            }
            
            repaint();
        }

        private Point getClosestVertex(Point point) {
            return new Point((point.x+CELL_SIDE_PIXEL_LENGTH/2)/cellSize(),
                             (point.y+CELL_SIDE_PIXEL_LENGTH/2)/cellSize());
        }

    }   
    
}
