package de.htw.pim.sg.csv;

import com.google.common.collect.Table;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Pascal
 */
public class CsvUtilTest extends TestCase {
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CsvUtilTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( CsvUtilTest.class );
    }

    /**
     * Test for reading a csv file
     */
    public void testReadCsvFile() {
        
        File csvFile = new File(READ_TEST_CSV);
        Table<Integer, Integer, String> table = CsvUtil.readCsvFile(csvFile,CsvUtil.SEPARATOR_SEMICOLON);
        assert(table.get(0,0).equals("Year"));
        assert(table.get(2,2).equals("Cougar"));
        assert(table.get(2,3).equals("2,38"));
    }
    
    /**
     * Test for writing csv file
     */
    public void testWriteCsvFile() {
        
        File csvInFile = new File(READ_TEST_CSV);
        Table<Integer, Integer, String> tableIn = CsvUtil.readCsvFile(csvInFile,CsvUtil.SEPARATOR_SEMICOLON);
        
        File csvOutFile = new File(WRITE_TEST_CSV);
        CsvUtil.writeCsvFile(tableIn,csvOutFile,CsvUtil.SEPARATOR_SEMICOLON);
        
        File csvFile = new File(READ_TEST_CSV);
        Table<Integer, Integer, String> table = CsvUtil.readCsvFile(csvFile,CsvUtil.SEPARATOR_SEMICOLON);
        assert(table.get(0,0).equals("Year"));
        assert(table.get(2,2).equals("Cougar"));
        assert(table.get(2,3).equals("2,38"));
    }
    
    private static final String READ_TEST_CSV = "src/test/java/resources/read_test.csv";
    private static final String WRITE_TEST_CSV = "src/test/java/resources/write_test.csv";
    
}
 