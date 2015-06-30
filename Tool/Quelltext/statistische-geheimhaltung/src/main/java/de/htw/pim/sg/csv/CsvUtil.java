package de.htw.pim.sg.csv;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Table;

import de.htw.pim.sg.table.TableUtil;

/**
 * This class provides methods to read and write csv files
 * 
 * @author Pascal
 */
public class CsvUtil {

    /* -------------------------------------------------------------------------
    properties
    --------------------------------------------------------------------------*/
    
    // logging
    private static final Logger _log = LoggerFactory.getLogger(CsvUtil.class);

    /* -------------------------------------------------------------------------
    constructors
    --------------------------------------------------------------------------*/
    
    //NOP
    
    /*--------------------------------------------------------------------------
    methods
    --------------------------------------------------------------------------*/
    
    /**
     * Liest eine übergebene CSV Datei
     * 
     * @param csvFile die CSV Dateo
     * @param separator der Seperator der CSV Daten z.B. CsvUtil.[SEPARATOR_SEMICOLON|SEPARATOR_TABULATOR]
     * @return die CSV Daten als eine "Liste von Zeilen"
     */
    private static List<String[]> readCsvData(File csvFile, char separator) {

        List<String[]> csvData = Collections.EMPTY_LIST;

        try {

            CSVReader reader = new CSVReader(new FileReader(csvFile), separator);
            csvData = reader.readAll();

        } catch (Exception exc) {

            _log.error(PROBLEM_READING_CSV_DATA + " " + csvFile.getAbsolutePath() + ": " + exc.getLocalizedMessage());
        }

        return csvData;
    }

    /**
     * Liest eine CSV Datei in eine Tabelle ein
     * 
     * @param csvFile die zu lesende CSV Datei
     * @return eine Tabelle, die die CSV Daten enthält
     */
    public static Table<Integer, Integer, String> readCsvFile(File csvFile, char separator) {
        return readCsvFile(csvFile, separator, false);
    }
    
    /**
     * Liest eine CSV Datei in eine Tabelle ein
     * 
     * @param csvFile die zu lesende CSV Datei
     * @param istErsteZeileUberschrift
     * @return eine Tabelle, die die CSV Daten enthält
     */
    public static Table<Integer, Integer, String> readCsvFile(File csvFile, char separator, boolean istErsteZeileUberschrift) {
        
        Table<Integer, Integer, String> csvTable = null;
        
        try {
            
            // CSV Daten aus Datei lesen
            List<String[]> csvData = readCsvData(csvFile, separator);
            
            if (istErsteZeileUberschrift) {
                csvData = csvData.subList(1, csvData.size());
            }
            
            // neue Tabelle erstellen
            int rows = csvData.size();
            int columns = csvData.get(0).length;
            csvTable = TableUtil.<String>newTable(rows, columns);
            
            // Tabelle mit CSV Daten füllen
            csvTable = TableUtil.fillTableWithCsvData(csvTable, csvData);
            
        } catch (Exception exc) {
            
            _log.error(PROBLEM_READING_CSV_FILE + " " + csvFile.getAbsolutePath() + ": " + exc.getLocalizedMessage());
        }
        
        return csvTable;
    }
   
    /**
     * Schreibt eine Tabelle im CSV Format in eine Datei
     * 
     * @param table die zu schreibende Tabelle
     * @param csvFile die zu schreibende CSV Datei
     */
    public static void writeCsvFile(Table<Integer, Integer, String> table, File csvFile, char separator) {

        try {

           // ssv writer initialiseiren
            CSVWriter writer = new CSVWriter(new FileWriter(csvFile), separator);
            
            // über Tabellenzeilen iterieren
            for (Integer rowKey : table.rowKeySet()) {
                
                // aktuelle Zeile lesen
                Map<Integer,String> row = table.row(rowKey);
                
                // neue Zeile erstellen
                List<String> nextLine = new ArrayList<String>();
                
                // über Spalten der aktuellen Zeile iterieren
                for (Integer columnKey : row.keySet()) {
                    
                    // Spalte in neue Zeile schreiben
                    nextLine.add(row.get(columnKey));
                }
                
                // Zeile Schreiben
                writer.writeNext(nextLine.toArray(new String[0]));
            }
            
            // close writer
            writer.close();

        } catch (Exception exc) {

             _log.error(PROBLEM_WRITING_CSV_FILE + " " + csvFile.getAbsolutePath() + ": " + exc.getLocalizedMessage());
        }

    }
    
    /* -------------------------------------------------------------------------
    constants
    -------------------------------------------------------------------------
     */
    // csv seperators
    public static final char SEPARATOR_SEMICOLON = ';';
    public static final char SEPARATOR_TABULATOR = '\t';
    public static final char SEPARATOR_COMMA = ',';
    
    // error messages
    public static final String PROBLEM_READING_CSV_DATA = "Problem beim Lesen der CSV Daten";
    public static final String PROBLEM_READING_CSV_FILE = "Problem beim Lesen der CSV Datei";
    public static final String PROBLEM_WRITING_CSV_FILE = "Problem beim Schreiben der CSV Datei";

    
}
