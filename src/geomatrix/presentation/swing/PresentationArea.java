/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import geomatrix.gridplane.GridPoint;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nil
 */
public class PresentationArea {
    protected boolean isDisplayed;
    protected Color color;
    protected Set<GridPoint> vertexs;
    protected boolean isBoundingRectangleDisplayed;

    public PresentationArea(boolean isDisplayed, Color color) {
        this.isDisplayed = isDisplayed;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        vertexs = new HashSet<GridPoint>();
        isBoundingRectangleDisplayed = false;
    }

    boolean containsVertex(GridPoint vertex) {
        return vertexs.contains(vertex);
    }

    void removeVertexFromArea(GridPoint vertex) {
        vertexs.remove(vertex);
    }

    void addVertexToArea(GridPoint vertex) {
        vertexs.add(vertex);
    }

}
