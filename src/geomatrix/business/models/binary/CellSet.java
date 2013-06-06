
package geomatrix.business.models.binary;

import java.util.Iterator;

/**
 * A class implementing this interface represents a set of grid cells.
 * The remove method of the iterator is optional.
 * @author Nil
 */
public interface CellSet 
    extends Iterable<Cell> {
    
    /**
     * Returns whether the cell cell is contained in the set.
     * @param cell cell whose presence in this set is to be tested.
     * @return true if cell is contained in this cell set.
     */
    boolean contains(Cell cell);
    
    /**
     * Returns whether all other in other are contained in this cell set.
     * @param other cell set with the other whose presence in this set is to be
     * tested.
     * @return true if every cell in other is contained in this cell set.
     */
    boolean contains(CellSet other);
    
    /**
     * This cell set becomes the union of this cell set and cells. cells is not
     * modified.
     * @param cells the cell set with which this cell set is to be united.
     */
    void union(CellSet other);
    
    /**
     * This cell set becomes the intersection of this cell set and cells. cells
     * is not modified.
     * @param cells the cell set with which this cell set is to be intersected.
     */
    void intersection(CellSet other);
    
    /**
     * This cell set becomes the difference of this cell set minus cells. cells
     * is not modified.
     * @param cells the cell set to be rested to this cell set.
     */    
    void difference(CellSet other);
    
    /**
     * Returns an iterator through the contained cells.
     * @return an iterator through the contained cells.
     */
    @Override
    Iterator<Cell> iterator();
    
}
