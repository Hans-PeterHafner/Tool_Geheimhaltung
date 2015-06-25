package de.htw.sg.safe.vereinheitlichung;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;

public class VereinheitlichungVerfahren
{
    private MetrischesVereinheitlichungVerfahren metrischeVereinheitlichung;
    private KategorialesVereinheitlichungVerfahren kategorialeVereinheitlichung;

    
    
    public VereinheitlichungVerfahren(
            MetrischesVereinheitlichungVerfahren metrischeVereinheitlichung,
            KategorialesVereinheitlichungVerfahren kategorialeVereinheitlichung)
    {
        this.metrischeVereinheitlichung = metrischeVereinheitlichung;
        this.kategorialeVereinheitlichung = kategorialeVereinheitlichung;
    }

    /**
     * Vereinheitlicht definierte Gruppen der übergebenen Statistikdatei 
     * @param statistikdatei enthält Gruppen, die vereinheitlicht werden
     * @param gruppenListe definiert die zu vereinheitlichten Gruppen: Jedes Listenelement ist eine Gruppe. 
     * Jede Gruppe ist definiert als Liste mit den Zeilennummern der Originaldatensätze.
     * @return
     */
    public StatistikDatei vereinheitliche(StatistikDatei statistikdatei, Collection<List<Integer>> gruppenListe)
    {
        Table<Integer,Integer,String> vereinheitlichteTabelle = HashBasedTable.create(gruppenListe.size(), statistikdatei.getSpalten().size());

        int spaltenIndex = 0;
        
        for (Merkmal merkmal : statistikdatei.getSpalten())
        {
            Map<Integer, String> originalSpalte = statistikdatei.getWerte().column(spaltenIndex);
            Map<Integer, String> vereinheitlichteSpalte;
            

            if (merkmal.getTyp() == MerkmalTyp.METRISCH)
            {
                 vereinheitlichteSpalte = metrischeVereinheitlichung.vereinheitliche(originalSpalte, gruppenListe);
            }
            else
            {
                vereinheitlichteSpalte = kategorialeVereinheitlichung.vereinheitliche(originalSpalte, gruppenListe);
            }
            
            uebertrageVereinheitlichteSpalte(vereinheitlichteTabelle, vereinheitlichteSpalte, spaltenIndex);
            
            spaltenIndex++;
        }
        
        return new StatistikDatei(statistikdatei.getSpalten(), vereinheitlichteTabelle, statistikdatei.getKategorialeMerkmalsauspraegungen());
    }

    private void uebertrageVereinheitlichteSpalte(
            Table<Integer, Integer, String> vereinheitlichteTabelle,
            Map<Integer, String> vereinheitlichteSpalte, int spaltenIndex)
    {
        vereinheitlichteSpalte.forEach((row, value) -> vereinheitlichteTabelle.put(row, spaltenIndex, value));
    }
}
