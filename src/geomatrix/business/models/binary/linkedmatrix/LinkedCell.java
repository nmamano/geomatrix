
package geomatrix.business.models.binary.linkedmatrix;

import geomatrix.business.models.binary.Cell;

/**
 * Represents a cell linked to a "next" cell and a "previous" cell.
 * @author Nil
 */
class LinkedCell {
    
    /**
     * previous only takes value in case the cell is contained.
     */
    LinkedCell previous;
    
    /**
     * next only takes value in case the cell is contained.
     */
    LinkedCell next;
    
    Cell cell;
    boolean contained;

    /**
     * Constructor.
     * previous and next are initialized null.
     * contained is initialized false.
     */
    public LinkedCell(Cell cell) {
        this.cell = cell;
    }
    
}
