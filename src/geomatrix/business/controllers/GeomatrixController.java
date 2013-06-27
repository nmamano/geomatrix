/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.events.AreaModifiedEvent;
import geomatrix.business.models.binary.geomatrix.PointSet;
import java.util.ArrayList;
import java.util.List;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class GeomatrixController extends BusinessController {
    
    private int selected;
    private PointSet[] points;
    
    public GeomatrixController(JAXBDataController data) {
        super(data);
        points = new PointSet[3];
    }

    public void setSelected(int i) {
        selected = i;
        notifyAreaModified(i);
    }

    private void notifyAreaModified(int ID) {
        notify(new AreaModifiedEvent(ID, points[ID].isValidArea(),
                points[ID].getContainedCells(), points[ID].getPoints(),
                points[ID].getInvalidLines()));
    }
    
    
    
}
