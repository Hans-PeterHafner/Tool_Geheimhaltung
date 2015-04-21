/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.methods.micro_aggregation;

import com.google.common.collect.Table;
import de.htw.pim.sg.Configuration;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.table.TableUtil;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Pascal
 */
public class MicroAggregationUtilTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MicroAggregationUtilTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        
        return new TestSuite(MicroAggregationUtilTest.class);
    }
    
    public void testPerformOneDimensionalMicroAggregationWithVariableGroupSize() {
        
        // read sample data
        File inputCsv = new File(SAMPLE_INPUT);
        Table<Integer,Integer,String> inputData = CsvUtil.readCsvFile(inputCsv, CsvUtil.SEPARATOR_SEMICOLON);
        Table<Integer,Integer,Double> inputData_ = TableUtil.transformToDoubleTable(inputData);
        
        Table<Integer,Integer,Double> outputData_  = MicroAggregationUtil.getInstance().performOneDimensionalMicroAggregationWithVariableGroupSize(3, inputData_, 0);
        Table<Integer,Integer,String> outputData = TableUtil.transformToStringTable(outputData_, Configuration.numberFormat);

        File outputCsv = new File(SAMPLE_OUPUT);
        CsvUtil.writeCsvFile(outputData, outputCsv,CsvUtil.SEPARATOR_SEMICOLON);
    }

    // SEPERATOR anpassen !!!
    private static final String SAMPLE_INPUT = "src/test/java/resources/aggregate_test_input.csv";
    private static final String SAMPLE_OUPUT = "src/test/java/resources/aggregate_test_output.csv";
    //private static final String SAMPLE_INPUT    = "src/test/java/resources/sample.input.csv";
    //private static final String SAMPLE_OUPUT    = "src/test/java/resources/sample.output.csv";
}