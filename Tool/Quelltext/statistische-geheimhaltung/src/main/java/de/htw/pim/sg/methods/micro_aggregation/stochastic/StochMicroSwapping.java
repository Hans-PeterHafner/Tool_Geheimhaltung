package de.htw.pim.sg.methods.micro_aggregation.stochastic;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.htw.pim.sg.methods.micro_aggregation.mdav.MdavMath;

import java.util.*;

/**
 * @author Kirill Povolotskyy
 *         Date: 07/07/12
 */
public class StochMicroSwapping {
    private ArrayTable<Integer, Integer, Double> csv;
    private HashBasedTable<Integer, Integer, Double> tmpTable;
    private ArrayTable<Integer, Integer, Double> anonymizedTable;
    private int groupSize;
    private int anonymizedRowCount;
    private Random rng = new Random();
    private boolean bootstrap;

    public StochMicroSwapping(Table<Integer, Integer, Double> table, int groupSize, boolean bootstrap) {
        csv = (ArrayTable) table;
        anonymizedTable = ArrayTable.create(csv);
        this.groupSize = groupSize;
        this.bootstrap = bootstrap;
    }


    /**
     * eine Partition anonymisieren
     *
     * @param usedColumns Spalten auf die der Algorithmus angewandt werden soll
     */
    private void anonymize(int[] usedColumns) {
        Map<Integer, Double> centroid;
        int remainingRows = csv.rowMap().size();
        List<Map<Integer, Double>> rowList = tableToRowList(tmpTable);

        while (remainingRows >= 2 * groupSize) {
            anonymizeGroup(usedColumns, rowList);
            remainingRows = remainingRows - groupSize;
        }

        //anonymize remaining records
        anonymizeRemainingRecords(usedColumns, rowList, remainingRows);
    }

    /**
     * eine Gruppe anonymisieren
     * @param usedColumns
     * @param rowList
     */
    private void anonymizeGroup(int[] usedColumns, List<Map<Integer, Double>> rowList) {
        Map<Integer, Double> record;
        Table<Integer, Integer, Double> group = HashBasedTable.create();
        int groupRows = 0;

        for (int i = 0; i < groupSize; i++) {
            int randomInt = rng.nextInt(rowList.size());
            record = rowList.get(randomInt);
            if (!bootstrap) {
                rowList.remove(randomInt);
            }

            addRowToTable(groupRows, record, group);
            groupRows++;
        }

        Map<Integer, Double> centroid = MdavMath.calculateCentroid(usedColumns, group);

        for (int i = 0; i < groupSize; i++) {
            addRowToTable(anonymizedRowCount, centroid, anonymizedTable);
            anonymizedRowCount++;
        }

    }


    private List<Map<Integer, Double>> tableToRowList(Table<Integer, Integer, Double> table) {
        List<Map<Integer, Double>> mapList = new ArrayList<Map<Integer, Double>>();

        for (Map<Integer, Double> row : table.rowMap().values()) {
            mapList.add(row);
        }

        return mapList;
    }


    /**
     * @param partitions
     * @return
     */
    public Table<Integer, Integer, Double> run(List<int[]> partitions) {
        for (int[] partition : partitions) {
            anonymizedRowCount = 0;

            tmpTable = HashBasedTable.create(anonymizedTable);
            anonymize(partition);
        }

        return anonymizedTable;
    }


    /**
     * Die restlichen Daten anonymisieren
     *
     * @param usedColumns
     */
    private void anonymizeRemainingRecords(int[] usedColumns, List<Map<Integer, Double>> rowList, int remainingRows) {
        Table<Integer, Integer, Double> group;

        group = getRemainingRows(rowList, remainingRows);

        Map<Integer, Double> centroid = MdavMath.calculateCentroid(usedColumns, group);
        for (int i = 0; i < remainingRows; i++) {
            addRowToTable(anonymizedRowCount, centroid, anonymizedTable);
            anonymizedRowCount++;
        }
    }

    private Table<Integer, Integer, Double> getRemainingRows(List<Map<Integer, Double>> rowList, int remainingRows){
        Table<Integer, Integer, Double> group = HashBasedTable.create();
        if(bootstrap){
            for(int i = 0; i < remainingRows; i++){
                addRowToTable(0, rowList.get(rng.nextInt(anonymizedTable.rowKeySet().size())), group);
            }
        }else{
            int groupRows = 0;
            for (Map<Integer, Double> record : rowList) {
                addRowToTable(groupRows, record, group);
                groupRows++;
            }
        }

        return group;

    }

    public void addRowToTable(int row, Map<Integer, Double> record, Table<Integer, Integer, Double> table) {
        for (Map.Entry<Integer, Double> column : record.entrySet()) {
            table.put(row, column.getKey(), column.getValue());
        }
    }

}
