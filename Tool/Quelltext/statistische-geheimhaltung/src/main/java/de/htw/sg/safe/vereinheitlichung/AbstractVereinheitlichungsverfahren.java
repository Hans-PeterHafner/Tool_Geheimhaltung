package de.htw.sg.safe.vereinheitlichung;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractVereinheitlichungsverfahren
{
    public Map<Integer, String> vereinheitliche(Map<Integer, String> originalSpalte, Collection<List<Integer>> gruppenListe)
    {
        Map<Integer, String> vereinheitlichteSpalte = new HashMap<>();
        
        int zeile = 0;
        for (List<Integer> gruppe : gruppenListe)
        {
            String vereinheitlichterGruppenwert = bestimmeWertFuerGruppe(originalSpalte, gruppe);
            for (int indexInGruppe = 0; indexInGruppe < gruppe.size(); indexInGruppe++)
            {
                vereinheitlichteSpalte.put(zeile++, vereinheitlichterGruppenwert);
            }
        }
        
        return vereinheitlichteSpalte;
    }

    protected abstract String bestimmeWertFuerGruppe(Map<Integer, String> originalSpalte, List<Integer> gruppe);
}
