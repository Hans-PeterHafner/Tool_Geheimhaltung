package de.htw.sg.cluster.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GewaehlteVerfahrenPanel<VerfahrenGuiWrapper extends AbstractVerfahrenGuiWrapper<VerfahrenGuiWrapper>> extends JPanel
{
    private static final long serialVersionUID = -1472936151542742698L;
    
    private Map<JPanel, VerfahrenGuiWrapper> verfahrenMap = new HashMap<>();
    
    public GewaehlteVerfahrenPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(260, 150));
    }
    
    public void addVerfahren(VerfahrenGuiWrapper verfahren)
    {
        JPanel verfahrenPanel = verfahren.getKonfigurationPanel(this);
        verfahrenMap.put(verfahrenPanel, verfahren);
        add(verfahrenPanel);
        revalidate();
    }
    
    @Override
    public void remove(Component comp)
    {
        verfahrenMap.remove(comp);
        super.remove(comp);
    }

    public <T> List<T> getGewaehlteVerfahren()
    {
        List<T> verfahrenliste = new ArrayList<>();
        verfahrenMap.forEach((panel, verfahren) -> verfahrenliste.add(verfahren.getVerfahren()));
        return verfahrenliste;
    }
}
