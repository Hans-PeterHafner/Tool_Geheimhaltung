/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.util;

/**
 *
 * @author Michael Schidlauske
 */
public class DistanceElement implements Comparable
{
    String wert = "";
    int index1 = 0;
    int index2 = 0;
    
    public DistanceElement(){}

    public DistanceElement(String wert, int index1)
    {
        this.wert = wert;
        this.index1 = index1;
    }
    
    
    public int compareTo(Object o) {
        
        DistanceElement de = (DistanceElement) o;
        return wert.compareTo(de.getWert());
    }

    public int getIndex1() {
        return index1;
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }

    public String getWert() {
        return wert;
    }

    public void setWert(String wert) {
        this.wert = wert;
    }
}
