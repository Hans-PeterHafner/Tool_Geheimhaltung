package de.htw.pim.sg.gui.util;

import de.htw.pim.sg.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Kirill Povolotskyy
 *         Date: 08/07/12
 */
public class DecimalCellRenderer extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        value = Configuration.numberFormat.format( value);

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
