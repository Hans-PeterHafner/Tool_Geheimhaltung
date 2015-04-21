package de.htw.pim.sg.methods.micro_aggregation;

import com.google.common.collect.*;
import de.htw.pim.sg.methods.micro_aggregation.exception.IllegalColumnSizeException;
import de.htw.pim.sg.methods.micro_aggregation.exception.IllegalGroupSizeException;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Diese Klasse stellt Methoden zur Anonymisierung von Daten mit Hilfe von
 * Mikroaggregationsverfahren bereit.
 *
 * Das Grundprinzip von Mikroaggregationsverfahren ist die Gruppierung von
 * möglichst ähnlichen Merkmalsträgern und deren Vereinheitlichung durch das
 * Ersetzen der Merkmalswerte durch den Durchschnittswert der Gruppe.
 *
 * Weitere Informationen: "Anonymisierungsvefahren für Paneldaten" von Jörg
 * Höhne
 *
 * @version 25. Februar 2012
 * @author Pascal Wasem, 3479498, PI Master [pascal.wasem@googlemail.com]
 */
//TODO implements AnonymizingInterface
public class MicroAggregationUtil {

    /*
     * -------------------------------------------------------------------------
     * properties
     * -------------------------------------------------------------------------
     */
    
    // logging
    private static final Logger _log = LoggerFactory.getLogger(MicroAggregationUtil.class);
    
    // singleton instance
    private static MicroAggregationUtil _instance = null;

    /*
     * -------------------------------------------------------------------------
     * constructor
     * -------------------------------------------------------------------------
     */
    private MicroAggregationUtil() {
        //NOP
    }

    /**
     * Benutzen Sie diese Methode um eine Instanz der Klasse
     * MicroAggregationUtil zu erhalten
     *
     * @return Eine Instanz der Klasse MicroAggregationUtil, die bereit zur
     * Benutzung ist
     */
    public static MicroAggregationUtil getInstance() {

        if (_instance == null) {

            _instance = new MicroAggregationUtil();
        }

        return _instance;
    }
    /*
     * -------------------------------------------------------------------------
     * public methods
     * -------------------------------------------------------------------------
     */

    /**
     * Diese Methode führt die <b>Unabhängige eindimensionale Mikroaggregation
     * mit variabler Gruppengröße</b> für die angebene Spaltennummer der übergebenen Tabelle durch.
     * D.h. die Größe der Mikroaggregationsgruppen wird dynamisch ermittelt. 
     * Getestet werden Gruppengrößen im Intervall [initialGroupSize,2*initialGroupSize-1]
     * 
     * @param data die Tabelle
     * @param columnNumber die zu anonymisierende Spalte der Tabelle (die erste Spalte hat den Index 0)
     * 
     * @return Die Tabelle mit anonymisierter Spalte
     * @throws IllegalColumnSizeException falls die angegebene Spalte weniger als 3 Element hat
     *
     */
    public Table<Integer, Integer, Double> performOneDimensionalMicroAggregationWithVariableGroupSize(int initialGroupSize, Table<Integer, Integer, Double> data, int columnNumber) {
        
        // Infonachricht ausgeben
        _log.info("Führe Mikroaggregation mit variabler Gruppengröße durch (Startgröße " + initialGroupSize + ") ... ");


        Table<Integer, Integer, Double> table = ArrayTable.create(data);


        table = sortTable(columnNumber, table);
        // Spaltenwerte lesen
        List<Double> column = readColumn(table, columnNumber);

        // Spalte absteigend sortieren
  //      column = sortDescending(column);
        table = sortTable(columnNumber, table);

        // Spalte anonymisieren
        column = anonymizeColumnWithVariableGroupSize(column,initialGroupSize);
        
        // Spalte durchmischen
     //   Collections.shuffle(column);

        // anonymisierte Spalte in Tabelle schreiben
        table = writeColumn(table, columnNumber, column);

        // Tabelle mit anonymisierter Spalte zurückgeben
        return table;
    }

     /**
     * Diese Methode führt die <b>Unabhängige eindimensionale Mikroaggregation
     * mit fester Gruppengröße</b> für die angebene Spaltennummer der übergebenen Tabelle durch.
     * 
     * @param data die Tabelle
     * @param columnNumber die zu anonymisierende Spalte der Tabelle (die erste Spalte hat den Index 0)
     * @param groupSize die Größe der Mikroaggregationsgruppen 
     * 
     * @return Die Tabelle mit anonymisierter Spalte
     * 
     * @throws IllegalColumnSizeException falls die angegebene Spalte weniger als 3 Element hat
     * @throws IllegalColumnSizeException falls die angegebene Gruppengröße kleiner 3 ist
     *
     */
    public Table<Integer, Integer, Double> performOneDimensionalMicroAggregationWithFixedGroupSize(Table<Integer, Integer, Double> data, int columnNumber, int groupSize) {
        
        // Infonachricht ausgeben
        _log.info("Führe Mikroaggregation mit fester Gruppengröße von " + groupSize + " durch...");
        
        // String -> Double
        Table<Integer, Integer, Double> table = ArrayTable.create(data);

        table = sortTable(columnNumber, table);
        // Spaltenwerte lesen
        List<Double> column = readColumn(table, columnNumber);

        // Spalte absteigend sortieren
     //   column = sortDescending(column);
        // Spalte anonymisieren
        column = anonymizeColumnWithFixedGroupSize(column, groupSize);

        // anonymisierte Spalte in Tabelle schreiben
        table = writeColumn(table, columnNumber, column);

        // Tabelle mit anonymisierter Spalte zurückgeben
        return table;
    }

    private Table<Integer, Integer, Double> sortTable(int columnNumber, Table<Integer, Integer, Double> table) {
        List<Map<Integer, Double>> mapList = new ArrayList<Map<Integer, Double>>( table.rowMap().values() );
        Collections.sort(mapList, new RowComparator(columnNumber));
        Collections.reverse(mapList);

        Table<Integer, Integer, Double> newTable = ArrayTable.create(table.rowKeySet(), table.columnKeySet());

        int i = 0;
        for (Map<Integer, Double> row : mapList) {
            addRowToTable(i, row, newTable);
            i++;
        }


        return table;
    }


    private void addRowToTable(int row, Map<Integer, Double> record, Table<Integer, Integer, Double> table) {
        for (int column : table.columnKeySet()) {
            table.put(row, column, record.get(column));
        }
    }

    /*
     * -------------------------------------------------------------------------
     * private methods
     * -------------------------------------------------------------------------
     */
    
    /**
     * Diese Methode führt die <b>Unabhängige eindimensionale Mikroaggregation
     * mit variabler Gruppengröße</b> für eine einzelne Spalte durch
     *
     * @param column die Spalte
     * @return die anonymisierte Spalte
     */
    private List<Double> anonymizeColumnWithVariableGroupSize(List<Double> column, int initialGroupSize) throws IllegalColumnSizeException {

        // Infonachricht ausgeben
        _log.info("Anononymisiere Spalte mit variabler Gruppengröße... ");
        
        if (column.size() < initialGroupSize) {

            throw new IllegalColumnSizeException(ILLEGAL_COLUMN_SIZE_ERROR_MESSAGE + ": " + column.size());
        }
        
        // anonymisierte Spalte
        List<Double> anonymizedColumn = Lists.newArrayList();

        // Optimale Gruppengröße finden
        int optimalGroupSize = findOptimalGroupSize(column,initialGroupSize);

        // Spalte mit optimaler Gruppengröße anonymisiern
        anonymizedColumn = anonymizeColumnWithFixedGroupSize(column, optimalGroupSize);

        // anonymisierte Spalte zurückgeben
        return anonymizedColumn;
    }

    /**
     * Diese Methode führt die <b>Unabhängige eindimensionale Mikroaggregation
     * mit fester Gruppengröße</b> für eine einzelne Spalte durch
     *
     * @param column die Spalte
     * @param groupSize die Gruppengröße
     * @return die anonymisierte Spalte
     */
    private List<Double> anonymizeColumnWithFixedGroupSize(List<Double> column, int groupSize) throws IllegalColumnSizeException {
        
        // Infonachricht ausgeben
        _log.info("Anononymisiere Spalte mit fester Gruppengröße... ");

         if (groupSize < 3) {

            throw new IllegalGroupSizeException(ILLEGAL_GROUP_SIZE_ERROR_MESSAGE + ": " + groupSize);
        }
        
        //anonymisierte Spalte
        List<Double> anonymizedColumn = Lists.newArrayList();

        // Spalte in Sub-Gruppen der aktuelle Gruppengröße teilen
        List<List<Double>> microAggregationGroups = splitColumn(column, groupSize);

        // Über Mikroaggeagtionsgruppen iterieren
        for (List<Double> microAggregationGroup : microAggregationGroups) {

            // Mikroaggreagtionsgruppe anonymisieren
            List<Double> anonymizedMicroAggrgationGroup = anonymizeMicroAggrgationGroup(microAggregationGroup, groupSize);

            // anonymisierte Mikroaggreagtionsgruppe zu anonymisierter Spalte hinzufügen
            anonymizedColumn.addAll(anonymizedMicroAggrgationGroup);
        }

        // anonymisierte Spalte zurückgeben
        return anonymizedColumn;
    }

    private List<Double> anonymizeMicroAggrgationGroup(List<Double> microAggregationGroup, int groupSize) {
        
         // Infonachricht ausgeben
        _log.info("Anonymisiere Mikroaggregationsgruppe...");

        List<Double> anonymizedMicroAggregationGroup = Lists.newArrayList();

        // Mittwert der Mikroaggreationsgruppe berechnen
        Double meanValue = computeMeanValue(microAggregationGroup);

        // Standardabweichung der Mikroaggreationsgruppe berechnen
        Double standardDeviation = computeStandardDeviation(microAggregationGroup, meanValue);

        // Mikroaggrationsgruppe in ZWEI möglichst gleich Größe Gruppen teilen
        int biggerValuesGroupSize = computeBiggerValuesGroupSize(microAggregationGroup);
        List<List<Double>> microAggregationGroupSplit = splitMicroAggregationGroup(microAggregationGroup, biggerValuesGroupSize);

        if (microAggregationGroupSplit.size() > 1) {

            // größere Werte lesen
            List<Double> biggerValues = microAggregationGroupSplit.get(0);
            // größere Werte anonymisieren
            biggerValues = anonymizeBiggerValues(biggerValues, meanValue, biggerValuesGroupSize, groupSize, standardDeviation);

            // kleinere Werte lesen
            List<Double> smallerValues = microAggregationGroupSplit.get(1);
            // kleinere Werte anonymisieren
            smallerValues = anonymizeSmallerValues(smallerValues, meanValue, biggerValuesGroupSize, groupSize, standardDeviation);

            // anonymisierte Werte zusammenführen
            anonymizedMicroAggregationGroup = mergeLists(biggerValues, smallerValues);

        } else {
            
            // Rest, d.h. einzelner Wert übrig
            List<Double> rest = microAggregationGroupSplit.get(0);
           
            // TODO Rest wie größere Werte behandeln !?!
            rest = anonymizeBiggerValues(rest, meanValue, biggerValuesGroupSize, groupSize, standardDeviation);
            
            // TODO einzelnen Wert anonymisieren
            anonymizedMicroAggregationGroup.addAll(rest);
        }

        return anonymizedMicroAggregationGroup;
    }

    private List<Double> anonymizeBiggerValues(List<Double> biggerValues, Double meanValue, int biggerValuesGroupSize, int groupSize, Double standardDeviation) {

        List<Double> anonymizedBiggerValues = Lists.newArrayList();

        // Wert laut Formel durch Mittelwert plus Standardabweichung ersetzen
        Double anonymizedValue = meanValue + Math.sqrt((groupSize - biggerValuesGroupSize) / biggerValuesGroupSize) * standardDeviation;
        
        // Infonachricht ausgeben
        _log.info("Anonymisere größere Werte mit: " + anonymizedValue);

        for (int i = 0; i < biggerValues.size(); i++) {

            anonymizedBiggerValues.add(anonymizedValue);
        }

        return anonymizedBiggerValues;
    }

    private List<Double> anonymizeSmallerValues(List<Double> smallerValues, Double meanValue, int biggerValuesGroupSize, int groupSize, Double standardDeviation) {

        List<Double> anonymizedSmallerValues = Lists.newArrayList();

        // Wert laut Formel durch Mittelwert minus Standardabweichung ersetzen
        Double anonymizedValue = meanValue - Math.sqrt(biggerValuesGroupSize / (groupSize - biggerValuesGroupSize)) * standardDeviation;
        
          // Infonachricht ausgeben
        _log.info("Anonymisere kleinere Werte mit: " + anonymizedValue);

        for (int i = 0; i < smallerValues.size(); i++) {

            anonymizedSmallerValues.add(anonymizedValue);
        }
        
        return anonymizedSmallerValues;
    }

    private List<Double> mergeLists(List<Double> first, List<Double> second) {


        List<Double> merge = Lists.newArrayList();

        merge.addAll(first);
        merge.addAll(second);

        return merge;
    }

    /**
     * Findet die optimale Gruppengröße im Kontext einer Mikroaggregation mit
     * variabler Gruppengröße
     *
     * @param column die Spalte die anonymisiert werden soll
     * @return die optimale Gruppengröße
     */
    private int findOptimalGroupSize(List<Double> column, int initialGroupSize) {

        // Infonachricht ausgeben
        _log.info("Bestimme optimale Gruppengröße... ");

        // optimale Gruppengröße, d.h. Summe grupppeninternen Varianz für alle Untergruppen minimal
        int optimalGroupSize = -1; // mit ungültiger Gruppengröße initialisieren
        Double minVarianceSum = Double.POSITIVE_INFINITY; // mit "schlechtestem Wert" initialisieren

        // alle möglichen Gruppengrößen für Spalte testen
        for (int currentGroupSize = initialGroupSize; currentGroupSize <= 2 * initialGroupSize - 1; currentGroupSize++) { // TODO

            _log.info("Teste Gruppengröße: " + currentGroupSize);

            // Summe der grupppeninternen Varianz für aktuell Gruppengröße
            Double currentVarianceSum = 0.0;

            // Spalte in Sub-Gruppen der aktuelle Gruppengröße teilen
            List<List<Double>> microAggregationGroups = splitColumn(column, currentGroupSize);

            // Über Mikragreagtionsgruppen iterieren
            int microAggregationGroupNumber = 1;
            for (List<Double> microAggregationGroup : microAggregationGroups) {

                // Mittwert der Mikroaggreationsgruppe berechnen
                Double meanValue = computeMeanValue(microAggregationGroup);

                // Standardabweichung der Mikroaggreationsgruppe berechnen
                Double standardDeviation = computeStandardDeviation(microAggregationGroup, meanValue);

                // Varianz der Mikroaggreationsgruppe berechnen
                Double variance = computeVariance(standardDeviation);

                // Varianz für alle Mikroaggreationsgruppen der aktuellen Gruppengröße aufsummieren
                currentVarianceSum += variance;

                // Infonachricht für aktuelle Mikroaggreationsgruppe ausgeben
                String subgroupInfoMessage = buildSubgroupInfoMessage(microAggregationGroupNumber, microAggregationGroup, meanValue, standardDeviation, variance);
                _log.info(subgroupInfoMessage);

                microAggregationGroupNumber++;
            }

            // Test ob aktuelle Gruppegröße Verbeserung bringt
            String improvementInformation = "Keine Verbesserung";
            if (currentVarianceSum < minVarianceSum) {

                minVarianceSum = currentVarianceSum;
                optimalGroupSize = currentGroupSize;
                improvementInformation = "Verbesserung";
            }

            // Infonachricht für aktuelle Gruppengröße ausgeben
            String groupSizeInfoMessage = buildGroupSizeInfoMessage(currentGroupSize, currentVarianceSum, improvementInformation);
            _log.info(groupSizeInfoMessage);
        }

        // Infonachricht über gefundene optimale Gruppengröße ausgeben
        _log.info("... optimale Gruppengröße: " + optimalGroupSize);
        return optimalGroupSize;
    }

    private String buildGroupSizeInfoMessage(int currentGroupSize, Double currentVarianceSum, String improvementInformation) {

        // StringBuilder ist schneller und braucht weniger Speicher als String + String + ... + String !!!
        StringBuilder groupSizeInfoMessage = new StringBuilder();

        groupSizeInfoMessage.append("Summe der Varianz aller Untergruppen der Größe ");
        groupSizeInfoMessage.append(currentGroupSize);
        groupSizeInfoMessage.append(": ");
        groupSizeInfoMessage.append(currentVarianceSum);
        groupSizeInfoMessage.append(" (");
        groupSizeInfoMessage.append(improvementInformation);
        groupSizeInfoMessage.append(")");

        return groupSizeInfoMessage.toString();
    }

    private String buildSubgroupInfoMessage(int microAggregationGroupNumber, List<Double> microAggregationGroup, Double meanValue, Double standardDeviation, Double variance) {

        // StringBuilder ist schneller und braucht weniger Speicher als String + String + ... + String !!!
        StringBuilder subgroupInfoMessage = new StringBuilder();

        subgroupInfoMessage.append("Untergruppe(");
        subgroupInfoMessage.append(microAggregationGroupNumber);
        subgroupInfoMessage.append("): ");
        subgroupInfoMessage.append(microAggregationGroup.toString());
        subgroupInfoMessage.append("; Mittelwert: ");
        subgroupInfoMessage.append(meanValue);
        subgroupInfoMessage.append("; Standardabweichung: ");
        subgroupInfoMessage.append(standardDeviation);
        subgroupInfoMessage.append("; Varianz: ");
        subgroupInfoMessage.append(variance);

        return subgroupInfoMessage.toString();
    }

    private List<Double> readColumn(Table<Integer, Integer, Double> table, int columnNumber) {
        
        // Infonachricht ausgeben
        _log.info("Lese Spalte " + columnNumber + "...");

        // Spalte aus Tablle auswählen
        Map<Integer, Double> tableColumn = table.column(columnNumber);

        // Spaltenwerte lesen
        List<Double> column = Lists.newArrayList(tableColumn.values());

        return column;
    }

    private Table<Integer, Integer, Double> writeColumn(Table<Integer, Integer, Double> table, int columnNumber, List<Double> column) {

        // Infonachricht ausgeben
        _log.info("Schreibe Spalte " + columnNumber + "...");
        
        int rowCounter = 0;
        for (Double value : column) {

            // Wert in Zelle schreiben
            table.put(rowCounter, columnNumber, value);

            // nächste Zeile
            rowCounter++;
        }

        return table;
    }

    /**
     * Sortiert eine Spalte absteigend
     *
     * @param column die Spalte
     * @return die absteigend sortierte Spalte
     */
    private List<Double> sortDescending(List<Double> column) {
        
        // Infonachricht ausgeben
        _log.info("Sortiere Spalte absteigend...");

        // Spalte aufsteigend sortieren 
        Collections.sort(column);

        // Spalte umkehren
        Collections.reverse(column);

        return column;
    }

    private List<List<Double>> splitColumn(List<Double> column, int microAggregationGroupSize) {
        
        // Infonachricht ausgeben
        _log.info("Teile Spalte in Mikroaggregationsgruppen der Größe " + microAggregationGroupSize);

        List<List<Double>> microAggregationGroups = Lists.partition(column, microAggregationGroupSize);

        return microAggregationGroups;
    }

    private List<List<Double>> splitMicroAggregationGroup(List<Double> microAggregationGroup, int biggerValuesGroupSize) {

        // Infonachricht ausgeben
        _log.info("Teile Mikroaggregationsgruppe der größeren und kleinere Werte...");
        
        return Lists.partition(microAggregationGroup, biggerValuesGroupSize);
    }

    private int computeBiggerValuesGroupSize(List<Double> microAggregationGroup) {
        
        Double biggerValuesGroupSizeAsDouble = Math.rint(microAggregationGroup.size() / 2.0);
        int biggerValuesGroupSize = biggerValuesGroupSizeAsDouble.intValue();

        if (biggerValuesGroupSize < 1) { // kann bei einer Gruppengröße von 1 gleich 0 sein

            biggerValuesGroupSize = 1;
        }
        
        // Infonachricht ausgeben
        _log.info("Bestimme Anzahl der größeren Werte: " + biggerValuesGroupSize);

        return biggerValuesGroupSize;

    }

    /**
     * Berechnet den Mittelwert
     */
    private Double computeMeanValue(List<Double> microAggregationGroup) {
        
        int microAggregationGroupSize = microAggregationGroup.size();

        Double sum = 0.0;

        for (Double value : microAggregationGroup) {

            sum = sum + value;
        }

        Double meanValue = sum / microAggregationGroupSize;
        
         // Infonachricht ausgeben
        _log.info("Bestimmte Mittelwert: " + meanValue);

        return meanValue;
    }

    /**
     * Berechnet die Standardabweichung
     */
    private Double computeStandardDeviation(List<Double> microAggregationGroup, Double meanValue) {

        Double sum = 0.0;

        for (Double value : microAggregationGroup) {

            sum = sum + Math.pow(value - meanValue, 2);
        }

        Double standardDeviation = Math.sqrt(sum / microAggregationGroup.size());
        
        // Infonachricht ausgeben
        _log.info("Bestimmte Standardabweichung: " + standardDeviation);

        return standardDeviation;
    }

    /**
     * Berechnet die Varianz
     */
    private Double computeVariance(Double standardDeviation) {

        Double variance = Math.pow(standardDeviation, 2);
                
         // Infonachricht ausgeben
        _log.info("Bestimmte Varianz: " + variance);
        
        return variance;
        
    }
    
    private void shuffle(List<Double> column) {
        
        // Infonachricht ausgeben
        _log.info("Mische Spalte durch...");
        
         // Spalte durchmischen
        Collections.shuffle(column);
    }
    
    /*
     * -------------------------------------------------------------------------
     * constants
     * -------------------------------------------------------------------------
     */

    // error messages
    private static final String ILLEGAL_COLUMN_SIZE_ERROR_MESSAGE = "Ungültige Spaltengröße";
    private static final String ILLEGAL_GROUP_SIZE_ERROR_MESSAGE = "Ungültige Gruppengröße";
}
