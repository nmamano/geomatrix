/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.geomatrix;

import geomatrix.business.models.binary.Cell;
import geomatrix.business.models.binary.CellSet;
import geomatrix.business.models.binary.GridPoint;
import geomatrix.business.models.binary.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
public class GeomatrixTest {
    
    //private List<GridPoint> vertexSet1, vertexSet2;
            
    public GeomatrixTest() {
        
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
     * Test of copy method, of class Geomatrix.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        Geomatrix original = null;
        Geomatrix expResult = null;
        Geomatrix result = Geomatrix.copy(original);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class Geomatrix.
     */
    @Test
    public void testContains_Cell() {
        System.out.println("contains");
        Cell cell = null;
        Geomatrix instance = new Geomatrix();
        boolean expResult = false;
        boolean result = instance.contains(cell);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contains method, of class Geomatrix.
     */
    @Test
    public void testContains_CellSet() {
        System.out.println("contains");
        CellSet c = null;
        Geomatrix instance = new Geomatrix();
        boolean expResult = false;
        boolean result = instance.contains(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of union method, of class Geomatrix.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
        CellSet c = null;
        Geomatrix instance = new Geomatrix();
        instance.union(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of intersection method, of class Geomatrix.
     */
    @Test
    public void testIntersection() {
        System.out.println("intersection");
        CellSet c = null;
        Geomatrix instance = new Geomatrix();
        instance.intersection(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of difference method, of class Geomatrix.
     */
    @Test
    public void testDifference() {
        System.out.println("difference");
        CellSet c = null;
        Geomatrix instance = new Geomatrix();
        instance.difference(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iterator method, of class Geomatrix.
     */
    @Test
    public void testIterator_0args() {
        System.out.println("iterator");
        Geomatrix instance = new Geomatrix();
        Iterator expResult = null;
        Iterator result = instance.iterator();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of translation method, of class Geomatrix.
     */
    @Test
    public void testTranslation() {
        System.out.println("translation");
        GridPoint p = null;
        Geomatrix instance = new Geomatrix();
        instance.translation(p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of verticalReflection method, of class Geomatrix.
     */
    @Test
    public void testVerticalReflection() {
        System.out.println("verticalReflection");
        Geomatrix instance = new Geomatrix();
        instance.verticalReflection();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of horizontalReflection method, of class Geomatrix.
     */
    @Test
    public void testHorizontalReflection() {
        System.out.println("horizontalReflection");
        Geomatrix instance = new Geomatrix();
        instance.horizontalReflection();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of rotation method, of class Geomatrix.
     */
    @Test
    public void testRotation() {
        System.out.println("rotation");
        int degrees = 0;
        Geomatrix instance = new Geomatrix();
        instance.rotation(degrees);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBoundingRectangle method, of class Geomatrix.
     */
    @Test
    public void testGetBoundingRectangle() {
        System.out.println("getBoundingRectangle");
        Geomatrix instance = new Geomatrix();
        Rectangle expResult = null;
        Rectangle result = instance.getBoundingRectangle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decomposition method, of class Geomatrix.
     */
    @Test
    public void testDescomposition() {
        System.out.println("descomposition");
        Geomatrix instance = new Geomatrix();
        List expResult = null;
        List result = instance.decomposition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iterator method, of class Geomatrix.
     */
    @Test
    public void testIterator_Rectangle() {
        System.out.println("iterator");
        Rectangle rectangle = null;
        Geomatrix instance = new Geomatrix();
        Iterator expResult = null;
        Iterator result = instance.iterator(rectangle);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of symmetricDifference method, of class Geomatrix.
     */
    @Test
    public void testSymmetricDifference() {
        System.out.println("symmetricDifference");
        CellSet c = null;
        Geomatrix instance = new Geomatrix();
        instance.symmetricDifference(c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class Geomatrix.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        Geomatrix instance = new Geomatrix();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of valid method, of class Geomatrix.
     */
    @Test
    public void testValid() {
        System.out.println("valid");
        Geomatrix instance = new Geomatrix();
        boolean expResult = false;
        boolean result = instance.valid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvalidLines method, of class Geomatrix.
     */
    @Test
    public void testGetInvalidLines() {
        System.out.println("getInvalidLines");
        Set<GridPoint> vertexs = null;
        List expResult = null;
        List result = Geomatrix.getInvalidLines(vertexs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Geomatrix.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Geomatrix instance = new Geomatrix();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}