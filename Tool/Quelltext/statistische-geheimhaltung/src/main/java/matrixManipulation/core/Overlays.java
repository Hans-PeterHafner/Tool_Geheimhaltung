package matrixManipulation.core;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import javax.swing.text.Position;

import matrixManipulation.core.ResultMatrixs;

/**
 * @author Ciprian Savu
 * in Dieser Klasse finden die Overlays statt
 */
public abstract class Overlays {
	
	
	/**
	 * Funktion zum diagonalen Ueberlagern einer Matrix
	 * Gibt ein Objekt der Klasse ResultMatrixs zurueck.
	 * 
	 * @param originalValuesMatrix
	 * @param repeatCount
	 * @param nullHandling
	 * @param allSteps
	 * @param alphaValue
	 * @param betaValue
	 * @return ResultMatrixs
	 */
	public static ResultMatrixs cyclicDiagonalOverlay(int [][] originalValuesMatrix, int repeatCount, int nullHandling, boolean allSteps, double alphaValue, double betaValue, boolean saveLog, String logPath){
		ResultMatrixs returnValues = new ResultMatrixs();
		//Datei fuer log vorbereiten, falls notwendig
		if (saveLog) {
			initiateLogFile(logPath);
			appendLogToLogFile(logPath, new StringBuilder().append("<center><h2>Diagonale zyklische &Uuml;berlagerung</h2></center><br><br>"));
		}
		
		StringBuilder logOutputNullIgnore = new StringBuilder();
		StringBuilder logOutputWithNegativeValues = new StringBuilder();
		
		boolean transponed = false;
		int valueMatrixNullIgnore[][] = null;
		int valueMatrixWithNegativeValues[][] = null;
		// Sammlung aller Zwischenschritte (Matritzen ohne null)
		Vector<int[][]> colMatrixNullIgnore = null;
		// Sammlung aller Zwischenschritte (Matritzen mit Null oder negative Werte)
		Vector<int[][]> colMatrixWithNegativeValues = null;
		int width = 0, length = 0;
		int nullCount = 0;
		
		//Matrizen, und Collections nach Bedarf initialisieren
		if ((nullHandling == 0) || (nullHandling == 2)){
		
			// Mehr Zeilen als Spalten? -> Matrix transponieren 
			if (originalValuesMatrix.length > originalValuesMatrix[0].length){
				valueMatrixNullIgnore = transposeMatrix(originalValuesMatrix);
				transponed = true;
			} else {
				valueMatrixNullIgnore = matrixCopy(originalValuesMatrix);
			}
			length = valueMatrixNullIgnore.length;
			width = valueMatrixNullIgnore[0].length;
			colMatrixNullIgnore = new Vector<int[][]>();
		}
		if ((nullHandling == 1) || (nullHandling == 2)){
			// Mehr Zeilen als Spalten? -> Matrix transponieren 
			if (originalValuesMatrix.length > originalValuesMatrix[0].length){
				valueMatrixWithNegativeValues = transposeMatrix(originalValuesMatrix);
				transponed = true;
			} else {
				valueMatrixWithNegativeValues = matrixCopy(originalValuesMatrix);
			}
			length = valueMatrixWithNegativeValues.length;
			width = valueMatrixWithNegativeValues[0].length;
			colMatrixWithNegativeValues = new Vector<int[][]>();
		}
		//Schreibe Original-Werte Matrix
		if (saveLog){
			if (nullHandling == 0){
				appendLogToLogFile(logPath, toStringMatrixHtml(valueMatrixNullIgnore).insert(0, "<b>Originalmatrix</b><br>" ));
			} else {
				appendLogToLogFile(logPath, toStringMatrixHtml(valueMatrixWithNegativeValues).insert(0, "<b>Originalmatrix</b><br>"));
			}
		}
		
		//Gesamtdurchgaenge
		int totalCount = width * repeatCount;

		//Gewicht eines einzelnen X-Durchlaufs
		double slice = 100/(double)totalCount;
		//Fortschrittsvariable fuer Ladebalken
		double totalProgress = 0.0;
		
		//Wiederhole bis Anzahl der Wiederholungen erreicht
		for (int rc = 0; rc < repeatCount; rc++){
			
			// Zufalls-Variable initialisieren
			Random rand = new Random();
			if (saveLog){
				if ((nullHandling == 0) || (nullHandling == 2)){
					logOutputNullIgnore = new StringBuilder().append("<b>Durchgang " + (rc+1) + ", Verfahren: Zyklen mit 0 werden &uuml;bersprungen<br><br></b>");
				}
				if ((nullHandling == 1) || (nullHandling == 2)){
					logOutputWithNegativeValues = new StringBuilder().append("<b>Durchgang " + (rc+1) + ", Verfahren: Zyklen mit 0 oder negativen Werten werden berechnet<br><br></b>");
				}
			}
			// ein kompletter Durchgang (alle x werte)
			for (int x0 = 0; x0 < width; x0++ ){
				//in jedem Durchgang Zufallszahl erzeugen
				double randomNumber = rand.nextDouble();
				int calculateWithNullValues = 0;
				int calculateWithNegativeValues = 0; 
				// Benoetigte Multiplikator-Werte ausrechnen
				if ((nullHandling == 0) || (nullHandling == 2)){
					//Bestimmen ob eine Berechnung ueberhaupt durchgefuehrt werden muss
					calculateWithNullValues = determineIfCalculate(valueMatrixNullIgnore, x0, randomNumber, alphaValue, betaValue);
					if (calculateWithNullValues == 0) {
						if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixDiagonal(valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
					} else if (calculateWithNullValues == -2) {
						nullCount++;
						calculateWithNullValues = 0;
						if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixDiagonal(valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
					}
					
				}
				if ((nullHandling == 1) || (nullHandling == 2)){
					if (randomNumber <= alphaValue){
						calculateWithNegativeValues = 1;
					} else if ((randomNumber > alphaValue) && (randomNumber <= (alphaValue + betaValue))){
						calculateWithNegativeValues = -1;
					}
					
				}
				
				// Matrix mit ignorierten 0-Werten ausrechnen, wenn benoetigt
				if (((nullHandling == 0) || (nullHandling == 2)) && (calculateWithNullValues != 0)){
					
					valueMatrixNullIgnore = overlayMatrixWithMultiplicant(valueMatrixNullIgnore, x0, calculateWithNullValues);
					//Log schreiben
					if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixDiagonal(valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
				}
				
				// Matrix mit 0 und negativ-Werten ausrechnen, wenn benoetigt
				if (((nullHandling == 1) || (nullHandling == 2)) && (calculateWithNegativeValues != 0)){
					valueMatrixWithNegativeValues = overlayMatrixWithMultiplicant(valueMatrixWithNegativeValues, x0, calculateWithNegativeValues);
				}
				//Log schreiben
				if ( saveLog && ((nullHandling == 1) || (nullHandling == 2))) logOutputWithNegativeValues.append(toHtmlStringMatrixDiagonal(valueMatrixWithNegativeValues, x0, randomNumber, calculateWithNegativeValues, rc));
				
				//Prozentueller Status im Ladebalken setzen
				totalProgress += slice;
				Statics.getView().setProgressBar((int)totalProgress, "Berechnungsvorgang: " + (int)totalProgress + "%" );

			} // Ende X-Durchgang
			
			// Alle Zwischenschritte anzeigen?
			if (allSteps){
				// Letzter Schritt = Ergebnis, nicht aufnehmen
				if (rc < (repeatCount-1)){
					if ((nullHandling == 0) || (nullHandling == 2)){
						colMatrixNullIgnore.add(matrixCopy(valueMatrixNullIgnore));
					}
					if ((nullHandling == 1) || (nullHandling == 2)){
						colMatrixWithNegativeValues.add(matrixCopy(valueMatrixWithNegativeValues));
					}
				}
			}
			// Log an Datei anhaengen, falls erwuenscht
			if (saveLog){
				if (nullHandling == 0){
					logOutputNullIgnore.append("<br><br>");
					appendLogToLogFile(logPath, logOutputNullIgnore);
				}
				if (nullHandling == 1) {
					logOutputWithNegativeValues.append("<br><br>");
					appendLogToLogFile(logPath, logOutputWithNegativeValues);
				} 
				if (nullHandling == 2){
					StringBuilder newLog = new StringBuilder().append("<table border=\"1\"><tr><td align=\"center\">");
					newLog.append(logOutputNullIgnore);
					newLog.append("</td><td>&nbsp;</td><td align=\"center\">");
					newLog.append(logOutputWithNegativeValues);
					newLog.append("</td></tr></table><br><br>");
					appendLogToLogFile(logPath, newLog);
				}
			}
						
		} // Ende Wiederholungen

		// Ergebnis-Matrizen aufnehmen
		if (!transponed){
			if ((nullHandling == 0) || (nullHandling == 2)){
				colMatrixNullIgnore.add(matrixCopy(valueMatrixNullIgnore));
			}
			if ((nullHandling == 1) || (nullHandling == 2)){
				colMatrixWithNegativeValues.add(matrixCopyWithoutNegatives(valueMatrixWithNegativeValues));
			}
		} else {
			if ((nullHandling == 0) || (nullHandling == 2)){
				colMatrixNullIgnore.add(transposeMatrixWithoutNegativeValues(valueMatrixNullIgnore));
			}
			if ((nullHandling == 1) || (nullHandling == 2)){
				colMatrixWithNegativeValues.add(transposeMatrixWithoutNegativeValues(valueMatrixWithNegativeValues));
			}
		}
			
		//Log schreiben, Ergebnismatrizen + Anzahl der uebersprungenen Zyklen beim Verfahren 1
		if (saveLog){
			
			StringBuilder logOutput = new StringBuilder();
			if (nullHandling == 2){
				logOutput.append("<br><br><b>Ergebnis-Matrizen</b><br><br>");
				logOutput.append("<table border=\"1\"><tr><td align=\"center\"><b>Verfahren: Zyklen mit 0 werden &uuml;bersprungen<br></b>");
				logOutput.append(toStringMatrixHtml(colMatrixNullIgnore.lastElement()));
				logOutput.append("</td><td>&nbsp;</td><td align=\"center\"><b>Verfahren: Zyklen mit 0 oder negativen Werten werden berechnet<br></b>");
				logOutput.append(toStringMatrixHtml(colMatrixWithNegativeValues.lastElement()));
				logOutput.append("</td></tr></table><br><br>");
			} else {
				logOutput.append("<br><br><b>Ergebnis-Matrix</b><br><br>");
				if (nullHandling == 0){
					logOutput.append(toStringMatrixHtml(colMatrixNullIgnore.lastElement()));
				} else {
					logOutput.append(toStringMatrixHtml(colMatrixWithNegativeValues.lastElement()));
				}
			}
			
			appendLogToLogFile(logPath, logOutput);
			
			if (nullHandling == 0 || nullHandling == 2){
				logOutputNullIgnore = new StringBuilder().append("<br><br>Anzahl der &Uuml;bersprungenen Zyklen beim Verfahren, bei welchem die Zyklen mit 0 &uuml;bersprungen wurden: ");
				logOutputNullIgnore.append(nullCount);
				appendLogToLogFile(logPath, logOutputNullIgnore);
			}
		}

		
		// Rueckgabe vorbereiten
		returnValues.setTransposed(transponed);
		returnValues.setNullCount(nullCount);
		returnValues.setColMatrixNullIgnore(colMatrixNullIgnore);
		returnValues.setColMatrixWithNegativeValues(colMatrixWithNegativeValues);
		return returnValues;
	}

	/**
	 * Funktion zum Ueberlagern einer Matrix mit einer zufaelligen Permutation 
	 * Gibt ein Objekt der Klasse ResultMatrixs zurueck. 
	 * 
	 * @param originalValuesMatrix
	 * @param repeatCount
	 * @param nullHandling
	 * @param allSteps
	 * @param alphaValue
	 * @param betaValue
	 * @return ResultMatrixs
	 */
	public static ResultMatrixs permutantOverlay(int [][] originalValuesMatrix, int repeatCount, int nullHandling, boolean allSteps, double alphaValue, double betaValue, boolean saveLog, String logPath){
		ResultMatrixs returnValues = new ResultMatrixs();
		
		//Datei fuer log vorbereiten, falls notwendig
		if (saveLog) {
			initiateLogFile(logPath);
			appendLogToLogFile(logPath, new StringBuilder().append("<center><h2>Zyklische &Uuml;berlagerung mit Permutationen</h2></center><br><br>"));
		}

		StringBuilder logOutputNullIgnore = new StringBuilder();
		StringBuilder logOutputWithNegativeValues = new StringBuilder();
		
		boolean transponed = false;
		int valueMatrixNullIgnore[][] = null;
		int valueMatrixWithNegativeValues[][] = null;
		// Sammlung aller Zwischenschritte (Matritzen ohne null)
		Vector<int[][]> colMatrixNullIgnore = null;
		// Sammlung aller Zwischenschritte (Matritzen mit Null oder negative Werte)
		Vector<int[][]> colMatrixWithNegativeValues = null;
		int width = 0, length = 0;
		int nullCount = 0;
		int[] overlayPositions = null;
		
		//Matrizen, und Collections nach Bedarf initialisieren
		if ((nullHandling == 0) || (nullHandling == 2)){
		
			// Mehr Zeilen als Spalten? -> Matrix transponieren 
			if (originalValuesMatrix.length > originalValuesMatrix[0].length){
				valueMatrixNullIgnore = transposeMatrix(originalValuesMatrix);
				transponed = true;
			} else {
				valueMatrixNullIgnore = matrixCopy(originalValuesMatrix);
			}
			length = valueMatrixNullIgnore.length;
			width = valueMatrixNullIgnore[0].length;
			colMatrixNullIgnore = new Vector<int[][]>();
			overlayPositions = generatePermutantMultiplikateMatrix(valueMatrixNullIgnore);
		}
		
		if ((nullHandling == 1) || (nullHandling == 2)){
			// Mehr Zeilen als Spalten? -> Matrix transponieren 
			if (originalValuesMatrix.length > originalValuesMatrix[0].length){
				valueMatrixWithNegativeValues = transposeMatrix(originalValuesMatrix);
				transponed = true;
			} else {
				valueMatrixWithNegativeValues = matrixCopy(originalValuesMatrix);
			}
			length = valueMatrixWithNegativeValues.length;
			width = valueMatrixWithNegativeValues[0].length;
			colMatrixWithNegativeValues = new Vector<int[][]>();
			overlayPositions = generatePermutantMultiplikateMatrix(valueMatrixWithNegativeValues);
		}
		
		//Schreibe Original-Werte Matrix
			if (saveLog){
				if (nullHandling == 0){
					appendLogToLogFile(logPath, toStringMatrixHtml(valueMatrixNullIgnore).insert(0, "<b>Originalmatrix</b><br>"));
				} else {
					appendLogToLogFile(logPath, toStringMatrixHtml(valueMatrixWithNegativeValues).insert(0, "<b>Originalmatrix</b><br>"));
				}
			}
		
		//Gesamtdurchgaenge
		int totalCount = width * repeatCount;

		//Gewicht eines einzelnen X-Durchlaufs
		double slice = 100/(double)totalCount;
		//Fortschrittsvariable fuer Ladebalken
		double totalProgress = 0.0;			
			
		//Wiederhole bis Anzahl der Wiederholungen erreicht
		for (int rc = 0; rc < repeatCount; rc++){
			// Zufalls-Variable initialisieren
			Random rand = new Random();
			
			//Log schreiben
			if (saveLog){
				if ((nullHandling == 0) || (nullHandling == 2)){
					logOutputNullIgnore = new StringBuilder().append("<b>Durchgang " + (rc+1) + ", Verfahren: Zyklen mit 0 werden &uuml;bersprungen<br><br></b>1. Basiszyklus: ");
					logOutputNullIgnore.append(toHtmlStringOverlayMatrix(overlayPositions, valueMatrixNullIgnore, "+", "-"));
				}
				if ((nullHandling == 1) || (nullHandling == 2)){
					logOutputWithNegativeValues = new StringBuilder().append("<b>Durchgang " + (rc+1) + ", Verfahren: Zyklen mit 0 oder negativen Werten werden berechnet<br><br></b>1. Basiszyklus: ");
					logOutputWithNegativeValues.append(toHtmlStringOverlayMatrix(overlayPositions, valueMatrixWithNegativeValues, "+", "-"));
				}
			}
			
			// ein kompletter Durchgang (alle x werte)
			for (int x0 = 0; x0 < width; x0++ ){
				//in jedem Durchgang Zufallszahl erzeugen
				double randomNumber = rand.nextDouble();
				int calculateWithNullValues = 0;
				int calculateWithNegativeValues = 0; 
				
				// Benoetigte Multiplikator-Werte ausrechnen
				if ((nullHandling == 0) || (nullHandling == 2)){
					calculateWithNullValues = determineIfCalculate(valueMatrixNullIgnore, overlayPositions, randomNumber, alphaValue, betaValue);
					if (calculateWithNullValues == -2) {
						calculateWithNullValues = 0;
						nullCount++;
						if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixPermutations(overlayPositions, valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
					} else if (calculateWithNullValues == 0) {
						if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixPermutations(overlayPositions, valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
					}
				}
				if ((nullHandling == 1) || (nullHandling == 2)){
					if (randomNumber <= alphaValue){
						calculateWithNegativeValues = 1;
					} else if ((randomNumber > alphaValue) && (randomNumber <= (alphaValue + betaValue))){
						calculateWithNegativeValues = -1;
					}
					
				}
				
				// Matrix mit ignorierten 0-Werten ausrechnen, wenn benoetigt
				if (((nullHandling == 0) || (nullHandling == 2)) && (calculateWithNullValues != 0)){
					if (calculateWithNullValues == 1){
						valueMatrixNullIgnore = addX1SubX2(overlayPositions, valueMatrixNullIgnore);
					} else {
						valueMatrixNullIgnore = addX2SubX1(overlayPositions, valueMatrixNullIgnore);
					}
					//Log schreiben
					if (saveLog) logOutputNullIgnore.append(toHtmlStringMatrixPermutations(overlayPositions, valueMatrixNullIgnore, x0, randomNumber, calculateWithNullValues, rc));
				}
				
				// Matrix mit 0 und negativ-Werten ausrechnen, wenn benoetigt
				if (((nullHandling == 1) || (nullHandling == 2)) && (calculateWithNegativeValues != 0)){
					if (calculateWithNegativeValues == 1){
						valueMatrixWithNegativeValues = addX1SubX2(overlayPositions, valueMatrixWithNegativeValues);
					} else {
						valueMatrixWithNegativeValues = addX2SubX1(overlayPositions, valueMatrixWithNegativeValues);
					}
				}
				//Log schreiben
				if (saveLog && ((nullHandling == 1) || (nullHandling == 2))) logOutputWithNegativeValues.append(toHtmlStringMatrixPermutations(overlayPositions, valueMatrixWithNegativeValues, x0, randomNumber, calculateWithNegativeValues, rc));
				
				// Positionen der Ueberlagerungswerte um 1 weiterschieben
				if (nullHandling == 0){
					overlayPositions = shiftPositionsArray(overlayPositions, valueMatrixNullIgnore);
				} else {
					overlayPositions = shiftPositionsArray(overlayPositions, valueMatrixWithNegativeValues);
				}
				
				//Prozentueller Status im Ladebalken setzen
				totalProgress += slice;
				Statics.getView().setProgressBar((int)totalProgress, "Berechnungsvorgang: " + (int)totalProgress + "%" );
				
			} // Ende X-Durchgang
			
			// Alle Zwischenschritte anzeigen?
			if (allSteps){
				// Letzter Schritt = Ergebnis, nicht aufnehmen
				if (rc < (repeatCount-1)){
					if ((nullHandling == 0) || (nullHandling == 2)){
						colMatrixNullIgnore.add(matrixCopy(valueMatrixNullIgnore));
					}
					if ((nullHandling == 1) || (nullHandling == 2)){
						colMatrixWithNegativeValues.add(matrixCopy(valueMatrixWithNegativeValues));
					}
				}
			}
			// Log an Datei anhaengen, falls erwuenscht
			if (saveLog){
				if (nullHandling == 0){
					logOutputNullIgnore.append("<br><br>");
					appendLogToLogFile(logPath, logOutputNullIgnore);
				}
				if (nullHandling == 1) {
					logOutputWithNegativeValues.append("<br><br>");
					appendLogToLogFile(logPath, logOutputWithNegativeValues);
				} 
				if (nullHandling == 2){
					StringBuilder newLog = new StringBuilder().append("<table border=\"1\"><tr><td align=\"center\">");
					newLog.append(logOutputNullIgnore);
					newLog.append("</td><td>&nbsp;</td><td align=\"center\">");
					newLog.append(logOutputWithNegativeValues);
					newLog.append("</td></tr></table><br><br>");
					appendLogToLogFile(logPath, newLog);
				}
			}
		} // Ende Wiederholungen
		
		// Ergebnis-Matrizen aufnehmen
		if (!transponed){
			if ((nullHandling == 0) || (nullHandling == 2)){
				colMatrixNullIgnore.add(matrixCopy(valueMatrixNullIgnore));
			}
			if ((nullHandling == 1) || (nullHandling == 2)){
				colMatrixWithNegativeValues.add(matrixCopyWithoutNegatives(valueMatrixWithNegativeValues));
			}
		} else {
			if ((nullHandling == 0) || (nullHandling == 2)){
				colMatrixNullIgnore.add(transposeMatrixWithoutNegativeValues(valueMatrixNullIgnore));
			}
			if ((nullHandling == 1) || (nullHandling == 2)){
				colMatrixWithNegativeValues.add(transposeMatrixWithoutNegativeValues(valueMatrixWithNegativeValues));
			}
		}
		
		//Log schreiben, Anzahl der uebersprungenen Zyklen beim Verfahren 1
		if (saveLog){
			
			StringBuilder logOutput = new StringBuilder();
			if (nullHandling == 2){
				logOutput.append("<br><br><b>Ergebnis-Matrizen</b><br><br>");
				logOutput.append("<table border=\"1\"><tr><td align=\"center\"><b>Verfahren: Zyklen mit 0 werden &uuml;bersprungen<br></b>");
				logOutput.append(toStringMatrixHtml(colMatrixNullIgnore.lastElement()));
				logOutput.append("</td><td>&nbsp;</td><td align=\"center\"><b>Verfahren: Zyklen mit 0 oder negativen Werten werden berechnet<br></b>");
				logOutput.append(toStringMatrixHtml(colMatrixWithNegativeValues.lastElement()));
				logOutput.append("</td></tr></table><br><br>");
			} else {
				logOutput.append("<br><br><b>Ergebnis-Matrix</b><br><br>");
				if (nullHandling == 0){
					logOutput.append(toStringMatrixHtml(colMatrixNullIgnore.lastElement()));
				} else {
					logOutput.append(toStringMatrixHtml(colMatrixWithNegativeValues.lastElement()));
				}
			}
			
			appendLogToLogFile(logPath, logOutput);
			
			if (nullHandling == 0 || nullHandling == 2){
				logOutputNullIgnore = new StringBuilder().append("<br><br>Anzahl der &Uuml;bersprungenen Zyklen beim Verfahren, bei welchem die Zyklen mit 0 &uuml;bersprungen wurden: ");
				logOutputNullIgnore.append(nullCount);
				appendLogToLogFile(logPath, logOutputNullIgnore);
			}
		}
		
		// Rueckgabe vorbereiten
		returnValues.setTransposed(transponed);
		returnValues.setNullCount(nullCount);
		returnValues.setColMatrixNullIgnore(colMatrixNullIgnore);
		returnValues.setColMatrixWithNegativeValues(colMatrixWithNegativeValues);
		
		return returnValues;
	}

	
	/** Funktion zum Erzeugen eines Arrays mit den Werten einer Permutation
	 * Dabei werden zunaechst x Werte zufaellig generiert, wobei x der tiefe der uebergebenen Matrix entspricht
	 * Pro Zeile der Matrix wird ein Wert aus n generiert, wobei n anfangs der breite der uebergebenen Matrix entspricht
	 * n wird pro verarbeitete Zeile um 1 reduziert
	 * anschliessend wird geprueft, dass kein Wert doppelt vorkommt und ein Array mit der Position der Permutation der jeweiligen Zeile zurueckgibt   
	 * @param valuesMatrix
	 * @return
	 */
	private static int[] generatePermutantMultiplikateMatrix(int[][] valuesMatrix){
		// Erzeuge Array fuer Positionen
		int [] positionsArray =  new int[valuesMatrix.length];
		
		// Befuelle Array mit fallenden n, wobei n = breite der Matrix, n wird pro Durchlauf reduziert
		int n = valuesMatrix[0].length;
		Random rand = new Random();
		for (int y  = 0; y < positionsArray.length; y++){
			
			//System.out.print("Druchlauf: " + y + ", maxWert: " + (n));
			positionsArray[y] = rand.nextInt(n--);
			//System.out.println(", Wert: " + positionsArray[y]);
		}
		
		//System.out.println("Vorher:");
		//outputPositionsArray(positionsArray);
		
		//Matrix zur vermeidung von doppel-Belegungen erschaffen
		int [][] permutantMatrix = new int[valuesMatrix.length][valuesMatrix[0].length];
		//Matrix initiieren
		for (int y = 0; y < permutantMatrix.length; y++){
			for (int x = 0; x < permutantMatrix[0].length; x++){
				permutantMatrix[y][x] = 0;
			}
		}
		
		// Werte in Matrix schreiben
		for (int y = 0; y < permutantMatrix.length; y++){
			for (int x = 0; x < permutantMatrix[0].length; x++){
				//Entspricht die Stelle der zufaellig gewaehlten Zahl?
				if (x == positionsArray[y]){
					//Stelle bereits besetzt?
					if (permutantMatrix[y][x] == 2){
						positionsArray[y] = (positionsArray[y] +1);
					} else {
						//Position merken
						positionsArray[y] = x;
						// x1 = 1 setzen
						permutantMatrix[y][x] = 1;
						// x2 = -1 setzen
						// ist x letzte Stelle in Matrix? -> Dann x2 auf Stelle 0 in Matrix setzen 
						if (x == (permutantMatrix[0].length-1)) {
							permutantMatrix[y][0] = -1;
						} else {
							// Sonst x2 = position von x1 +1
							permutantMatrix[y][x+1] = -1;
						}
						
						//Zeilen unterhalb mit 2 markieren
						for (int j = y+1; j < permutantMatrix.length; j++){
							permutantMatrix[j][x] = 2;
						}
						//Durchlauf abbrechen
						break;
					}
				}
			}
		}
		//System.out.println("nachher:");
		//outputPositionsArray(positionsArray);
		//System.out.println("Matrix:");
		//outputMatrix(permutantMatrix);

		return positionsArray;
	}
	
	
	/** Funktion, welche bestimmt, ob die Berechnung einer Diagonale ueberhaupt stattfinden soll
	 *  
	 * @param valuesMatrix
	 * @param x
	 * @param randomNumber
	 * @param alphaValue
	 * @param betaValue
	 * @return
	 */
	private static int determineIfCalculate(int[][] valuesMatrix, int x, double randomNumber, double alphaValue, double betaValue){
		int returnValue = 2;
		//bestimmen ob Berechnung ueberhaupt notwendig ist 
		if (randomNumber > (alphaValue + betaValue)) {
			returnValue = 0;
		}
		
		if (returnValue != 0){
			boolean calculate = true;
			int x0 = x;
			int x1 = x0;
			for (int y = 0; y < valuesMatrix.length; y++){
				// x1 bestimmen
				if (x0 == (valuesMatrix[0].length -1)){
					x1 = 0;
				} else {
					x1++;
				}
				// Null gefunden? 
				if ((valuesMatrix[y][x0] == 0) || (valuesMatrix[y][x1] == 0)){
//					System.out.println("null gefunden");
					returnValue = -2;
					calculate = false;
					break;
				}
				x0 += 1;
				if (x0 == valuesMatrix[0].length){
					x0 = 0;
				}
			} // Ende for-Schleife
			if (calculate){
				if (randomNumber <= alphaValue){
					returnValue = 1;
				} else if ((randomNumber > alphaValue) && (randomNumber <= (alphaValue + betaValue))){
					returnValue = -1;
				}
			}
		}
		
		return returnValue;
	}
	
	
	/** Funktion, welche bestimmt, ob die Ueberlagerung durch eine permutation ueberhaupt stattfinden soll
	 *  
	 * @param valuesMatrix
	 * @param x
	 * @param randomNumber
	 * @param alphaValue
	 * @param betaValue
	 * @return
	 */
	private static int determineIfCalculate(int[][] valuesMatrix, int[] permutationsArray, double randomNumber, double alphaValue, double betaValue){
		int returnValue = 2;
		//bestimmen ob Berechnung ueberhaupt notwendig ist 
		if (randomNumber > (alphaValue + betaValue)) {
			returnValue = 0;
		}
		
		if (returnValue != 0){
			boolean calculate = true;
			int x0 = 0, x1 = x0;
			for (int y = 0; y < valuesMatrix.length; y++){
				// x0 bestimmen
				x0 = permutationsArray[y];
				// x1 bestimmen
				if (x0 == (valuesMatrix[0].length -1)){
					x1 = 0;
				} else {
					x1 = (x0 +1);
				}
				
				// Null gefunden? 
				if ((valuesMatrix[y][x0] == 0) || (valuesMatrix[y][x1] == 0)){
					returnValue = -2;
					calculate = false;
					break;
				}
				
				
			} // Ende for-Schleife
			if (calculate){
				if (randomNumber <= alphaValue){
					returnValue = 1;
				} else if ((randomNumber > alphaValue) && (randomNumber <= (alphaValue + betaValue))){
					returnValue = -1;
				}
			}
		}
		
		return returnValue;
	}
	
	
	/** Funktion, welche die Matrix diagonal ueberlagert
	 * 
	 * @param valuesMatrix
	 * @param x
	 * @param multiplicant
	 * @return
	 */
	private static int[][] overlayMatrixWithMultiplicant(int[][] valuesMatrix, int x, int multiplicant){
		
		int x0 = x;
		int x1 = x + 1;
		if (x0 == (valuesMatrix[0].length -1)){
			x1 = 0;
		} else {
			x1 = x0 + 1;
		}
		for (int y = 0; y < valuesMatrix.length; y++){
			valuesMatrix[y][x0] += multiplicant;
			valuesMatrix[y][x1] += (multiplicant * -1);
			x0++;
			if (x0 == (valuesMatrix[0].length -1)){
				x1 = 0;
			} else {
				x1++;
			}
			if (x0 == valuesMatrix[0].length){
				x0 = 0;
			}
		}
		return valuesMatrix;
	}
	
	/**
	 * Funktion zum shiften der Werte um eins nach rechts. Das uebergebene Array wird dabei veraendert!
	 * @param pArray
	 * @param valuesMatrix
	 * @return pArray
	 */
	private static int[] shiftPositionsArray(int[] pArray, int[][] valuesMatrix){
		int maxWidth = valuesMatrix[0].length-1;
		for (int y = 0; y < pArray.length; y++){
			if (pArray[y] == maxWidth){
				pArray[y] = 0;
			} else {
				pArray[y] = pArray[y]+1;
			}
		}
		return pArray;
	}
	
	/**
	 * Funktion zum Addieren von 1 zu x1 und zum substrahieren von 1 von x2
	 * @param pArray
	 * @param valuesMatrix
	 * @return pArray
	 */
	private static int[][] addX1SubX2(int[] pArray, int[][] valuesMatrix){
		
		int maxWidth = valuesMatrix[0].length-1;
		for (int y = 0; y < pArray.length; y++){
			//x1 setzen
			valuesMatrix[y][pArray[y]] = valuesMatrix[y][pArray[y]]+1;
			//x2 setzen
			if (pArray[y] == maxWidth){
				valuesMatrix[y][0] = valuesMatrix[y][0]-1;
			} else {
				valuesMatrix[y][(pArray[y]+1)] = valuesMatrix[y][(pArray[y]+1)]-1;
			}
		}
		return valuesMatrix;
	}
	
	/**
	 * Funktion zum Addieren von 1 zu x2 und zum substrahieren von 1 von x1
	 * @param pArray
	 * @param valuesMatrix
	 * @return pArray
	 */
	private static int[][] addX2SubX1(int[] pArray, int[][] valuesMatrix){
		
		int maxWidth = valuesMatrix[0].length-1;
		for (int y = 0; y < pArray.length; y++){
			//x1 setzen
			valuesMatrix[y][pArray[y]] = valuesMatrix[y][pArray[y]]-1;
			//x2 setzen
			if (pArray[y] == maxWidth){
				valuesMatrix[y][0] = valuesMatrix[y][0]+1;
			} else {
				valuesMatrix[y][(pArray[y]+1)] = valuesMatrix[y][(pArray[y]+1)]+1;
			}
		}
		return valuesMatrix;
	}

	/**
	 * Funktion zum kopieren der Werte einer Matrix
	 * @param orig
	 * @return
	 */
	private static int[][] matrixCopy(int[][] orig){
		int [][] newMatrix = new int[orig.length][orig[0].length];
		for (int y = 0; y < orig.length; y++){
			for (int x = 0; x < orig[0].length; x++){
				newMatrix[y][x] = orig[y][x];
			}
		}
		
		return newMatrix;
	}
	
	/**
	 * Funktion zum Kopieren der Werte einer Matrix, 
	 * negative Werte werden auf 0 gesetzt
	 * @param orig
	 * @return
	 */
	private static int[][] matrixCopyWithoutNegatives(int[][] orig){
		int [][] newMatrix = new int[orig.length][orig[0].length];
		for (int y = 0; y < orig.length; y++){
			for (int x = 0; x < orig[0].length; x++){
				if (orig[y][x] < 0) {
					newMatrix[y][x] = 0;
				} else {
					newMatrix[y][x] = orig[y][x];
				}
			}
		}
		
		return newMatrix;
	}
	
	/** Funktion zum debuggen, gitbt eine Matrix auf der Konsole aus
	 * 
	 * @param orig
	 */
	private static void toStringMatrix(int[][] orig){
		for (int y = 0; y < orig.length; y++){
			for (int x = 0; x < orig[0].length; x++){
				System.out.print(orig[y][x] + "\t");
			}
			System.out.println("");
		}
	}
	
	/** Ausgabe fuer Debuging-Zwecke in HTML, gibt eine Matrix in HTML-formatierten Text zurück
	 * 
	 * @param orig
	 */
	private static StringBuilder toStringMatrixHtml(int[][] orig){
		
		StringBuilder htmlOutput = new StringBuilder();
		htmlOutput .append("<table border=\"1\">");
		for (int y = 0; y < orig.length; y++){
			htmlOutput.append("<tr>");
			for (int x = 0; x < orig[0].length; x++){
				htmlOutput.append("<td align=\"center\">");
				htmlOutput.append(orig[y][x]);
				htmlOutput.append("</td>");  
				//System.out.print(orig[y][x] + "\t");
			}
			htmlOutput.append("</tr>");
			//System.out.println("");
		}
		htmlOutput.append("</table><br><br>");
		
		return htmlOutput;
	}
	
	/** Ausgabe fuer Debuging-Zwecke in HTML, wird bei der diagonal-Ueberlagerung genutzt
	 * 
	 * @param orig
	 * @param x0
	 * @param randomNumber
	 * @param mul
	 * @param gesamtDurchlauf
	 */
	private static StringBuilder toHtmlStringMatrixDiagonal(int[][] orig, int x, double randomNumber, int mul, int gesamtDurchlauf){
		//Ausgabe sammeln
		StringBuilder htmlOutput = new StringBuilder();
		
		htmlOutput.append( "Durchgang: ").append((gesamtDurchlauf + 1)).append(", x: ").append(x).append(", Zufallszahl (*100): ").append((int)(randomNumber * 100)).append(", Multiplikator: ").append(mul).append("<br>");
		htmlOutput.append( "<table border=\"1\">");
		
		int xh0 = x;
		int xh1 = 1;
		if (x == (orig[0].length -1)){
			xh1 = 0;
		} else {
			xh1 = xh0+1;
		}
		
		//Zeilenweise
		for (int y = 0; y < orig.length; y++){
			htmlOutput.append( "<tr>");

			for (int x0 = 0; x0 < orig[0].length; x0++){
				if ((xh0 == x0) || (xh1 == x0)){
					htmlOutput.append("<td><font color=\"red\"><b>").append(orig[y][x0]).append("</b></color></td>");
				} else {
					htmlOutput.append("<td>").append(orig[y][x0]).append("</td>");
				}
			}
			htmlOutput.append("</tr>");
			xh0++;
			// x1 bestimmen
			if (xh0 == (orig[0].length -1)){
				xh1 = 0;
			} else {
				xh1++;
			}
			if (xh0 == orig[0].length){
				xh0 = 0;
			}
		}
		htmlOutput.append( "</table><br>");

		return htmlOutput;

	}
	
	/** Gibt die zuletzt ueberlagerte Matrix in HTML-Formatierten Text zurueck
	 * 
	 * @param positionsArray
	 * @param valuesMatrix
	 * @param overlayX1
	 * @param overlayX2 
	 * @return String
	 */
	private static StringBuilder toHtmlStringMatrixPermutations(int[] positionsArray, int [][] valuesMatrix, int x0, double randomNumber, int mul, int gesamtDurchlauf){
		StringBuilder htmlOutput = new StringBuilder();

		htmlOutput.append("Durchgang: ").append((gesamtDurchlauf + 1)).append(", x: ").append(x0).append(", Zufallszahl (*100): ").append((int)(randomNumber * 100)).append(", Multiplikator: ").append(mul +"<br>");
		htmlOutput.append("<table border=\"1\">");
		
		for (int y = 0; y < valuesMatrix.length; y++){
			htmlOutput.append("<tr>");
			for (int x = 0; x < valuesMatrix[0].length; x++){
				if (x == 0){
					if (positionsArray[y] == (valuesMatrix[0].length-1)){
						htmlOutput.append("<td><font color=\"blue\"><b>").append(valuesMatrix[y][x]).append("</b></color></td>");
					} else if (x == positionsArray[y]){
						htmlOutput.append("<td><font color=\"red\"><b>").append(valuesMatrix[y][x]).append("</b></color></td>");
					} else {
						htmlOutput.append("<td>").append(valuesMatrix[y][x]).append("</td>");
					}
				} else {
					if (x == positionsArray[y]){
						htmlOutput.append("<td><font color=\"red\"><b>").append(valuesMatrix[y][x]).append("</b></color></td>");
					} else if (x == (positionsArray[y]+1)){
						htmlOutput.append("<td><font color=\"blue\"><b>").append(valuesMatrix[y][x]).append("</b></color></td>");
					} else {
						htmlOutput.append("<td>").append(valuesMatrix[y][x]).append("</td>");
					}
				}
			}
			htmlOutput.append("</tr>");
		}
		
		htmlOutput.append("</table><br>");
		
		return htmlOutput;
	}
	
	/** Gibt die Ueberlagerungsmartix in html-Formatierten Text zurueck
	 * 
	 * @param positionsArray
	 * @param valuesMatrix
	 * @param overlayX1
	 * @param overlayX2
	 * @return
	 */
	private static String toHtmlStringOverlayMatrix(int[] positionsArray, int [][] valuesMatrix, String overlayX1, String overlayX2){
		StringBuilder htmlOutput = new StringBuilder();
		int [][] overlayMatrix = new int[valuesMatrix.length][valuesMatrix[0].length];
		//Matrix initiieren
		for (int y = 0; y < overlayMatrix.length; y++){
			for (int x = 0; x < overlayMatrix[0].length; x++){
				if (positionsArray[y] == x){
					overlayMatrix[y][x] = 1;
					//x2 setzen
					if (positionsArray[y] == (overlayMatrix[0].length-1)){
						overlayMatrix[y][0] = -1;
					} else {
						overlayMatrix[y][(positionsArray[y]+1)] = -1;
						x++;
					}
				} else {
					overlayMatrix[y][x] = 0;
				}
			}
		}
		htmlOutput.append("<table border=\"1\">");
		for (int y = 0; y < overlayMatrix.length; y++){
			htmlOutput.append("<tr>");
			for (int x = 0; x < overlayMatrix[0].length; x++){
				if (overlayMatrix[y][x] == 1){
					htmlOutput.append("<td><font color=\"red\"><b>").append(overlayX1).append("</b></color></td>");
				} else if (overlayMatrix[y][x] == -1){
					htmlOutput.append("<td><font color=\"blue\"><b>").append(overlayX2).append("</b></color></td>");
				} else {
					htmlOutput.append("<td>0</td>");
				}
			}
			htmlOutput.append("</tr>");
		}
		
		htmlOutput.append("</table><br>");
		return htmlOutput.toString();
	}
	
	
	private static void appendLogToLogFile(String logPath, StringBuilder text){
		// In Log-Datei schreiben
		File file = new File(logPath);
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(text.toString());
			writer.write(System.getProperty("line.separator"));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** initialisiert das Log-File
	 * 	Leert Datei, falls schon vorhanden
	 */
	private static void initiateLogFile(String logPath){
		File file = new File(logPath);
		try {
			FileWriter writer = new FileWriter(file);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** Transponiert eine Matrix; nimmt eine Matrix entgegen und gibt eine Matrix zurueck
	 * 
	 * @param origMatrix
	 * @return
	 */
	private static int[][] transposeMatrix(int[][] origMatrix){
		
		int [][] newMatrix = new int[origMatrix[0].length][origMatrix.length];
		for (int y = 0; y < origMatrix.length; y++){
			for (int x = 0; x < origMatrix[0].length; x++){
				newMatrix[x][y] = origMatrix[y][x];
			}
		}
		
		return newMatrix;
	}


	/** Transponiert eine Matrix; nimmt eine Matrix entgegen und gibt eine Matrix zurueck
	 * setzt negative Werte auf 0
	 * 
	 * @param origMatrix
	 * @return
	 */
	private static int[][] transposeMatrixWithoutNegativeValues(int[][] origMatrix){
		
		int [][] newMatrix = new int[origMatrix[0].length][origMatrix.length];
		for (int y = 0; y < origMatrix.length; y++){
			for (int x = 0; x < origMatrix[0].length; x++){
				if (origMatrix[y][x] < 0){
					newMatrix[x][y] = 0;
				} else {
					newMatrix[x][y] = origMatrix[y][x];
				}
			}
		}
		
		return newMatrix;
	}
}