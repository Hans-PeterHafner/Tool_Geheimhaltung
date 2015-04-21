package matrixManipulation.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import matrixManipulation.core.Overlays;
import matrixManipulation.core.ResultMatrixs;
import matrixManipulation.core.Statics;
import matrixManipulation.model.MatrixDataTableModel;




/**
 * @author Ciprian Savu
 * Diese Klasse implementiert einen ActionListener und startet die eigentlichen Overlays an
 * 
 */
public class overlayActionListener implements ActionListener{

	public static String actionCommand;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.actionCommand = arg0.getActionCommand();
		
		if (actionCommand.equals("startCyclicDiagonalOverlay") || actionCommand.equals("permutantOverlayOptions")){
			if (Statics.originalValues == null){
				Statics.getView().onNewStatus("Bitte zuerst eine .csv-Datei laden!", Color.red,5);
				return;
			}

		
		 SwingWorker<ResultMatrixs, Void> worker = new SwingWorker<ResultMatrixs, Void>() {
			 
			private JButton disabledButton; 
			private int nullHandling;
			private boolean allSteps;
		 
			 @Override
			public ResultMatrixs doInBackground(){
				boolean saveLog = false;
				String logPath = "";
				double alphaValue = 1.0, betaValue = 1.0;
				disabledButton = null;
				int repeatCount = 0;
				nullHandling = 0;
				allSteps = false;
				
				//Ermitteln ob der Detail-Log gespeichert werden soll 
				for (Component c : Statics.getView().getLoadSavePanel().getComponents()){
					if (c instanceof JCheckBox){
						saveLog = ((JCheckBox) c).isSelected();
					} else if (c instanceof JTextField){
						logPath = ((JTextField) c).getText();
					}
				}
				
				Object _o = Statics.getView().getOptionsPanels().getComponent(Statics.getView().getOptionsPanels().getComponentCount() -1);
				if (_o instanceof JScrollPane){
					try{
						
						//Lese Werte auf JSrollpane und darunterliegende JPanels
						JScrollPane jsp = (JScrollPane)_o;
						JPanel outerPanel = (JPanel)jsp.getViewport().getView();

						//double alphaValue = 0.0, betaValue = 0.0, alphaEnd = null, betaEnd = null;
						for (Component c : outerPanel.getComponents()){
							
							// Panel?
							if (c instanceof JPanel){
								for (Component ic : ((JPanel) c).getComponents()){
									// Kombobox?
									if (ic instanceof JComboBox) {
										JComboBox jcb = (JComboBox)ic;
										// Null-Wert-Kombobox?
										if (jcb.getName().equals("nullValueHandle")){
											nullHandling = jcb.getSelectedIndex();
										} else if(jcb.getName().equals("allSteps")){
											if (jcb.getSelectedItem().equals("alle")){
												allSteps = true;
											}
										}
										// Textfeld?
									} else if (ic instanceof JTextField){
										if (ic.getName().equals("alphaValue")){
											alphaValue = Double.parseDouble(((JTextField) ic).getText());
											Statics.alpha = alphaValue;
										} else if (ic.getName().equals("betaValue")){
											betaValue = Double.parseDouble(((JTextField) ic).getText());
											Statics.beta = betaValue;
										} else if (ic.getName().equals("repeatCount")){
											repeatCount = Integer.parseInt(((JTextField) ic).getText());
											if (repeatCount < 1) {
												repeatCount = 1;
											} 
										}
										// Button?
									} else if (ic instanceof JButton){
										disabledButton = (JButton)ic;
										disabledButton.setEnabled(false);
									}
									
								} // ende inneres for
							} 
						} // ende aeusseres for
					} catch (Exception e){
						e.printStackTrace();
					}
				}
				
				if ((alphaValue + betaValue) > 1.0000){
					Statics.getView().onNewStatus("alpha + beta duerfen zusammen höchsten 1.0 ergeben. abbruch", Color.red, 5);
					if (disabledButton != null) disabledButton.setEnabled(true);
					return null;
				}
				Statics.lastNullHandling = nullHandling;
				
				Statics.getView().onNewStatus("Werte gesammelt, beginne Berechnung", Color.green, 5);
				
				ResultMatrixs objects = null;

				Statics.getView().showProgressBar("Berechnungsvorgang: 0%", 100);
				
				if (overlayActionListener.actionCommand.equals("startCyclicDiagonalOverlay")){
					objects = Overlays.cyclicDiagonalOverlay(Statics.originalValues, repeatCount, nullHandling, allSteps, alphaValue, betaValue, saveLog, logPath );
				} else if(overlayActionListener.actionCommand.equals("permutantOverlayOptions")){
					objects = Overlays.permutantOverlay(Statics.originalValues, repeatCount, nullHandling, allSteps, alphaValue, betaValue, saveLog, logPath );
				}
				
				Statics.getView().removeProgressBar();
				return objects;
			}
			//		HIER
					

			 @Override
			public void done() {

				ResultMatrixs objects = null;
				try {
					objects = get();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					objects = null;
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					objects = null;
				}
				
				if (objects != null){
				
					Statics.getView().onNewStatus("Berechnung fertiggestellt", Color.green, 5);
					Statics.resultObjects = objects;
					if (objects != null){
						for (Component c : Statics.getView().getLoadSavePanel().getComponents()){
							if (c.getName().equals("btnErgebnisseSpeichern")){
								c.setEnabled(true);
								break;
							}
						}
					}
					disabledButton.setEnabled(true);
					
					// Ausgabe
					
					int nullCount = objects.getNullCount();
					Collection<int[][]> colMatrixNullIgnore = objects.getColMatrixNullIgnore();
					Collection<int[][]> colMatrixWithNegativeValues = objects.getColMatrixWithNegativeValues();
					
					// Alle Schritte anzeigen?
					
					// zyklen mit 0 ueberspringen
					if (nullHandling == 0){
						// Nur End-Ergebnis anzeigen
						if (!allSteps){
							JTable changedDataTable = new JTable();
							MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel((int[][])colMatrixNullIgnore.toArray()[colMatrixNullIgnore.size()-1]);
			    			changedDataTable.setModel(changedDataTableModel);
		
			    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    			int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(changedDataScrollPane);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						} else {
							// Alle Schleifendurchlaeufe anzeigen
							JTabbedPane tabs = new JTabbedPane();
							int pos = 1;
							for (int[][] e: colMatrixNullIgnore){
								JTable changedDataTable = new JTable();
								MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel(e);
				    			changedDataTable.setModel(changedDataTableModel);
				    			
				    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				    			
								tabs.addTab("<html>&Uuml;</hml>" + pos++, changedDataScrollPane);
							}
							int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(tabs);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						}
						
					} else if (nullHandling == 1){
						// Zyklen mit 0 nicht ueberspringen
						// Nur End-Ergebnis anzeigen
						if (!allSteps){
							JTable changedDataTable = new JTable();
							int[][] erg = (int[][]) colMatrixWithNegativeValues.toArray()[colMatrixWithNegativeValues.size()-1];
			    			MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel(erg);
			    			changedDataTable.setModel(changedDataTableModel);
		
			    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    			int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(changedDataScrollPane);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						} else {
							// Alle Schleifendurchlaeufe anzeigen
							JTabbedPane tabs = new JTabbedPane();
							int pos = 1;
							for (int[][] e: colMatrixWithNegativeValues){
								JTable changedDataTable = new JTable();
								MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel(e);
				    			changedDataTable.setModel(changedDataTableModel);
				    			
				    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				    			
								tabs.addTab("#" + pos++, changedDataScrollPane);
							}
							int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(tabs);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						}
	
					} else {
						//Beide Verfahren
						// Nur End-Ergebnis anzeigen
						if (!allSteps){
	
							JTabbedPane tabs = new JTabbedPane();
							int pos = 1;
							// zuerst die Werte mit Uebersprungenen 0en
							JTable changedDataTable = new JTable();
							MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel((int[][])colMatrixNullIgnore.toArray()[colMatrixNullIgnore.size()-1]);
			    			changedDataTable.setModel(changedDataTableModel);
			    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    			
							tabs.addTab("<html>&Uuml;" + pos + "</html>", changedDataScrollPane);
							
							// nun die Werte ohne uebersprungenen 0en 
							changedDataTable = new JTable();
							changedDataTableModel = new MatrixDataTableModel((int[][]) (colMatrixWithNegativeValues.toArray()[colMatrixWithNegativeValues.size() -1]));
							changedDataTable.setModel(changedDataTableModel);
							changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							tabs.addTab("#" + pos++ + "", changedDataScrollPane);
			    			
			    			int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(tabs);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						} else {
							// Alle Schleifendurchlaeufe anzeigen
							JTabbedPane tabs = new JTabbedPane();
							int pos = 1;
							for (int[][] e: colMatrixNullIgnore){
								// zuerst die Werte mit Uebersprungenen 0en
								JTable changedDataTable = new JTable();
								MatrixDataTableModel changedDataTableModel = new MatrixDataTableModel(e);
				    			changedDataTable.setModel(changedDataTableModel);
				    			JScrollPane changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				    			changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				    			
								tabs.addTab("<html>&Uuml;" + pos + "</html>", changedDataScrollPane);
								
								// nun die Werte ohne uebersprungenen 0en 
								changedDataTable = new JTable();
								changedDataTableModel = new MatrixDataTableModel((int[][]) (colMatrixWithNegativeValues.toArray()[pos -1]));
								changedDataTable.setModel(changedDataTableModel);
								changedDataScrollPane = new JScrollPane(changedDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
								changedDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
								tabs.addTab("#" + pos++ + "", changedDataScrollPane);
							}
							int dividerLocation = Statics.getView().getInnerSplitPane().getDividerLocation();
			    			Statics.getView().getInnerSplitPane().setRightComponent(tabs);
			    			Statics.getView().getInnerSplitPane().setDividerLocation(dividerLocation);
						}
					}
				} else {
					//Berechnungsfehler
				}
			} 
		};
			worker.execute();
		}
		
	}

}
