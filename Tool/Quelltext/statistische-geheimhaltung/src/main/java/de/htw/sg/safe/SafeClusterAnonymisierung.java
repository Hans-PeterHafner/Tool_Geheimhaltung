package de.htw.sg.safe;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.htw.sg.cluster.RealMatrixClusterVerfahren;
import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

/**
 * Implementierung des SAFE-Anonymisierungsverfahrens mit Hilfe von
 * Clusteralgorithmen
 * 
 * @author Boris
 *
 */
public class SafeClusterAnonymisierung
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SafeClusterAnonymisierung.class);
    
    /**
     * anonymisiert eine Tabelle mit dem SAFE-Verfahren
     * @param basisdatei wird anonymisiert
     * @param parameter Parameter für das SAFE-Verfahren
     * @return die anonymisierten Datensätze
     */
    public StatistikDatei anonymisieren(StatistikDatei basisdatei, SafeParameter parameter)
    {
        validiereParameter(basisdatei, parameter);
     
        Set<List<Integer>> clusterListe = new HashSet<List<Integer>>();
        
        for (RealMatrixClusterVerfahren clusterVerfahren : parameter.getClusterVerfahrenListe())
        {
            berechneCluster(basisdatei, clusterListe, clusterVerfahren, parameter);
        }
        
        return bestimmeBesteVereinheitlichung(basisdatei, clusterListe, parameter);
    }

    private StatistikDatei bestimmeBesteVereinheitlichung(
            StatistikDatei basisdatei, Set<List<Integer>> clusterListe, SafeParameter parameter)
    {
        StatistikDatei besteVereinheitlichung = null;
        for (VereinheitlichungVerfahren vereinheitlichung : parameter.getVereinheitlichungVerfahrenListe())
        {
            StatistikDatei neueVereinheitlichung = vereinheitlichung.vereinheitliche(basisdatei, clusterListe);
            
            if (besteVereinheitlichung == null || istNeueVereinheitlichungBesser(basisdatei, besteVereinheitlichung, neueVereinheitlichung))
            {
                besteVereinheitlichung = neueVereinheitlichung;
            }
        }  
        return besteVereinheitlichung;
    }

    private boolean istNeueVereinheitlichungBesser(StatistikDatei basisdatei,
            StatistikDatei besteVereinheitlichung, StatistikDatei neueVereinheitlichung)
    {
        //Abstand zu kategorialen Merkmalen darf nicht verschlechtert werden.
        //Abstand zu metrischen Merkmalen sollte verbessert werden.
        return !(SafeUtils.berechneAbstandKategorialerMerkmale(basisdatei, neueVereinheitlichung) >= SafeUtils.berechneAbstandKategorialerMerkmale(basisdatei, besteVereinheitlichung)
                && SafeUtils.abstandKombinationMetrischeKategorialeMerkmale(basisdatei, neueVereinheitlichung) >= SafeUtils.abstandKombinationMetrischeKategorialeMerkmale(basisdatei, besteVereinheitlichung));
    }

    private void validiereParameter(StatistikDatei basisdatei, SafeParameter parameter)
    {
        Objects.requireNonNull(basisdatei, "basisdatei darf nicht null sein");
        Objects.requireNonNull(parameter, "parameter darf nicht null sein");

        Objects.requireNonNull(parameter.getClusterVerfahrenListe(), "clusterverfahrenliste darf nicht null sein");
        if (parameter.getClusterVerfahrenListe().isEmpty())
        {
            throw new IllegalArgumentException("clusterverfahrenliste darf nicht leer sein");
        }
    }

    private void berechneCluster(StatistikDatei basisdatei, Set<List<Integer>> clusterListe,
            RealMatrixClusterVerfahren clusterVerfahren, SafeParameter parameter)
    {
        LOGGER.info("Verwende Clusterverfahren: " + clusterVerfahren.getClass().getName());
        
        addGefiltertToClusterListe(clusterListe, clusterVerfahren.cluster(basisdatei.bestimmeMetrischeMerkmalMatrix()), parameter);
        addGefiltertToClusterListe(clusterListe, clusterVerfahren.cluster(basisdatei.bestimmeZuordnungsmatrixMitAllenMerkmalen().getMatrix()), parameter);
    }

    private void addGefiltertToClusterListe(Set<List<Integer>> clusterListe, List<List<Integer>> cluster, SafeParameter parameter)
    {
        clusterListe.addAll(cluster.stream().filter(gruppe -> gruppe.size() >= parameter.getMinGruppenGroesse()).collect(Collectors.toList()));
    }
}
