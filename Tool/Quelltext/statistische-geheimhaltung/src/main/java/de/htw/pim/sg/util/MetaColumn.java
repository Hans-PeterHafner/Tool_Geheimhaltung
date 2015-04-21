/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.util;

/**
 *
 * @author Michael Schidlauske
 */
public class MetaColumn {
    
    private double treshhold;
    private double weight;
    private ColumnType ctype;
    private boolean keyVariable;
    private int columnIndex;
       
    public MetaColumn(double treshhold, double weight, ColumnType ctype,
                        boolean keyVariable, int columnIndex)
    {
        this.treshhold = treshhold;
        this.weight = weight;
        this.ctype = ctype;
        this.keyVariable = keyVariable;
        this.columnIndex = columnIndex;
    }

    public boolean isKeyVariable() {
        return keyVariable;
    }

    public void setKeyVariable(boolean keyVariable) {
        this.keyVariable = keyVariable;
    }

    public ColumnType getCtype() {
        return ctype;
    }

    public void setCtype(ColumnType ctype) {
        this.ctype = ctype;
    }

    public double getTreshhold() {
        return treshhold;
    }

    public void setTreshhold(double treshhold) {
        this.treshhold = treshhold;
    }
    
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
     public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
    
}
