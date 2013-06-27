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
public class AreaDisplayedEvent implements Event {

    public int areaID;
    public boolean displayed;

    public AreaDisplayedEvent(int areaID, boolean displayed) {
        this.areaID = areaID;
        this.displayed = displayed;
    }
    
}
