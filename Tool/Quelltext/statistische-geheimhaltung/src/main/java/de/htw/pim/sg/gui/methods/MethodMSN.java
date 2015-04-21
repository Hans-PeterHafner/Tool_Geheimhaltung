
package de.htw.pim.sg.gui.methods;

import com.google.common.collect.Table;
import com.jgoodies.forms.layout.CellConstraints;
import de.htw.pim.sg.Configuration;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.methods.MethodResult;
import de.htw.pim.sg.methods.multiplicative_stochastic_noise.MultiplicativeStochasticNoise;
import net.miginfocom.swing.MigLayout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Andy
 */
public class MethodMSN extends AbstractMethod {

    private JLabel lblSpalte;
    private JTextField txtSpalte;
    private JLabel lblS;
    private JTextField txtS;
    private JLabel lblF;
    private JTextField txtF;
    private JPanel configPanel;
    private JPanel panSpalte;
    private CellConstraints c;

    /**
     * 
     * @param launcher
     */
    public MethodMSN(MainLauncher launcher) {
        super("Multipl. Stochastic Noise", launcher);
    }

    /**
     * 
     * @return
     */
    @Override
    protected JPanel createConfigPanel() {
        buildPanel();
        configurePanel();
        
        return configPanel;
    }
    
    private void buildPanel(){
        lblF = new JLabel("f   ");
        txtF = new JTextField();
        lblS = new JLabel("s   ");
        txtS = new JTextField();

        lblSpalte = new JLabel("Anwendung auf Spalte   ");
        txtSpalte = new JTextField();
        panSpalte = new JPanel();
        configPanel = new JPanel();
        configPanel.setLayout(new MigLayout("fill"));
        c = new CellConstraints();
        
        configPanel.add(lblSpalte);
        configPanel.add(txtSpalte, "growx, pushx, wrap, span");
        configPanel.add(lblF, "right");
        configPanel.add(txtF, "growx, pushx");
        configPanel.add(lblS);
        configPanel.add(txtS, "growx, pushx");
    }
    
    private void configurePanel(){
        txtSpalte.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        });
    }
    
    /**
     * Beim Hinzufügen des Verfahren soll ein neues leeres Verfahren angeboten werden.
     * An Platz 2 der Auswahl an verfügbaren Verfahren.
     */
    @Override
    protected void add() {        
        launcher.getAusgewaehlteMethoden().add(this);
        launcher.refreshInputTable();

        JPanel panTop = launcher.getPanTop();
        panTop.remove(this);
        panTop.add(new MethodMSN(launcher), "cell 0 2, growx, pushx");
        launcher.refreshPanBot();
    }

    /**
     * 
     * @param table
     * @return
     */
    @Override
    public MethodResult run(Table table) {
        int spalte = Integer.parseInt(txtSpalte.getText());
        double f;
        double s;

        try {
            f = Configuration.numberFormat.parse(txtF.getText()).doubleValue();
        } catch (ParseException e) {
            txtF.setToolTipText("Bitte eine Gleitkommazahl im richtigen Format eingeben");
            throw new RuntimeException(e);
        }

        try {
            s = Configuration.numberFormat.parse(txtS.getText()).doubleValue();
        } catch (ParseException e) {
            txtS.setToolTipText("Bitte eine Gleitkommazahl im richtigen Format eingeben");
            throw new RuntimeException(e);
        }

        MultiplicativeStochasticNoise msn = new MultiplicativeStochasticNoise(table);
        Table<Integer, Integer, Double> anonymizedTable = msn.run(spalte, f, s);

        Map<String, String> parameters = new Hashtable<String, String>();
        parameters.put("Spalte", Integer.toString(spalte));
        parameters.put("f", Configuration.numberFormat.format(f));
        parameters.put("s", Configuration.numberFormat.format(s));

        return new MethodResult(anonymizedTable, parameters);
    }
}
