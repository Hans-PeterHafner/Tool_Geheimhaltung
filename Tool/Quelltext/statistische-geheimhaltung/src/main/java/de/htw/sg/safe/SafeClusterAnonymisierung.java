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
        LOGGER.info("Neuer SAFE-Anonymisierungstask. Parameter: " + parameter);
        LOGGER.info("Anonymisiere Datei mit {} Zeilen und {} Spalten.", basisdatei.getWerte().rowKeySet().size(), basisdatei.getWerte().columnKeySet().size());
        
        validiereParameter(basisdatei, parameter);
     
        StatistikDatei normalisierteDatei = parameter.getNormalisierungsverfahren().normalisieren(basisdatei);
        
        Set<List<Integer>> clusterListe = new HashSet<List<Integer>>();
        
        for (RealMatrixClusterVerfahren clusterVerfahren : parameter.getClusterVerfahrenListe())
        {
            berechneCluster(normalisierteDatei, clusterListe, clusterVerfahren, parameter);
        }
        
        return bestimmeBesteVereinheitlichung(basisdatei, clusterListe, parameter);
    }

    private StatistikDatei bestimmeBesteVereinheitlichung(
            StatistikDatei basisdatei, Set<List<Integer>> clusterListe, SafeParameter parameter)
    {
        LOGGER.info("Bestimme aus {} Gruppierungen die beste.", clusterListe.size());
        StatistikDatei besteVereinheitlichung = null;
        for (VereinheitlichungVerfahren vereinheitlichung : parameter.getVereinheitlichungVerfahrenListe())
        {
            StatistikDatei neueVereinheitlichung = vereinheitlichung.vereinheitliche(basisdatei, clusterListe);
            
            if (besteVereinheitlichung == null || istNeueVereinheitlichungBesser(basisdatei, besteVereinheitlichung, neueVereinheitlichung))
            {
                besteVereinheitlichung = neueVereinheitlichung;
            }
        }  
        LOGGER.info("Anonymisierte Datei wurde bestimmt.");
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

        Objects.requireNonNull(parameter.getVereinheitlichungVerfahrenListe(), "VereinheitlichungVerfahrenListe darf nicht null sein");
        if (parameter.getVereinheitlichungVerfahrenListe().isEmpty())
        {
            throw new IllegalArgumentException("VereinheitlichungVerfahrenListe darf nicht leer sein");
        }
        
        if (parameter.getMinGruppenGroesse() < 3)
        {
            throw new IllegalArgumentException("minGruppenGroesse muss >= 3 sein");
        }
    }

    private void berechneCluster(StatistikDatei basisdatei, Set<List<Integer>> clusterListe,
            RealMatrixClusterVerfahren clusterVerfahren, SafeParameter parameter)
    {
        LOGGER.info("Verwende Clusterverfahren: " + clusterVerfahren.getClass().getName());
        
        if (!basisdatei.getKategorialenMerkmale().isEmpty())
        {
            addGefiltertToClusterListe(clusterListe, clusterVerfahren.cluster(basisdatei.bestimmeZuordnungsmatrixMitAllenMerkmalen().getMatrix()), parameter);

            if (!basisdatei.getMetrischenMerkmale().isEmpty())
            {
                addGefiltertToClusterListe(clusterListe, clusterVerfahren.cluster(basisdatei.bestimmeMetrischeMerkmalMatrix()), parameter);
            }
        }
    }

    private void addGefiltertToClusterListe(Set<List<Integer>> clusterListe, List<List<Integer>> cluster, SafeParameter parameter)
    {
        clusterListe.addAll(cluster.stream().filter(gruppe -> gruppe.size() >= parameter.getMinGruppenGroesse()).collect(Collectors.toList()));
    }
}
