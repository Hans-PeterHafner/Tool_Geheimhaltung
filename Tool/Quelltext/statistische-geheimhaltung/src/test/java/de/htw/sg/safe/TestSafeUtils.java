package de.htw.sg.safe;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;

/**
 * Test von {@link SafeUtils}
 */
public class TestSafeUtils
{
    @Test
    public void testberechneKroneckerProdukt() throws Exception
    {
        RealMatrix m1 = new Array2DRowRealMatrix(new double[][] {
                {1, 2},
                {3, 4},
                {5, 6}
        });
        
        RealMatrix m2 = new Array2DRowRealMatrix(new double[][] {
                {7, 8},
                {9, 0}
        });
        
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {7, 8, 14, 16},
                {9, 0, 18, 0},
                {21, 24, 28, 32},
                {27, 0, 36, 0},
                {35, 40, 42, 48},
                {45, 0, 54, 0},
        });
        
        assertEquals(expected, SafeUtils.berechneKroneckerProdukt(m1, m2));
    }
    
    @Test
    public void testKommtJederWertMindestensNMalVor() throws Exception
    {
        Table<Integer, Integer, String> tabelle = HashBasedTable.create(2, 10);
        tabelle.put(0, 0, "A"); tabelle.put(0, 1, "1");
        tabelle.put(1, 0, "B"); tabelle.put(1, 1, "2");
        tabelle.put(2, 0, "A"); tabelle.put(2, 1, "1");
        tabelle.put(3, 0, "A"); tabelle.put(3, 1, "1");
        tabelle.put(4, 0, "C"); tabelle.put(4, 1, "3");
        tabelle.put(5, 0, "A"); tabelle.put(5, 1, "1");
        tabelle.put(6, 0, "C"); tabelle.put(6, 1, "3");
        tabelle.put(7, 0, "B"); tabelle.put(7, 1, "2");
        
        assertEquals("n=1", true, SafeUtils.kommtJedeZeileMindestensNMalVor(tabelle, 1));
        assertEquals("n=2", true, SafeUtils.kommtJedeZeileMindestensNMalVor(tabelle, 2));
        assertEquals("n=3", false, SafeUtils.kommtJedeZeileMindestensNMalVor(tabelle, 3));
    }
    
    @Test
    public void testAbstandKategorialerMerkmale() throws Exception
    {
        Table<Integer, Integer, String> tabelle1 = HashBasedTable.create(1, 3);
        tabelle1.put(0, 0, "A"); 
        tabelle1.put(1, 0, "A");
        tabelle1.put(2, 0, "B"); 

        Table<Integer, Integer, String> tabelle2 = HashBasedTable.create(1, 3);
        tabelle2.put(0, 0, "A"); 
        tabelle2.put(1, 0, "B");
        tabelle2.put(2, 0, "B"); 
        
        StatistikDatei datei1 = new StatistikDatei(Arrays.asList(new Merkmal("M1", MerkmalTyp.KATEGORIAL)), tabelle1);
        StatistikDatei datei2 = new StatistikDatei(Arrays.asList(new Merkmal("M1", MerkmalTyp.KATEGORIAL)), tabelle2);
        assertEquals(0, SafeUtils.berechneAbstandKategorialerMerkmale(datei1, datei1));
        assertEquals(0, SafeUtils.berechneAbstandKategorialerMerkmale(datei2, datei2));
        assertEquals(2, SafeUtils.berechneAbstandKategorialerMerkmale(datei1, datei2));
    }

    @Test
    public void testAbstandKombinationMetrischeKategorialeMerkmale() throws Exception
    {
        Table<Integer, Integer, String> tabelle1 = HashBasedTable.create(1, 3);
        tabelle1.put(0, 0, "A"); tabelle1.put(0, 1, "1");
        tabelle1.put(1, 0, "A"); tabelle1.put(1, 1, "2");
        tabelle1.put(2, 0, "B"); tabelle1.put(2, 1, "3");
        
        Table<Integer, Integer, String> tabelle2 = HashBasedTable.create(1, 3);
        tabelle2.put(0, 0, "A"); tabelle2.put(0, 1, "1");
        tabelle2.put(1, 0, "B"); tabelle2.put(1, 1, "2");
        tabelle2.put(2, 0, "B"); tabelle2.put(2, 1, "2");
        
        List<Merkmal> merkmalliste = Arrays.asList(new Merkmal("M1", MerkmalTyp.KATEGORIAL), new Merkmal("M2", MerkmalTyp.METRISCH));
        StatistikDatei datei1 = new StatistikDatei(merkmalliste, tabelle1);
        StatistikDatei datei2 = new StatistikDatei(merkmalliste, tabelle2);
        assertEquals(0, SafeUtils.abstandKombinationMetrischeKategorialeMerkmale(datei1, datei1), 0.0);
        assertEquals(0, SafeUtils.abstandKombinationMetrischeKategorialeMerkmale(datei2, datei2), 0.0);
        assertEquals(3, SafeUtils.abstandKombinationMetrischeKategorialeMerkmale(datei1, datei2), 0.0);
    }
}
