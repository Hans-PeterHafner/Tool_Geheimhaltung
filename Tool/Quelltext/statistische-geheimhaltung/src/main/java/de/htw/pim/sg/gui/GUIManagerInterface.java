package de.htw.pim.sg.gui;

import com.google.common.collect.Table;
import de.htw.pim.sg.methods.AnonymizingMethods;
import de.htw.pim.sg.matching.MatchingMethods;
import de.htw.pim.sg.util.MetaData;

/** 
 * Dieses Interface bereitet die Schnittstelle zur GUI.
 * Die GUI bedient dieses Interafce, um die Daten zu erhalten.
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohammed Abdulla, PI-Master
 */
public interface GUIManagerInterface {

    
    /**
     * Methode zum abrufen der Eingabedaten
     * 
     * @return Tabelle mit Eingabedaten
     */
    public Table<Integer, Integer, String> getData();
    
    /**
     * Methode zum Abrufen der bereits anonymisierten Daten
     * 
     * @return Tabelle mit den anonymisierten Daten
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
	    Table<Double, Double, Double> data);
    
    /**
     * Methode zur Simulation eines Datenangriffs
     * 
     * @param metadata	    Die Metadaten zu den Daten
     * @param method	    Die Matching-Methode
     * @param data	    Die echten Daten
     * @param anonymData    Die anonymisierten Daten
     */
    public void match(MetaData metadata, MatchingMethods method,  
	    Table<Integer, Integer, String> data, 
	    Table<Integer, Integer, String> anonymData);

    
}
