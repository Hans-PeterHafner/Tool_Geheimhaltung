package de.htw.sg.safe.gui;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.htw.pim.sg.csv.CsvUtil;
import net.miginfocom.swing.MigLayout;

public class StatistikFileChooser extends JFileChooser
{
    private static final long serialVersionUID = -3744857363726837149L;
    private JCheckBox checkboxErsteZeileUeberschrift = new JCheckBox();
    private JComboBox<SeparatorItem> comboboxSpaltentrennzeichen = initComboboxTrennzeichen();

    public StatistikFileChooser()
    {
        setAccessory(createAdditionalComponentsPanel());
    }
    
    private JComboBox<SeparatorItem> initComboboxTrennzeichen()
    {
        JComboBox<SeparatorItem> combobox = new JComboBox<>();
        
        combobox.addItem(new SeparatorItem(CsvUtil.SEPARATOR_COMMA, "Komma"));
        combobox.addItem(new SeparatorItem(CsvUtil.SEPARATOR_SEMICOLON, "Semikolon"));
        combobox.addItem(new SeparatorItem(CsvUtil.SEPARATOR_TABULATOR, "Tabulator"));
        
        return combobox;
    }

    public JComponent createAdditionalComponentsPanel()
    {
        JPanel panel = new JPanel(new MigLayout("wrap 2"));
        
        JLabel ueberschrift = new JLabel("CSV-Eigenschaften");
        ueberschrift.setFont(Font.decode("Arial-BOLD-18"));
        panel.add(ueberschrift, "wrap");

        panel.add(new JLabel("Erste Zeile Überschrift:"));
        panel.add(checkboxErsteZeileUeberschrift);

        panel.add(new JLabel("Spaltentrennzeichen:"));
        panel.add(comboboxSpaltentrennzeichen);
        
        return panel;
    }
    
    public char getSpaltentrennzeichen()
    {
        return ((SeparatorItem) comboboxSpaltentrennzeichen.getSelectedItem()).separator;
    }
    
    public boolean istErsteZeileUeberschrift()
    {
        return checkboxErsteZeileUeberschrift.isSelected();
    }
}

class SeparatorItem
{
    char separator;
    String text;
    
    public SeparatorItem(char separator, String text)
    {
        this.separator = separator;
        this.text = text;
    }
    
    @Override
    public String toString()
    {
        return text;
    }
}
