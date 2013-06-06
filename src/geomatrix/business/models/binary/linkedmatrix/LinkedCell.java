
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

    /**
     * Returns whether this linked cell is consistent.
     * For a linked cell to be consistent, the coordinates of its cell must
     * be the same as the coordinates where it is located in the matrix.
     * If it is contained, previous and next can't be null, unlees it is the
     * first or last contained cell of the list.
     * If it is not contained, previous and next have to be null.
     * @param i the row coordinate in the matrix.
     * @param j the column coordinate in the matrix.
     * @param isFirst indicates if it is the first cell of the list.
     * @param isLast indicates if it is the last cell of the list.
     * @return 
     */
    boolean valid(int i, int j, boolean isFirst, boolean isLast) {
        if (i != cell.x || j != cell.y) return false;
        if (contained) {
            if (previous == null && ! isFirst ||
                    next == null && ! isLast) return false;
        }
        else {
            if (previous != null || next != null) return false;
        }
        return true;
    }
    
}
