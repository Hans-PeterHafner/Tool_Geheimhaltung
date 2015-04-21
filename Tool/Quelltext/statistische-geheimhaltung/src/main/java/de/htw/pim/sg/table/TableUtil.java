package de.htw.pim.sg.table;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import de.htw.pim.sg.Configuration;
import de.htw.pim.sg.table.transformer.CustomDoubleToStringTransformer;
import de.htw.pim.sg.table.transformer.DoubleToStringTransformer;
import de.htw.pim.sg.table.transformer.StringToDoubleTransformer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pascal Wasem
 */
public class TableUtil {

    /* -------------------------------------------------------------------------
    properties
    --------------------------------------------------------------------------*/
    
    // logging
    private static final Logger _log = LoggerFactory.getLogger(TableUtil.class);
    
    /* -------------------------------------------------------------------------
    constructor
    --------------------------------------------------------------------------*/
    
    // NOP
    
    /* -------------------------------------------------------------------------
    methods
    --------------------------------------------------------------------------*/
    /**
     * Erstellt eine neue Tabelle mit dem angegebenen Typ und angegeben Größen.
     * Z.b. TableUtil.<String>newTable(10, 10) oder TableUtil.<Double>newTable(10, 10)
     * 
     * @param rows Anzahl der Tabellenzeilen
     * @param columns Anzahl der Tabellenspalten
     * @return Eine Tabelle der angebenen Größe und vom angegebenen Typ
     */
    public static <T> Table<Integer, Integer, T> newTable(Integer rows, int columns) {

        try {

            Collection<Integer> rowKeys = createIndexCollection(rows);
            Collection<Integer> columnKeys = createIndexCollection(columns);

            return ArrayTable.create(rowKeys, columnKeys);

        } catch (Exception exc) {

            _log.error(PROBLEM_CREATING_NEW_TABLE + ": " + exc.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Befüllte die eine Tabelle mit CSV Daten
     * 
     * @param table die zu befüllende Tabelle
     * @param csvData die CSV Daten
     * @return die befüllte Tabelle
     */
    public static Table<Integer, Integer, String> fillTableWithCsvData(Table<Integer, Integer, String> table, List<String[]> csvData) {

        try {
  
            // iterate over rows
            int rowCounter = 0;
            for (String[] row : csvData) {

                // iterate over row cells
                int columnCounter = 0;
                for (String cell : row) {

                    // write cell value identified by row and column to table
                    table.put(rowCounter, columnCounter, cell);

                    // next column
                    columnCounter++;
                }

                // next row
                rowCounter++;
            }
           
        } catch (Exception exc) {

            _log.error(PROBLEM_FILLING_TABLE + ": " + exc.getLocalizedMessage());
            return null;
        }
        
        return table;
    }
    
    /**
     * Wandelt eine String Tabelle in eine Double Tabelle um.
     * Sollte die String Tabelle Werte enthalten die sich nicht in Double umwandeln lassen,
     * werden diese Werte in Double.NaN umgewandelt.
     * 
     * @param table die umzuwandelnde String Tabelle
     * @return die entsprechende Double Tabelle
     */
    public static Table<Integer,Integer,Double> transformToDoubleTable(Table<Integer, Integer, String> table) {
       
       // Umwandlung von String zu Double 
       StringToDoubleTransformer stringToDoubleTransformer = new StringToDoubleTransformer(Configuration.numberFormat);
       
       // die transformierte Tabelle ist unveränderlich!
       Table<Integer,Integer,Double> transformedTable = Tables.transformValues(table, stringToDoubleTransformer);
       
       // es muss also noch eine veränderliche Kopie erstellt werden
       return ArrayTable.create(transformedTable); 
    }

    
    /**
     * Wandelte eine Double Tabelle in eine String Tabelle um
     * 
     * @param table die umzuwandelnde Double Tabelle
     * @return die entsprechende String Tabelle
     */
    public static Table<Integer,Integer,String> transformToStringTable(Table<Integer, Integer, Double> table, NumberFormat nf) {
        
        // Umwandlung von Double zu String
       CustomDoubleToStringTransformer doubleToStringTransformer = new CustomDoubleToStringTransformer(nf);
       
       // die transformierte Tabelle ist unveränderlich!
       Table<Integer,Integer,String> transformedTable = Tables.transformValues(table, doubleToStringTransformer);
       
       // es muss also noch eine veränderliche Kopie erstellt werden
       return ArrayTable.create(transformedTable); 
      
    }

    /**
     * Wandelte eine Double Tabelle in eine String Tabelle um
     *
     * @param table die umzuwandelnde Double Tabelle
     * @return die entsprechende String Tabelle
     */
    public static Table<Integer,Integer,String> transformToStringTable(Table<Integer, Integer, Double> table) {

        // Umwandlung von Double zu String
        DoubleToStringTransformer doubleToStringTransformer = new DoubleToStringTransformer();

        // die transformierte Tabelle ist unveränderlich!
        Table<Integer,Integer,String> transformedTable = Tables.transformValues(table, doubleToStringTransformer);

        // es muss also noch eine veränderliche Kopie erstellt werden
        return ArrayTable.create(transformedTable);

    }


    /**
     * Erstellt einen Tabellen Index mit angebener Größe
     * 
     * @param maxIndex der Größte Index (excluded)
     * @return ein Index der angebenen Größe [0,maxIndex)
     */
    private static Collection<Integer> createIndexCollection(Integer maxIndex) {

        Collection<Integer> indexCollection = new ArrayList<Integer>();

        int index = 0;
        while (index < maxIndex) {

            indexCollection.add(index);
            index++;
        }

        return indexCollection;
    }
    
    /* -------------------------------------------------------------------------
    constants
    --------------------------------------------------------------------------*/
    
    // error messages
    private static final String PROBLEM_FILLING_TABLE        = "Problem beim Füllen der Tabelle";
    private static final String PROBLEM_CREATING_NEW_TABLE   = "Problem beim Erstellen der neuen Tabelle";
    
}