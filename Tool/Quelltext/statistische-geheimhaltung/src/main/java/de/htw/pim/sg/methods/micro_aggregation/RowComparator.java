package de.htw.pim.sg.methods.micro_aggregation;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Kirill Povolotskyy
 * Date: 05/07/12
 */
public class RowComparator implements Comparator<Map<Integer, Double>>{

    private int column;

    public RowComparator(int column){
        this.column = column;
    }

    @Override
    public int compare(Map<Integer, Double> row1, Map<Integer, Double> row2) {
        double val1 = row1.get(column);
        double val2 = row2.get(column);

        return Double.compare(val1, val2);
    }
}
