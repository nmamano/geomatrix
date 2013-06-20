/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.presentation.swing;

/**
 *
 * @author Nil
 */
class HideSelectedAreaException extends Exception {

    /**
     * Creates a new instance of
     * <code>HideSelectedAreaException</code> without detail message.
     */
    public HideSelectedAreaException() {
    }

    /**
     * Constructs an instance of
     * <code>HideSelectedAreaException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HideSelectedAreaException(String msg) {
        super("The selected area is being tried to hide.");
    } 
}
