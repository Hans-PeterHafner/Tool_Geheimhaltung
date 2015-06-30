package de.htw.sg.safe.gui;

import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.miginfocom.swing.MigLayout;
import de.htw.pim.sg.gui.util.DataViewWrapper;
import de.htw.sg.cluster.gui.AbstractClusterGuiWrapper;
import de.htw.sg.cluster.gui.DBSCANGuiWrapper;
import de.htw.sg.cluster.gui.GewaehlteVerfahrenPanel;
import de.htw.sg.cluster.gui.KMeansPlusPlusGuiWrapper;
import de.htw.sg.safe.SafeParameter;
import de.htw.sg.safe.gui.verfahrenauswahlliste.VerfahrenAuswahlListe;
import de.htw.sg.safe.vereinheitlichung.ArithmetischeMittelwertVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.HaeufigstesMerkmalVereinheitlichung;
import de.htw.sg.safe.vereinheitlichung.KategorialesVereinheitlichungVerfahren;
import de.htw.sg.safe.vereinheitlichung.MetrischesVereinheitlichungVerfahren;
import de.htw.sg.safe.vereinheitlichung.VereinheitlichungVerfahren;

public class SafeConfigurationPanel extends JPanel
{
    private static final long serialVersionUID = -1232168156484872834L;
    
    private GewaehlteVerfahrenPanel<AbstractClusterGuiWrapper> gewaehlteClusterVerfahrenListe = new GewaehlteVerfahrenPanel<>();
    private VerfahrenAuswahlListe<AbstractClusterGuiWrapper> clusterVerfahrenAuswahlListe = new VerfahrenAuswahlListe<>(gewaehlteClusterVerfahrenListe);

    private JComboBox<DataViewWrapper<MetrischesVereinheitlichungVerfahren>> metrischeCombobox;
    private JComboBox<DataViewWrapper<KategorialesVereinheitlichungVerfahren>> kategorialeCombobox;

    private JSpinner spinnerMinGruppenGroesse = new JSpinner();

    public SafeConfigurationPanel(DateiTabelleView dateiTabelle, JFrame frame)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        initClusterVerfahrenAuswahlListe(frame);
        
        add(new JLabel("Verfügbare Clusterverfahren"));
        add(new JScrollPane(clusterVerfahrenAuswahlListe));
        add(Box.createVerticalStrut(20));
        add(new JLabel("Ausgewählte Clusterverfahren"));
        add(new JScrollPane(gewaehlteClusterVerfahrenListe));
        
        add(Box.createVerticalStrut(20));

        add(new JLabel("Vereinheitlichungsverfahren"));
        add(createPanelVereinheitlichungsverfahren());

        add(Box.createVerticalStrut(20));
        
        add(new JLabel("Sonstige Parameter"));
        add(createPanelSonstigeParameter());

        add(Box.createVerticalStrut(20));
        add(new JButton(new SafeAnonymisierungDurchfuehrenAction(dateiTabelle, this)));
    }

    private void initClusterVerfahrenAuswahlListe(JFrame frame)
    {
        DefaultListModel<AbstractClusterGuiWrapper> verfahrenAuswahlListeModel = new DefaultListModel<>();
        verfahrenAuswahlListeModel.addElement(new KMeansPlusPlusGuiWrapper(verfahrenAuswahlListeModel));
        verfahrenAuswahlListeModel.addElement(new DBSCANGuiWrapper(verfahrenAuswahlListeModel));
        clusterVerfahrenAuswahlListe.setModel(verfahrenAuswahlListeModel);
        clusterVerfahrenAuswahlListe.getModel().addListDataListener(new ListDataListener()
        {
            @Override
            public void intervalRemoved(ListDataEvent e)
            {
                frame.revalidate();
                frame.validate();
                frame.repaint();
            }
            
            @Override
            public void intervalAdded(ListDataEvent e)
            {
                frame.revalidate();
                frame.validate();
                frame.repaint();
            }
            
            @Override
            public void contentsChanged(ListDataEvent e)
            {
            }
        });
    }
    
    private JPanel createPanelVereinheitlichungsverfahren()
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        panel.add(new JLabel("Für metrische Merkmale:"));
        DefaultComboBoxModel<DataViewWrapper<MetrischesVereinheitlichungVerfahren>> metrischeComboboxModel = new DefaultComboBoxModel<>();
        metrischeComboboxModel.addElement(new DataViewWrapper<MetrischesVereinheitlichungVerfahren>(new ArithmetischeMittelwertVereinheitlichung(), "Arithmetischer Mittelwert"));
        metrischeCombobox = new JComboBox<>(metrischeComboboxModel);
        panel.add(metrischeCombobox);
        
        panel.add(new JLabel("Für kategoriale Merkmale:"));
        DefaultComboBoxModel<DataViewWrapper<KategorialesVereinheitlichungVerfahren>> kategorialeComboboxModel = new DefaultComboBoxModel<>();
        kategorialeComboboxModel.addElement(new DataViewWrapper<KategorialesVereinheitlichungVerfahren>(new HaeufigstesMerkmalVereinheitlichung(), "Häufigstes Merkmal in Gruppe"));
        kategorialeCombobox = new JComboBox<>(kategorialeComboboxModel);
        panel.add(kategorialeCombobox);

        return panel;
    }
    
    private JPanel createPanelSonstigeParameter()
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        panel.add(new JLabel("Minimale Clustergröße:"));
        spinnerMinGruppenGroesse.setModel(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));
        panel.add(spinnerMinGruppenGroesse);

        return panel;
    }

    public SafeParameter createParameter()
    {
        SafeParameter parameter = new SafeParameter();
        
        parameter.setMinGruppenGroesse((int) spinnerMinGruppenGroesse.getValue());
        parameter.setClusterVerfahrenListe(gewaehlteClusterVerfahrenListe.getGewaehlteVerfahren());
        parameter.setVereinheitlichungVerfahrenListe(Arrays.asList(new VereinheitlichungVerfahren(
                ((DataViewWrapper<MetrischesVereinheitlichungVerfahren>) metrischeCombobox.getSelectedItem()).getData(), 
                (((DataViewWrapper<KategorialesVereinheitlichungVerfahren>) kategorialeCombobox.getSelectedItem()).getData()))));
        
        return parameter;
    }
}





