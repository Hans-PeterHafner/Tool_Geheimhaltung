/**
 * @author Ciprian Savu
 * Table Model fuer aus .csv-Datei eingelesene Daten oder fuer visualisierung 
 * ueberlagerter Daten 
 */
package matrixManipulation.model;

import javax.swing.table.AbstractTableModel;


public class MatrixDataTableModel extends AbstractTableModel {

	private int[][] values;
	
	/**
	 * Konstruktor mit Werten, initialisiert Table Model
	 * @param values
	 */
	public MatrixDataTableModel(int[][] values){
		this.values = values;
	}

	/**
	 * Gibt die Anzahl der Spalten zurueck
	 */
	@Override
	public int getColumnCount() {
		if (this.values == null){
			return 0;	
		} else {
			try{
				return this.values[0].length;
			} catch (Exception e){
				return 0;
			}
		}
	}

	/**
	 * Gibt die Anzahl der Zeilen zurueck
	 */
	@Override
	public int getRowCount() {
		if (this.values == null){
			return 0;	
		} else {
			return this.values.length;
		}
	}

	/**
	 * Gibt den Wert an einer bestimmten Position der Matrix zurueck
	 */
	@Override
	public Object getValueAt(int row, int col) {

		try {
			//System.out.println("Ausgabe @ " + row + " " + col);
			return this.values[row][col];
			
		} catch (Exception e){
			//System.err.println("Fehler @ " + row + " " + col);
			return null;
		}
	}

	/**
	 * Editieren der Zellen verhindern
	 */
	@Override
	public boolean isCellEditable( int rowIndex, int columnIndex )
	{
		  return false;
	}
}
