package de.htw.sg.safe;

import java.util.ArrayList;
import java.util.List;

import de.htw.sg.cluster.RealMatrixClusterVerfahren;
import de.htw.sg.safe.normalisierung.Normalisierungsverfahren;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

/**
 * Kapselt Parameter für die {@link SafeClusterAnonymisierung}
 */
public class SafeParameter
{
    private List<RealMatrixClusterVerfahren> clusterVerfahrenListe = new ArrayList<>();
    private List<VereinheitlichungVerfahren> vereinheitlichungVerfahrenListe = new ArrayList<>();
    private int minGruppenGroesse = 3;
    private Normalisierungsverfahren normalisierungsverfahren = new Studentisierung();
    
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

    public Normalisierungsverfahren getNormalisierungsverfahren()
    {
        return normalisierungsverfahren;
    }

    public void setNormalisierungsverfahren(Normalisierungsverfahren normalisierungsverfahren)
    {
        this.normalisierungsverfahren = normalisierungsverfahren;
    }
}
