package de.htw.sg.safe.gui.verfahrenauswahlliste;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.htw.sg.cluster.gui.AbstractVerfahrenGuiWrapper;
import de.htw.sg.cluster.gui.GewaehlteVerfahrenPanel;

public class VerfahrenAuswahlListe<VerfahrenGuiWrapper extends AbstractVerfahrenGuiWrapper<VerfahrenGuiWrapper>> extends JList<VerfahrenGuiWrapper>
{
    private static final long serialVersionUID = -2658775360504389195L;
    
    private GewaehlteVerfahrenPanel<VerfahrenGuiWrapper> gewaehlteVerfahrenListe;

    public VerfahrenAuswahlListe(GewaehlteVerfahrenPanel<VerfahrenGuiWrapper> gewaehlteVerfahrenListe)
    {
        setCellRenderer(new VerfahrenAuswahlListeCellRenderer());
        addMouseListener(new VerfahrenAuswahlListeMouseListener());
        
        setPreferredSize(new Dimension(100, 100));
        
        this.gewaehlteVerfahrenListe = gewaehlteVerfahrenListe;
    }
    
    class VerfahrenAuswahlListeMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (getModel().getSize() > 0)
            {
                DefaultListModel<VerfahrenGuiWrapper> model = (DefaultListModel<VerfahrenGuiWrapper>) getModel();
                
                VerfahrenGuiWrapper selectedVerfahren = getSelectedValue();
                model.removeElement(selectedVerfahren);
                
                gewaehlteVerfahrenListe.addVerfahren(selectedVerfahren);
            }
        }
    }

    class VerfahrenAuswahlListeCellRenderer implements ListCellRenderer<VerfahrenGuiWrapper>
    {
        
        @Override
        public Component getListCellRendererComponent(JList<? extends VerfahrenGuiWrapper> list,
                VerfahrenGuiWrapper value, int index, boolean isSelected, boolean cellHasFocus)
        {
            return value.getAuswahlListeItemComponent(isSelected);
        }
    }
}



