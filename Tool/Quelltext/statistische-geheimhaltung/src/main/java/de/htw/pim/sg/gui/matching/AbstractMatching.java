package de.htw.pim.sg.gui.matching;

import de.htw.pim.sg.gui.MainLauncher;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Abstrakte Klasse als Schablone um Matching Verfahren in der GUI dar zu stellen.
 * Alle GUI-Einbindungen von Matchingverfahren werden von dieser Klasse abgeleitet.
 * Die Panels für Konfigurationsmöglichkeiten und Ergebnispräsentation müssen bei Ableitungen der Klasse implementiert werden.
 * 
 * @author Andy
 */
public abstract class AbstractMatching extends JPanel {

    protected MainLauncher launcher;

    /**
     * Grundaufbau der GUI Anzeige für Matchingverfahren
     * 
     * @param launcher
     * @param titel
     */
    public AbstractMatching(MainLauncher launcher, String titel) {
        this.launcher = launcher;
        launcher.getMatchingDialog().setTitle(titel);
        
        JPanel configPanel = createConfigPanel();
        JPanel resultPanel = createResultPanel();

        setLayout(new BorderLayout());
        add(configPanel, BorderLayout.NORTH);
        add(new JButton(new AbstractAction(titel +" starten") {

            public void actionPerformed(ActionEvent e) {
                run();
            }
        }),BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

    }

    /**
     * hier muss implementiert werden, was beim Anstoßen des Verfahrens aufgerufen werden soll.
     */
    protected abstract void run();

    /**
     * Hier muss das GUI-Panel zur Konfiguration des Matching Verfahrens implementiert werden.
     * @return
     */
    protected abstract JPanel createConfigPanel();

    /**
     * Hier muss das GUI-Panel zur Ergebnispräsentation des Matching Verfahrens implementiert werden.
     * @return
     */
    protected abstract JPanel createResultPanel();
}
