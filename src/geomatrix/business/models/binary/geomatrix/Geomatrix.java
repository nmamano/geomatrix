/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Area;
import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.CellSet;
import geomatrix.business.models.binary.Point;
import geomatrix.business.models.binary.Rectangle;
import geomatrix.utils.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Geomatrix implementation of the master interface.
 * @author Nil
 */
public class Geomatrix implements Area {

    private List<Point> vertexs;
    private List<VerticalEdge> verticalEdges;
    private List<HorizontalEdge> horizontalEdges;
    
    /**
     * Given first x' value, permits fast access to all vertexs of the area
     * of the form (x',y).
     */
    private HashMap<Integer,List<Point>> vertexsStoredByX;
    
    /**
     * Given first y' value, permits fast access to all vertexs of the area
     * of the form (x,y').
     */
    private HashMap<Integer,List<Point>> vertexsStoredByY;
    
    /**
     * Given first x' value, permits fast access to all vertical edges of the area
     * of the form (x',y).
     */
    private HashMap<Integer,List<VerticalEdge>> verticalEdgesStoredByX;
 
    /**
     * Given first y' value, permits fast access to all horizontal edges of the area
     * of the form (x,y').
     */
    private HashMap<Integer,List<HorizontalEdge>> horizontalEdgesStoredByY;
    
    /**
     * Empty constructor.
     */
    public Geomatrix() {
        this(new ArrayList<Point>());
    }
    
    private Geomatrix(ArrayList<Point> vertexs) {
        this.vertexs = new ArrayList<Point>(vertexs);
        initializeDataStructsFromVertexs();
    }
    
    @Override
    public boolean contains(Cell cell) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(CellSet other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void union(CellSet other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void intersection(CellSet other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void difference(CellSet other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Cell> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
        @Override
    public void translation(Point p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void verticalReflection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void horizontalReflection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rotation(int degrees) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Rectangle getBoundingRectangle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Rectangle> descomposition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<Point> iterator(Rectangle rectangle) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void symmetricDifference(CellSet other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initializeDataStructsFromVertexs() {
        initializeMapsOfVertexs();
        initializeEdges();
        initializeMapsOfEdges();
    }

    /**
     * Initializes vertexsStoredByX and vertexsStoredByY.
     */
    private void initializeMapsOfVertexs() {
        vertexsStoredByX = new HashMap<Integer,List<Point>>();
        vertexsStoredByY = new HashMap<Integer,List<Point>>();
        
        for (Point p : vertexs) {
            if (!vertexsStoredByX.containsKey(p.x))
                vertexsStoredByX.put(p.x, new ArrayList<Point>());
            vertexsStoredByX.get(p.x).add(p);
            
            if (!vertexsStoredByY.containsKey(p.y))
                vertexsStoredByY.put(p.y, new ArrayList<Point>());
            vertexsStoredByY.get(p.y).add(p);
        }
    }

    /**
     * Initializes the vertical and horizontal edges.
     * Pre: vertexsStoredByX and vertexsStoredByY have been initialized.
     */
    private void initializeEdges() {
        verticalEdges = new ArrayList<VerticalEdge>();
        horizontalEdges = new ArrayList<HorizontalEdge>();
        Map<Point,Boolean> visitedVertexs = new HashMap<Point,Boolean>();
        for (Point vertex : vertexs) {
            visitedVertexs.put(vertex, Boolean.FALSE);
        }

        Iterator<Point> it = vertexs.iterator();
        while (it.hasNext()) {
            
            Point vertex = it.next();
            while (! visitedVertexs.get(vertex)) {
                //in each iteration of this loop, we add a vertical and a
                //horizontal edge with a common associated vertex
                
                visitedVertexs.put(vertex, Boolean.TRUE);                
                VerticalEdge verticalEdge = findVerticalEdge(vertex);
                verticalEdges.add(verticalEdge);
                vertex = verticalEdge.getOtherEndpoint(vertex);
                
                visitedVertexs.put(vertex, Boolean.TRUE);
                HorizontalEdge horizontalEdge = findHorizontalEdge(vertex);
                horizontalEdges.add(horizontalEdge);
                vertex = horizontalEdge.getOtherEndpoint(vertex);
            }           
        }
    }

    /**
     * Given a vertex, returns the vertical edge associated to it.
     * This method is used to initialize the edges. It is not intended to get
     * the associated vertical edge once the edges have been initialized.
     * @param vertex
     * @return 
     */
    private VerticalEdge findVerticalEdge(Point vertex) {
        Pair<Integer, Pair<Point, Point>> result = countVertexsSouthAndGetClosestSouthAndNorth(vertex);
        int southCount = result.first;
        Point closestSouth = result.second.first;
        Point closestNorth = result.second.second;
        if (isTopEndpoint(southCount)) {
            return new VerticalEdge(vertex.x, vertex.y, closestSouth.y);
        }
        else {
            return new VerticalEdge(vertex.x, vertex.y, closestNorth.y);
        }
    }
        
    /**
     * Returns the number of vertexs directly south of 'vertex' and the closest
     * vertexs directly south and north of 'vertex' or null if there is none.
     * @param vertex
     * @return the number of vertexs directly south of 'vertex' and the closest
     * vertexs directly south and north of 'vertex' or null if there is none.
     */
    private Pair<Integer, Pair<Point, Point>> countVertexsSouthAndGetClosestSouthAndNorth(Point vertex) {
                                              
        int southCount = 0;
        Point closestSouth = null;
        Point closestNorth = null;
        for(Point p : vertexsStoredByX.get(vertex.x)) {
            if (p.y > vertex.y) {
                ++southCount;
                if (closestSouth == null || closestSouth.y > p.y) {
                    closestSouth = p;
                }
            }
            else if (p.y < vertex.y) {
                if (closestNorth == null || closestNorth.y < p.y) {
                    closestNorth = p;
                }
            }
        }
        return new Pair<Integer, Pair<Point, Point>>(southCount,
                new Pair<Point, Point>(closestSouth, closestNorth));
    }
    
    /**
     * Returns whether a vertex with 'southCount' other vertexes directly below
     * it is the top endpoint of its associated vertical edge.
     * A vertex is the top endpoint of its associated vertical edge if there is
     * an odd number of vertexes directly below it.
     * @param southCount Indicates how many vertexes there are directly below
     * the vertex.
     * @return true if a vertex with 'southCount' other vertexes directly below
     * it is the top endpoint of its associated vertical edge. false otherwise.
     */
    private boolean isTopEndpoint(int southCount) {
        return southCount%2 == 1;
    }
         
    /**
     * Given a vertex, returns the horizontal edge associated to it.
     * This method is used to initialize the edges. It is not intended to get
     * the associated horizontal edge once the edges have been initialized.
     * @param vertex
     * @return 
     */
    private HorizontalEdge findHorizontalEdge(Point vertex) {
        Pair<Integer, Pair<Point, Point>> result = countVertexsEastAndGetClosestEastAndWest(vertex);
        int eastCount = result.first;
        Point closestEast = result.second.first;
        Point closestWest = result.second.second;
        if (isLeftEndpoint(eastCount)) {
            return new HorizontalEdge(vertex.y, vertex.x, closestEast.x);
        }
        else {
            return new HorizontalEdge(vertex.y, vertex.x, closestWest.x);
        }
    }

    /**
     * Returns the number of vertexs directly east of 'vertex' and the closest
     * vertexs directly east and west of 'vertex' or null if there is none.
     * @param vertex
     * @return the number of vertexs direectly east of 'vertex' and the closest
     * vertexs directly east and west of 'vertex' or null if there is none.
     */
    private Pair<Integer, Pair<Point, Point>> countVertexsEastAndGetClosestEastAndWest(Point vertex) {
                                              
        int eastCount = 0;
        Point closestEast = null;
        Point closestWest = null;
        for(Point p : vertexsStoredByY.get(vertex.y)) {
            if (p.x > vertex.x) {
                ++eastCount;
                if (closestEast == null || closestEast.x > p.x) {
                    closestEast = p;
                }
            }
            else if (p.x < vertex.x) {
                if (closestWest == null || closestWest.x < p.x) {
                    closestWest = p;
                }
            }
        }
        return new Pair<Integer, Pair<Point, Point>>(eastCount,
                new Pair<Point, Point>(closestEast, closestWest));
    }

    /**
     * Returns whether a vertex with 'eastCount' other vertexes directly to the
     * right of it is the left endpoint of its associated horizontal edge.
     * A vertex is the left endpoint of its associated vertical edge if there is
     * an odd number of vertexes directly east of it.
     * @param eastCount Indicates how many vertexes there are directly east of
     * the vertex.
     * @return true if a vertex with 'eastCount' other vertexes directly east of
     * it is the left endpoint of its associated horizontal edge. false
     * otherwise.
     */
    private boolean isLeftEndpoint(int eastCount) {
        return eastCount%2 == 1;
    }

    /**
     * Initializes verticalEdgesStoredByX and verticalEdgesStoredByY.
     */
    private void initializeMapsOfEdges() {
        verticalEdgesStoredByX = new HashMap<Integer,List<VerticalEdge>>();
        horizontalEdgesStoredByY = new HashMap<Integer,List<HorizontalEdge>>();
        
        for (VerticalEdge e : verticalEdges) {
            if (!verticalEdgesStoredByX.containsKey(e.x))
                verticalEdgesStoredByX.put(e.x, new ArrayList<VerticalEdge>());
            verticalEdgesStoredByX.get(e.x).add(e);
        }
        for (HorizontalEdge e : horizontalEdges) {
            if (!horizontalEdgesStoredByY.containsKey(e.y))
                horizontalEdgesStoredByY.put(e.y, new ArrayList<HorizontalEdge>());
            horizontalEdgesStoredByY.get(e.y).add(e);
        }
    }

}
