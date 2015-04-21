/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.manager;

/**
 * Diesse Klasse (singleton) hält alle relevanten Einstellungen 
 * des Programms bereit
 * 
 * @version 1.0, 09.Februar 2012
 * @author Mohamemd Abdulla, PI-Master
 */
public class Properties {
    
    private static Properties _instance = null;
    
    
    private char   separator; 
    private String sourceDataPath	= null; 
    private String anonymizedDataPath	= null; 
    
    
    public static Properties getInstance() {
	if(_instance == null) {
	    _instance = new Properties();
	}
	return _instance;
    }
    
    
    public void initProperties(char separator, String sourceDataPath, 
	    String anonymizedDataPath){

	//TODO vervollständigen!!
	this.separator		= separator; 
	this.sourceDataPath	= sourceDataPath; 
	this.anonymizedDataPath = anonymizedDataPath;
	
    }
    
    
    
    /****************** Getter **********************/
    public char getSeparator(){
	return separator;
    }
    
    public String getSourceDataPath(){
	return sourceDataPath;
    }
    
    public String getAnonymizedDataPath(){
	return anonymizedDataPath;
    }
}
