package de.htw.pim.sg.methods.micro_aggregation.mdav;

/**
 * @author Kirill Povolotskyy
 * Time: 12:45
 */

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;


//TODO implements AnonymizingInterface
public class Mdav {
    private ArrayTable<Integer, Integer, Double> csv;
    private ArrayTable<Integer, Integer, Double> distanceMatrix;
    private ArrayTable<Integer, Integer, Double> tmpTable;
    private ArrayTable<Integer, Integer, Double> anonymizedTable;
    private int groupSize;

    public Mdav(Table<Integer, Integer, Double> table, int groupSize) {
        csv = (ArrayTable) table;
        anonymizedTable = ArrayTable.create(csv);
        this.groupSize = groupSize;
    }


    /**
     * MDAV Algorithmus anwenden
     * @param usedColumns Spalten auf die der Algorithmus angewandt werden soll
     */
    private void anonymize(int[] usedColumns) {
        Map<Integer, Double> centroid;
        int remainingRows = csv.rowMap().size();

        while (remainingRows >= 3 * groupSize) {
            centroid = MdavMath.calculateCentroid(usedColumns, tmpTable);
            int groupRecord1 = getFarthestRecordFromCentroid(centroid, usedColumns);
            int groupRecord2 = getFarthestDistanceRow(groupRecord1);
            //anonymize group 1
            anonymizeGroupAroundRecord(groupRecord1, usedColumns);
            //anonymize group 2
            anonymizeGroupAroundRecord(groupRecord2, usedColumns);
            remainingRows = remainingRows - 2 * groupSize;
        }

        //anonymize remaining records
        if (remainingRows >= 2 * groupSize) {
            centroid = MdavMath.calculateCentroid(usedColumns, tmpTable);
            int groupRecord1 = getFarthestRecordFromCentroid(centroid, usedColumns);
            anonymizeGroupAroundRecord(groupRecord1, usedColumns);
        }

        anonymizeRemainingRecords(usedColumns);
    }


    /**
     *
     * @param partitions
     * @return
     */
    public Table<Integer, Integer, Double> run(List<int[]> partitions) {
        for (int[] partition : partitions) {
            tmpTable = ArrayTable.create(csv);
            distanceMatrix = MdavMath.calculateDistanceMatrix(partition, tmpTable);
            anonymize(partition);
        }

        return anonymizedTable;
    }


    /**
     * Die restlichen Daten anonymisieren
     * @param usedColumns
     */
    private void anonymizeRemainingRecords(int[] usedColumns) {
        Table<Integer, Integer, Double> remainingRecords = HashBasedTable.create();
        List<Integer> rowsToModify = new ArrayList<Integer>();
        int rowCount = 0;

        for (int i = 0; i < tmpTable.rowMap().size(); i++) {
            if (tmpTable.get(i, usedColumns[0]) != null) {
                MdavUtils.addRowToTable(rowCount, tmpTable.row(i), remainingRecords, usedColumns);
                rowsToModify.add(i);
                rowCount++;
            }
        }

        Map<Integer, Double> centroid = MdavMath.calculateCentroid(usedColumns, remainingRecords);
        for (int i = 0; i < remainingRecords.rowMap().size(); i++) {
            MdavUtils.addRowToTable(rowsToModify.get(i), centroid, anonymizedTable, usedColumns);
        }

    }

    /**
     *
     * @param row
     * @param usedColumns
     */
    private void anonymizeGroupAroundRecord(int row, int[] usedColumns) {
        Table<Integer, Integer, Double> group = HashBasedTable.create();
        List<Integer> rowsToModify = new ArrayList<Integer>();
        Map<Integer, Double> record = tmpTable.row(row);

        MdavUtils.addRowToTable(0, record, group, usedColumns);
        MdavUtils.removeData(row, usedColumns, tmpTable, distanceMatrix);
        rowsToModify.add(row);

        for (int i = 1; i < groupSize; i++) {
            int nearestRow = getNearestDistanceRow(row);
            Map<Integer, Double> nearestRecord = tmpTable.row(nearestRow);
            MdavUtils.addRowToTable(i, nearestRecord, group, usedColumns);
            MdavUtils.removeData(nearestRow, usedColumns, tmpTable, distanceMatrix);
            rowsToModify.add(nearestRow);
        }

        Map<Integer, Double> centroid = MdavMath.calculateCentroid(usedColumns, group);

        for (int i = 0; i < groupSize; i++) {
            MdavUtils.addRowToTable(rowsToModify.get(i), centroid, anonymizedTable, usedColumns);
        }
    }

    private int getNearestDistanceRow(int row) {
        Map<Integer, Double> distanceToRow = distanceMatrix.row(row);
        double minDistance = Double.POSITIVE_INFINITY;
        int nearestRow = -1;

        for (int i = 0; i < distanceToRow.size(); i++) {
            if (distanceToRow.get(i) != null && distanceToRow.get(i) < minDistance && i != row) {
                minDistance = distanceToRow.get(i);
                nearestRow = i;
            }
        }
        distanceMatrix.erase(row, nearestRow);

        return nearestRow;
    }


    private int getFarthestDistanceRow(int row) {
        Map<Integer, Double> distanceToRow = distanceMatrix.row(row);
        double maxDistance = -1.0;
        int farthestRow = -1;

        for (int i = 0; i < distanceToRow.size(); i++) {
            if (distanceToRow.get(i) != null && distanceToRow.get(i) > maxDistance && i != row) {
                maxDistance = distanceToRow.get(i);
                farthestRow = i;
            }
        }
        distanceMatrix.erase(row, farthestRow);

        return farthestRow;
    }


    private int getFarthestRecordFromCentroid(Map<Integer, Double> record, int[] usedColumns) {
        Map<Integer, Map<Integer, Double>> rows = tmpTable.rowMap();
        double max = 0.0;
        int farthestRecord = 0;
        int i = 0;

        for (Map.Entry<Integer, Map<Integer, Double>> rowEntry : rows.entrySet()) {
            if (rowEntry.getValue().get(usedColumns[0]) != null) {
                double distance = MdavMath.calcRowDistance(record, rowEntry.getValue(), usedColumns);
                if (distance > max) {
                    max = distance;
                    farthestRecord = i;
                }
            }
            i++;
        }
        return farthestRecord;
    }

}