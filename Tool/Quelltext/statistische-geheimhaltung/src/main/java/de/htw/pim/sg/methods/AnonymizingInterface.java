package de.htw.pim.sg.methods;

import com.google.common.collect.ArrayTable;

/**
 * Interface für einheitliche Schnittstelle der Anonymisierungsmethoden zum Manager
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohammed Abdulla, PI-Master
 */
public interface AnonymizingInterface {
    /**
     * Damit stößt man die Anonymisierung an.
     * 
     */
    //TODO eine sinnvolle Alternative suchen
//    public void anonymize(ArrayTable<Integer, Integer, String> dataToAnonymize);
    
    /**
     * Methode zum Zurückgeben der anonymisierten Daten als Tabelle
     * 
     * @return ArrayTable mit anonymisierten Daten
     */
    public ArrayTable getAnonymizedData();
    
    
}
