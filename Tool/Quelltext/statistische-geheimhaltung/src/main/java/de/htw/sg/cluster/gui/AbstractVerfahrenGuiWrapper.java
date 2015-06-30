package de.htw.sg.cluster.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public abstract class AbstractVerfahrenGuiWrapper<VerfahrenGuiWrapper>
{
    private DefaultListModel<VerfahrenGuiWrapper> verfahrenAuswahlListeModel;
    
    public AbstractVerfahrenGuiWrapper(
            DefaultListModel<VerfahrenGuiWrapper> verfahrenAuswahlListeModel)
    {
        this.verfahrenAuswahlListeModel = verfahrenAuswahlListeModel;
    }

    public abstract String getVerfahrensname();

    public Component getAuswahlListeItemComponent(boolean isSelected)
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        panel.add(new JLabel(getVerfahrensname()));
        
        JButton addButton = new JButton("+");
        panel.add(addButton);
        
        panel.setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
        
        return panel;
    }

    public JPanel getKonfigurationPanel(GewaehlteVerfahrenPanel gewaehlteClusterVerfahrenPanel)
    {
        JPanel panel = new JPanel(new MigLayout("wrap 1"));
        
        panel.add(new JLabel(getVerfahrensname()));
        panel.add(getKonfigurationPanelFuerParameter());
        
        JButton entfernenButton = new JButton("Entfernen");
        panel.add(entfernenButton);
        entfernenButton.addActionListener(new ActionListener()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent e)
            {
                gewaehlteClusterVerfahrenPanel.remove(panel);
                verfahrenAuswahlListeModel.addElement((VerfahrenGuiWrapper) AbstractVerfahrenGuiWrapper.this);
                
                gewaehlteClusterVerfahrenPanel.revalidate();
                gewaehlteClusterVerfahrenPanel.repaint();
            }
        });
        
        return panel;
    }
    
    public abstract <T> T getVerfahren();

    protected abstract JPanel getKonfigurationPanelFuerParameter();
}
