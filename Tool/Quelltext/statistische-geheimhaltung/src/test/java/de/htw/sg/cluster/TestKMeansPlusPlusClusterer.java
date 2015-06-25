package de.htw.sg.cluster;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class TestKMeansPlusPlusClusterer
{

    @Test
    public void testClusteringKThree() throws Exception
    {
        KMeansPlusPlusRealMatrixClusterVerfahren kMeansPlusPlusClusterer = new KMeansPlusPlusRealMatrixClusterVerfahren(3);
        
        List<List<Integer>> clusterListe = kMeansPlusPlusClusterer.cluster(createMatrix());
        
        assertEquals(3, clusterListe.size());
    }

    private RealMatrix createMatrix()
    {
        return new Array2DRowRealMatrix(new double[][] {
                {0, 0, 1},
                {1, 0, 2},
                {2, 0, 1},
                {3, 0, 2},
                {4, 0, 0},
                {5, 0, 0},
                {6, 0, 0},
                {7, 0, 0},
                {8, 0, 0},
                {9, 0, 0},
                {10, 0, 0},
                {11, 0, 0},
                {12, 0, 0},
                {13, 0, 0},
        });
    }

}
