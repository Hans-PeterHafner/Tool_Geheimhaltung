package de.htw.sg.cluster.gui;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import de.htw.sg.cluster.DBSCANClustererRealMatrixClusterVerfahren;

public class DBSCANGuiWrapper extends AbstractClusterGuiWrapper
{
    private JSpinner textFeldMinimaleClusterGroesse = new JSpinner();
    private JSpinner textFeldEpsilon = new JSpinner();

    public DBSCANGuiWrapper(DefaultListModel<AbstractClusterGuiWrapper> verfahrenAuswahlListeModel)
    {
        super(verfahrenAuswahlListeModel);
        textFeldMinimaleClusterGroesse.setModel(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));
        textFeldEpsilon.setModel(new SpinnerNumberModel(0.1, 0.0001, 100, 0.1));
    }
    
    @Override
    public String getVerfahrensname()
    {
        return "DBSCAN";
    }

    @Override
    protected JPanel getKonfigurationPanelFuerParameter()
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        panel.add(new JLabel("Minimale Clustergröße:"));
        panel.add(textFeldMinimaleClusterGroesse);
        
        panel.add(new JLabel("Epsilon:"));
        panel.add(textFeldEpsilon);
        
        return panel;
    }

    @Override
    public DBSCANClustererRealMatrixClusterVerfahren getVerfahren()
    {
        return new DBSCANClustererRealMatrixClusterVerfahren(
                (double) textFeldEpsilon.getValue(), 
                (int) textFeldMinimaleClusterGroesse.getValue());
    }
}
