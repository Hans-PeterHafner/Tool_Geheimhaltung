package de.htw.sg.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;

abstract class ApacheMathBasedRealMatrixClusterVerfahren implements RealMatrixClusterVerfahren
{
    private Clusterer<ClusterableRealMatrixRow> clusterer;
    
    public ApacheMathBasedRealMatrixClusterVerfahren(Clusterer<ClusterableRealMatrixRow> clusterer)
    {
        this.clusterer = clusterer;
    }

    @Override
    public List<List<Integer>> cluster(RealMatrix matrix)
    {
        Objects.requireNonNull(matrix, "matrix darf nicht null sein");
        List<List<Integer>> ergebnis = new ArrayList<List<Integer>>();
        
        for (Cluster<ClusterableRealMatrixRow> gruppe : clusterer.cluster(realMatrixToClusterableCollection(matrix)))
        {
            //Vermerke welche Zeilen in der Gruppe enthalten sind
            ergebnis.add(gruppe.getPoints().stream().map(datensatz -> datensatz.getRowIndex()).collect(Collectors.toList()));
        }
        
        return ergebnis;
    }

    private Collection<ClusterableRealMatrixRow> realMatrixToClusterableCollection(RealMatrix matrix)
    {
        Collection<ClusterableRealMatrixRow> clusterableCollection = new ArrayList<>(matrix.getRowDimension());
        
        for (int zeile = 0; zeile < matrix.getRowDimension(); zeile++)
        {
            clusterableCollection.add(new ClusterableRealMatrixRow(matrix.getRow(zeile), zeile));
        }
        
        return clusterableCollection;
    }
    
    
}
