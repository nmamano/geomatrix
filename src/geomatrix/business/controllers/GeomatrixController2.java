/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.geomatrix.PointSet;
import java.util.ArrayList;
import java.util.List;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class GeomatrixController2 extends BusinessController {
    
    private PointSet[] points;
    
    public GeomatrixController2(JAXBDataController data) {
        super(data);
        points = new PointSet[3];
    }
    
    
    
}
