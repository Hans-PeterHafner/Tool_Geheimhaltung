package de.htw.sg.safe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Table;

import de.htw.pim.sg.csv.CsvUtil;
import de.htw.sg.cluster.KMeansPlusPlusRealMatrixClusterVerfahren;
import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;
import de.htw.sg.safe.vereinheitlichung.ArithmetischeMittelwertVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.HaeufigstesMerkmalVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

/**
 * Unittest der Klasse {@link SafeClusterAnonymisierung} 
 */
public class TestSafeAnonymisierung
{
    private static final File TEST_DATEI = new File("src/test/resources/datensaetze/mikrozensus2010/mikrozensus2010_vereinfacht.csv");
    private static final char COLUMN_SEPERATOR = ',';
    private static final boolean ERSTE_ZEILE_UEBERSCHRIFT = true;
    
    private SafeClusterAnonymisierung safe = new SafeClusterAnonymisierung();
    
    @Test
    public void testAnonymisieren() throws Exception
    {
        StatistikDatei basisdatei = loadStatistikDatei();
        
        StatistikDatei anonymisierteDaten = safe.anonymisieren(basisdatei, createSafeParameter());
        
        assertFalse("Originaldaten sind bereits anonymisiert", SafeUtils.kommtJedeZeileMindestensNMalVor(basisdatei.getWerte(), 3));
        assertNotNull("anonymisierteDaten ist null", anonymisierteDaten);
        assertFalse("anonymisierteDaten sollten != originalDaten sein", anonymisierteDaten.equals(basisdatei.getWerte()));
        assertTrue("nicht anonymisiert genug", SafeUtils.kommtJedeZeileMindestensNMalVor(anonymisierteDaten.getWerte(), 3));
    
        CsvUtil.writeCsvFile(anonymisierteDaten.getWerte(), new File("src/test/resources/datensaetze/mikrozensus2010/mikrozensus2010anonym.csv"), ',');
    }

    private StatistikDatei loadStatistikDatei()
    {
        Table<Integer, Integer, String> werte = CsvUtil.readCsvFile(TEST_DATEI, COLUMN_SEPERATOR, ERSTE_ZEILE_UEBERSCHRIFT);

        List<Merkmal> merkmalsliste = Arrays.asList(
                new Merkmal("Land (Ost/West)", MerkmalTyp.KATEGORIAL),
                new Merkmal("# Personen im Haushalt", MerkmalTyp.METRISCH),
                new Merkmal("Erwerbstyp", MerkmalTyp.KATEGORIAL),
                new Merkmal("Privathaushalt/Gemeinschaftsunterkunft", MerkmalTyp.KATEGORIAL),
                new Merkmal("Alter in Jahren", MerkmalTyp.METRISCH),
                new Merkmal("Geschlecht", MerkmalTyp.KATEGORIAL),
                new Merkmal("Familienstand", MerkmalTyp.KATEGORIAL),
                new Merkmal("Weitere Wohnung vorhanden?", MerkmalTyp.KATEGORIAL)
        );
        
        return new StatistikDatei(merkmalsliste, werte);
    }
    
    private SafeParameter createSafeParameter()
    {
        //23374 Zeilen ohne Überschrift, :3 -> 7791 Cluster 
        
        SafeParameter parameter = new SafeParameter();
            
        parameter.setClusterVerfahrenListe(Arrays.asList(new KMeansPlusPlusRealMatrixClusterVerfahren(10)));
        parameter.setVereinheitlichungVerfahrenListe(Arrays.asList(new VereinheitlichungVerfahren(new ArithmetischeMittelwertVereinheitlichung(), new HaeufigstesMerkmalVereinheitlichung())));
        parameter.setMinGruppenGroesse(3);
        
        return parameter;
    }
    
}
