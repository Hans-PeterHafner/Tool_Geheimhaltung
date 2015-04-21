
package de.htw.pim.sg.gui.actions;

import com.google.common.collect.Table;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.gui.OeffnenDialog;
import de.htw.pim.sg.table.TableUtil;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

/** Öffnet einen Dialog zum Datei Öffnen (und zum Speichern).
 *
 * @author Andy
 */
public class OpenFileAction extends AbstractAction {

    private MainLauncher launcher;
    private JCheckBox chkKopfzeile;
    private Table<Integer, Integer, String> table;
    private JDialog dialog;

    /**
     * 
     * @param n Beschriftung des Button (der Action)
     * @param launcher Das MainLauncher Panel aus dem der Öffnen Dialog aufgerufen wird
     */
    public OpenFileAction(String n, MainLauncher launcher) {
        super(n);
        this.launcher = launcher;
    }

    public void actionPerformed(ActionEvent e) {
        OeffnenDialog od = new OeffnenDialog();
        File inputFile = od.erstelleDialog(launcher);
        table = CsvUtil.readCsvFile(inputFile, CsvUtil.SEPARATOR_SEMICOLON);
        
        dialog = new JDialog(launcher.getFrame(), "Bitte auswählen", true);
        chkKopfzeile = new JCheckBox("Erste Zeile Spaltenbeschriftung?");
        JButton btnOk = new JButton(new AbstractAction("OK") {

            public void actionPerformed(ActionEvent e) {
                launcher.setHatKopf(chkKopfzeile.isSelected());
                launcher.setInputTable(TableUtil.transformToDoubleTable(table));
                launcher.refreshInputTable();
                dialog.setVisible(false);
            }
        });
        
        dialog.setLayout(new BorderLayout());
        dialog.add(chkKopfzeile, BorderLayout.NORTH);
        dialog.add(btnOk,BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        
    }
    
}
