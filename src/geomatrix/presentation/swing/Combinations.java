/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

/**
 *
 * @author Nil
 */
public enum Combinations {
    
    None(0), Area1(1), Area2(2), Area3(3), Area12(4), Area13(5), Area23(6), Area123(7);
    
    private final int combination;

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
}
