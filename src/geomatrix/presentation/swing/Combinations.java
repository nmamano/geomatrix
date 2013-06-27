/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nil
 */
public enum Combinations {
    
    None(0), Area1(1), Area2(2), Area3(3), Area12(4), Area13(5), Area23(6), Area123(7);
    
    private final int combination;

    private static final Color AREA_1_COLOR = Color.RED;
    private static final Color AREA_2_COLOR = Color.BLUE;
    private static final Color AREA_3_COLOR = Color.GREEN;
    private static final Color OVERLAPPED_CELLS_COLOR = Color.GRAY;
    private static final Color DOUBLY_OVERLAPPED_CELLS_COLOR = Color.BLACK;
    
    private Combinations(int combination) {
        this.combination = combination;
    }

    Combinations add(int areaID) {
        if (this == None) {
            if (areaID == 1) return Area1;
            else if (areaID == 2) return Area2;
            else if (areaID == 3) return Area3;
        }
        if (this == Area1) {
            if (areaID == 2) return Area12;
            else if (areaID == 3) return Area13;
        }
        if (this == Area2) {
            if (areaID == 1) return Area12;
            else if (areaID == 3) return Area23;            
        }
        if (this == Area3) {
            if (areaID == 1) return Area13;
            else if (areaID == 2) return Area23;              
        }
        if (this == Area12) {
            if (areaID == 3) return Area123;
        }
        if (this == Area13) {
            if (areaID == 2) return Area123;
        }
        if (this == Area23) {
            if (areaID == 1) return Area123;
        }
        return this;
    }

    Color getColor() {
        if (this == None) return null;
        if (this == Area1) return AREA_1_COLOR;
        if (this == Area2) return AREA_2_COLOR;
        if (this == Area3) return AREA_3_COLOR;
        if (this == Area123) return DOUBLY_OVERLAPPED_CELLS_COLOR;
        return OVERLAPPED_CELLS_COLOR;
    }

    List<Color> getColors() {
        List<Color> colors = new ArrayList<Color>();
        if (this == Area1 || this == Area12 || this == Area13 || this == Area123) colors.add(AREA_1_COLOR);
        if (this == Area2 || this == Area12 || this == Area23 || this == Area123) colors.add(AREA_2_COLOR);
        if (this == Area3 || this == Area13 || this == Area23 || this == Area123) colors.add(AREA_3_COLOR);
        return colors;
    }
}
