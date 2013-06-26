/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.utils;

/**
 *
 * @author Nil
 */
public abstract class Coordinates {
    public int x;
    public int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float distance(Coordinates other) {
        assert(this.getClass() == other.getClass());
        float xOffset = Math.abs(x - other.x);
        //-1 because squares are 1 unit wide
        float yOffset = Math.abs(y - other.y);
        return (float) Math.sqrt(xOffset*xOffset + yOffset*yOffset);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
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
        final Coordinates other = (Coordinates) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "(" + x + "," + y + ") ";
    }
    
    
    
    
}
