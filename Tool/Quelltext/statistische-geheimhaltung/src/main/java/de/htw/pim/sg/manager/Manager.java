package de.htw.pim.sg.manager;

import de.htw.pim.sg.methods.AnonymizingMethods;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.matching.MatchingMethods;
import de.htw.pim.sg.util.MetaData;
import java.io.File;

/**
 * Diese Klasse agiert als Manager für alle Funktionalitäten des Programms. 
 * Sie dient als zentrale Schnittstelle zur GUI, zum Filesystem und zu den 
 * einzelnen Anonymisierungs- bzw. Matching-Methoden
 * 
 * @version 1.0, 09.Februar 2012
 * @author Moahmmed Abdulla, PI-Master
 */
public class Manager implements ManagerInterface {

    private static ManagerInterface _instance = null;
    
    private MetaData metadata = null;
    
    /**
     * Methode liefert die Singleton-Instanz des Managers
     * 
     * @return Manager-Instanz
     */
    public static ManagerInterface getInstance() {
	if(_instance == null){
	    _instance = new Manager();
	}
	return _instance;
    }

    
    
    public Table<Integer, Integer, String> getData() {
	return CsvUtil.readCsvFile(
		new File(Properties.getInstance().getSourceDataPath()), 
		Properties.getInstance().getSeparator()
	    );
    }

    public Table<Integer, Integer, String> getAnonymizedData() {
	return CsvUtil.readCsvFile(
		new File(Properties.getInstance().getAnonymizedDataPath()), 
		Properties.getInstance().getSeparator()
	    );
    }

    
    public MetaData getMetaData() {
	if(metadata == null){
	    metadata = MetaData.getInstance();
	}
	return metadata;
    }

    
    public void setMetaData(MetaData newMetaData) {
	metadata = newMetaData;
    }

    
    public void anonymize(MetaData metadata, AnonymizingMethods method, 
	    ArrayTable<Double, Double, Double> data) {
	//TODO implementieren
	//Mikroaggregation ausführen
	if(method == AnonymizingMethods.MICROAGGREGATION) {
	    
	} //Multiplicative Noise ausführen
	else if(method == AnonymizingMethods.MULTIPLICATIVE_NOISE){
	    
	}
    }

    public void match(MetaData metadata, MatchingMethods method, ArrayTable<Integer, Integer, String> data, ArrayTable<Integer, Integer, String> anonymData) {
	//TODO implementieren
	throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
