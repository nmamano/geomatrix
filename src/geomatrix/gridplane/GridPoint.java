
package geomatrix.gridplane;

import geomatrix.utils.Coordinates;

/**
 * Represents a grid point.
 * @author Nil
 */
public class GridPoint extends Coordinates {
    
    public GridPoint(int x, int y) {
        super(x,y);
    }
    
    public GridPoint copy() {
        return new GridPoint(x, y);
    }
}
