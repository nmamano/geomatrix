/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.controllers;

import geomatrix.business.models.binary.geomatrix.Geomatrix;
import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class AreaController extends BusinessController {

    private Geomatrix area1, area2, area3;
    public AreaController(JAXBDataController data) {
        super(data);
        area1 = new Geomatrix();
        area2 = new Geomatrix();
        area3 = new Geomatrix();
    }
    
    
    
}
