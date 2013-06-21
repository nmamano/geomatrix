/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Nil
 */
public class PresentationArea {
    protected boolean isDisplayed;
    protected Color color;
    protected Set<Point> vertexs;
    protected boolean isBoundingRectangleDisplayed;

    public PresentationArea(boolean isDisplayed, Color color) {
        this.isDisplayed = isDisplayed;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
        vertexs = new HashSet<Point>();
        isBoundingRectangleDisplayed = false;
    }

    boolean containsVertex(Point vertex) {
        return vertexs.contains(vertex);
    }

    void removeVertexFromArea(Point vertex) {
        vertexs.remove(vertex);
    }

    void addVertexToArea(Point vertex) {
        vertexs.add(vertex);
    }

}
