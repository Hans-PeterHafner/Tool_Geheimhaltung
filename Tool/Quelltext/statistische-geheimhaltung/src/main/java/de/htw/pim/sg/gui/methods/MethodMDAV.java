package de.htw.pim.sg.gui.methods;

import com.google.common.collect.Table;
import com.jgoodies.forms.layout.CellConstraints;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.pim.sg.methods.MethodResult;
import de.htw.pim.sg.methods.micro_aggregation.mdav.Mdav;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Kirill Povolotskyy
 * Time: 23:04
 */
public class MethodMDAV extends AbstractMethod {


    private JLabel lblPartitions;
    private JLabel lblGroupSize;

    private JTextField txtPartitions;
    private JTextField txtGroupSize;
    private JPanel configPanel;
    private JPanel panSpalte;
    private CellConstraints c;

    public MethodMDAV(MainLauncher launcher) {
        super("Multivariate microaggregation", launcher);
    }

    @Override
    protected JPanel createConfigPanel() {
        buildPanel();
        configurePanel();

        return configPanel;
    }

    private void configurePanel() {
        txtPartitions.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
            }
        });
    }

    @Override
    protected void add() {
        launcher.getAusgewaehlteMethoden().add(this);
        launcher.refreshInputTable();

        JPanel panTop = launcher.getPanTop();

        panTop.remove(this);
        panTop.add(new MethodMDAV(launcher), "cell 0 3, growx, pushx");
        launcher.refreshPanBot();
    }

    private void buildPanel() {
        lblPartitions = new JLabel("Partitionen   ");
        txtPartitions = new JTextField();
        txtPartitions.setToolTipText("Spalten durch \",\" und Gruppen durch \";\" trennen. z.B. 1,2;5,6");

        lblGroupSize = new JLabel("Gruppengröße   ");
        txtGroupSize = new JTextField();

        panSpalte = new JPanel();
        configPanel = new JPanel();
        configPanel.setLayout(new MigLayout("fill"));
        c = new CellConstraints();

        configPanel.add(lblPartitions);
        configPanel.add(txtPartitions, "pushx, growx, wrap");
        configPanel.add(lblGroupSize);
        configPanel.add(txtGroupSize, "pushx, growx, wrap");
    }

    @Override
    public MethodResult run(Table table) {
        String text = txtPartitions.getText();
        String[] txtPartitions = text.split(";");
        List<int[]> partitions = new LinkedList<int[]>();
        int[] tmpPartition;

        if (txtPartitions.length == 0) {
            String[] txtVariables = text.split(",");
            tmpPartition = new int[txtVariables.length];
            for (int i = 0; i < txtVariables.length; i++) {
                tmpPartition[i] = Integer.parseInt( txtVariables[i] );
            }

        }

        for (String partition : txtPartitions) {
            String[] splitPartition = partition.split(",");
            tmpPartition = new int[splitPartition.length];
            for (int i = 0; i < splitPartition.length; i++) {
                tmpPartition[i] = Integer.parseInt(splitPartition[i].trim());
            }
            partitions.add(tmpPartition);

        }


        int groupSize = Integer.parseInt(txtGroupSize.getText());
        Mdav mdav = new Mdav(table, groupSize);

        Table<Integer, Integer, Double> anonymizedTable = mdav.run(partitions);
        Map<String, String> parameters = new Hashtable<String, String>();
        parameters.put("Partitionen", text);
        parameters.put("Gruppengröße", txtGroupSize.getText());

        return new MethodResult(anonymizedTable, parameters);
    }
}