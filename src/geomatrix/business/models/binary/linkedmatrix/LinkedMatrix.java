
package geomatrix.business.models.binary.linkedmatrix;

import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.CellSet;
import java.util.Iterator;

/**
 * Implementation of CellSet with a linked matrix of booleans.
 * The cells migth have only positive coordinates in a fixed range.
 * @author Nil
 */
public class LinkedMatrix implements CellSet {

    private LinkedCell[][] cellArray;
    
    /**
     * Represents the maximum width of the matrix. The matrix can contain cells
     * from column 0 to column maxX-1
     */
    private int maxX;
    
    /**
     * Represents the maximum heigth of the matrix. The matrix can contain cells
     * from row 0 to row maxY-1
     */
    private int maxY;
    
    LinkedCell first;
    LinkedCell last;

    private LinkedMatrix(LinkedMatrix matrix) {
        this(matrix.maxX, matrix.maxY);
        for (Object nextCell : matrix) {
            Cell cell = (Cell) nextCell;
            add(cell);
        }
    }
    
    public LinkedMatrix(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        first = last = null;
        cellArray = new LinkedCell[maxX][maxY];
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                Cell cell = new Cell(i,j);
                cellArray[i][j] = new LinkedCell(cell);
            }
        }
    }
    
    /**
     * Pre: 0 <= cell.x < maxX and 0 <= cell.y < maxY
     * @param cell
     * @return 
     */
    @Override
    public boolean contains(Cell cell) {
        return cellArray[cell.x][cell.y].contained;
    }

    /**
     * Pre: for each cell <x,y> contained in other, 0 <= x < maxX and
     * 0 <= y < maxY
     * @param other
     * @return 
     */
    @Override
    public boolean contains(CellSet other) {
        for (Object nextCell : other) {
            Cell cell = (Cell) nextCell;
            if (! contains(cell)) return false;
        }
        return true;
    }

    /**
     * Pre: for each cell <x,y> contained in other, 0 <= x < maxX and
     * 0 <= y < maxY
     * @param other 
     */
    @Override
    public void union(CellSet other) {
        for (Object nextCell : other) {
            Cell cell = (Cell) nextCell;
            add(cell);
        }
    }

    /**
     * Pre: for each cell <x,y> contained in other, 0 <= x < maxX and
     * 0 <= y < maxY
     * @param other 
     */
    @Override
    public void intersection(CellSet other) {
        for (Object nextCell : this) {
            Cell cell = (Cell) nextCell;
            if (! other.contains(cell)) {
                remove(cell);
            }
        }
    }

    /**
     * Pre: for each cell <x,y> contained in other, 0 <= x < maxX and
     * 0 <= y < maxY
     * @param other 
     */
    @Override
    public void difference(CellSet other) {
        for (Object nextCell : other) {
            Cell cell = (Cell) nextCell;
            if (contains(cell)) {
                remove(cell);
            }
        }
    }

    @Override
    public Iterator<Cell> iterator() {
        return new LinkedMatrixIterator();
    }

    /**
     * Sets the cell 'cell' as contained.
     * @param cell the cell to be added to the set.
     */
    private void add(Cell cell) {
        if (contains(cell)) return;

        LinkedCell cellBoolean = cellArray[cell.x][cell.y];
        cellBoolean.contained = true;

        //case: matrix is empty
        if (last == null) {
            last = first = cellBoolean;
        }
        else {
            last.next = cellBoolean;
            cellBoolean.previous = last;
            last = cellBoolean;    
        }
    }

    /**
     * Sets the cell 'cell' as not contained.
     * @param cell the cell to be removed from the set.
     */
    private void remove(Cell cell) {
        if (! contains(cell)) return;

        LinkedCell cellBoolean = cellArray[cell.x][cell.y];
        cellBoolean.contained = false;

        //case: cell is the only element
        if (cellBoolean == first && cellBoolean == last) {
            last = first = null;
        }
        //case: cell is the first element
        else if (cellBoolean == first) {
            first = first.next;
            first.previous = null;
        }
        //case: cell is the last element
        else if (cellBoolean == last) {
            last = last.previous;
            last.next = null;
        }
        else {
            cellBoolean.previous.next = cellBoolean.next;
            cellBoolean.next.previous = cellBoolean.previous;     
        }
    }

    /**
     * Represents an iterator through the contained cells.
     */
    private class LinkedMatrixIterator
        implements Iterator<Cell> {

        private LinkedCell current;
        
        public LinkedMatrixIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Cell next() {
            Cell currentCell = current.cell;
            current = current.next;
            return currentCell;
        }

        @Override
        public void remove() {
            LinkedCell startingCurrent = current;
            current = current.next;
            LinkedMatrix.this.remove(startingCurrent.cell);
        }
    }
    
}
