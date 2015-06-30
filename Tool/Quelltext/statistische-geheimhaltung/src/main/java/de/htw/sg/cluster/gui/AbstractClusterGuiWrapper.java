package de.htw.sg.cluster.gui;

import javax.swing.DefaultListModel;


public abstract class AbstractClusterGuiWrapper extends AbstractVerfahrenGuiWrapper<AbstractClusterGuiWrapper>
{

    public AbstractClusterGuiWrapper(
            DefaultListModel<AbstractClusterGuiWrapper> verfahrenAuswahlListeModel)
    {
        super(verfahrenAuswahlListeModel);
    }

}
