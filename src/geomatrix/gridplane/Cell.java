
package geomatrix.gridplane;

import geomatrix.utils.Coordinates;

/**
 * A cell of the grid.
 * @author Nil
 */
public class Cell extends Coordinates {
    
    public Cell(int x, int y) {
        super(x,y);
    }

    public Cell copy() {
        return new Cell(x, y);
    }

}
