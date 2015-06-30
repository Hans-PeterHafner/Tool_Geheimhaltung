package de.htw.sg.safe.normalisierung;

import java.util.Map;

import de.htw.pim.sg.table.TableUtil;
import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;

/**
 * Abstrakte Vorlage zur Normalisierung metrische Merkmalsspalten in einer
 * {@link StatistikDatei}.
 * 
 * @author Boris
 *
 */
public abstract class Normalisierungsverfahren
{
    public StatistikDatei normalisieren(StatistikDatei datei)
    {
        StatistikDatei normalisierteDatei = new StatistikDatei(datei.getSpalten(), TableUtil.copyTable(datei.getWerte()));
        
        int spaltenIndex = 0;
        
        for (Merkmal merkmal : normalisierteDatei.getSpalten())
        {
            if (merkmal.getTyp() == MerkmalTyp.METRISCH)
            {
                normalisiereSpalte(normalisierteDatei.getWerte().column(spaltenIndex));
            }

            spaltenIndex++;
        }
        
        return normalisierteDatei;
    }

    protected abstract void normalisiereSpalte(Map<Integer, String> spalte);
}
