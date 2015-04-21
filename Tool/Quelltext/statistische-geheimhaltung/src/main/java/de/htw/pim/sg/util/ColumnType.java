/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.util;

/**
 *
 * @author Michael Schidlauske
 */
public enum ColumnType {
    
    Identifier(0), Blocking(1), Ordinal(2), Nominal(3), Numerical(4); 
    
    ColumnType(double weight)
    {
        this.weight = weight;
    }
    
    private final double weight;
    
    public double standardWeight() { return weight; }
}
