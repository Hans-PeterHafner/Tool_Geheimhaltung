package de.htw.pim.sg.matching;

import com.google.common.collect.ArrayTable;
import de.htw.pim.sg.util.MetaData;


/**
 * Interface für einheitliche Schnittstelle der Matchingverfahren zum Manager
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohammed Abdulla, PI-Master
 */
public interface MatchingInterface {

    /**
     * Stößt das Matching an
     * 
     * @param sourceData Original Daten
     * @param anonymData Anonymisierten Daten
     */
    public void startMatching(ArrayTable<Integer, Integer, String> sourceData, 
	    ArrayTable<Integer, Integer, String> anonymData,
            MetaData meta) throws Exception;
    
    
    /**
     * Liefert die Ergebnisse des Matchings zurück
     * 
     * @return Objekt vom Typ MatchingResult
     */
    
    public MatchingResult getMatchingResults();
}
