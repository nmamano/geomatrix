/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.hashmatrix;

import geomatrix.business.models.binary.MasterAPI;
import geomatrix.gridplane.Cell;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Nil
 */
public class HashMatrix implements MasterAPI {

    HashSet<Cell> cells;

    public HashMatrix(HashSet<Cell> cells) {
        this.cells = cells;
    }
    
    @Override
    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }

    @Override
    public boolean contains(MasterAPI other) {
        for (Cell cell : other) {
            if (! (contains(cell))) return false;
        }
        return true;
    }

    @Override
    public void union(MasterAPI other) {
        for (Cell cell : other) cells.add(cell);
    }

    @Override
    public void intersection(MasterAPI other) {
        for (Cell cell : this) {
            if (! other.contains(cell)) {
                cells.remove(cell);
            }
        }
    }

    @Override
    public void difference(MasterAPI other) {
        for (Cell cell : other) cells.remove(cell);
    }

    @Override
    public Iterator<Cell> iterator() {
        return cells.iterator();
    }

}
