package de.htw.sg.safe;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;

import de.htw.pim.sg.csv.CsvUtil;
import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.model.Zuordnungsmatrix;

/**
 * Unittest der Klasse {@link StatistikDatei}
 */
public class TestStatistikdatei
{
    private static final File TEST_DATEI = new File("src/test/java/resources/testStatistikDatei.csv");
    
    private StatistikDatei datei;
    private List<Merkmal> kategorialenMerkmale;
    private List<Merkmal> metrischenMerkmale;
    
    @Before
    public void ladeTestDatei()
    {
        Table<Integer, Integer, String> werte = CsvUtil.readCsvFile(TEST_DATEI, ',', true);

        datei = new StatistikDatei(createMerkmalsliste(), werte);
    }
    
    @Test
    public void testGetKategorialenMerkmale() throws Exception
    {
        assertEquals(kategorialenMerkmale, datei.getKategorialenMerkmale());
    }

    @Test
    public void testGetMetrischeMerkmale() throws Exception
    {
        assertEquals(metrischenMerkmale, datei.getMetrischenMerkmale());
    }
    
    @Test
    public void testBestimmeZuornungsmatrizen() throws Exception
    {
        Zuordnungsmatrix z1 = new Zuordnungsmatrix(kategorialenMerkmale.get(0), new Array2DRowRealMatrix(new double[][] {
                {1, 0},
                {0, 1},
                {1, 0},
        }));

        Zuordnungsmatrix z2 = new Zuordnungsmatrix(kategorialenMerkmale.get(1), new Array2DRowRealMatrix(new double[][] {
                {1, 0},
                {1, 0},
                {0, 1},
        }));

        Zuordnungsmatrix z12 = new Zuordnungsmatrix(kategorialenMerkmale, new Array2DRowRealMatrix(new double[][] {
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0},
        }));
        
        List<Zuordnungsmatrix> expected = Arrays.asList(new Zuordnungsmatrix[] { z1, z2, z12 });
        
        assertEquals(expected, datei.bestimmeZuordnungsmatrizen());
    }
    
    @Test
    public void testBestimmeZuordnungsmatrixMitAllenMerkmalen() throws Exception
    {
        Zuordnungsmatrix z12 = new Zuordnungsmatrix(kategorialenMerkmale, new Array2DRowRealMatrix(new double[][] {
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, 1, 0, 0},
        }));
        
        assertEquals(z12, datei.bestimmeZuordnungsmatrixMitAllenMerkmalen());
    }
    
    @Test
    public void testBestimmeMetrischeWerteMatrix()
    {
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {34},
                {25},
                {21},
        });
        
        assertEquals(expected, datei.bestimmeMetrischeMerkmalMatrix());
    }

    private List<Merkmal> createMerkmalsliste()
    {
        kategorialenMerkmale = Arrays.asList(new Merkmal[] {
                new Merkmal("K1", MerkmalTyp.KATEGORIAL),
                new Merkmal("K2", MerkmalTyp.KATEGORIAL),
        });

        metrischenMerkmale = Arrays.asList(new Merkmal[] {
                new Merkmal("M1", MerkmalTyp.METRISCH),

        });
        
        return Arrays.asList(new Merkmal[] {
                new Merkmal("K1", MerkmalTyp.KATEGORIAL),
                new Merkmal("M1", MerkmalTyp.METRISCH),
                new Merkmal("K2", MerkmalTyp.KATEGORIAL),
        });
    }
}
