package de.htw.sg.safe.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.google.common.collect.Table;

import de.htw.pim.sg.table.TableUtil;
import de.htw.sg.safe.model.Merkmal;
import de.htw.sg.safe.model.MerkmalTyp;
import de.htw.sg.safe.model.StatistikDatei;

public class DateiTabelleView extends JTable
{
    private static final long serialVersionUID = 5467200701901844381L;

    public DateiTabelleView()
    {
        TableColumn column = new TableColumn();
        column.setHeaderValue("Bitte eine Datei laden!");
        addColumn(column);
        
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) 
            {
                JTableHeader header = (JTableHeader)e.getSource();
                int viewColumnIndex = columnModel.getColumnIndexAtX(e.getX());
                int columnIndex = header.getTable().convertColumnIndexToModel(viewColumnIndex);
                TableColumn column = getColumnModel().getColumn(columnIndex);
                
                if (column.getHeaderValue() instanceof MerkmalSpaltenHeaderCell)
                {
                    MerkmalSpaltenHeaderCell merkmalSpalteHeaderCell = (MerkmalSpaltenHeaderCell) column.getHeaderValue();
                    merkmalSpalteHeaderCell.combobox.setSelectedIndex((merkmalSpalteHeaderCell.combobox.getSelectedIndex() + 1) % merkmalSpalteHeaderCell.combobox.getItemCount());
                }
                
                header.repaint();
            }
        });
    }

    public void zeigeDaten(Table<Integer, Integer, String> daten, boolean istErsteZeileUeberschrift)
    {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(daten.columnKeySet().size());
        
        for (Entry<Integer, Map<Integer, String>> row : daten.rowMap().entrySet())
        {
            if (!istErsteZeileUeberschrift || row.getKey() > 0)
            {
                Vector<String> rowVector = new Vector<>();
                row.getValue().forEach((index, column) -> rowVector.add(column));
                model.addRow(rowVector);
            }
        }
        
        setModel(model);
        
        if (istErsteZeileUeberschrift)
        {
            daten.row(0).forEach((colIndex, value) -> initSpaltenHeader(colIndex, value));
        }
    }
    
    public void aktualisiereDaten(Table<Integer, Integer, String> daten)
    {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);
        
        for (Entry<Integer, Map<Integer, String>> row : daten.rowMap().entrySet())
        {
            Vector<String> rowVector = new Vector<>();
            row.getValue().forEach((index, column) -> rowVector.add(column));
            model.addRow(rowVector);
        }
        
        setModel(model);
    }

    private void initSpaltenHeader(Integer colIndex, String value)
    {
        MerkmalSpaltenHeaderCell spaltenHeader = new MerkmalSpaltenHeaderCell(value);
        TableColumn spalte = getTableHeader().getColumnModel().getColumn(colIndex);
        spalte.setHeaderValue(spaltenHeader);
        spalte.setHeaderRenderer(new MerkmalSpaltenHeaderRenderer());
    }

    public StatistikDatei createBasisdateiUndLeeren()
    {
        StatistikDatei statistikDatei = new StatistikDatei(createMerkmalSpalten(), createWerte());
        
        DefaultTableModel model = (DefaultTableModel) getModel();
        
        model.setRowCount(0);
        
        return statistikDatei;
    }

    private Table<Integer, Integer, String> createWerte()
    {
        Table<Integer, Integer, String> werte = TableUtil.newTable(getRowCount(), getColumnCount());
        
        for (int i = 0; i < getRowCount(); i++)
        {
            for (int j = 0; j < getColumnCount(); j++)
            {
                werte.put(i, j, (String) getValueAt(i, j));
            }
        }
        
        return werte;
    }

    private List<Merkmal> createMerkmalSpalten()
    {
        List<Merkmal> merkmalsliste = new ArrayList<>();
        Enumeration<TableColumn> columns = getColumnModel().getColumns();
        
        while (columns.hasMoreElements())
        {
            MerkmalSpaltenHeaderCell merkalHeaderCell = (MerkmalSpaltenHeaderCell) columns.nextElement().getHeaderValue();
            merkmalsliste.add(new Merkmal(merkalHeaderCell.merkmalsname, (MerkmalTyp) merkalHeaderCell.combobox.getSelectedItem()));
        }
        
        return merkmalsliste;
    }
}

class MerkmalSpaltenHeaderCell
{
    String merkmalsname;
    JComboBox<MerkmalTyp> combobox = new JComboBox<>();
    
    public MerkmalSpaltenHeaderCell(String merkmalName)
    {
        this.merkmalsname = merkmalName;
        
        createCombobox();
    }

    private void createCombobox()
    {
        combobox.addItem(MerkmalTyp.KATEGORIAL);
        combobox.addItem(MerkmalTyp.METRISCH);
    }
}

class MerkmalSpaltenHeaderRenderer implements TableCellRenderer
{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        if (value instanceof MerkmalSpaltenHeaderCell)
        {
            MerkmalSpaltenHeaderCell merkmalHeader = (MerkmalSpaltenHeaderCell) value;

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            JLabel merkmalsname = new JLabel(merkmalHeader.merkmalsname);
            merkmalsname.setFont(Font.decode("Arial-BOLD-12"));
            panel.add(merkmalsname);
            panel.add(merkmalHeader.combobox);

            return panel;
        }
        else
        {
            return new JLabel(value.toString());
        }
    }
}
