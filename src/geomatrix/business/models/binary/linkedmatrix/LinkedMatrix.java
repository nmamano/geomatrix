
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

    /**
     * Constructor from another linked matrix.
     * @param matrix linked matrix this matrix will be a copy of.
     */
    private LinkedMatrix(LinkedMatrix matrix) {
        this(matrix.maxX, matrix.maxY);
        for (Object nextCell : matrix) {
            Cell cell = (Cell) nextCell;
            add(cell);
        }
    }
    
    /**
     * Constructor of empty cell set. maxX and maxY determine the maximum size
     * of the matrix thorough its life time.
     * @param maxX
     * @param maxY 
     */
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
        if (cellBoolean.equals(first) && cellBoolean.equals(last)) {
            last = first = null;
        }
        //case: cell is the first element
        else if (cellBoolean.equals(first)) {
            first = first.next;
            first.previous = null;
        }
        //case: cell is the last element
        else if (cellBoolean.equals(last)) {
            last = last.previous;
            last.next = null;
        }
        else {
            cellBoolean.previous.next = cellBoolean.next;
            cellBoolean.next.previous = cellBoolean.previous;     
        }
    }
    
    /**
     * Returns whether this linked matrix is consistent.
     * A linked matrix is consistent if all its linked cells are consistent and
     * the iteration goes trough them all.
     * @return true if this linked matrix is consistent. false otherwise.
     */
    public boolean valid() {
        return areCellsValid() && isListExhaustive();
    }

    /**
     * Returns whether all linked cells are valid.
     * @return true if all linked cells are valid. false otherwise.
     */
    private boolean areCellsValid() {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                boolean isFirst = cellArray[i][j].equals(first);
                boolean isLast = cellArray[i][j].equals(last);
                if (! cellArray[i][j].valid(i, j, isFirst, isLast)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns whether the iteration visits all contained cells.
     * It also returns false if the iteration happens to fall in a loop.
     * @return true if the iteration visits all contained cells. False
     * otherwise.
     */
    private boolean isListExhaustive() {
        boolean[][] visitedCells;
        try {
            visitedCells = markVisitedCells();
        }
        catch (IteratorLoopException e) {
            //list is cyclic
            return false;
        }
        return doVisitedCellsMatch(visitedCells);  
    }

    /**
     * Marks the cells visited by the iterator (that is, the contained cells) in
     * a matrix of booleans.
     * @return the matrix of booleans with the mapping of visited cells by the
     * iterator.
     * @throws IteratorLoopException 
     */
    private boolean[][] markVisitedCells() throws IteratorLoopException {
        boolean[][] containedCells = new boolean[maxX][maxY];
        for (Object nextCell : this) {
            Cell cell = (Cell) nextCell;
            int x = cell.x;
            int y = cell.y;
            if (containedCells[x][y]) {
                //list has a cycle!
                throw new IteratorLoopException();
            }
            containedCells[x][y] = true;
        }
        return containedCells;
    }

    /**
     * Returns whether the cells marked by cellArray and containedCells are the
     * same.
     * @param visitedCells a matrix of booleans indicating the visited cells by
     * the iterator.
     * @return true if the visited cells and the contained cells match. false
     * otherwise.
     */
    private boolean doVisitedCellsMatch(boolean[][] visitedCells) {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                if (cellArray[i][j].contained != visitedCells[i][j]) {
                    return false;
                }
            }
        }
        return true;
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
