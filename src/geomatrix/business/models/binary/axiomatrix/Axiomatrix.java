/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.axiomatrix;

import geomatrix.business.models.binary.MasterAPI;
import geomatrix.gridplane.Cell;
import java.util.Iterator;

/**
 *
 * @author Nil
 */
public class Axiomatrix implements MasterAPI {
    
    private boolean[][] cells;
    
    private int maxX;
    private int maxY;

    public Axiomatrix(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        cells = new boolean[maxX][maxY];
    }

    @Override
    public boolean contains(Cell cell) {
        return cells[cell.x][cell.y];
    }

    @Override
    public boolean contains(MasterAPI other) {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                Cell cell = new Cell(i, j);
                if (!contains(cell) && other.contains(cell)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void union(MasterAPI other) {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                Cell cell = new Cell(i, j);
                if (other.contains(cell)) {
                    cells[i][j] = true;
                }
            }
        }
    }

    @Override
    public void intersection(MasterAPI other) {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                Cell cell = new Cell(i, j);
                if (! other.contains(cell)) {
                    cells[i][j] = false;
                }
            }
        }
    }

    @Override
    public void difference(MasterAPI other) {
        for (int i = 0; i < maxX; ++i) {
            for (int j = 0; j < maxY; ++j) {
                Cell cell = new Cell(i, j);
                if (other.contains(cell)) {
                    cells[i][j] = false;
                }
            }
        }
    }

    @Override
    public Iterator<Cell> iterator() {
        return new CellIterator();
    }

    private static class CellIterator implements Iterator<Cell> {

        public CellIterator() {
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Cell next() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
}
