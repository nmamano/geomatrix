/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix;

import geomatrix.business.controllers.AreaController;
import geomatrix.presentation.swing.MainFrame;
import manticore.Application;
import manticore.Debug;
import manticore.data.DataController;
import manticore.data.JAXBDataController;
import manticore.presentation.PresentationController;
import manticore.presentation.SwingController;

/**
 *
 * @author Nil
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Debug.enable();
        
        // Create a new Application
        Application app = new Application();
        
        // Set the data controller
        JAXBDataController dataController = new DataController();
        app.setDataController(dataController);
        
        // Add business controllers
        app.addBusiness(AreaController.class);
        
        // Add presentation controllers
        PresentationController swingController = new SwingController(MainFrame.class);
        app.addPresentation(swingController);
        
        // Run application
        app.init();
    }
}
