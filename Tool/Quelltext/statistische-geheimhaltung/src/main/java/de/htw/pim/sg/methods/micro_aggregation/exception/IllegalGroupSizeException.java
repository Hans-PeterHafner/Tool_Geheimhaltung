/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.methods.micro_aggregation.exception;

/**
 *
 * @author p.wasem
 */
public class IllegalGroupSizeException extends IllegalArgumentException {

    private static final long serialVersionUID = 1228359756614353468L;

    public IllegalGroupSizeException() {
        super();
    }

    public IllegalGroupSizeException(String s) {
        super(s);
    }
}
