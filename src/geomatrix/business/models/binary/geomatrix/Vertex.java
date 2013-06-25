/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.GridPoint;
import geomatrix.utils.DiagonalDirection;

/**
 * A convex vertex has exactly one adjacent contained cell: the one in
 * 'differentDirection' (the direction in which the adjacent cell containment is
 * different than the rest).
 * A concave vertex has exactly three adjacent contained cells: all except the
 * one in 'differentDirection'.
 * @author Nil
 */
public class Vertex extends GridPoint {

    /**
     * The direction in which the adjacent cell containment is different than
     * the rest.
     */
    private DiagonalDirection differentDirection;
    private boolean isConvex;
    public Vertex(int x, int y) {
        super(x, y);
    }
    
    public void setAdjacentCells(DiagonalDirection dir, boolean b) {
        this.differentDirection = dir;
        isConvex = b;
    }
    
    public boolean isAdjacentCellContained(DiagonalDirection dir) {
        if (isConvex) return this.differentDirection == dir;
        else return this.differentDirection != dir;
    }
    
    
}
