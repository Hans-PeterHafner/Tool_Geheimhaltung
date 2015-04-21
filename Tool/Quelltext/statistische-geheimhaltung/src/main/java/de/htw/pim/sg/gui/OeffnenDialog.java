package de.htw.pim.sg.gui;

import java.awt.Component;
import java.io.File;
import java.util.prefs.*;
import javax.swing.JFileChooser;

/**
 * @author Andy
 */
public class OeffnenDialog {
    private Preferences prefs;


    public OeffnenDialog() {
        prefs = Preferences.userNodeForPackage(this.getClass());
    }

    /**
     * Oeffnet einen Dialog zum Auswaehlen einer Datei.
     * Gibt den Pfad der ausgewaelten Datei zurueck.
     *
     * @param c
     * @return
     */
    public File erstelleDialog(Component c) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String lastDir = prefs.get("lastDirOpen", "/home");
        chooser.setCurrentDirectory(new File(lastDir));
        chooser.setVisible(true);
        chooser.setMultiSelectionEnabled(false);
        int result = chooser.showOpenDialog(c);

        if (result == JFileChooser.APPROVE_OPTION) {
            File inputFile = chooser.getSelectedFile();

            prefs.put("lastDirOpen", chooser.getCurrentDirectory().getAbsolutePath());
            return inputFile;
        }
        chooser.setVisible(false);
        return null;
    }
}
