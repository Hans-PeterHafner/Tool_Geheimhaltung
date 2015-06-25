package de.htw.sg.safe.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.htw.sg.safe.SafeClusterAnonymisierung;
import de.htw.sg.safe.model.StatistikDatei;

public class SafeAnonymisierungDurchfuehrenAction extends AbstractAction
{
    private static final long serialVersionUID = 2462285453781644237L;
    private DateiTabelleView dateiTabelle;
    private SafeConfigurationPanel configurationPanel;

    public SafeAnonymisierungDurchfuehrenAction(DateiTabelleView dateiTabelle, SafeConfigurationPanel configurationPanel)
    {
        putValue(Action.NAME, "Anonymisierung durchführen");
        
        this.dateiTabelle = dateiTabelle;
        this.configurationPanel = configurationPanel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SafeClusterAnonymisierung anonymisierung = new SafeClusterAnonymisierung();
        
        StatistikDatei statistikDatei = anonymisierung.anonymisieren(dateiTabelle.createBasisdateiUndLeeren(), configurationPanel.createParameter());
        dateiTabelle.aktualisiereDaten(statistikDatei.getWerte());
    }
}
