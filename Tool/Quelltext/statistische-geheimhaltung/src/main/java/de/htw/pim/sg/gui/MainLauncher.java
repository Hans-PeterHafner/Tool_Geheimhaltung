package de.htw.pim.sg.gui;

import com.google.common.collect.Table;
import de.htw.pim.sg.gui.actions.OpenFileAction;
import de.htw.pim.sg.gui.actions.RunAction;
import de.htw.pim.sg.gui.matching.StandardMatching;
import de.htw.pim.sg.gui.methods.*;
import de.htw.pim.sg.gui.util.DecimalCellRenderer;
import de.htw.pim.sg.methods.micro_aggregation.MicroAggregationUtil;
import net.java.dev.designgridlayout.DesignGridLayout;
import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * MainLauncher ist die Hauptanwendung der GUI. In main wird ein Fenster erzeugt
 * und dem Konstruktor übergeben. Dort wird dann das Hauptpanel gebaut und in
 * dem Fenster dar gestellt.
 *
 * In dem mittleren GUI Teil wird oben eine Liste aller verfügbaren Verfahren
 * angezeigt. Wählt man dort ein aus, wird dieses in den unteren Teil verschoben
 * und oben i.d.R. ein neues und leeres Objekt dieses Verfahrens angelegt.
 * Außerdem wird der Button zum Hinzufügen gegen eine Buttonleiste zum ändern
 * der Reihenfolge und Entfernen getauscht.
 *
 * Rechts werden alle Matchingverfahren aufgelistet. Per Klick auf den Button
 * wird ein Popup geöffnet mit entsprechender Ein und Ausgabe für das jeweilige
 * Verfahren.
 *
 * Links ist Platz für eine Vorschau Ansicht (Twelbereich) der geöffneten
 * Tabelle (Datei). Dort erscheinen erst Daten wenn eine Datei geöffnet wurde.
 *
 * @author Andy/Kirill Povolotskyy
 */
public class MainLauncher extends JPanel {

	private JPanel mainPanel;
	private JPanel topPanel;
    private JPanel contentPanel;
    private JMenuBar menueBar = new JMenuBar();
    private JMenu menu = new JMenu("Start");
    private JPanel panMatching = new JPanel();
    private JPanel panMatchingList = new JPanel();
    private JPanel panContolls = new JPanel();
    private JPanel panMain = new JPanel();
    private JPanel panTop = new JPanel();
    private JPanel panBot = new JPanel();
    private JPanel panInfoView = new JPanel();
    private JScrollPane spTop = new JScrollPane(panTop);
    private JScrollPane spBot = new JScrollPane(panBot);
    private JTable tblIn = new JTable();
    private JTable tblOut = new JTable();
    private JScrollPane spTableView = new JScrollPane(tblIn);
    private JScrollPane spTableViewOut = new JScrollPane(tblOut);
    private JLabel lblVerfuegbare = new JLabel();
    private JLabel lblAusgewaehlte = new JLabel();
    private JLabel lblSpaltenanzahl = new JLabel();
    private JLabel lblZeilenanzahl = new JLabel();
    private JLabel lblAuszug = new JLabel();
    private JTextField txtSpaltenanzahl = new JTextField();
    private JTextField txtZeilenanzahl = new JTextField();
    private OpenFileAction openFileAction = new OpenFileAction("Datei öffnen", this);
    private RunAction runAction = new RunAction("Anonymisierung starten", this);
    private ArrayList<AbstractMethod> ausgewaehlteMethoden = new ArrayList<AbstractMethod>();
    private MicroAggregationUtil micro = MicroAggregationUtil.getInstance();
    //Method creation
    private MethodMicroAggregation microAggregation = new MethodMicroAggregation(this);
    private MethodMDAV mdav = new MethodMDAV(this);
    private MethodMSN msn = new MethodMSN(this);
    private MethodStochMicroAggregation stochMicro = new MethodStochMicroAggregation(this);
    private MethodStochMicroSwapping stochSwapping = new MethodStochMicroSwapping(this);
    //---
    private Table<Integer, Integer, Double> inputTable = null;
    private Table<Integer, Integer, Double> outputTable = null;
    private int maxZeilen = 50;
    private TableModel tmIn;
    private TableModel tmOut;
    private boolean hatKopf;
    private String spaltennamen[];
    private JDialog matchingDialog = new JDialog();
    private MainLauncher launcher;

    /**
     *
     * @param f
     */
    public MainLauncher(JPanel f) {
       // super();
    	
        this.mainPanel = f;
        this.topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        this.contentPanel = new JPanel();
        
        this.mainPanel.setLayout(new BorderLayout(0, 0));
        this.mainPanel.add(topPanel, BorderLayout.NORTH);
        this.mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        this.launcher = this;

        buildUI();
        configureUI();
        initUI();




    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
   //     JFrame f = new JFrame();
   //     new MainLauncher(f);
    }

    private void buildUI() {
        MigLayout layout = new MigLayout("fill");
        contentPanel.setLayout(layout);
        contentPanel.add(panMain, "grow, push");
        contentPanel.add(panContolls, "top, wmin 300lp, gapright 15lp, gapleft 15lp");
        contentPanel.add(panMatching, "grow, push");



        panContolls.setLayout(new MigLayout("fill", "", "[][]"));
        
        panMatching.setLayout(new MigLayout("fill"));
        panMain.setLayout(new MigLayout("fill"));


        buildMenubar();

        panTop.setLayout(new MigLayout("fill"));
        panTop.add(lblVerfuegbare, "wrap");
        //methods
        panTop.add(microAggregation, "cell 0 1, growx, push");
        panTop.add(msn, "cell 0 2, growx, push");
        panTop.add(mdav, "cell 0 3, growx, push");
        panTop.add(stochMicro, "cell 0 4, growx, push");
        panTop.add(stochSwapping, "cell 0 5, growx, push");

        panBot.setLayout(new MigLayout("fill"));
        panBot.add(lblAusgewaehlte, "wrap");



        panContolls.add(spTop, "wrap, hmin 40%, push, grow");
        panContolls.add(spBot, "wrap, push, grow");

        panMatchingList.add(new JButton(new AbstractAction("Standard Matching") {

            public void actionPerformed(ActionEvent e) {
                matchingDialog.setContentPane(new StandardMatching(launcher));
                matchingDialog.pack();
                matchingDialog.setLocationRelativeTo(contentPanel);
                matchingDialog.setVisible(true);
            }
        }));

        panMatching.add(new JLabel("Matchingverfahren"), "wrap");
        panMatching.add(panMatchingList, "wrap");
        panMatching.add(spTableViewOut, "grow, push");
        panMatching.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panInfoView.add(lblSpaltenanzahl);
        panInfoView.add(txtSpaltenanzahl);
        panInfoView.add(lblZeilenanzahl);
        panInfoView.add(txtZeilenanzahl);

        panMain.add(panInfoView, "wrap");
        panMain.add(lblAuszug, "wrap");
        panMain.add(spTableView, "grow, push");
        panMain.setBorder(BorderFactory.createLineBorder(Color.BLACK));





    }

    private void buildMenubar() {
    	topPanel.add(new JButton(openFileAction));
    	topPanel.add(new JButton(runAction));
 //       f.setJMenuBar(menueBar);
    }

    private void configureUI() {
        tblIn.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblOut.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        spTop.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spTop.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        spBot.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spBot.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        txtSpaltenanzahl.setPreferredSize(new Dimension(50, 20));
        txtSpaltenanzahl.setEnabled(false);
        txtZeilenanzahl.setPreferredSize(new Dimension(50, 20));
        txtZeilenanzahl.setEnabled(false);

    }

    private void initUI() {
        runAction.setEnabled(false);
        lblVerfuegbare.setText("Verfügbare Verfahren");
        lblAusgewaehlte.setText("Gewählte Verfahren");
        lblSpaltenanzahl.setText("Anzahl an Spalten");
        lblZeilenanzahl.setText(" Anzahl an Zeilen");
        lblAuszug.setText("Ein Auszug aus der geladenen Tabelle (" + maxZeilen + " Zeilen)");
   //     f.setTitle("Statistische Geheimhaltung");
    }

    /**
     * Aktualisiert die Ansicht des unteren Panels auf die Anzeige der
     * ausgewählten Verfahren
     */
    public void refreshPanBot() {
        panBot.removeAll();
        panBot.add(lblAusgewaehlte, "wrap");
        for (AbstractMethod method : ausgewaehlteMethoden) {
            panBot.add(method, "wrap, growx, push");
        }
        panBot.revalidate();
        panTop.revalidate();
        this.revalidate();
    }

    public void refreshInputTable(){
        refreshTable(tblIn, inputTable);
    }

    public void refreshOutputTable() {
        refreshTable(tblOut, outputTable);
    }

    /**
     * Aktualisiert den Status der Buttons und füllt die Vorschautabelle falls
     * Daten eingelesen wurden
     */
    private void refreshTable(JTable guiTable, Table<Integer, Integer, Double> table) {
        runAction.setEnabled(table != null && ausgewaehlteMethoden.size() > 0);

        if (table != null && !table.isEmpty()) {
            int i = 0, k = 0, l = 0;


            Set<Integer> spalten = table.columnKeySet();
            int spaltenanzahl = spalten.size();
            spaltennamen = new String[spaltenanzahl];
            for (Integer spalte : spalten) {
                spaltennamen[i++] = spalte.toString();
            }

            int rowCount = table.rowMap().size();
            int maxTableRows = rowCount > maxZeilen ? maxZeilen : rowCount;

            Double data[][] = new Double[maxTableRows][spaltenanzahl];
            boolean ersteZeile = true;
            for (Integer rowKey : table.rowKeySet()) {
                l = 0;
                Map<Integer, Double> row = table.row(rowKey);
                for (Integer columnKey : row.keySet()) {
                    if (hatKopf && ersteZeile) {
                        spaltennamen[l] = row.get(columnKey).toString();
                    } else {
                        data[k][l] = row.get(columnKey);
                    }
                    l++;
                }
                if (++k >= maxZeilen) {
                    break;
                }
                if (hatKopf && ersteZeile) {
                    k = 0;
                }
                ersteZeile = false;
            }
            TableModel tm = new DefaultTableModel(data, spaltennamen) {

                public Class<?> getColumnClass(int column) {
                    return getValueAt(0, column).getClass();
                }
            };
            guiTable.setModel(tm);
            guiTable.setAutoCreateRowSorter(true);

            TableColumn column;
            Enumeration<TableColumn> cols = guiTable.getColumnModel().getColumns();

            while(cols.hasMoreElements()){
                column = cols.nextElement();
                column.setCellRenderer(new DecimalCellRenderer());

            }

            txtZeilenanzahl.setText("" + table.rowKeySet().size());
            txtSpaltenanzahl.setText("" + spaltenanzahl);
        }
    }


    /**
     *
     * @return
     */
    public ArrayList<AbstractMethod> getAusgewaehlteMethoden() {
        return ausgewaehlteMethoden;
    }

    /**
     *
     * @param table Die Tabelle mit den Eingabedaten
     */
    public void setInputTable(Table<Integer, Integer, Double> table) {
        this.inputTable = table;
    }

    /**
     *
     * @return
     */
    public Table<Integer, Integer, Double> getInputTable() {
        return inputTable;
    }

    /**
     *
     * @return
     */
    public JPanel getPanTop() {
        return panTop;
    }

    /**
     *
     * @return
     */
    public Frame getFrame() {
        return null;
    }

    /**
     *
     * @param b Hat die eingelesene Datei einen Tabellenkopf? (Ist erste Zeile
     * Spaltenbeschriftung)
     */
    public void setHatKopf(boolean b) {
        hatKopf = b;
    }

    /**
     *
     * @return
     */
    public boolean getHatKopf() {
        return hatKopf;
    }

    /**
     *
     * @param table Tabelle mit anonymisierten Daten
     */
    public void setOutputTable(Table<Integer, Integer, Double> table) {
        outputTable = table;
        refreshOutputTable();
    }

    /**
     *
     * @return
     */
    public Table<Integer, Integer, Double> getOutputTable() {
        return outputTable;
    }

    /**
     *
     * @return
     */
    public String[] getSpaltenNamen() {
        return spaltennamen;
    }

    /**
     *
     * @return
     */
    public JDialog getMatchingDialog() {
        return matchingDialog;
    }
}
