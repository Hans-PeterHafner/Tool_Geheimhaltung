package de.htw.sg.safe.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SafeClusterPanel extends JPanel
{
    private static final long serialVersionUID = 5093320563339617257L;

    private DateiTabelleView dateiTabelle = new DateiTabelleView();
    private SafeConfigurationPanel configurationPanel;
    
    public SafeClusterPanel(JFrame frame)
    {
        setLayout(new BorderLayout());
        
        configurationPanel = new SafeConfigurationPanel(dateiTabelle, frame);
        
        add(createToolbar(), BorderLayout.NORTH);
        add(new JScrollPane(dateiTabelle), BorderLayout.CENTER);
        add(configurationPanel, BorderLayout.EAST);
    }

    private Component createToolbar()
    {
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        toolbarPanel.add(new JButton(new DateiFuerSafeClusteringLadenAction(dateiTabelle)));
        toolbarPanel.add(new JButton (new DateiFuerSafeClusteringSpeichernAction(dateiTabelle)));
        
        return toolbarPanel;
    }

}
