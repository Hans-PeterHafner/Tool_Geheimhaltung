package de.htw.sg.safe;

import java.util.List;

import de.htw.sg.cluster.RealMatrixClusterVerfahren;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

/**
 * Kapselt Parameter für die {@link SafeClusterAnonymisierung}
 */
public class SafeParameter
{
    private List<RealMatrixClusterVerfahren> clusterVerfahrenListe;
    private List<VereinheitlichungVerfahren> vereinheitlichungVerfahrenListe;
    private int minGruppenGroesse = 3;
    
    public List<RealMatrixClusterVerfahren> getClusterVerfahrenListe()
    {
        return clusterVerfahrenListe;
    }

    public void setClusterVerfahrenListe(List<RealMatrixClusterVerfahren> clusterVerfahrenListe)
    {
        this.clusterVerfahrenListe = clusterVerfahrenListe;
    }

    public List<VereinheitlichungVerfahren> getVereinheitlichungVerfahrenListe()
    {
        return vereinheitlichungVerfahrenListe;
    }

    public void setVereinheitlichungVerfahrenListe(List<VereinheitlichungVerfahren> vereinheitlichungVerfahrenListe)
    {
        this.vereinheitlichungVerfahrenListe = vereinheitlichungVerfahrenListe;
    }

    public int getMinGruppenGroesse()
    {
        return minGruppenGroesse;
    }

    public void setMinGruppenGroesse(int minGruppenGroesse)
    {
        this.minGruppenGroesse = minGruppenGroesse;
    }
}
