package de.htw.sg.cluster.gui;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import de.htw.sg.cluster.KMeansPlusPlusRealMatrixClusterVerfahren;

public class KMeansPlusPlusGuiWrapper extends AbstractClusterGuiWrapper
{
    private JSpinner textFeldParameterK = new JSpinner();

    public KMeansPlusPlusGuiWrapper(DefaultListModel<AbstractClusterGuiWrapper> verfahrenAuswahlListeModel)
    {
        super(verfahrenAuswahlListeModel);
        textFeldParameterK.setModel(new SpinnerNumberModel(3, 2, Integer.MAX_VALUE, 1));
    }
    
    @Override
    public String getVerfahrensname()
    {
        return "k-means++";
    }

    @Override
    protected JPanel getKonfigurationPanelFuerParameter()
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        panel.add(new JLabel("k:"));
        panel.add(textFeldParameterK);
        
        return panel;
    }

    @Override
    public KMeansPlusPlusRealMatrixClusterVerfahren getVerfahren()
    {
        return new KMeansPlusPlusRealMatrixClusterVerfahren(
                (int) textFeldParameterK.getValue());
    }
}
