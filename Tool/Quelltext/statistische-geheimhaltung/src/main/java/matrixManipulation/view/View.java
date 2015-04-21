package matrixManipulation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileSystemView;

import Startup.StatistischeGeheimhaltung;

import matrixManipulation.actions.LoadSaveActionListener;
import matrixManipulation.actions.overlayOptionsActionListener;

import matrixManipulation.core.Statics;

/** @author Ciprian Savu
 * Diese Klasse zeichnet das Haupt-Fenster
 */
public class View extends JPanel {

	private JFrame mainFrame;
	private JPanel mainPanel;
	private JTextField statusBar;
	private Color statusBarColor;
	private JSplitPane outerSplitPane;
	private JSplitPane innerSplitPane; 
	private JPanel transformedValuesPanel;
	private JComboBox comboBox;
	private JPanel optionsPanels;
	private JPanel loadSavePanel;
	private JProgressBar progressBar = null;
	private JDialog viewBlocker = null;
	private JLabel viewBlockerText = null;
	
	/**
	 * Standardkonstruktor
	 */
	public View(JPanel newPanel){
	//	super("Matrixmanipulation");
		initialize(newPanel);
	}
	
	/** 
	 * Initialisiert die Komponenten der View
	 */
	private void initialize(JPanel newPanel){
		
		// Das Haupt-Panel
		mainPanel = newPanel;
		Statics.setView(this);
		mainPanel.setBackground(Color.WHITE);
//		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		// Status-Panel
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		statusPanel.setBackground(Color.WHITE);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
		mainPanel.add(statusPanel, BorderLayout.SOUTH);
		
		// Status-Bar
		statusBar = new JTextField("");
		statusBar.setEditable(false);
		statusBarColor = statusBar.getBackground();
		statusPanel.add(statusBar);
		
		/* Initialisiert das erste Splitpane, dieses zeigt links die Original-Daten und
		 * rechts das outerSplitPane  
		 */
		outerSplitPane = new JSplitPane();
		outerSplitPane.setResizeWeight(0.5);
		outerSplitPane.setBackground(Color.WHITE);
		mainPanel.add(outerSplitPane, BorderLayout.CENTER);
		
		// Initialisiert das zweite Splitpane, dieses zeigt links die Optionen und rechts die veraenderten Daten
		innerSplitPane = new JSplitPane();
		//innerSplitPane.setResizeWeight(0.001);
		outerSplitPane.setRightComponent(innerSplitPane);
		
		// Initialisiert das Optionen-Panel
		optionsPanels = new JPanel();
		optionsPanels.setBackground(Color.WHITE);
		innerSplitPane.setLeftComponent(optionsPanels);
		optionsPanels.setLayout(new BoxLayout(optionsPanels, BoxLayout.Y_AXIS));
		
		JPanel iOptionPanel = new JPanel();
		iOptionPanel.setBackground(new Color(255, 255, 153));
		iOptionPanel.setMaximumSize(new Dimension(32767, 30));
		FlowLayout flowLayout = (FlowLayout) iOptionPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		optionsPanels.add(iOptionPanel);
		
		JLabel lblUeberlagerungsMethode = new JLabel("<html>&Uuml;berlagerungsmethode w&auml;hlen</html>");
		lblUeberlagerungsMethode.setBackground(new Color(255, 255, 153));
		iOptionPanel.add(lblUeberlagerungsMethode);

		// Combobox mit der Berechnungsmethode
		comboBox = new JComboBox();
		comboBox.setBackground(Color.WHITE);
		optionsPanels.add(comboBox);
		comboBox.setMaximumSize(new Dimension(32767, 25));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Diagonale \u00DCberlagerung", "\u00DCberlagerung mit Permutationen"}));
		comboBox.addActionListener(new overlayOptionsActionListener());
		
		//Standardoptionen laden
		OptionChooserPanels.optionsPane = OptionChooserPanels.getcyclicDiagonalOverlayOptions();
		optionsPanels.add(OptionChooserPanels.optionsPane);
		
		transformedValuesPanel = new JPanel();
		transformedValuesPanel.setBackground(Color.WHITE);
		transformedValuesPanel.setForeground(Color.WHITE);
		innerSplitPane.setRightComponent(transformedValuesPanel);
		
		JPanel originalValuesPanel = new JPanel();
		originalValuesPanel.setBackground(Color.WHITE);
		outerSplitPane.setLeftComponent(originalValuesPanel);
		
		loadSavePanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) loadSavePanel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		loadSavePanel.setBackground(Color.WHITE);
		mainPanel.add(loadSavePanel, BorderLayout.NORTH);
		
		JButton btnDateiLaden = new JButton("Datei laden");
		btnDateiLaden.setActionCommand(Statics.LOAD_FILE);
		btnDateiLaden.addActionListener(new LoadSaveActionListener());
		btnDateiLaden.setName("btnDateiLaden");
		loadSavePanel.add(btnDateiLaden);
		
		JButton btnErgebnisseSpeichern = new JButton("Ergebnisse speichern");
		btnErgebnisseSpeichern.setName("btnErgebnisseSpeichern");
		btnErgebnisseSpeichern.setActionCommand(Statics.SAVE_FILE);
		btnErgebnisseSpeichern.addActionListener(new LoadSaveActionListener());
		btnErgebnisseSpeichern.setEnabled(false);
		loadSavePanel.add(btnErgebnisseSpeichern);
		
		//Dateiname der aktuell geladenen Datei
		JLabel fileNameText = new JLabel("     ");
		fileNameText.setName("fileNameText");
		loadSavePanel.add(fileNameText);
		
		JTextField fileNameTextField = new JTextField();
		fileNameTextField.setName("fileNameTextField");
		fileNameTextField.setEditable(false);
		fileNameTextField.setEnabled(false);
		fileNameTextField.setVisible(false);
		fileNameTextField.setHorizontalAlignment(JTextField.RIGHT);
		fileNameTextField.setColumns(15);
		loadSavePanel.add(fileNameTextField);
		
		JLabel freeSpace = new JLabel("        ");
		freeSpace.setName("freeSpace");
		loadSavePanel.add(freeSpace);
				
		JCheckBox chkBoxSaveDetailLog = new JCheckBox();
		chkBoxSaveDetailLog.setName("chkBoxSaveDetailLog");
		chkBoxSaveDetailLog.setActionCommand("chkBoxSaveDetailLog");
		chkBoxSaveDetailLog.setToolTipText("Detail-Log speichern");
		chkBoxSaveDetailLog.setText("Detail-Log speichern");
		chkBoxSaveDetailLog.setBackground(loadSavePanel.getBackground());
		loadSavePanel.add(chkBoxSaveDetailLog);
		
		JTextField textFieldSaveDetailLog = new JTextField();
		textFieldSaveDetailLog.setColumns(20);
		textFieldSaveDetailLog.setEnabled(false);
		textFieldSaveDetailLog.setName("textFieldSaveDetailLog");
		textFieldSaveDetailLog.setText(System.getProperty("user.dir") + "\\log.html");
		textFieldSaveDetailLog.setToolTipText("Pfad des Detail-Logs");
		loadSavePanel.add(textFieldSaveDetailLog);
		
		JButton btnSaveDetailLog = new JButton(FileSystemView.getFileSystemView().getSystemIcon(new File(System.getProperty("user.dir"))));
		btnSaveDetailLog.setToolTipText("Datei auswählen");
		btnSaveDetailLog.setActionCommand(Statics.SAVE_LOG);
		btnSaveDetailLog.setName(Statics.SAVE_LOG);
		btnSaveDetailLog.addActionListener(new LoadSaveActionListener());
		loadSavePanel.add(btnSaveDetailLog);
		
		// ViewBlocker initialisieren
		this.viewBlocker = new JDialog(StatistischeGeheimhaltung.mainFrame, "", true);
		this.viewBlocker.getContentPane().setBackground(new Color(255, 255, 153));
		this.viewBlocker.setVisible(false);
		this.viewBlocker.setSize(new Dimension(300,50));
		
		this.progressBar = new JProgressBar(0, 0);
		this.viewBlocker.add(BorderLayout.CENTER, this.progressBar);
		this.viewBlockerText = new JLabel(" ");
		this.viewBlocker.add(BorderLayout.NORTH, viewBlockerText);
		this.viewBlocker.setUndecorated(true);
		this.viewBlocker.add(BorderLayout.SOUTH,new JLabel(" "));
		
	}
	
	/**
	 * Funktion zum Sperren der View und Anzeigen eines Ladebalkens, kommt bei nebenläufigen Funktionen wie Speichern oder Laden zum Einsatz
	 */
	public void showProgressBar(String actionText, int maxValues){
		//System.out.println("Text: " + actionText + ", maxValues: " + maxValues);
		this.progressBar.setMaximum(maxValues);
		this.progressBar.setValue(0);
		this.viewBlocker.setSize(new Dimension(300,50));
		
		this.viewBlocker.add(BorderLayout.CENTER, this.progressBar);
		this.viewBlockerText.setText(actionText);
		this.viewBlocker.setLocationRelativeTo(StatistischeGeheimhaltung.mainFrame);
		
		Thread t = new Thread(new Runnable() {
		      public void run() {
		    	  try{
		    	  viewBlocker.setVisible(true);
		    	  } catch (Exception e){
		    		  //nothing to do
		    	  }
		      }
		    });
		    t.start();
	}
	
	/**
	 * Funktion zum Setzen des Stati des Ladebalkens
	 */
	public void setProgressBar(int progress, String status){
		//System.out.println(" Text: " + status + ", progress: " + progress);
		this.progressBar.setValue(progress);
		this.viewBlockerText.setText(status);
	}

	
	/**
	 * Funktion zum beenden des Ladebalkens
	 */
	public void removeProgressBar(){
		//this.viewBlocker.setModal(false);
		this.viewBlocker.setVisible(false);
	}

	
	/** Funktion zum neuSetzen des Textes der Statusleiste
	 *  @param text - Der anzuzeigende Text
	 *  @param farbe - Die Hintergrundfarbe
	 *  @param maxSeconds - Die Anzeigedauer des Textes, 0 bedeutet: unbegrenzt, bleibt stehen bis es ueberschrieben wird
	 */
	
	public void onNewStatus(String text, Color farbe, int maxSeconds){
		
		final String myText = text;
		final int myMaxSeconds = maxSeconds;
		statusBar.setText(text);
		statusBar.setBackground(farbe);
		if (maxSeconds > 0){
		
			Thread thread = new Thread(){
				@Override
				public void run(){
					for (int i = 0; i <= myMaxSeconds; i++){
						try {
							this.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//Pruefen ob eigener Text nicht bereits ueberschrieben wurde
						if (!statusBar.getText().equals(myText)){
							break;
						}
						//Text wieder von der Statusleiste entfernen
						if (i == myMaxSeconds) {
							statusBar.setText("");
							statusBar.setBackground(null);
						}
						
					}
				}
				
			};
			
			thread.start();
		}
		statusBar.setText(text);
		statusBar.setBackground(farbe);
	}
	
	/**
	 * getter fuer den auesseren Splitpane
	 * @return
	 */
	public JSplitPane getOuterSplitPane(){
		return this.outerSplitPane;
	}

	/**
	 * getter fuer den inneren Splitpane
	 * @return
	 */
	public JSplitPane getInnerSplitPane(){
		return this.innerSplitPane;
	}

	/**
	 * @return the mainFrame
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * @param mainFrame the mainFrame to set
	 */
	public void setMainFrame(JFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	/**
	 * @return the statusBar
	 */
	public JTextField getStatusBar() {
		return statusBar;
	}

	/**
	 * @param statusBar the statusBar to set
	 */
	public void setStatusBar(JTextField statusBar) {
		this.statusBar = statusBar;
	}

	/**
	 * @return the statusBarColor
	 */
	public Color getStatusBarColor() {
		return statusBarColor;
	}

	/**
	 * @param statusBarColor the statusBarColor to set
	 */
	public void setStatusBarColor(Color statusBarColor) {
		this.statusBarColor = statusBarColor;
	}

	/**
	 * @return the transformedValuesPanel
	 */
	public JPanel getTransformedValuesPanel() {
		return transformedValuesPanel;
	}

	/**
	 * @param transformedValuesPanel the transformedValuesPanel to set
	 */
	public void setTransformedValuesPanel(JPanel transformedValuesPanel) {
		this.transformedValuesPanel = transformedValuesPanel;
	}

	/**
	 * @return the comboBox
	 */
	public JComboBox getComboBox() {
		return comboBox;
	}

	/**
	 * @param comboBox the comboBox to set
	 */
	public void setComboBox(JComboBox comboBox) {
		this.comboBox = comboBox;
	}

	/**
	 * @return the optionsPanels
	 */
	public JPanel getOptionsPanels() {
		return optionsPanels;
	}

	/**
	 * @param optionsPanels the optionsPanels to set
	 */
	public void setOptionsPanels(JPanel optionsPanels) {
		this.optionsPanels = optionsPanels;
	}

	/**
	 * @param outerSplitPane the outerSplitPane to set
	 */
	public void setOuterSplitPane(JSplitPane outerSplitPane) {
		this.outerSplitPane = outerSplitPane;
	}

	/**
	 * @param innerSplitPane the innerSplitPane to set
	 */
	public void setInnerSplitPane(JSplitPane innerSplitPane) {
		this.innerSplitPane = innerSplitPane;
	}

	/**
	 * @return the loadSavePanel
	 */
	public JPanel getLoadSavePanel() {
		return loadSavePanel;
	}

	/**
	 * @param loadSavePanel the loadSavePanel to set
	 */
	public void setLoadSavePanel(JPanel loadSavePanel) {
		this.loadSavePanel = loadSavePanel;
	}
	
	
	
	
}