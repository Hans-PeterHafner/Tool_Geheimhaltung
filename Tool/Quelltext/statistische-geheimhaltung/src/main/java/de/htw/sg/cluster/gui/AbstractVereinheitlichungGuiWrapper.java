package de.htw.sg.cluster.gui;

import javax.swing.DefaultListModel;

public abstract class AbstractVereinheitlichungGuiWrapper extends AbstractVerfahrenGuiWrapper<AbstractVereinheitlichungGuiWrapper>
{
    public AbstractVereinheitlichungGuiWrapper(
            DefaultListModel<AbstractVereinheitlichungGuiWrapper> verfahrenAuswahlListeModel)
    {
        super(verfahrenAuswahlListeModel);
    }
}
