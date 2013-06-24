
package geomatrix.business.models.binary;

import geomatrix.utils.Interval;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a rectangle of the grid.
 * It is represented by the coordinates of two grid points:
 * topLeft: the grid point where the left vertical edge and the top horizontal
 * edge coincide.
 * bottomRight: the grid point where the right vertical edge and the bottom
 * horizontal edge coincide.
 * @author Nil
 */
public class Rectangle {
    
    public GridPoint topLeft;
    public GridPoint bottomRight;

    public Rectangle(GridPoint topLeft, GridPoint bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    /**
     * Returns the distance between 2 Rectangles.
     * @param r1
     * @param r2
     * @return 
     */
    public static float distance(Rectangle r1, Rectangle r2) {
        int xOffset = r1.getXinterval().distance(r2.getXinterval());
        int yOffset = r1.getYinterval().distance(r2.getYinterval());
        return GridPoint.distance(new GridPoint(0,0), new GridPoint(xOffset, yOffset));
    }
    
    /**
     * Returns the interval of x values between the vertical segments of this
     * rectangle.
     * @return an interval containing the x values between the vertical
     * segments.
     */
    public Interval getXinterval() {
        return new Interval(topLeft.x, bottomRight.x);
    }
    
    /**
     * Returns the interval of y values between the horizontal segments of this
     * rectangle.
     * @return an interval containing the y values between the horizontal
     * segments.
     */
    public Interval getYinterval() {
        return new Interval(topLeft.y, bottomRight.y);
    }
    
    /**
     * Returns whether a rectangle is consistent.
     * For a rectangle to be consistent, the coordinates of topLeft must be lower
     * than the coordinates of bottomRight (thus ensuring it is not empty).
     * @return true if the rectangle is consistent. false otherwise.
     */
    boolean valid() {
        return topLeft.x < bottomRight.x && topLeft.y < bottomRight.y;
    }
    
    /**
     * In netbeans we trust.
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.topLeft != null ? this.topLeft.hashCode() : 0);
        hash = 79 * hash + (this.bottomRight != null ? this.bottomRight.hashCode() : 0);
        return hash;
    }

    /**
     * In netbeans we trust.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rectangle other = (Rectangle) obj;
        if (this.topLeft != other.topLeft && (this.topLeft == null || !this.topLeft.equals(other.topLeft))) {
            return false;
        }
        if (this.bottomRight != other.bottomRight && (this.bottomRight == null || !this.bottomRight.equals(other.bottomRight))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + topLeft.toString() + ", " + bottomRight.toString() + ']';
    }

    public Collection<Point> getVertexes() {
        Collection<Point> vertexes = new ArrayList();
        vertexes.add(new Point(topLeft.x, topLeft.y));
        vertexes.add(new Point(bottomRight.y, bottomRight.x));
        vertexes.add(new Point(topLeft.x, bottomRight.y));
        vertexes.add(new Point(topLeft.y, bottomRight.x));
        return vertexes;
    }
    
    
    
}
