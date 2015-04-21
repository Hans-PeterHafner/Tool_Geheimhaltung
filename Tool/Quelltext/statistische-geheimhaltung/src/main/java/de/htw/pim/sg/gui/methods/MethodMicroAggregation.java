
package de.htw.pim.sg.gui.methods;

import com.google.common.collect.Table;
import com.jgoodies.forms.layout.CellConstraints;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.methods.MethodResult;
import de.htw.pim.sg.methods.micro_aggregation.MicroAggregationUtil;
import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Andy
 */
public class MethodMicroAggregation extends AbstractMethod {

    private JLabel lblSpalte;
    private JTextField txtSpalte;
    private JCheckBox chbVarriabel;
    private JLabel lblGruppengr;
    private JTextField txtGruppengr;
    private JPanel panGruppengr;
    private JPanel panSpalte;
    private JPanel configPanel;
    private CellConstraints c;
    
    /**
     * 
     * @param launcher
     */
    public MethodMicroAggregation(MainLauncher launcher) {
        super("Micro Aggregation", launcher);
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
    
    private void buildPanel() {
        lblSpalte = new JLabel("Anwendung auf Spalte");
        lblGruppengr = new JLabel("Gruppengröße");
        txtSpalte = new JTextField();
        txtGruppengr = new JTextField("3");
        chbVarriabel = new JCheckBox("Varriable Gruppengr.", true);

        configPanel = new JPanel();
        configPanel.setLayout(new MigLayout("fill"));
        c = new CellConstraints();

        configPanel.add(lblSpalte);
        configPanel.add(txtSpalte, "wrap, pushx, growx");
        configPanel.add(chbVarriabel, "wrap");
        configPanel.add(lblGruppengr);
        configPanel.add(txtGruppengr, "wrap, pushx, growx");
    }
   
    private void configurePanel() {
        txtGruppengr.setEnabled(false);
        
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
        
        txtGruppengr.setPreferredSize(new Dimension(40, 20));
        txtGruppengr.addKeyListener(new KeyAdapter() {
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
        
        chbVarriabel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (chbVarriabel.isSelected()) {
                    txtGruppengr.setEnabled(false);
                } else {
                    txtGruppengr.setEnabled(true);
                }
            }
        });
    }

    /**
     * 
     */
    @Override
    protected void add() {
        launcher.getAusgewaehlteMethoden().add(this);
        launcher.refreshInputTable();

        JPanel panTop = launcher.getPanTop();
        panTop.remove(this);
        panTop.add(new MethodMicroAggregation(launcher), "cell 0 1, pushx, growx");
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
        int gruppengr = Integer.parseInt(txtGruppengr.getText());
        MicroAggregationUtil micro = MicroAggregationUtil.getInstance();
        Table<Integer, Integer, Double> anonymizedTable;

        if (chbVarriabel.isSelected()) {
            anonymizedTable = micro.performOneDimensionalMicroAggregationWithVariableGroupSize(3, table, spalte);
        }else{
            anonymizedTable = micro.performOneDimensionalMicroAggregationWithFixedGroupSize(table, spalte, gruppengr);
        }

        Map<String, String> parameters = new Hashtable<String, String>();
        parameters.put("Gruppengröße", Integer.toString(gruppengr));
        parameters.put("Spalte", Integer.toString(spalte));
        parameters.put("Variable Gruppengröße", Boolean.toString(chbVarriabel.isSelected()));

        return new MethodResult(anonymizedTable, parameters);
    }
}
