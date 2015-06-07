package de.htw.sg.cluster;

import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

public class KMeansPlusPlusRealMatrixClusterVerfahren extends ApacheMathBasedRealMatrixClusterVerfahren
{
    public KMeansPlusPlusRealMatrixClusterVerfahren(int k)
    {
        super(new KMeansPlusPlusClusterer<>(k));
    }
}
