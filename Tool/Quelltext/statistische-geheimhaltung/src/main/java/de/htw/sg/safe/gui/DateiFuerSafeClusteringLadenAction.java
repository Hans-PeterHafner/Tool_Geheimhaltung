package de.htw.sg.safe.gui;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import com.google.common.collect.Table;

import de.htw.pim.sg.csv.CsvUtil;

public class DateiFuerSafeClusteringLadenAction extends AbstractAction
{
    private static final long serialVersionUID = 8392455834797045268L;
    
    private DateiTabelleView dateiTabelle;
    
    public DateiFuerSafeClusteringLadenAction(DateiTabelleView dateiTabelle)
    {
        Objects.requireNonNull(dateiTabelle, "dateiTabelle darf nicht null sein");
        
        putValue(Action.NAME, "Datei laden");

        this.dateiTabelle = dateiTabelle;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        StatistikFileChooser fileChooser = new StatistikFileChooser();
        
        if (fileChooser.showOpenDialog(dateiTabelle) == JFileChooser.APPROVE_OPTION)
        {
            Table<Integer,Integer,String> daten = CsvUtil.readCsvFile(fileChooser.getSelectedFile(), fileChooser.getSpaltentrennzeichen());
            dateiTabelle.zeigeDaten(daten, fileChooser.istErsteZeileUeberschrift());
        }
    }
}
