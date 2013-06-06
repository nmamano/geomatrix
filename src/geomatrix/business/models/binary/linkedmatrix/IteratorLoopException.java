/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geomatrix.business.models.binary.linkedmatrix;

/**
 * Represents an exception thrown when the iteration through the Linked Matrix
 * contained cells goes in a loop.
 * @author Nil
 */
class IteratorLoopException extends Exception {

    /**
     * Creates a new instance of
     * <code>IteratorLoopException</code> without detail message.
     */
    public IteratorLoopException() {
    }

    /**
     * Constructs an instance of
     * <code>IteratorLoopException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public IteratorLoopException(String msg) {
        super("The linked matrix iteration is cyclic.");
    }
}
