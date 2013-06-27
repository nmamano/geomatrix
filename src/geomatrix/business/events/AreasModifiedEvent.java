/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.events;

import geomatrix.gridplane.Cell;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.HorizontalSegment;
import geomatrix.gridplane.Line;
import geomatrix.gridplane.UnitarySegment;
import geomatrix.gridplane.VerticalSegment;
import geomatrix.presentation.swing.AreaElements;
import java.awt.Color;
import java.util.Collection;
import manticore.Event;

/**
 *
 * @author Nil
 */
public class AreasModifiedEvent implements Event {

    public AreaElements area1;
    public AreaElements area2;
    public AreaElements area3;

    public AreasModifiedEvent(AreaElements area1, AreaElements area2, AreaElements area3) {
        this.area1 = area1;
        this.area2 = area2;
        this.area3 = area3;
    }

}
