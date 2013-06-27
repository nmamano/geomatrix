/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.events;

import manticore.Event;

/**
 *
 * @author Nil
 */
public class AreaSelectedEvent implements Event {

    public int areaID;
    
    public AreaSelectedEvent(int areaID) {
        this.areaID = areaID;
    }
    
}
