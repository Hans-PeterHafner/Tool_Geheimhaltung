/** @author Ciprian Savu
 * Diese Abstrakte Klasse enthaelt Konstanten und oeffentliche Variablen 
 */
package matrixManipulation.core;

import matrixManipulation.view.View;
import matrixManipulation.core.ResultMatrixs;

public abstract class Statics {

	public static View view = null;
	public static final String LOAD_FILE = "LoadFile";
	public static final String SAVE_FILE = "SaveFile";
	public static final String SAVE_LOG = "SaveLog";
	public static int[][] originalValues = null;
	public static ResultMatrixs resultObjects = null;
	public static int lastNullHandling = -1;
	public static String filePath = "$user";
	public static double alpha = 0.33;
	public static double beta = 0.33;
	/**
	 * Setter fuer die View
	 * @param v
	 */
	public static void setView(View v){
		view = v;
	}
	
	/**
	 * Getter fuer die View
	 * @return
	 */
	public static View getView(){
		return view;
	}
}
