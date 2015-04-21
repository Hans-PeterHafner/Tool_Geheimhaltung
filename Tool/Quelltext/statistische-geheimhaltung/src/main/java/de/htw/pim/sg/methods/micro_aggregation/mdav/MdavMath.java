package de.htw.pim.sg.methods.micro_aggregation.mdav;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Kirill Povolotskyy
 *         Date: 26/06/12
 */
public class MdavMath {


    /**
     * Euklidische Distanz zwischen 2 Datensätzen berechnen
     * @param row1
     * @param row2
     * @param columns
     * @return
     */

    public static double calcRowDistance(Map<Integer, Double> row1, Map<Integer, Double> row2, int[] columns) {
        double result = 0.0;

        for (int currentColumn : columns) {
            double value1 = row1.get(currentColumn);
            double value2 = row2.get(currentColumn);
            result = result + square(row1.get(currentColumn) - row2.get(currentColumn));
        }

        return result;
    }


    /**
     * Centroid berechnen
     * @param usedColumns   Spalten, die für die Berechnung verwendet werden sollen
     * @param table Datenmenge aus der der Centroid berechnet werden soll
     * @return  Centroid als Datensatz
     */
    public static Map<Integer, Double> calculateCentroid(int[] usedColumns, Table<Integer, Integer, Double> table) {
        Map<Integer, Double> result = new HashMap<Integer, Double>(usedColumns.length);
        Map<Integer, Map<Integer, Double>> rows = table.rowMap();

        double[] centroid = new double[usedColumns.length];


        for (Map.Entry<Integer, Map<Integer, Double>> rowEntry : rows.entrySet()) {
            for (int i = 0; i < centroid.length; i++) {
                if (rowEntry.getValue().get(usedColumns[i]) == null) {
                    break;
                }
                centroid[i] = centroid[i] + rowEntry.getValue().get(usedColumns[i]);
            }
        }

        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = centroid[i] / table.rowMap().size();
        }

        for (int i = 0; i < centroid.length; i++) {
            result.put(usedColumns[i], centroid[i]);
        }

        return result;
    }

    /**
     *
     * @param usedColumns
     * @param table
     * @return Centroid als Tabelle
     */
    public static Table<Integer, Integer, Double> calculateCentroidTable(int[] usedColumns, Table<Integer, Integer, Double> table) {
        Table<Integer, Integer, Double> result = ArrayTable.create(table);
        Map<Integer, Map<Integer, Double>> rows = table.rowMap();

        double[] centroid = new double[usedColumns.length];


        for (Map.Entry<Integer, Map<Integer, Double>> rowEntry : rows.entrySet()) {
            for (int i = 0; i < centroid.length; i++) {
                if (rowEntry.getValue().get(usedColumns[i]) == null) {
                    break;
                }
                centroid[i] = centroid[i] + rowEntry.getValue().get(usedColumns[i]);
            }
        }

        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = centroid[i] / table.rowMap().size();
        }


        for (Map<Integer, Double> row : result.rowMap().values()) {
            for (int i = 0; i < centroid.length; i++) {
                row.put(usedColumns[i], centroid[i]);
            }
        }


        return result;
    }

    /**
     * Distanzmatrix für eine Datenmenge berechnen
     * @param usedColumns Spalten, die für die berechnung verwendet werden sollen
     * @param table
     * @return
     */
    public static ArrayTable<Integer, Integer, Double> calculateDistanceMatrix(int[] usedColumns, ArrayTable<Integer, Integer, Double> table) {
        ArrayTable<Integer, Integer, Double> distanceMatrix = ArrayTable.create(table.rowKeyList(), table.rowKeyList());

        Map<Integer, Double> row1;
        for (int k = 0; k < table.rowMap().size(); k++) {
            row1 = table.row(k);

            for (int i = 0; i < table.rowMap().size(); i++) {
                distanceMatrix.put(k, i, MdavMath.calcRowDistance(row1, table.row(i), usedColumns));
            }
        }

        return distanceMatrix;
    }

    public static double square(double x) {
        return x * x;
    }
}
