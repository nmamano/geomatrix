/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.business.controllers.AreaController;
import javax.swing.JPanel;

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

    public MapPanel(AreaController areaController, int selectedArea, boolean isArea1Displayed, boolean isArea2Displayed, boolean isArea3Displayed) {
        this.areaController = areaController;
        this.selectedArea = selectedArea;
        this.isArea1Displayed = isArea1Displayed;
        this.isArea2Displayed = isArea2Displayed;
        this.isArea3Displayed = isArea3Displayed;
    }
    
    
}
