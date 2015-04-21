package de.htw.pim.sg.gui.actions;

import com.google.common.collect.Table;
import de.htw.pim.sg.Configuration;
import de.htw.pim.sg.csv.CsvUtil;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.gui.SpeichernDialog;
import de.htw.pim.sg.gui.methods.AbstractMethod;
import de.htw.pim.sg.methods.MethodResult;
import de.htw.pim.sg.table.TableUtil;
import de.htw.pim.sg.util.Reporter;

import java.awt.event.ActionEvent;
import java.io.File;
//import java.lang.invoke.MethodHandleProxies;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;

/**
 * Stößt den Vorgang der Anonymisierung an.
 * Beim Auslösen der Aktion wird die Liste aller ausgewählten Verfahren abgearbeitet.
 * In jedem der Verfahren wird die run() Mehtode aufgerufen.
 *
 * @author Andy
 */
public class RunAction extends AbstractAction{
    private MainLauncher launcher;

    /**
     * 
     * @param n Beschriftung des Button (der Action)
     * @param launcher
     */
    public RunAction(String n, MainLauncher launcher){
        super(n);
        this.launcher = launcher;
    }
    
    public void actionPerformed(ActionEvent e) {
        SpeichernDialog sd = new SpeichernDialog(null);
        File outputFile = sd.erstelleDialog(launcher);
        Map<String, Map<String, String>> methods = new HashMap<String, Map<String, String>>();
        
        Table<Integer, Integer, Double> table = launcher.getInputTable();
        // Für alle ausgewählte Methoden run() aufrufen
        for (AbstractMethod method : launcher.getAusgewaehlteMethoden()){
            MethodResult result = method.run(table);
            table = result.getAnonymizedTable();
            methods.put(method.getTitel(), result.getParemeters());
        }
        launcher.setOutputTable(table);

        Reporter.createReport(methods, outputFile.getParent());
        CsvUtil.writeCsvFile(TableUtil.transformToStringTable(table, Configuration.numberFormat), outputFile, CsvUtil.SEPARATOR_SEMICOLON);
    }
    
}
