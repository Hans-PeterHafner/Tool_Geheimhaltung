package de.htw.pim.sg.manager;

import de.htw.pim.sg.methods.AnonymizingMethods;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.htw.pim.sg.matching.MatchingMethods;
import de.htw.pim.sg.util.MetaData;

/**
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohammed Abdulla, PI-Master
 */
public interface ManagerInterface {
    
    /**
     * Methode zum abrufen der Eingabedaten
     * 
     * @return Tabelle mit Eingabedaten
     */
    public Table<Integer, Integer, String> getData();
    
    
    /**
     * Methode zum abrufen der anonymisierten Daten
     * 
     * @return Tabelle der anonymisierten Daten
     */
    public Table<Integer, Integer, String> getAnonymizedData();
    
    /**
     * Methode zum Abrufen der Metadaten
     * 
     * @return MetaData-Objekt
     */
    public MetaData getMetaData();
    
    /**
     * Methode zum Aktualisieren der Metadaten
     * 
     * @param newMetaData Das aktualisierte MetaData-Objekt
     */
    public void setMetaData(MetaData newMetaData);
    
    
    /**
     * Methode zum anonymisieren der Daten. 
     * 
     * @param metadata  Die Metadaten zu den Daten
     * @param data	Die zu anonymisierenden Daten
     * @param method	Die Anonymisierungsmethode
     */
    public void anonymize(MetaData metadata, AnonymizingMethods method, 
	    ArrayTable<Double, Double, Double> data);
    
    /**
     * Methode zur Simulation eines Datenangriffs
     * 
     * @param metadata	    Die Metadaten zu den Daten
     * @param method	    Die Matching-Methode
     * @param data	    Die echten Daten
     * @param anonymData    Die anonymisierten Daten
     */
    public void match(MetaData metadata, MatchingMethods method,  
	    ArrayTable<Integer, Integer, String> data, 
	    ArrayTable<Integer, Integer, String> anonymData);

}
