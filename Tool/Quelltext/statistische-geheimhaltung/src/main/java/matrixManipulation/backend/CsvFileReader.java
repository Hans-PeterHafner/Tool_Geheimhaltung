package matrixManipulation.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.LinkedList;

/** @author Ciprian Savu
 * Diese Klasse liest .csv Dateien ein
 */
public class CsvFileReader {

	private String fileName;
	private File file;
	private int[][] values;
	
	/** 
	 * Standardkonstruktuor, setzt alles auf Null;
	 */
	public CsvFileReader(){
		this.fileName = "";
		this.file = null;
		this.values = null;
	}
	
	/**
	 * Konstruktor mit Werten
	 * @param fileName
	 */
	public CsvFileReader(String fileName){
		this.fileName = fileName;
		this.file = null;
		this.values = null;
	}
	
	/**
	 * Funktion Parse, liest eine Datei ein und gibt ein 2-Dimensionales int Array zurueck, falls erfolgreich
	 * bei Misserfolg,gibt die Funktion null zurueck 
	 */
	public int[][] parse(){
		if (checkIfFileExists()){
			int lines = getLineCount();
			int rows = getRowCount();
			/*System.out.println(" Zeilen: " + getLineCount());
			System.out.println("Spalten: " + getRowCount());*/
			if (lines > 0 && rows > 0){
				this.values = readValues(lines,rows); // new int[lines-1][rows-1];
				if (this.values != null){
					return this.values;
				} else {
					return null;
				}
			} else {
				System.err.println("Konnte keine verwertbaren Daten lesen");
				return null;
			}
		} else {
			System.err.println("keine Datei gefunden " + this.file.getAbsolutePath());
			return null;
		}
	}
	
	/**
	 * Gibt die aus der .csv Datei gelesenen Werte in Form eines 2-Dimensionalen Arrays aus, oder null
	 * @param lines
	 * @param rows
	 * @return
	 */
	private int[][] readValues(int lines, int rows){
		int mValues[][] = new int[lines][rows];
		BufferedReader fileReader = null;
		
		try{
			fileReader = new BufferedReader(new FileReader (file.getAbsolutePath()));
			String line;
			int lineCounter = 0;
			while ((line = fileReader.readLine()) != null){
				line = line.replaceAll(",", ";");
				line = line.replaceAll("\t", ";");
				line = line.replaceAll(" ", "");
				if (line.contains(";")){
					String[] splitted = line.split(";");
					for (int i = 0; i < rows; i++){
						mValues[lineCounter][i] = Integer.parseInt(splitted[i]);
					}
					lineCounter++;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		if ((mValues.length > 0) || (mValues[0].length > 0)){
			return mValues;
		} else {
			return null;
		}
	}
	
	/**
	 * gibt die Anzahl der nicht leeren Zeilen in einer Datei zurueck 
	 * @return
	 */
	private int getLineCount(){
		BufferedReader fileReader = null;
		int lines = 0;
		
		try{
			fileReader = new BufferedReader(new FileReader (file.getAbsolutePath()));
			String line;
			while ((line = fileReader.readLine()) != null){
				if (line.contains(",") || line.contains(";") || line.contains("\t")){
					lines++;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
		
		return lines;
	}
	
	/**
	 * gibt die Anzahl der Spalten in einer Datei zurueck 
	 * @return
	 */
	private int getRowCount(){
		
		BufferedReader fileReader = null;
		int rows = 0;
		
		try{
			fileReader = new BufferedReader(new FileReader (file.getAbsolutePath()));
			String line;
			while ((line = fileReader.readLine()) != null){
				if (line.contains(",")){
					rows = line.split(",").length;
					break;
				}
				if (line.contains(";")){
					rows = line.split(";").length;
					break;
				}
				if (line.contains("\t")){
					rows = line.split("\t").length;
					break;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
		
		return rows;
	}
	
	
	/** Prueft ob Datei existiert, ergaenzt Variable "file"
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkIfFileExists(){
		boolean fileExists = true;
		try{			
			//System.out.println("Dateiname: " + this.fileName);
			this.file = new File(this.fileName);
			
		} catch (Exception e){
			e.printStackTrace();
			this.file = null;
			fileExists = false;
		}
		return this.file.exists();
		
	}

	
}
