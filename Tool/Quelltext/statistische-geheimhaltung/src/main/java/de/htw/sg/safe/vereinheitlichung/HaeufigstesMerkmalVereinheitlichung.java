package de.htw.sg.safe.vereinheitlichung;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HaeufigstesMerkmalVereinheitlichung extends KategorialesVereinheitlichungVerfahren
{
    @Override
    protected String bestimmeWertFuerGruppe(Map<Integer, String> originalSpalte, List<Integer> gruppe)
    {
        //Merkmalswert -> Anzahl
        Map<String, Integer> haeufigkeitstabelle = new HashMap<>();
        
        for (int zeilenIndex : gruppe)
        {
            String merkmal = originalSpalte.get(zeilenIndex);
            if (!haeufigkeitstabelle.containsKey(merkmal))
            {
                haeufigkeitstabelle.put(merkmal, 1);
            }
            else
            {
                haeufigkeitstabelle.put(merkmal, haeufigkeitstabelle.get(merkmal) + 1);
            }
        }  
        
        String haeufigstesMerkmal = null;

        for (String merkmal : haeufigkeitstabelle.keySet())
        {
            if (haeufigstesMerkmal == null || haeufigkeitstabelle.get(merkmal) > haeufigkeitstabelle.get(haeufigstesMerkmal))
            {
                haeufigstesMerkmal = merkmal;
            }
        }
        
        return haeufigstesMerkmal;
    }
}
