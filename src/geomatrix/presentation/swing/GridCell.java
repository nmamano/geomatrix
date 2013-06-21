/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

/**
 *
 * @author Nil
 */
public class GridCell {
    public int x;
    public int y;

    public GridCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GridCell other = (GridCell) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    
}
