package de.htw.sg.cluster;

import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public interface RealMatrixClusterVerfahren
{
    /**
     * @param matrix
     * @return Liste mit Clustern. Jedes Cluster ist wiederum eine Liste mit den
     * Zeilenindizes der Datensätze in diesem Cluster.
     */
    List<List<Integer>> cluster(RealMatrix matrix); 
}
