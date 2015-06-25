package de.htw.sg.safe;

import java.util.Map;

import de.htw.sg.safe.normalisierung.Normalisierungsverfahren;

/**
 * Vergleiche http://de.wikipedia.org/wiki/Studentisierung
 * 
 * @author Boris
 *
 */
public class Studentisierung extends Normalisierungsverfahren
{
    @Override
    protected void normalisiereSpalte(Map<Integer, String> spalte)
    {
        double arithmetischesMittel = berechneArithmetischesMittel(spalte);
        
        double stichprobenStandardabweichung = berechneStichprobenStandardabweichung(spalte, arithmetischesMittel);
        
        for (int i = 0; i < spalte.size(); i++)
        {
            double normalisierterWert = (Double.parseDouble(spalte.get(i)) - arithmetischesMittel) / stichprobenStandardabweichung;
            spalte.put(i, String.valueOf(normalisierterWert));
        }
    }

    private double berechneArithmetischesMittel(Map<Integer, String> spalte)
    {
        double summe = 0;
        
        for (int i = 0; i < spalte.size(); i++)
        {
            summe += Double.parseDouble(spalte.get(i));
        }

        return summe / spalte.size();
    }
    
    private double berechneStichprobenStandardabweichung(Map<Integer, String> spalte, double arithmetischesMittel)
    {
        double summeQuadratierterDifferenzen = 0;
        
        for (int i = 0; i < spalte.size(); i++)
        {
            double wert = Double.parseDouble(spalte.get(i));
            
            summeQuadratierterDifferenzen += (wert - arithmetischesMittel) * (wert - arithmetischesMittel);
        }
        
        return Math.sqrt(summeQuadratierterDifferenzen / spalte.size());
    }
}
