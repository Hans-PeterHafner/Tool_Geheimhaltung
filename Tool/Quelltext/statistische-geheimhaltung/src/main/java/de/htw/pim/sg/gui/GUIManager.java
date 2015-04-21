package de.htw.pim.sg.gui;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.htw.pim.sg.manager.Manager;
import de.htw.pim.sg.matching.MatchingMethods;
import de.htw.pim.sg.methods.AnonymizingMethods;
import de.htw.pim.sg.util.MetaData;

/**
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohammed Abdulla, PI-Master
 */
public class GUIManager implements GUIManagerInterface {
    
    private static GUIManagerInterface _instance = null; 
    
    /**
     * Methode liefert die Singleton-Instanz des GUIManagers
     * 
     * @return GUIManager-Instanz
     */
    public static GUIManagerInterface getInstance() {
	if(_instance == null) {
	    _instance = new GUIManager();
	}
	
	return _instance; 
    }
    
    public Table<Integer, Integer, String> getData() {
	
	return Manager.getInstance().getData();
    }

    public Table<Integer, Integer, String> getAnonymizedData() {
	
	return Manager.getInstance().getAnonymizedData();
    }

    public MetaData getMetaData() {
	return Manager.getInstance().getMetaData();
    }

    public void setMetaData(MetaData newMetaData) {
	Manager.getInstance().setMetaData(newMetaData);
    }

    public void anonymize(MetaData metadata, AnonymizingMethods method, 
	    ArrayTable<Double, Double, Double> data) {
	
	Manager.getInstance().anonymize(metadata, method, data);
    }

    public void match(MetaData metadata, MatchingMethods method, 
	    ArrayTable<Integer, Integer, String> data, 
	    ArrayTable<Integer, Integer, String> anonymData) {
	
	Manager.getInstance().match(metadata, method, data, anonymData);
    }

    public void anonymize(MetaData metadata, AnonymizingMethods method, Table<Double, Double, Double> data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void match(MetaData metadata, MatchingMethods method, Table<Integer, Integer, String> data, Table<Integer, Integer, String> anonymData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
