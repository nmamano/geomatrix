
package geomatrix.gridplane;

import geomatrix.gridplane.GridPoint;
import geomatrix.utils.Axis;
import geomatrix.utils.Interval;
import java.util.ArrayList;
import java.util.Collection;
import manticore.Debug;

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
     * @param other
     * @return 
     */
    public float distance(Rectangle other) {
        int xOffset = getXInterval().distance(other.getXInterval());
        int yOffset = getYInterval().distance(other.getYInterval());
        GridPoint origin = new GridPoint(0, 0);
        return origin.distance(new GridPoint(xOffset, yOffset));
    }
    
    /**
     * Returns the interval of x values between the vertical segments of this
     * rectangle.
     * @return an interval containing the x values between the vertical
     * segments.
     */
    public Interval getXInterval() {
        return new Interval(topLeft.x, bottomRight.x);
    }
    
    /**
     * Returns the interval of y values between the horizontal segments of this
     * rectangle.
     * @return an interval containing the y values between the horizontal
     * segments.
     */
    public Interval getYInterval() {
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

    public Collection<GridPoint> getVertexes() {
        Collection<GridPoint> vertexes = new ArrayList();
        vertexes.add(new GridPoint(topLeft.x, topLeft.y));
        vertexes.add(new GridPoint(bottomRight.x, bottomRight.y));
        vertexes.add(new GridPoint(topLeft.x, bottomRight.y));
        vertexes.add(new GridPoint(bottomRight.x, topLeft.y));
        return vertexes;
    }
    
    public Collection<Segment> getEdges() {
        Collection<Segment> edges = new ArrayList();
        edges.add(getLeftEdge());
        edges.add(getRightEdge());
        edges.add(getTopEdge());
        edges.add(getBottomEdge());
        return edges;    
    }
    
    public Segment getLeftEdge() {
        return new Segment(topLeft.x, getYInterval(), Axis.Vertical);
    }   
    
    public Segment getRightEdge() {
        return new Segment(bottomRight.x, getYInterval(), Axis.Vertical);
    }  
    
    public Segment getTopEdge() {
        return new Segment(topLeft.y, getXInterval(), Axis.Horizontal);
    }  
    
    public Segment getBottomEdge() {
        return new Segment(bottomRight.y, getXInterval(), Axis.Horizontal);
    }
    
    public int getWidth() {
        return bottomRight.x - topLeft.x;
    }

    public int getHeight() {
        return bottomRight.y - topLeft.y;
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
    
}
