/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.htw.pim.sg.gui;

import java.awt.Component;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

/**
 *
 * @author Andy
 */
public class SpeichernDialog {

    private Preferences prefs;

    /**
     *
     * @param path
     */
    public SpeichernDialog(String path) {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    /**
     * Oeffnet einen Dialog zum Speichern einer Datei. Gibt den Pfad der
     * ausgewaelten Datei zurueck.
     *
     * @param c
     * @return
     */
    public File erstelleDialog(Component c) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Speichern unter...");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String lastDir = prefs.get("lastDirSave", "/home");
        chooser.setCurrentDirectory(new File(lastDir));
        chooser.setVisible(true);
        chooser.setMultiSelectionEnabled(false);
        int result = chooser.showOpenDialog(c);

        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = chooser.getSelectedFile();

            prefs.put("lastDirSave", chooser.getCurrentDirectory().getAbsolutePath());
            return outputFile;
        }
        chooser.setVisible(false);
        return null;
    }
}
