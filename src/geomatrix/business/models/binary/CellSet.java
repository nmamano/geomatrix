
package geomatrix.business.models.binary;

/**
 * A class implementing this interface represents a set of grid cells.
 * @author Nil
 */
public interface CellSet 
    extends Iterable {
    
    /**
     * Returns whether the cell cell is contained in the set.
     * @param cell cell whose presence in this set is to be tested.
     * @return true if cell is contained in this cell set.
     */
    boolean contains(Cell cell);
    
    /**
     * Returns whether all cells in cells are contained in this cell set.
     * @param cells cell set with the cells whose presence in this set is to be
     * tested.
     * @return true if every cell in cells is contained in this cell set.
     */
    boolean contains(CellSet cells);
    
    /**
     * This cell set becomes the union of this cell set and cells. cells is not
     * modified.
     * @param cells the cell set with which this cell set is to be united.
     */
    void union(CellSet cells);
    
    /**
     * This cell set becomes the intersection of this cell set and cells. cells
     * is not modified.
     * @param cells the cell set with which this cell set is to be intersected.
     */
    void intersection(CellSet cells);
    
    /**
     * This cell set becomes the difference of this cell set minus cells. cells
     * is not modified.
     * @param cells the cell set to be rested to this cell set.
     */    
    void difference(CellSet cells);
 
    /**
     * This cell set becomes the symmetric difference of this cell set and
     * cells. cells is not modified.
     * @param cells the cell set with which this cell set is to be applied the
     * symmetric difference.
     */   
    void symmetricDifference(CellSet cells);
    
    /**
     * Returns the cardinality of this set.
     * @return the cardinality of this set.
     */
    int size();
    
}
