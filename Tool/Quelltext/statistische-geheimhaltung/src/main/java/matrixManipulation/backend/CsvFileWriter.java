package matrixManipulation.backend;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import matrixManipulation.core.Statics;

/**
 * 
 * @author Ciprian Savu
 * Diese Klasse schreibt .csv Dateien
 */
public abstract class CsvFileWriter {
	/** Schreibt eine .csv Datei. Falls die Zieldatei schon existiert, wird sie ueberschrieben
	 * 
	 * @param fileName
	 * @param matrix
	 * @return
	 */
	public static boolean save(String fileName, int[][] matrix){
		File file = new File(fileName);
		
		try {
			FileWriter writer = new FileWriter(file,false);
			StringBuilder outputString = new StringBuilder();
			for (int y = 0; y < matrix.length; y++){
				for (int x = 0; x < matrix[0].length; x++){
					outputString.append(matrix[y][x]);
					if (x < (matrix[0].length -1)) outputString.append(";");
				}
				outputString.append("\r\n");
				
				writer.write(outputString.toString());
				outputString = new StringBuilder();
				int percent = (int)(((double)(y+1) / (double)matrix.length)*100);
				Statics.getView().setProgressBar(y+1, "Speichervorgang: " + percent + " %");
				Statics.getView().onNewStatus("Speichervorgang aktiv, bitte warten. Momentaner Status: " + percent + " Prozent gespeichert", Color.YELLOW, 0);
			}
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
}
