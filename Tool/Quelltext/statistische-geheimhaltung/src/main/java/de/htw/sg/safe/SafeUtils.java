package de.htw.sg.safe;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Table;

import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.model.Zuordnungsmatrix;

public class SafeUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SafeUtils.class);
    
    private SafeUtils() {}
    
    /**
     * Berechnet das Kroneckerprodukt zweier Matrizen
     * @param m1 linker Faktor
     * @param m2 rechter Faktor
     * @return Kronecker Produkt von m1 und m2
     * @see http://de.wikipedia.org/wiki/Kronecker-Produkt
     */
    public static RealMatrix berechneKroneckerProdukt(RealMatrix m1, RealMatrix m2)
    {
        /*
         * m1: m x n
         * m2: p x r
         * result: mp x nr 
         */
        RealMatrix result = new Array2DRowRealMatrix(
                m1.getRowDimension() * m2.getRowDimension(), 
                m1.getColumnDimension() * m2.getColumnDimension());
        
        for (int row = 0; row < m1.getRowDimension(); row++)
        {
            for (int col = 0; col < m1.getColumnDimension(); col++)
            {
                result.setSubMatrix(
                        m2.scalarMultiply(m1.getEntry(row, col)).getData(), 
                        row * m2.getRowDimension(), 
                        col * m2.getColumnDimension());
            }
        }
        
        return result;
    }
    
    /**
     * sind alle statistischen Objekte mindestens n-mal enthalten?
     */
    public static boolean kommtJedeZeileMindestensNMalVor(Table<Integer, Integer, String> anonymisierteDaten, int n)
    {
        Hashtable<String, Integer> countTable = new Hashtable<>();
        
        for (int rowIndex : anonymisierteDaten.rowKeySet())
        {
            String rowValue = "";
            Map<Integer, String> row = anonymisierteDaten.row(rowIndex);
            
            for (int columnIndex : row.keySet())
            {
                String cellValue = row.get(columnIndex);
                rowValue += cellValue + ",";
            }
            
            Integer count = countTable.get(rowValue);
            
            if (count == null)
            {
                count = 0;
            }
            
            countTable.put(rowValue, count + 1);
        }
        
        for (String key : countTable.keySet())
        {
            if (countTable.get(key) < n)
            {
                LOGGER.info("Anzahl < " + n + ": \"" + key + "\"");
                return false;
            }
        }
        
        return true;
    }

    public static int berechneAbstandKategorialerMerkmale(StatistikDatei datei1, StatistikDatei datei2)
    {
        List<Zuordnungsmatrix> zuordnungsmatrizen1 = bestimmeAbstandRelevanteZuordnungsmatrizen(datei1);
        List<Zuordnungsmatrix> zuordnungsmatrizen2 = bestimmeAbstandRelevanteZuordnungsmatrizen(datei2);

        int abstand = 0;
        
        for (int i=0; i<zuordnungsmatrizen1.size(); i++)
        {
            abstand += berechneAbstandDerAuswertungstabellen(zuordnungsmatrizen1.get(i).berechneAuswertungsmatrix(), zuordnungsmatrizen2.get(i).berechneAuswertungsmatrix());
        }
        
        return abstand;
    }

    private static List<Zuordnungsmatrix> bestimmeAbstandRelevanteZuordnungsmatrizen(
            StatistikDatei datei)
    {
        List<Zuordnungsmatrix> zuordnungsmatrizen = datei.bestimmeNichtKombinierteZuordnungsmatrizen();
        if (zuordnungsmatrizen.size() > 1)
        {
            zuordnungsmatrizen.add(datei.bestimmeZuordnungsmatrixMitAllenMerkmalen());
        }
        return zuordnungsmatrizen;
    }

    private static int berechneAbstandDerAuswertungstabellen(RealMatrix auswertungsmatrix1,
            RealMatrix auswertungsmatrix2)
    {
        int abstand = 0;
        
        for (int i=0; i<auswertungsmatrix1.getRowDimension(); i++)
        {
            abstand += Math.abs(auswertungsmatrix1.getEntry(i, i) - auswertungsmatrix2.getEntry(i, i));
        }
        
        return abstand;
    }

    public static double abstandKombinationMetrischeKategorialeMerkmale(StatistikDatei datei1,
            StatistikDatei datei2)
    {
        Function<Zuordnungsmatrix, RealMatrix> z1Consumer = z -> z.getMatrix().transpose().multiply(datei1.bestimmeMetrischeMerkmalMatrix());
        Function<Zuordnungsmatrix, RealMatrix> z2Consumer = z -> z.getMatrix().transpose().multiply(datei2.bestimmeMetrischeMerkmalMatrix());
        
        List<RealMatrix> auswertungstabellen1 = bestimmeAbstandRelevanteZuordnungsmatrizen(datei1).stream().map(z1Consumer).collect(Collectors.toList());
        List<RealMatrix> auswertungstabellen2 = bestimmeAbstandRelevanteZuordnungsmatrizen(datei2).stream().map(z2Consumer).collect(Collectors.toList());

        double abstand = 0;
        
        for (int i = 0; i < auswertungstabellen1.size(); i++)
        {
            RealMatrix auswertungsmatrix1 = auswertungstabellen1.get(i);
            RealMatrix auswertungsmatrix2 = auswertungstabellen2.get(i);
            for (int j = 0; j < auswertungsmatrix1.getRowDimension(); j++)
            {
                for (int k = 0; k < auswertungsmatrix1.getColumnDimension(); k++)
                {
                    if (j < auswertungsmatrix2.getRowDimension())
                    {
                        abstand += Math.abs(auswertungsmatrix1.getEntry(j, k) - auswertungsmatrix2.getEntry(j, k)); 
                    }
                    else
                    {
                        abstand += Math.abs(auswertungsmatrix1.getEntry(j, k));
                    }
                }
            }
        }
        
        return abstand;
    }
}
