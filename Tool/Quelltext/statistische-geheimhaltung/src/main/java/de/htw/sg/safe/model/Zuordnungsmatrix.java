package de.htw.sg.safe.model;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class Zuordnungsmatrix
{
    private List<Merkmal> kombinierteMerkmale;
    private RealMatrix matrix;

    public Zuordnungsmatrix(Merkmal merkmal, RealMatrix matrix)
    {
        this(Arrays.asList(new Merkmal[] {merkmal}), matrix);
    }

    public Zuordnungsmatrix(List<Merkmal> kombinierteMerkmale, RealMatrix matrix)
    {
        this.kombinierteMerkmale = kombinierteMerkmale;
        this.matrix = matrix;
    }

    public List<Merkmal> getKombinierteMerkmale()
    {
        return kombinierteMerkmale;
    }

    public RealMatrix getMatrix()
    {
        return matrix;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((kombinierteMerkmale == null) ? 0 : kombinierteMerkmale.hashCode());
        result = prime * result + ((matrix == null) ? 0 : matrix.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof Zuordnungsmatrix))
        {
            return false;
        }
        Zuordnungsmatrix other = (Zuordnungsmatrix) obj;
        if (kombinierteMerkmale == null)
        {
            if (other.kombinierteMerkmale != null)
            {
                return false;
            }
        }
        else if (!kombinierteMerkmale.equals(other.kombinierteMerkmale))
        {
            return false;
        }
        if (matrix == null)
        {
            if (other.matrix != null)
            {
                return false;
            }
        }
        else if (!matrix.equals(other.matrix))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Zuordnungsmatrix [kombinierteMerkmale=" + kombinierteMerkmale + ", matrix=" + matrix
                + "]";
    }

    /**
     * berechnet das Produkt aus der Transponierten multipliziert mit dieser Matrix.
     * Ergebnis ist eine Diagonalmatrix, die angibt, wie of jedes Merkmal auftritt.
     */
    public RealMatrix berechneAuswertungsmatrix()
    {
        return matrix.transpose().multiply(matrix);
    }
}
