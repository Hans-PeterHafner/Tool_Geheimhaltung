/**
 * @author Ciprian Savu
 * Diese Abstrakte Klassen enthält Funktionen, welche GUI-Elemente zurückliefern
 */
package matrixManipulation.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import matrixManipulation.actions.overlayActionListener;
import matrixManipulation.core.Statics;


/**
 * @author Chip
 *
 */
public abstract class OptionChooserPanels {

	// Das momentan gesetzen Optionen Panel
	public static JScrollPane optionsPane = null;
	
	/** Zeichnet das Options-Panel fuer die diagonale Ueberlagerung
	 * 
	 * @return
	 */
	public static JScrollPane getcyclicDiagonalOverlayOptions(){
		
		JPanel panel = new JPanel();
		panel.setName("cyclicDiagonalOverlayOptions");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		//panel.setAlignmentX(1);
		
		// Anzahl der Wiederholungen
		JPanel repeatCountPanel = new JPanel();
		repeatCountPanel.setLayout(new BoxLayout(repeatCountPanel, BoxLayout.LINE_AXIS));
		repeatCountPanel.add(new JLabel("Wiederholungen: "));
		JTextField repeatCount = new JTextField("1");
		repeatCount.setColumns(3);
		repeatCount.setMaximumSize(new Dimension(20,20));
		repeatCount.setName("repeatCount");
		repeatCountPanel.add(repeatCount);
		repeatCountPanel.add(Box.createHorizontalGlue());
		panel.add(repeatCountPanel);
		
		panel.add(new JLabel(" "));
		
		
		// Ausgabe des Hinweises fuer alpha und beta
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
		textPanel.add(new JLabel("<html>&#945;,&#946; zwischen 0 und 1</html>"));
		textPanel.add(Box.createHorizontalGlue());
		panel.add(textPanel);
		
		// Panel fuer die Eingabe des ALPHA-Wertes
		JPanel alphaPanel = new JPanel();
		alphaPanel.setLayout(new BoxLayout(alphaPanel, BoxLayout.LINE_AXIS));
		//alphaPanel.setMaximumSize(new Dimension(32767, 30));
		alphaPanel.add(new JLabel("alpha = "));
		
		JTextField alphaValue = new JTextField(String.valueOf(Statics.alpha));
		alphaValue.setColumns(3);
		alphaValue.setMaximumSize(new Dimension(20,20));
		alphaValue.setName("alphaValue");
		alphaPanel.add(alphaValue);
		
		alphaPanel.add(Box.createHorizontalGlue());
		panel.add(alphaPanel);

			
		// Panel fuer die Eingabe des BETA-Wertes
		JPanel betaPanel = new JPanel();
		betaPanel.setLayout(new BoxLayout(betaPanel, BoxLayout.LINE_AXIS));
		betaPanel.add(new JLabel("beta  = "));
		
		JTextField betaValue = new JTextField(String.valueOf(Statics.beta));
		betaValue.setColumns(3);
		betaValue.setMaximumSize(new Dimension(20,20));
		betaValue.setName("betaValue");
		betaPanel.add(betaValue);
		betaPanel.add(Box.createHorizontalGlue());

		panel.add(betaPanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer 0-Wert - Behandlung
		JPanel nullValueTextPanel = new JPanel();
		nullValueTextPanel.setLayout(new BoxLayout(nullValueTextPanel, BoxLayout.LINE_AXIS));
		nullValueTextPanel.add(new JLabel("0-Wert Behandlung"));
		nullValueTextPanel.add(Box.createHorizontalGlue());
		panel.add(nullValueTextPanel);
		
		//Panel fuer 0-Wert-DropDown
		JPanel nullValueHandlePanel = new JPanel();
		nullValueHandlePanel.setLayout(new BoxLayout(nullValueHandlePanel, BoxLayout.LINE_AXIS));
		JComboBox nullValueHandle = new JComboBox();
		nullValueHandle.setModel(new DefaultComboBoxModel(new String[] {"<html>Zyklen mit 0 &uuml;berspringen</html>", "<html>Zyklen mit 0 nicht &uuml;berspringen</html>", "Beide Verfahren"}));
		nullValueHandle.setMaximumSize(new Dimension(100,20));
		nullValueHandle.setName("nullValueHandle");
		nullValueHandlePanel.add(nullValueHandle);
		nullValueHandlePanel.add(Box.createHorizontalGlue());
		panel.add(nullValueHandlePanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer Zwischenschritte
		JPanel allStepsTextPanel = new JPanel();
		allStepsTextPanel.setLayout(new BoxLayout(allStepsTextPanel, BoxLayout.LINE_AXIS));
		allStepsTextPanel.add(new JLabel("Zwischenschritte anzeigen?"));
		allStepsTextPanel.add(Box.createHorizontalGlue());
		panel.add(allStepsTextPanel);
		
		JPanel allStepsPanel = new JPanel();
		allStepsPanel.setLayout(new BoxLayout(allStepsPanel, BoxLayout.LINE_AXIS));
		JComboBox allSteps = new JComboBox();
		allSteps.setModel(new DefaultComboBoxModel(new String[] {"alle", "nur End-Ergebnis", }));
		allSteps.setName("allSteps");
		allSteps.setMaximumSize(new Dimension(100,20));
		allStepsPanel.add(allSteps);
		allStepsPanel.add(Box.createHorizontalGlue());
		panel.add(allStepsPanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer JButton
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
		JButton btnStartCalculation = new JButton("Berechnung starten");
		btnStartCalculation.setActionCommand("startCyclicDiagonalOverlay");
		
		btnStartCalculation.addActionListener(new overlayActionListener());

		btnPanel.add(btnStartCalculation);
		//btnPanel.add(Box.createHorizontalGlue());
		panel.add(btnPanel);
		
		JScrollPane pane = new JScrollPane(panel);
		pane.setMaximumSize(new Dimension(30000,230));
		
		return pane;
		
		
	}
	
	
	/** Zeichnet das Options-Panel fuer die zufaellige Ueberlagerung
	 * 
	 * @return
	 */
	public static JScrollPane getPermutantOverlayOptions(){
		
		JPanel panel = new JPanel();
		panel.setName("permutantOverlayOptions");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		//panel.setAlignmentX(1);
		
		// Anzahl der Wiederholungen
		JPanel repeatCountPanel = new JPanel();
		repeatCountPanel.setLayout(new BoxLayout(repeatCountPanel, BoxLayout.LINE_AXIS));
		repeatCountPanel.add(new JLabel("Wiederholungen: "));
		JTextField repeatCount = new JTextField("1");
		repeatCount.setColumns(3);
		repeatCount.setMaximumSize(new Dimension(20,20));
		repeatCount.setName("repeatCount");
		repeatCountPanel.add(repeatCount);
		repeatCountPanel.add(Box.createHorizontalGlue());
		panel.add(repeatCountPanel);
		
		panel.add(new JLabel(" "));
		
		
		// Ausgabe des Hinweises fuer alpha und beta
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
		textPanel.add(new JLabel("<html>&#945;,&#946; zwischen 0 und 1</html>"));
		textPanel.add(Box.createHorizontalGlue());
		panel.add(textPanel);
		
		// Panel fuer die Eingabe des ALPHA-Wertes
		JPanel alphaPanel = new JPanel();
		alphaPanel.setLayout(new BoxLayout(alphaPanel, BoxLayout.LINE_AXIS));
		//alphaPanel.setMaximumSize(new Dimension(32767, 30));
		alphaPanel.add(new JLabel("alpha = "));
		
		JTextField alphaValue = new JTextField(String.valueOf(Statics.alpha));
		alphaValue.setColumns(3);
		alphaValue.setMaximumSize(new Dimension(20,20));
		alphaValue.setName("alphaValue");
		alphaPanel.add(alphaValue);
		
		alphaPanel.add(Box.createHorizontalGlue());
		panel.add(alphaPanel);

			
		// Panel fuer die Eingabe des BETA-Wertes
		JPanel betaPanel = new JPanel();
		betaPanel.setLayout(new BoxLayout(betaPanel, BoxLayout.LINE_AXIS));
		betaPanel.add(new JLabel("beta  = "));
		
		JTextField betaValue = new JTextField(String.valueOf(Statics.beta));
		betaValue.setColumns(3);
		betaValue.setMaximumSize(new Dimension(20,20));
		betaValue.setName("betaValue");
		betaPanel.add(betaValue);
		betaPanel.add(Box.createHorizontalGlue());

		panel.add(betaPanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer 0-Wert - Behandlung
		JPanel nullValueTextPanel = new JPanel();
		nullValueTextPanel.setLayout(new BoxLayout(nullValueTextPanel, BoxLayout.LINE_AXIS));
		nullValueTextPanel.add(new JLabel("0-Wert Behandlung"));
		nullValueTextPanel.add(Box.createHorizontalGlue());
		panel.add(nullValueTextPanel);
		
		//Panel fuer 0-Wert-DropDown
		JPanel nullValueHandlePanel = new JPanel();
		nullValueHandlePanel.setLayout(new BoxLayout(nullValueHandlePanel, BoxLayout.LINE_AXIS));
		JComboBox nullValueHandle = new JComboBox();
		nullValueHandle.setModel(new DefaultComboBoxModel(new String[] {"<html>Perm. mit 0 &uuml;berspringen</html>", "<html>Perm. mit 0 nicht &uuml;berspringen</html>", "Beide Verfahren"}));
		nullValueHandle.setMaximumSize(new Dimension(100,20));
		nullValueHandle.setName("nullValueHandle");
		nullValueHandlePanel.add(nullValueHandle);
		nullValueHandlePanel.add(Box.createHorizontalGlue());
		panel.add(nullValueHandlePanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer Zwischenschritte
		JPanel allStepsTextPanel = new JPanel();
		allStepsTextPanel.setLayout(new BoxLayout(allStepsTextPanel, BoxLayout.LINE_AXIS));
		allStepsTextPanel.add(new JLabel("Zwischenschritte anzeigen?"));
		allStepsTextPanel.add(Box.createHorizontalGlue());
		panel.add(allStepsTextPanel);
		
		JPanel allStepsPanel = new JPanel();
		allStepsPanel.setLayout(new BoxLayout(allStepsPanel, BoxLayout.LINE_AXIS));
		JComboBox allSteps = new JComboBox();
		allSteps.setModel(new DefaultComboBoxModel(new String[] {"alle", "nur End-Ergebnis", }));
		allSteps.setName("allSteps");
		allSteps.setMaximumSize(new Dimension(100,20));
		allStepsPanel.add(allSteps);
		allStepsPanel.add(Box.createHorizontalGlue());
		panel.add(allStepsPanel);
		
		panel.add(new JLabel(" "));
		
		// Panel fuer JButton
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
		JButton btnStartCalculation = new JButton("Berechnung starten");
		btnStartCalculation.setActionCommand("permutantOverlayOptions");
		btnStartCalculation.addActionListener(new overlayActionListener());
		btnPanel.add(btnStartCalculation);
		//btnPanel.add(Box.createHorizontalGlue());
		panel.add(btnPanel);
		
		JScrollPane pane = new JScrollPane(panel);
		pane.setMaximumSize(new Dimension(30000,230));
		
		return pane;
		
		
	}
	
}
