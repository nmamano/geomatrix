/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.exceptions;

import manticore.business.BusinessController;
import manticore.data.JAXBDataController;

/**
 *
 * @author Nil
 */
public class ExampleController extends BusinessController {
    
    public ExampleController(JAXBDataController dataController) {
        super(dataController);
    }
    
    public void fireEvent() {
        // Business events are fired to notify changes to the presentation layer
        notify(new EventFired());
    }
    
}
