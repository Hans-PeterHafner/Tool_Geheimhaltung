/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.methods.micro_aggregation.exception;

/**
 *
 * @author p.wasem
 */
public class IllegalColumnSizeException extends IllegalArgumentException {

    private static final long serialVersionUID = -3228359766614353768L;

    public IllegalColumnSizeException() {
        super();
    }

    public IllegalColumnSizeException(String s) {
        super(s);
    }
}
