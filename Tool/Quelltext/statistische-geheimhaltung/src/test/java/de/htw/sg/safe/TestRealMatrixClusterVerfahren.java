package de.htw.sg.safe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.BeforeClass;
import org.junit.Test;

import de.htw.sg.cluster.DBSCANClustererRealMatrixClusterVerfahren;
import de.htw.sg.cluster.KMeansPlusPlusRealMatrixClusterVerfahren;
import de.htw.sg.cluster.RealMatrixClusterVerfahren;

public class TestRealMatrixClusterVerfahren
{
    private static ArrayList<List<Integer>> expected;
    private static Array2DRowRealMatrix matrix;
    
    @BeforeClass
    public static void init()
    {
        matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {3.0, 3.0, 3.0},
                {2.9, 2.9, 3.1},
                {2.9, 2.9, 3.1},
                {2.9, 2.9, 3.1},
                {2.9, 2.9, 3.1},
        });
        expected = new ArrayList<List<Integer>>();
        expected.add(Arrays.asList(0, 1, 2, 3, 4));
        expected.add(Arrays.asList(5, 6, 7, 8, 9));
    }
    
    @Test
    public void testKMeansPlusPlusClustering() throws Exception
    {
       testeClusterVerfahren(new KMeansPlusPlusRealMatrixClusterVerfahren(2));
    }

    @Test
    public void testDBSCANClustering() throws Exception
    {
        testeClusterVerfahren(new DBSCANClustererRealMatrixClusterVerfahren(1.0, 2));
    }

    private void testeClusterVerfahren(RealMatrixClusterVerfahren clusterVerfahren)
    {
        assertEquals(new HashSet<>(expected), new HashSet<>(clusterVerfahren.cluster(matrix)));
    }
}
