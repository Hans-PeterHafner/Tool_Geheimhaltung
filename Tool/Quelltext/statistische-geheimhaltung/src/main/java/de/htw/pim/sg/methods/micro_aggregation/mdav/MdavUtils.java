package de.htw.pim.sg.methods.micro_aggregation.mdav;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import java.util.Map;

/**
 *
 */
public class MdavUtils {

    public static void addRowToTable(int row, Map<Integer, Double> record, Table<Integer, Integer, Double> table, int[] usedColumns) {
        for (int column : usedColumns) {
            table.put(row, column, record.get(column));
        }
    }

    public static void removeData(int row, int[] usedColumns, ArrayTable<Integer, Integer, Double> table, ArrayTable<Integer, Integer, Double> distanceMatrix) {
        //daten entfernen
        for (int column : usedColumns) {
            table.erase(row, column);
        }

        //distanzwerte entfernen
        for (int i = 0; i < distanceMatrix.rowMap().size(); i++) {
            for (int column : usedColumns) {
                distanceMatrix.erase(i, row);
            }
        }
    }
}
