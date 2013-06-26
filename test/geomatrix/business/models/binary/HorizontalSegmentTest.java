/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary;

import geomatrix.gridplane.WideRay;
import geomatrix.gridplane.VerticalSegment;
import geomatrix.gridplane.HorizontalSegment;
import geomatrix.gridplane.GridPoint;
import geomatrix.gridplane.Cell;
import geomatrix.utils.Direction;
import geomatrix.utils.Interval;
import geomatrix.utils.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nil
 */
public class HorizontalSegmentTest {
    
    private HorizontalSegment sampleSegment() {
        return new HorizontalSegment(2, new Interval(-1, 3));
    }
    
    private GridPoint sampleLeftEndPoint() {
        return new GridPoint(-1,2);
    }
    
    private GridPoint sampleRightEndPoint() {
        return new GridPoint(3,2);
    }
    
    private VerticalSegment sampleVerticalSegment() {
        return new VerticalSegment(1, new Interval(0, 3));
    }
    
    private GridPoint sampleIntersectionPoint() {
        return new GridPoint(1,2);
    }
    
    private int sampleY() {
        return 2;
    }
    
    public HorizontalSegmentTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getOtherEndpoint method, of class HorizontalSegment.
     */
    @Test
    public void testGetOtherEndpoint() {
        System.out.println("getOtherEndpoint");
        GridPoint endpoint = sampleRightEndPoint();
        HorizontalSegment instance = sampleSegment();
        GridPoint expResult = sampleLeftEndPoint();
        GridPoint result = instance.getOtherEndpoint(endpoint);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndpoints method, of class HorizontalSegment.
     */
    @Test
    public void testGetEndpoints() {
        System.out.println("getEndpoints");
        HorizontalSegment instance = sampleSegment();
        Pair expOptionalResult1 = new Pair(sampleLeftEndPoint(), sampleRightEndPoint());
        Pair expOptionalResult2 = new Pair(sampleRightEndPoint(), sampleLeftEndPoint());
        Pair result = instance.getEndpoints();
        assert(expOptionalResult1.equals(result) || expOptionalResult2.equals(result)) :
                "Endpoints: " + sampleLeftEndPoint().toString() + ", " +
                sampleRightEndPoint().toString() + "\n" + "Result: " +
                result.toString();
    }

    /**
     * Test of getIntersection method, of class HorizontalSegment.
     */
    @Test
    public void testGetIntersection() {
        System.out.println("getIntersection");
        VerticalSegment segment = sampleVerticalSegment();
        HorizontalSegment instance = sampleSegment();
        GridPoint expResult = sampleIntersectionPoint();
        GridPoint result = instance.getIntersection(segment);
        assertEquals(expResult, result);
    }

    /**
     * Test of intersects method, of class HorizontalSegment.
     */
    @Test
    public void testIntersects() {
        System.out.println("intersects");
        WideRay ray = new WideRay(new Cell(0, 0), Direction.N);
        HorizontalSegment instance = sampleSegment();
        try {
            instance.intersects(ray);
        }
        catch (UnsupportedOperationException e) {
            return;
        }
        assert(false);
    }

    /**
     * Test of getY method, of class HorizontalSegment.
     */
    @Test
    public void testGetY() {
        System.out.println("getY");
        HorizontalSegment instance = sampleSegment();
        Integer expResult = sampleY();
        Integer result = instance.getY();
        assertEquals(expResult, result);
    }

}