/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.util;

import java.util.ArrayList;

/**
 *
 * @author Michael Schidlauske
 * @modified-by Mohammed Abdulla
 */
public class MetaData {
    
    
    private ArrayList<MetaColumn> columns;
    
    private static MetaData instance;
    
    private MetaData()
    {       
    }
    
    public static MetaData getInstance()
    {
        if(instance == null)
        {
            instance = new MetaData();
        }
        
        return instance;
    }
    
    public MetaData(ArrayList<MetaColumn> columns)
    {
        //todo als Singleton implementieren
        this.columns = columns;
    }

    public ArrayList<MetaColumn> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<MetaColumn> columns) {
        this.columns = columns;
    }
    
    public void addColumn(MetaColumn mc)
    {
        columns.add(mc);
    }
    
    public MetaColumn getColumn(int index) throws Exception
    {
        MetaColumn lmc = null;
        
        if(index < 0 || index > columns.size())
            throw new ArrayIndexOutOfBoundsException("Ungueltiger Index!");
        
        for(MetaColumn mc: columns)
        {
            if(mc.getColumnIndex() == index)
                lmc = mc;
        }
        
        return lmc;
    }
    
}
