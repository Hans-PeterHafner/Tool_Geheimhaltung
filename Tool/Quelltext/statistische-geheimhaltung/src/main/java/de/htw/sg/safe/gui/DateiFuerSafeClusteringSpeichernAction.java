package de.htw.sg.safe.gui;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.htw.pim.sg.csv.CsvUtil;

public class DateiFuerSafeClusteringSpeichernAction extends AbstractAction
{
    private static final long serialVersionUID = -6023832771540693135L;
    
    private DateiTabelleView dateiTabelle;

    public DateiFuerSafeClusteringSpeichernAction(DateiTabelleView dateiTabelle)
    {
        Objects.requireNonNull(dateiTabelle, "dateiTabelle darf nicht null sein");
        
        putValue(Action.NAME, "Datei speichern");
        
        this.dateiTabelle = dateiTabelle;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser fileChooser = new JFileChooser();
        
        if (fileChooser.showSaveDialog(dateiTabelle) == JFileChooser.APPROVE_OPTION)
        {
            CsvUtil.writeCsvFile(dateiTabelle.createBasisdateiUndLeeren().getWerte(), fileChooser.getSelectedFile(), CsvUtil.SEPARATOR_SEMICOLON);
            JOptionPane.showMessageDialog(dateiTabelle, "Die Datei wurde gespeichert.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
