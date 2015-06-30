package de.htw.sg.cluster;

import org.apache.commons.math3.ml.clustering.DBSCANClusterer;


public class DBSCANClustererRealMatrixClusterVerfahren extends ApacheMathBasedRealMatrixClusterVerfahren
{
    private static final double DEFAULT_EPSILON = 10;
    private static final int DEFAULT_MIN_POINT_PER_CLUSTER = 2;

    public DBSCANClustererRealMatrixClusterVerfahren()
    {
        this(DEFAULT_EPSILON, DEFAULT_MIN_POINT_PER_CLUSTER);
    }

    public DBSCANClustererRealMatrixClusterVerfahren(double epsilon, int minPointsPerCluster)
    {
        super(new DBSCANClusterer<>(epsilon, minPointsPerCluster));
    }
}
