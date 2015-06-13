package de.htw.sg.safe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Table;

import de.htw.sg.safe.SafeUtils;

/**
 * Modell einer Basisdatei für statistische Geheimhaltungsverfahren
 * 
 * @author Boris
 *
 */
public class StatistikDatei
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StatistikDatei.class);
    
    private List<Merkmal> spalten;
    private Table<Integer, Integer, String> werte;
    
    public StatistikDatei(List<Merkmal> spalten, Table<Integer, Integer, String> werte)
    {
        LOGGER.debug("Lege neue Statistikdatei mit {} Spalten und {} Zeilen an", spalten.size(), werte.rowKeySet().size());
        this.spalten = spalten;
        this.werte = werte;
    }
    
    /**
     * ACHTUNG - Diese Methode führt bei größeren Datenmengen zu einem Java-Heap-Space-Error,
     * weil es bei Merkmalskombinationen zu vielen Zeilen und Möglichkeiten kommt.
     */
    public List<Zuordnungsmatrix> bestimmeZuordnungsmatrizen()
    {
        List<Zuordnungsmatrix> zuordnungsmatrizen = bestimmeNichtKombinierteZuordnungsmatrizen();
                
        //Zuordnungsmatrizen der kombinierten Merkmale
        
        List<Zuordnungsmatrix> zuKombinierendeMatrizen = zuordnungsmatrizen;
        
        while (zuKombinierendeMatrizen.size() > 0)
        {
            List<Zuordnungsmatrix> kombinierteMatrizen = kombiniereZuordnungsmatrizen(zuKombinierendeMatrizen);
            
            zuordnungsmatrizen.addAll(kombinierteMatrizen);
            zuKombinierendeMatrizen = kombinierteMatrizen;
        }
        
        return zuordnungsmatrizen;
    }

    public List<Zuordnungsmatrix> bestimmeNichtKombinierteZuordnungsmatrizen()
    {
        List<Zuordnungsmatrix> zuordnungsmatrizen = new ArrayList<>();
        
        for (Merkmal kategorialesMerkmal : getKategorialenMerkmale())
        {
            zuordnungsmatrizen.add(bestimmeZuordnungsmatrix(kategorialesMerkmal));
        }
        
        return zuordnungsmatrizen;
    }
    
    /**
     * Berechnet aus bereits bestehenden Zuordnungsmatrizen die Kombination dieser
     * Matrizen (Kroneckerprodukt der Zeilenvektoren)
     * 
     * @param zuKombinierendeMatrizen Liste bereits berechneter Zuordnungsmatrizen gleicher Dimension. 
     * @return 
     */
    private List<Zuordnungsmatrix> kombiniereZuordnungsmatrizen(List<Zuordnungsmatrix> zuKombinierendeMatrizen)
    {
        List<Zuordnungsmatrix> kombinierteMatrizen = new ArrayList<>();
        
        for (int i = 0; i < zuKombinierendeMatrizen.size() - 1; i++)
        {
            for (int j = i + 1; j < zuKombinierendeMatrizen.size(); j++)
            {
                kombinierteMatrizen.add(kombiniereZuordnungsmatrizen(
                        zuKombinierendeMatrizen.get(i),
                        zuKombinierendeMatrizen.get(j)
                ));
            }
        }
        
        return kombinierteMatrizen;
    }

    private Zuordnungsmatrix kombiniereZuordnungsmatrizen(Zuordnungsmatrix z1, Zuordnungsmatrix z2)
    {
        RealMatrix kombinierteMatrix = new Array2DRowRealMatrix(
                z1.getMatrix().getRowDimension(), 
                z1.getMatrix().getColumnDimension() * z2.getMatrix().getColumnDimension());
        
        for (int zeile = 0; zeile < z1.getMatrix().getRowDimension(); zeile++)
        {
            RealMatrix kroneckerProdukt = SafeUtils.berechneKroneckerProdukt(
                    z1.getMatrix().getSubMatrix(zeile, zeile, 0, z1.getMatrix().getColumnDimension() - 1), 
                    z2.getMatrix().getSubMatrix(zeile, zeile, 0, z2.getMatrix().getColumnDimension() - 1));

            kombinierteMatrix.setSubMatrix(kroneckerProdukt.getData(), zeile, 0);
        }
        
        List<Merkmal> kombinierteMerkmale = new ArrayList<>();
        kombinierteMerkmale.addAll(z1.getKombinierteMerkmale());
        kombinierteMerkmale.addAll(z2.getKombinierteMerkmale());
        return new Zuordnungsmatrix(kombinierteMerkmale, kombinierteMatrix);
    }

    private Zuordnungsmatrix bestimmeZuordnungsmatrix(Merkmal kategorialesMerkmal)
    {
        Map<Integer, String> spalte = werte.column(spalten.indexOf(kategorialesMerkmal));
        
        Map<String, Integer> auspraegungsTabelle = new Hashtable<>();
        
        /* 
         * Alle verschiedenen Merkmalsausprägungen erfassen und einem Spaltenindex
         * in der Zuordnungsmatrix zuordnen
         */
        
        int anzahlAuspraegungen = 0;
        
        for (Map.Entry<Integer, String> zeile : spalte.entrySet())
        {
            if (!auspraegungsTabelle.containsKey(zeile.getValue()))
            {
                auspraegungsTabelle.put(zeile.getValue(), anzahlAuspraegungen++);
            }
        }
        
        /*
         * Jetzt kann die Zuordnungsmatrix bestimmt werden 
         */
        
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(spalte.size(), anzahlAuspraegungen);
        
        for (Map.Entry<Integer, String> zeile : spalte.entrySet())
        {
            matrix.setEntry(zeile.getKey(), auspraegungsTabelle.get(zeile.getValue()), 1);
        }
        
        return new Zuordnungsmatrix(kategorialesMerkmal, matrix);
    }

    public List<Merkmal> getKategorialenMerkmale()
    {
        return spalten.stream().filter(m -> m.getTyp() == MerkmalTyp.KATEGORIAL).collect(Collectors.toList());
    }

    /**
     * extrahiert alle metrischen Merkmale und schreibt diese in eine
     * Double-Matrix. Die Reihenfolge der metrischen Merkmale in der Basisdatei
     * bestimmen die Reihenfolge der Spalten dieser Matrix
     * @return Matrix mit allen metrischen Merkmalswerten dieser Datei
     */
    public RealMatrix bestimmeMetrischeMerkmalMatrix()
    {
        List<Merkmal> listeMetrischerMerkmale = getMetrischenMerkmale();
        
        RealMatrix matrix = new Array2DRowRealMatrix(werte.rowKeySet().size(), listeMetrischerMerkmale.size());
        
        int column = 0;
        for (Merkmal merkmal : listeMetrischerMerkmale)
        {
            Map<Integer, String> spalte = werte.column(spalten.indexOf(merkmal));

            for (Map.Entry<Integer, String> zeile : spalte.entrySet())
            {
                matrix.setEntry(zeile.getKey(), column, Double.parseDouble(zeile.getValue()));              
            }
        
            column++;
        }
        
        return matrix;
    }

    public List<Merkmal> getMetrischenMerkmale()
    {
        return spalten.stream().filter(m -> m.getTyp() == MerkmalTyp.METRISCH).collect(Collectors.toList());
    }

    public Table<Integer, Integer, String> getWerte()
    {
        return werte;
    }

    public List<Merkmal> getSpalten()
    {
        return Collections.unmodifiableList(spalten);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((spalten == null) ? 0 : spalten.hashCode());
        result = prime * result + ((werte == null) ? 0 : werte.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof StatistikDatei))
        {
            return false;
        }
        StatistikDatei other = (StatistikDatei) obj;
        if (spalten == null)
        {
            if (other.spalten != null)
            {
                return false;
            }
        }
        else if (!spalten.equals(other.spalten))
        {
            return false;
        }
        if (werte == null)
        {
            if (other.werte != null)
            {
                return false;
            }
        }
        else if (!werte.equals(other.werte))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "StatistikDatei [spalten=" + spalten + ", werte=" + werte + "]";
    }

    public Zuordnungsmatrix bestimmeZuordnungsmatrixMitAllenMerkmalen()
    {
        List<Zuordnungsmatrix> matrizen = bestimmeNichtKombinierteZuordnungsmatrizen();
        
        Zuordnungsmatrix zuordnungsmatrix = matrizen.get(0);
        
        for (int i = 1; i < matrizen.size(); i++)
        {
            zuordnungsmatrix = kombiniereZuordnungsmatrizen(zuordnungsmatrix, matrizen.get(i));
        }
        
        return zuordnungsmatrix;
    }
}
