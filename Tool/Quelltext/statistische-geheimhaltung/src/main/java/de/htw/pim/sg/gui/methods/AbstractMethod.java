
package de.htw.pim.sg.gui.methods;

import com.google.common.collect.Table;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.methods.MethodResult;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Abstrakte Klasse als Schablone um Anonymisierungsverfahren in der GUI dar zu stellen.
 * Alle GUI-Einbindungen von An.-verfahren werden von dieser Klasse abgeleitet.
 * Das Panel für Konfigurationsmöglichkeiten muss bei Ableitungen der Klasse implementiert werden.
 *
 * @author Andy
 */
public abstract class AbstractMethod extends JPanel {

    protected MainLauncher launcher;
    private String titel;
    private JLabel lblTitel = new JLabel();
    private JButton btnAdd = new JButton();
    private JPanel panConfig = new JPanel();
    private JPanel panButtons = new JPanel();
    private JButton btnRemove = new JButton("Entfernen");
    private JButton btnUp = new JButton("Hoch");
    private JButton btnDown = new JButton("Runter");

    /**
     * Grundaufbau der GUI Anzeige für Anonymisierungsverfahren
     * 
     * @param titel
     * @param launcher
     */
    public AbstractMethod(String titel, MainLauncher launcher) {
        this.titel = titel;
        this.launcher = launcher;
        this.lblTitel.setText("-- " +titel+ " --");
        panConfig = createConfigPanel();

        buildUI();
        configureUI();
    }
    
    private void buildUI(){
        setLayout(new MigLayout("fill"));
        add(panConfig, "north");
        add(btnAdd, "south");
    }
    
    private void configureUI(){
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), titel));
        
        btnAdd.setAction(new AbstractAction("Hinzufügen") {

            public void actionPerformed(ActionEvent e) {
                add();
                setSteuerleiste();
            }
        });
    }

    // Diese Mehtode wird beim hinzufügen eines Verfahrens aufgerufen.
    // So verschwindet der Button zum Hinzufügen und statt dessen erscheinen die
    // Buttons zum ändern der Reihenfolge und zum Entfernen.
    private void setSteuerleiste() {
        panButtons.add(btnUp);
        panButtons.add(btnRemove);
        panButtons.add(btnDown);

        btnUp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                goUp();
            }
        });
        btnDown.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                goDown();
            }
        });
        btnRemove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });

        remove(btnAdd);
        add(panButtons, BorderLayout.SOUTH);
    }
    
    // Wenn nicht erstes Element dann nach oben schieben.
    private void goUp() {
        ArrayList<AbstractMethod> methods = launcher.getAusgewaehlteMethoden();
        int index = methods.indexOf(this);
        if (index > 0) {
            methods.remove(index);
            methods.add(index - 1, this);
            launcher.refreshPanBot();
        }
    }

    // Wenn nicht letztes Element dann nach unten schieben.
    private void goDown() {
        ArrayList<AbstractMethod> methods = launcher.getAusgewaehlteMethoden();
        int index = methods.indexOf(this);
        if (index < methods.size() - 2) {
            methods.remove(index);
            methods.add(index + 1, this);
            launcher.refreshPanBot();
        } else if (index < methods.size() - 1) {
            methods.remove(index);
            methods.add(this);
            launcher.refreshPanBot();
        }
    }

    private void remove() {
        ArrayList<AbstractMethod> methods = launcher.getAusgewaehlteMethoden();
        methods.remove(methods.indexOf(this));
        launcher.refreshPanBot();
    }

    /**
     * 
     * @return
     */
    public String getTitel() {
        return titel;
    }
    
    /**
     * Wie soll die Oberflöche zur Konfiguration des Verfahrens aussehen?
     * @return
     */
    protected abstract JPanel createConfigPanel();

    /**
     *  Was soll beim Hinzufügen des Verfahrens noch passieren?
     */
    protected abstract void add();

    /**
     * Hier muss implementiert werden, was beim Anstoßen des Verfahrens aufgerufen werden soll.
     * 
     * @param table
     * @return
     */
    public abstract MethodResult run(Table table);
}
