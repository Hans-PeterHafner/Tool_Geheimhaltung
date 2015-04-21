package de.htw.pim.sg.mdav;

import com.google.common.collect.Table;
import de.htw.pim.sg.Configuration;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.methods.micro_aggregation.mdav.Mdav;
import de.htw.pim.sg.table.TableUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;


public class MdavTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MdavTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {

        return new TestSuite(MdavTest.class);
    }

    public void testMdav() {

        // read sample data
        Locale german = new Locale("de", "DE");
        Locale.setDefault(german);
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
        double d = 0;
        try {
            d = numberFormat.parse("3,5").doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        d = Double.parseDouble("3,5");


        final int groupSize = 2;
        List<int[]> partitions = new ArrayList<int[]>();
        int[] partition1 = new int[] {0, 1};
        partitions.add(partition1);
        int[] partition2 = new int[] {2, 3};
        partitions.add(partition2);

        File inputCsv = new File(SAMPLE_INPUT);
        Table<Integer,Integer,Double> inputData = TableUtil.transformToDoubleTable( CsvUtil.readCsvFile(inputCsv, CsvUtil.SEPARATOR_SEMICOLON) );


        Mdav mdav = new Mdav(inputData, groupSize);

        Table<Integer, Integer, Double> result = mdav.run(partitions);






        System.out.println(result);





//        Table<Integer,Integer,String> outputData  = MicroAggregationUtil.getInstance().performOneDimensionalMicroAggregationWithVariableGroupSize(3, inputData, 0);


        System.out.println(inputData);
        File outputCsv = new File(SAMPLE_OUPUT);
        CsvUtil.writeCsvFile(TableUtil.transformToStringTable(result, Configuration.numberFormat), outputCsv,CsvUtil.SEPARATOR_SEMICOLON);
    }

    // SEPERATOR anpassen !!!
 //   private static final String SAMPLE_INPUT = "src/test/java/resources/mdav_testinput.csv";
    private static final String SAMPLE_INPUT = "src/test/java/resources/mdav_testinput.csv";
    private static final String SAMPLE_OUPUT = "src/test/java/resources/mdav_output.csv";
    //private static final String SAMPLE_INPUT    = "src/test/java/resources/sample.input.csv";
    //private static final String SAMPLE_OUPUT    = "src/test/java/resources/sample.output.csv";
}
