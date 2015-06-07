package de.htw.sg.cluster;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class ClusterableRealMatrixRow implements Clusterable
{
    private double[] matrixRow;
    private int rowIndex;
    
    public ClusterableRealMatrixRow(double[] matrixRow, int rowIndex)
    {
        this.matrixRow = matrixRow;
        this.rowIndex = rowIndex;
    }

    @Override
    public double[] getPoint()
    {
        return matrixRow;
    }

    public int getRowIndex()
    {
        return rowIndex;
    }

}
