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
import geomatrix.business.models.binary.VerticalSegment;
import geomatrix.utils.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Area implementation of the master interface.
 * @author Nil
 */
public class Geomatrix implements Area {

    private List<Point> vertexs;
    private List<VerticalEdge> verticalEdges;
    private List<HorizontalEdge> horizontalEdges;
    
    /**
     * This list contains one arbitrary vertex from each path of this area.
     * A path is a closed cicle of edges.
     */
    private List<Point> pathRepresentatives;
    
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
        build(new ArrayList<Point>());
    }
    
    /**
     * Initializes the Geomatrix from the list of its vertexs.
     * @param vertexs the vertexs that this geomatrix has to have.
     */
    private void build(List<Point> vertexs) {
        this.vertexs = new ArrayList<Point>(vertexs);
        initializeDataStructsFromVertexs();
    }
    
    @Override
    public boolean contains(Cell cell) {
        //apply ray-casting algorithm; direction: positive side of the x axis
        int intersectionCount = 0;
        LeftWideRay ray = new LeftWideRay(cell);
        
        for (VerticalEdge edge : verticalEdges) {
            if (edge.intersects(ray)) ++intersectionCount;
            if (edge.x > ray.origin.x) break;
        }
        
        //return result according to the odd-even rule
        return (intersectionCount % 2) == 1;
    }

    @Override
    public boolean contains(CellSet c) {
        assert(c instanceof Geomatrix);
        Geomatrix other = (Geomatrix) c;
        
        if (other.isEmpty()) return true;
        
        //1) check whether one vertex of other is contained in this
        if (! contains(other.vertexs.get(0)))
            return false;
        
        //2) check that no edge of other intersects with an edge of this
        //area
        if (doEdgesIntersect(other)) return false;
        
        //3) check that no path of this geomatrix is within other.
        for (Point vertex : pathRepresentatives)
            if (other.contains(vertex)) return false;

        return true; 
    }

    @Override
    public void union(CellSet c) {
        assert(c instanceof Geomatrix);
        Geomatrix other = (Geomatrix) c;
        
        if (isEmpty()) {
            build(other.vertexs);
            return;
        }
            
        Set<Point> newAreaVertexs = new HashSet<Point>();
                
        //1) add intersections between this area and a, except double
        //intersections
        List<Point> intersectPoints = getEdgesIntersect(other);
        for (Point v : intersectPoints) {
            if (newAreaVertexs.contains(v))
                newAreaVertexs.remove(v);
            else
                newAreaVertexs.add(v);
        }     
        
        //2) find vextexs that have an odd number of adjacent contained squares
        //in either area
        for (Point myVertex : vertexs) {
            List<Boolean> otherAdjCells = other.areAdjacentCellsContained(myVertex);
            
            if (noneTrue(otherAdjCells)) {
                //if this vertex is disjoint from other, it is a vertex of the
                //union
                newAreaVertexs.add(myVertex);
            }
            //if this vertex is contained in other, it is not a vertex of
            //the union
            else if (!allTrue(otherAdjCells)) {
                List<Boolean> thisAdjCells = areAdjacentCellsContained(myVertex);
                int count = 0;
                if (thisAdjCells.get(0) || otherAdjCells.get(0)) ++count;
                if (thisAdjCells.get(1) || otherAdjCells.get(1)) ++count;
                if (thisAdjCells.get(2) || otherAdjCells.get(2)) ++count;
                if (thisAdjCells.get(3) || otherAdjCells.get(3)) ++count;
                if (count%2 == 1) newAreaVertexs.add(myVertex);
            }
        }
        
        for (Point otherVertex : other.vertexs) {
            List<Boolean> thisAdjCells = areAdjacentCellsContained(otherVertex);
            
            if (noneTrue(thisAdjCells)) {
                //if otherVertex is disjoint from this area, it is a vertex of 
                //the union
                newAreaVertexs.add(otherVertex);
            }
            //if otherVertex is contained in this area, it is not a vertex of
            //the union
            else if (!allTrue(thisAdjCells)) {
                List<Boolean> otherAdjCells = other.areAdjacentCellsContained(otherVertex);
                int count = 0;
                if (thisAdjCells.get(0) || otherAdjCells.get(0)) ++count;
                if (thisAdjCells.get(1) || otherAdjCells.get(1)) ++count;
                if (thisAdjCells.get(2) || otherAdjCells.get(2)) ++count;
                if (thisAdjCells.get(3) || otherAdjCells.get(3)) ++count;
                if (count%2 == 1) newAreaVertexs.add(otherVertex);
            }
        }
        
        build(new ArrayList(newAreaVertexs));
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
        initializeEdgesAndPathRepresentatives();
        sortVerticalEdges();
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
     * Initializes the vertical and horizontal edges and path representatives.
     * To initialize the path representatives, an arbitrary vertex from each
     * path is stored in pathRepresentatives.
     * Pre: vertexsStoredByX and vertexsStoredByY have been initialized.
     * 
     */
    private void initializeEdgesAndPathRepresentatives() {
        verticalEdges = new ArrayList<VerticalEdge>();
        horizontalEdges = new ArrayList<HorizontalEdge>();
        pathRepresentatives = new ArrayList<Point>();
        Map<Point,Boolean> visitedVertexs = new HashMap<Point,Boolean>();
        for (Point vertex : vertexs) {
            visitedVertexs.put(vertex, Boolean.FALSE);
        }

        Iterator<Point> it = vertexs.iterator();
        while (it.hasNext()) {
            
            Point vertex = it.next();
            if (! visitedVertexs.get(vertex)) {
                pathRepresentatives.add(vertex);
            }
            
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
     * Sorts vertical edges by their x coordinate.
     */
    private void sortVerticalEdges() {
        Collections.sort(verticalEdges);
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
    
    /**
     * Returns whether a given grid point is contained in the area.
     * @param point the grid point which might be contained.
     * @return true if point is contained in the area. false otherwise.
     */
    private boolean contains(Point point) {
        List<Boolean> adjacentSquares = areAdjacentCellsContained(point);
        for (Boolean b : adjacentSquares)
            if (! b) return false; 
        
        return true;
    }

    /**
     * Given a grid point, returns whether each of the four adjacent cells is
     * contained.
     * This is optimized acording to the following observation:
     * to see if a cell is contained, if it is known whether an adjacent
     * cell is contained or not, it must only be checked whether there
     * is an edge between them.
     * @param point the point the contention of the adjacent cells of which is
     * to be checked.
     * @return list with exactly 4 booleans indicating whether the top left 
     * cell, top right cell, bottom left cell and bottom right cell are
     * contained, in this order.
     */
    private List<Boolean> areAdjacentCellsContained(Point point) {
                
        // +-------------+-------------+
        // |             |             |
        // |             |             |
        // |             u             |
        // |   topLeft   p  topRight   |
        // |             E             |
        // |             |             |
        // |             |             |
        // +----leftE----p---rightE----+
        // |             |             |
        // |             d             |
        // |             o             |
        // | bottomLeft  w bottomRight |
        // |             n             |
        // |             E             |
        // |             |             |
        // +-------------+-------------+
             
        //one boolean for each adjacent cell: true if they are contained,
        //false otherwise
        boolean topLeft, topRight, bottomRight, bottomLeft;
        //one boolean for each adjacent line segment: true if they are part
        //of an edge, false otherwise
        //upE is not needed: we will reach the top left cell through the bottom
        //left cell
        boolean rightE, downE, leftE;
        rightE = downE = leftE = false;
        
        //find which line segments are edges
        if (verticalEdgesStoredByX.containsKey(point.x)) {
            for (VerticalEdge v : verticalEdgesStoredByX.get(point.x))
                if (v.contains(new VerticalSegment(point.x, point.y+1, point.y)))
                    downE = true;
        }
        if (horizontalEdgesStoredByY.containsKey(point.y)) {
            for (HorizontalEdge v : horizontalEdgesStoredByY.get(point.y)) {
                if (v.contains(new HorizontalEdge(point.y, point.x+1, point.x)))
                    rightE = true;
                if (v.contains(new HorizontalEdge(point.y, point.x, point.x-1)))
                    leftE = true;
            }
        }
        
        //check if bottomRight is contained with usual method
        bottomRight = contains(new Cell(point.x, point.y));
        
        //bottomLeftSq will be the same as bottomRightSq unless downE is true
        if (downE) bottomLeft = ! bottomRight;
        else bottomLeft = bottomRight;
        
        //topRightSq will be the same as bottomRightSq unless rightE is true
        if (rightE) topRight = ! bottomRight;
        else topRight = bottomRight;
        
        //topLeftSq will be the same as bottomLeftSq unless leftE is true
        if (leftE) topLeft = ! bottomLeft;
        else topLeft = bottomLeft;
        
        List<Boolean> result = new ArrayList<Boolean>();
        result.add(topLeft);
        result.add(topRight);
        result.add(bottomLeft);
        result.add(bottomRight);
        
        return result;
    }
    
    /**
     * Returns whether 2 edges of different areas intersect somewhere in the
     * plane.
     * @param other
     * @return 
     */
    private boolean doEdgesIntersect(Geomatrix other) {
        for (VerticalEdge myEdge : verticalEdges) {
            boolean betterByCoords = shouldIterateEdgesByCoords(myEdge.yInterval.size(), other.horizontalEdges.size());
            if (! betterByCoords) {
                for (HorizontalEdge otherEdge : other.horizontalEdges) {
                    if (myEdge.intersects(otherEdge)) return true;
                }
            }
            else {
                for (int y = myEdge.yInterval.low+1; y < myEdge.yInterval.high; ++y) {
                    for (HorizontalEdge otherEdge : other.horizontalEdgesStoredByY.get(y)) {
                        if (myEdge.intersects(otherEdge)) return true;
                    }
                }
            }
        }
        
        for (VerticalEdge otherEdge : other.verticalEdges) {
            boolean betterByCoords = shouldIterateEdgesByCoords(otherEdge.yInterval.size(), horizontalEdges.size());
            if (! betterByCoords) {
                for (HorizontalEdge myEdge : horizontalEdges) {
                    if (otherEdge.intersects(myEdge)) return true;
                }
            }
            else {
                for (int y = otherEdge.yInterval.low+1; y < otherEdge.yInterval.high; ++y) {
                    for (HorizontalEdge myEdge : horizontalEdgesStoredByY.get(y)) {
                        if (otherEdge.intersects(myEdge)) return true;
                    }
                }                
            }
        }
        
        return false;    
    }

    /**
     * In the context of finding the edge intersections of an edge e in respect
     * to the set of edges S of the opposed kind, returns whether it is more
     * efficient to check only the edges of S such that their fixed coordinate
     * falls within the interval of coordinates of e (with the
     * edgesSortedByCoordinate HashMaps) or to check all edges in S.
     * This second case is preferable when e is very large in comparison
     * to the size of S.
     * The optimum choice depends on the particular distribution of the edges of
     * S, so this method returns only an estimation. In particular, returns that
     * it is preferable to iterate through the coordinate-matching edges instead
     * of through all vertexs if the lentgh of e is smaller than 3/4 of the size
     * of S
     * @param edgeLength the lentgh of e.
     * @param numberOfEdges the size of S.
     * @return true if it is more efficient to check only the
     * coordinate-matching edges instead of all edges.
     */
    private boolean shouldIterateEdgesByCoords(int edgeLength, int numberOfEdges) {
        return edgeLength < (3*numberOfEdges)/4;
    }
        
    /**
     * Returns all grid points where 2 edges of this area and other intersect
     * somewhere in the grid.
     * @param other
     * @return 
     */
    private List<Point> getEdgesIntersect(Geomatrix other) {
        List<Point> intersectionPoints = new ArrayList<Point>();
        
        for (VerticalEdge myEdge : verticalEdges) {
            boolean betterByCoords = shouldIterateEdgesByCoords(myEdge.yInterval.size(), other.horizontalEdges.size());
            if (! betterByCoords) {
                for (HorizontalEdge otherEdge : other.horizontalEdges) {
                    if (myEdge.intersects(otherEdge)) {
                        intersectionPoints.add(myEdge.getIntersection(otherEdge));
                    }
                }
            }
            else {
                for (int y = myEdge.yInterval.low+1; y < myEdge.yInterval.high; ++y) {
                    for (HorizontalEdge otherEdge : other.horizontalEdgesStoredByY.get(y)) {
                        if (myEdge.intersects(otherEdge)) {
                            intersectionPoints.add(myEdge.getIntersection(otherEdge));
                        }
                    }
                }
            }
        }
        
        for (VerticalEdge otherEdge : other.verticalEdges) {
            boolean betterByCoords = shouldIterateEdgesByCoords(otherEdge.yInterval.size(), horizontalEdges.size());
            if (! betterByCoords) {
                for (HorizontalEdge myEdge : horizontalEdges) {
                    if (otherEdge.intersects(myEdge)) {
                        intersectionPoints.add(myEdge.getIntersection(otherEdge));
                    }
                }
            }
            else {
                for (int y = otherEdge.yInterval.low+1; y < otherEdge.yInterval.high; ++y) {
                    for (HorizontalEdge myEdge : horizontalEdgesStoredByY.get(y)) {
                        if (otherEdge.intersects(myEdge)) {
                            intersectionPoints.add(myEdge.getIntersection(otherEdge));
                        }
                    }
                }                
            }
        }
        
        return intersectionPoints;
    }

    private boolean isEmpty() {
        return vertexs.isEmpty();
    }

    private boolean noneTrue(List<Boolean> booleans) {
        for (Boolean b : booleans) if (b) return false;
        return true;
    }

    private boolean allTrue(List<Boolean> booleans) {
        for (Boolean b : booleans) if (! b) return false;
        return true;
    }

}
