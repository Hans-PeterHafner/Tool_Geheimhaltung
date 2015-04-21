
package de.htw.pim.sg.gui.matching;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.matching.Matching;
import de.htw.pim.sg.matching.MatchingResult;
import de.htw.pim.sg.table.TableUtil;
import de.htw.pim.sg.util.ColumnType;
import de.htw.pim.sg.util.MetaColumn;
import de.htw.pim.sg.util.MetaData;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Standard Matching Verfahren das von Michael implemntiert wurde.
 * @author Andy
 */
public class StandardMatching extends AbstractMatching {

    private JPanel spaltenPanel;
    private JPanel configPanel;
    private JPanel resultPanel;
    private JTextField txtTresholds[];
    private JTextField txtWeights[];
    private JComboBox comTyps[];
    private JCheckBox chkIsKeys[];
    private JTextField txtPossibleMatches;
    private JTextField txtRealMatches;
    private Table<Integer, Integer, String> tableOriginal;
    private Table<Integer, Integer, String> tableAnonym;

    /**
     * 
     * @param launcher
     */
    public StandardMatching(MainLauncher launcher) {
        super(launcher, "Standard Matching");
    }

    /**
     * 
     * @return
     */
    @Override
    protected JPanel createConfigPanel() {
        configPanel = new JPanel();
        configPanel.setLayout(new BorderLayout());
        spaltenPanel = new JPanel();
        spaltenPanel.setLayout(new BoxLayout(spaltenPanel, BoxLayout.X_AXIS));

        String[] spaltenNamen = launcher.getSpaltenNamen();
        txtTresholds = new JTextField[spaltenNamen.length];
        comTyps = new JComboBox[spaltenNamen.length];
        chkIsKeys = new JCheckBox[spaltenNamen.length];
        txtWeights = new JTextField[spaltenNamen.length];

        PanelBuilder builder = new PanelBuilder(new FormLayout(""));
        CellConstraints c = new CellConstraints();
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("right:max(pref; 100px)");

        builder.appendRow("top:pref");
        builder.addLabel("Spalte ", c.xy(1, 1));
        builder.appendRow("top:pref");
        builder.addLabel("Typ ", c.xy(1, 2));
        builder.appendRow("top:pref");
        builder.addLabel("Schwelle ", c.xy(1, 3));
        builder.appendRow("top:pref");
        builder.addLabel("Gewicht ", c.xy(1, 4));
        builder.appendRow("top:pref");
        builder.addLabel("Schlüssel ", c.xy(1, 5));

        int i = 0;
        for (String s : launcher.getSpaltenNamen()) {

            txtTresholds[i] = new JTextField();
            comTyps[i] = new JComboBox(ColumnType.values());
            chkIsKeys[i] = new JCheckBox();
            txtWeights[i] = new JTextField();

            txtTresholds[i].setText("0.01");
            txtTresholds[i].addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!((c >= '0') && (c <= '9')
                            || (c == KeyEvent.VK_BACK_SPACE)
                            || (c == KeyEvent.VK_DELETE)
                            || (c == '.')
                            || (c == ','))) {
                        getToolkit().beep();
                        e.consume();
                    }
                }
            });

            txtWeights[i].setText("1");
            txtWeights[i].addKeyListener(new KeyAdapter() {

                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!((c >= '0') && (c <= '9')
                            || (c == KeyEvent.VK_BACK_SPACE)
                            || (c == KeyEvent.VK_DELETE)
                            || (c == '.')
                            || (c == ','))) {
                        getToolkit().beep();
                        e.consume();
                    }
                }
            });

            builder.appendColumn("fill:max(pref; 50px)");
            builder.addLabel(s, c.xy(i + 2, 1));
            builder.add(comTyps[i], c.xy(i + 2, 2));
            builder.add(txtTresholds[i], c.xy(i + 2, 3));
            builder.add(txtWeights[i], c.xy(i + 2, 4));
            builder.add(chkIsKeys[i], c.xy(i + 2, 5));

            i++;
        }
        spaltenPanel.add(builder.getPanel());


        configPanel.add(spaltenPanel, BorderLayout.CENTER);

        return configPanel;
    }

    /**
     * 
     * @return
     */
    @Override
    protected JPanel createResultPanel() {
        resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        txtPossibleMatches = new JTextField();
        txtPossibleMatches.setEditable(false);
        txtRealMatches = new JTextField();
        txtRealMatches.setEditable(false);

        PanelBuilder builder = new PanelBuilder(new FormLayout(""));
        CellConstraints c = new CellConstraints();
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("right:max(pref; 100px)");
        builder.appendColumn("fill:max(pref; 50px)");

        builder.appendRow("top:pref");
        builder.addLabel("Mögliche Matches ", c.xy(1, 1));
        builder.add(txtPossibleMatches, c.xy(2, 1));

        builder.appendRow("top:pref");
        builder.addLabel("Tatsächliche Matches ", c.xy(1, 2));
        builder.add(txtRealMatches, c.xy(2, 2));

        resultPanel.add(new JLabel("Ergebnis"), BorderLayout.NORTH);
        resultPanel.add(builder.getPanel(), BorderLayout.CENTER);

        return resultPanel;
    }

    /**
     * 
     */
    @Override
    protected void run() {
        ArrayList<MetaColumn> metaColumns = new ArrayList<MetaColumn>();
        tableOriginal  = TableUtil.transformToStringTable(launcher.getInputTable());
        tableAnonym = TableUtil.transformToStringTable(launcher.getOutputTable());
        
        MetaData meta = new MetaData(metaColumns);
        
        // Alle Eingabedaten werden ermittelt und in einem meta-Objekt verpackt.
        for (int i=0;i<launcher.getSpaltenNamen().length;i++){
            Double treshold = Double.parseDouble(txtTresholds[i].getText());
            Double weight = Double.parseDouble(txtWeights[i].getText());
            ColumnType ctype = ColumnType.valueOf(comTyps[i].getSelectedItem().toString());
            meta.addColumn(new MetaColumn(treshold,weight,ctype,chkIsKeys[i].isSelected(), i));
        }
        // Instanz des Matching Verfahren erstellen und anstoßen.
        Matching matching = new Matching();
        try {
            matching.startMatching((ArrayTable<Integer, Integer, String>) tableOriginal, (ArrayTable<Integer, Integer, String>) tableAnonym, meta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MatchingResult result = matching.getMatchingResults();
        txtPossibleMatches.setText(""+result.getPossible_matches());
        txtRealMatches.setText(""+result.getReal_matches());
    }
}
