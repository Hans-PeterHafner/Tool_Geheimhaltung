/** Objekte dieser Klasse werden genutzt um die Ergebnisse von Ueberlagerungen zurueckgegeben zu werden
 * @author Ciprian Savu
 */

package matrixManipulation.core;

import java.util.Collection;
import java.util.Vector;

public class ResultMatrixs {

	private boolean transposed;
	private int nullCount;
	private Vector<int[][]> colMatrixNullIgnore;
	private Vector<int[][]> colMatrixWithNegativeValues;
	
	/** Standardkonstruktor
	 * 
	 */
	public ResultMatrixs() {
		this.transposed = false;
		this.nullCount = 0;
		this.colMatrixNullIgnore = null;
		this.colMatrixWithNegativeValues = null;
	}

	/**
	 * @return the transposed
	 */
	public boolean isTransposed() {
		return transposed;
	}

	/**
	 * @param transposed the transposed to set
	 */
	public void setTransposed(boolean transposed) {
		this.transposed = transposed;
	}

	/**
	 * @return the nullCount
	 */
	public int getNullCount() {
		return nullCount;
	}

	/**
	 * @param nullCount the nullCount to set
	 */
	public void setNullCount(int nullCount) {
		this.nullCount = nullCount;
	}

	/**
	 * @return the colMatrixNullIgnore
	 */
	public Vector<int[][]> getColMatrixNullIgnore() {
		return colMatrixNullIgnore;
	}

	/**
	 * @param colMatrixNullIgnore the colMatrixNullIgnore to set
	 */
	public void setColMatrixNullIgnore(Vector<int[][]> colMatrixNullIgnore) {
		this.colMatrixNullIgnore = colMatrixNullIgnore;
	}

	/**
	 * @return the colMatrixWithNegativeValues
	 */
	public Vector<int[][]> getColMatrixWithNegativeValues() {
		return colMatrixWithNegativeValues;
	}

	/**
	 * @param colMatrixWithNegativeValues the colMatrixWithNegativeValues to set
	 */
	public void setColMatrixWithNegativeValues(
			Vector<int[][]> colMatrixWithNegativeValues) {
		this.colMatrixWithNegativeValues = colMatrixWithNegativeValues;
	}
	
	
	
}
