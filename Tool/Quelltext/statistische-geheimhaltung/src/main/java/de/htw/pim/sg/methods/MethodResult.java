package de.htw.pim.sg.methods;

import com.google.common.collect.Table;

import java.util.Map;

/**
 * Speichert das Ergebnis einer Anonymisierung
 * @author Kirill Povolotskyy
 *         Date: 07/07/12
 */
public class MethodResult {

    Table<Integer, Integer, Double> anonymizedTable;
    Map<String, String> paremeters;

    public MethodResult(Table<Integer, Integer, Double> anonymizedTable, Map<String, String> paremeters){
        this.anonymizedTable = anonymizedTable;
        this.paremeters = paremeters;
    }

    public Map<String, String> getParemeters() {
        return paremeters;
    }

    public Table<Integer, Integer, Double> getAnonymizedTable() {
        return anonymizedTable;
    }
}
