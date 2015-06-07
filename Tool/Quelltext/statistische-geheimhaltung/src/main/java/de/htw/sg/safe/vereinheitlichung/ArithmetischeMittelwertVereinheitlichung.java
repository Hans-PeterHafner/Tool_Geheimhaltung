package de.htw.sg.safe.vereinheitlichung;

import java.util.List;
import java.util.Map;

public class ArithmetischeMittelwertVereinheitlichung extends MetrischesVereinheitlichungVerfahren
{
    @Override
    protected String bestimmeWertFuerGruppe(Map<Integer, String> originalSpalte, List<Integer> gruppe)
    {
        int summeVonGruppe = 0;
        for (int zeilenIndex : gruppe)
        {
             summeVonGruppe += Integer.parseInt(originalSpalte.get(zeilenIndex));
        }
        return String.valueOf(summeVonGruppe / gruppe.size());
    }
}
