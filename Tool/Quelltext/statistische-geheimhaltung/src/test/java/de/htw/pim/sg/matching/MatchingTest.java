/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.matching;

import com.google.common.collect.ArrayTable;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.util.ColumnType;
import de.htw.pim.sg.util.MetaColumn;
import de.htw.pim.sg.util.MetaData;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Schidlauske
 */
public class MatchingTest {
    
    File original = null;
    File anonym  = null;
    ArrayTable<Integer, Integer, String> tableOriginal;
    ArrayTable<Integer, Integer, String> tableAnonym;
    MetaData meta = null;
    Matching instance = null;
    
    public MatchingTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        ArrayList<MetaColumn> metaColumns = new ArrayList<MetaColumn>();
        
        original = new File("src/test/java/resources/Testdaten_matching.csv");
        tableOriginal  = (ArrayTable) CsvUtil.readCsvFile(original,CsvUtil.SEPARATOR_SEMICOLON); 
        
        anonym = new File("src/test/java/resources/Testdaten_matching_manipuliert.csv");
//        anonym = new File("src/test/java/resources/Testdaten_matching_manipuliert.csv");
        tableAnonym = (ArrayTable) CsvUtil.readCsvFile(anonym,CsvUtil.SEPARATOR_SEMICOLON);
        
        //TODO MetaColumns manuell anlegen, da GUI noch fehlt!
        
        meta = new MetaData(metaColumns);
        
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Identifier,false, 0));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Ordinal,false,1));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,2));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Ordinal,true,3));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,4));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Ordinal,true,5));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,6));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,7));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,8));
        meta.addColumn(new MetaColumn(0.01,1,ColumnType.Numerical,true,9));
        instance = new Matching();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of startMatching method, of class Matching.
     */
    @Test
    public void testStartMatching() throws Exception {
        
         double expResult = 88.2;
         double result = 0;
         MatchingResult mr;
        System.out.println("startMatching");
       
        instance.startMatching(tableOriginal, tableAnonym, meta);
        mr = instance.getMatchingResults();
        
        System.out.println("Quote reale Matches: " + mr.getReal_matches() + "%");
        result = mr.getReal_matches();
        assertEquals(expResult, result, 1.0);
       
        expResult = 88.4;
        System.out.println("Quote mögliche Matches: " + mr.getPossible_matches() + "%");
        result = mr.getPossible_matches();
        assertEquals(expResult, result, 1.0);
        
    }

  
}
