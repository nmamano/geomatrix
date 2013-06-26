/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary;

import geomatrix.gridplane.Rectangle;
import geomatrix.gridplane.GridPoint;
import java.util.Iterator;
import java.util.List;

/**
 * It is a cell set but viewed as an area. It offer more geometric focused
 * methods.
 * @author Nil
 */
public interface ExtendedAPI
        extends MasterAPI {
    
    /**
     * This area is slided p.x cells in the x axis and p.y cells in the
     * y axis.
     * @param p the point with the distances the area has to be translated.
     */
    void translation(GridPoint p);
    
    /**
     * This area is reflected in the y axis. In other words, this area will
     * consist of all cells of the form <x,y> such that the cell <-x,y> is
     * contained in this area.
     */
    void verticalReflection();
    
    /**
     * This area is reflected in the x axis. In other worrds, this area will
     * consist of all cells of the form <x,y> such that the cell <x,-y> is
     * contained in this area.
     */
    void horizontalReflection();
    
    /**
     * This area is rotated 'degrees' degrees counter clockwise.
     * Pre: 'degrees' is multiple of 90.
     */
    void rotation(int degrees);
    
    /**
     * Returns the smallest rectangle that contains this area.
     * @return 
     */
    public Rectangle getBoundingRectangle();
    
    /**
     * Returns a list of disjoint rectangles such that every cell contained in
     * this area is within one of the rectangles, and every cell non-contained
     * in this area is not.
     * @return a list of disjoint rectangles that wrap all cells of this area.
     */
    List<Rectangle> decomposition();
    
    /**
     * Returns an iterator through the points p such that all cells within a
     * rectangle with the dimensions of 'rectangle' and its topLeft point in p
     * would be contained in this area.
     * @param cells
     * @return 
     */
    Iterator<GridPoint> iterator(Rectangle rectangle);
    
    /**
     * This cell set becomes the symmetric difference of this cell set and
     * cells. cells is not modified.
     * @param cells the cell set with which this cell set is to be applied the
     * symmetric difference.
     */   
    void symmetricDifference(MasterAPI other);
    
    /**
     * Returns the cardinality of this set.
     * @return the cardinality of this set.
     */
    int size();
    

}
