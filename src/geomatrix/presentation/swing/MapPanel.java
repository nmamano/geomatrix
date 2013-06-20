/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.business.controllers.AreaController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    
    private static final int GRID_WIDTH = 40;
    private static final int GRID_DEPTH = 40;
    
    private static final Color COLOR_GRID = Color.decode("#EEEEEE");

    /**
     * Initialization.
     * All areas have no vertexs.
     * All areas are displayed.
     * Area 1 is selected.
     * @param swingController 
     */
    public MapPanel(SwingController swingController) {
        this.areaController = swingController.getBusinessController(AreaController.class);
        selectedArea = 1;
        isArea1Displayed = true;
        isArea2Displayed = true;
        isArea3Displayed = true;
        this.addMouseListener(new SelectGridPointListener());
        
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid((Graphics2D) g);
    }

    private void drawGrid(Graphics2D g) {        
        g.setColor(COLOR_GRID);
        
        for(int i = 0; i < GRID_WIDTH; ++i)
            g.drawLine(i, 0, i, GRID_DEPTH);
        
        for(int i = 0; i < GRID_DEPTH; ++i)
            g.drawLine(0, i, GRID_WIDTH, i);
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
    }

    private static class SelectGridPointListener extends MouseAdapter {

        public SelectGridPointListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
    
    
}
