package de.htw.sg.safe;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.vereinheitlichung.ArithmetischeMittelwertVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.HaeufigstesMerkmalVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

public class TestVereinheitlichungsVerfahren
{
    @Test
    public void testVereinheitlicheMitArithmetischemMittelwertUndHaeufigstesKategorialesMerkmal() throws Exception
    {
        VereinheitlichungVerfahren verfahren = new VereinheitlichungVerfahren(new ArithmetischeMittelwertVereinheitlichung(), new HaeufigstesMerkmalVereinheitlichung());
   
        StatistikDatei vereinheitlichteDatei = verfahren.vereinheitliche(erstelleStatistikDatei(), erstelleGruppenListe());
        
        assertNotNull("vereinheitlichteDatei ist null", vereinheitlichteDatei);
        assertEquals(createErwarteteVereinheitlichteDatei(), vereinheitlichteDatei);
    }

    private List<List<Integer>> erstelleGruppenListe()
    {
        return Arrays.asList(
                Arrays.asList(0,2,4),
                Arrays.asList(1,3)
        );
    }

    private StatistikDatei erstelleStatistikDatei()
    {
        List<Merkmal> spalten = Arrays.asList(
                new Merkmal("Alter", MerkmalTyp.METRISCH),
                new Merkmal("Geschlecht", MerkmalTyp.KATEGORIAL));
        
        Table<Integer, Integer, String> werte = HashBasedTable.create(5, 2);
        /*
         *  25  w
         *  48  m
         *  27  m
         *  49  m
         *  26  w 
         */
        werte.put(0, 0, "25");
        werte.put(1, 0, "48");
        werte.put(2, 0, "27");
        werte.put(3, 0, "49");
        werte.put(4, 0, "26");
        werte.put(0, 1, "w");
        werte.put(1, 1, "m");
        werte.put(2, 1, "m");
        werte.put(3, 1, "m");
        werte.put(4, 1, "w");
        
        return new StatistikDatei(spalten, werte);
    }
    
    private StatistikDatei createErwarteteVereinheitlichteDatei()
    {
        List<Merkmal> spalten = Arrays.asList(
                new Merkmal("Alter", MerkmalTyp.METRISCH),
                new Merkmal("Geschlecht", MerkmalTyp.KATEGORIAL));

        Table<Integer, Integer, String> werte = HashBasedTable.create(2, 2);
        /*
         * (25 + 27 + 26) / 3 = 26
         * (48 + 49) / 2 = 48
         * 
         *  26  w   3x
         *  48  m   2x
         */
        werte.put(0, 0, "26");
        werte.put(1, 0, "26");
        werte.put(2, 0, "26");
        werte.put(3, 0, "48");
        werte.put(4, 0, "48");
        werte.put(0, 1, "w");
        werte.put(1, 1, "w");
        werte.put(2, 1, "w");
        werte.put(3, 1, "m");
        werte.put(4, 1, "m");
        
        return new StatistikDatei(spalten, werte);
    }
}
