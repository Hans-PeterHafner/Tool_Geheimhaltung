package de.htw.sg.safe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.normalisierung.Normalisierungsverfahren;

public class TestNormalisierung
{
    @Test
    public void testStudentisierung() throws Exception
    {
        Normalisierungsverfahren normalisierung = new Studentisierung();
        
        Table<Integer, Integer, String> erwarteteWerte = HashBasedTable.create(5, 2);
        erwarteteWerte.put(0, 0, "A"); erwarteteWerte.put(0, 1, "0.5");
        erwarteteWerte.put(1, 0, "A"); erwarteteWerte.put(1, 1, "-0.5");
        erwarteteWerte.put(2, 0, "B"); erwarteteWerte.put(2, 1, "0.25");
        erwarteteWerte.put(3, 0, "A"); erwarteteWerte.put(3, 1, "0.75");
        erwarteteWerte.put(4, 0, "C"); erwarteteWerte.put(4, 1, "-2.0");
        erwarteteWerte.put(5, 0, "C"); erwarteteWerte.put(5, 1, "1.5");
        erwarteteWerte.put(6, 0, "C"); erwarteteWerte.put(6, 1, "0.25");
        erwarteteWerte.put(7, 0, "C"); erwarteteWerte.put(7, 1, "1.0");
        erwarteteWerte.put(8, 0, "C"); erwarteteWerte.put(8, 1, "-0.75");
        erwarteteWerte.put(9, 0, "C"); erwarteteWerte.put(9, 1, "-1.0");
        
        StatistikDatei erwartung = new StatistikDatei(createBasisDatei().getSpalten(), erwarteteWerte);
        
        StatistikDatei normalisierteDatei = normalisierung.normalisieren(createBasisDatei());
        assertNotNull("normalisierteDatei ist null", normalisierteDatei);
        assertEquals("normalisierteDatei entspricht nicht der erwartung", erwartung, normalisierteDatei);
    }

    private StatistikDatei createBasisDatei()
    {
        List<Merkmal> merkmale = Arrays.asList(new Merkmal[] {
                new Merkmal("K1", MerkmalTyp.KATEGORIAL),
                new Merkmal("M1", MerkmalTyp.METRISCH),
        });
        
        Table<Integer, Integer, String> werte = HashBasedTable.create(5, 2);
        werte.put(0, 0, "A"); werte.put(0, 1, "3");
        werte.put(1, 0, "A"); werte.put(1, 1, "-1");
        werte.put(2, 0, "B"); werte.put(2, 1, "2");
        werte.put(3, 0, "A"); werte.put(3, 1, "4");
        werte.put(4, 0, "C"); werte.put(4, 1, "-7");
        werte.put(5, 0, "C"); werte.put(5, 1, "7");
        werte.put(6, 0, "C"); werte.put(6, 1, "2");
        werte.put(7, 0, "C"); werte.put(7, 1, "5");
        werte.put(8, 0, "C"); werte.put(8, 1, "-2");
        werte.put(9, 0, "C"); werte.put(9, 1, "-3");
        
        return new StatistikDatei(merkmale, werte);
    }
}
