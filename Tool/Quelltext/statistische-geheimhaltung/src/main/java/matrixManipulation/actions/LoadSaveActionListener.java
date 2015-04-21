package matrixManipulation.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableRow;

import matrixManipulation.model.MatrixDataTableModel;

import matrixManipulation.backend.CsvFileReader;
import matrixManipulation.backend.CsvFileWriter;

import matrixManipulation.core.MatrixManipulation;
import matrixManipulation.core.Statics;

/** @author Ciprian Savu
 * Action-Listener Klasse fuer das Laden und Speichern von .csv Dateien
 */
public class LoadSaveActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals(Statics.LOAD_FILE)){
			
	        final JFileChooser fileChooser = new JFileChooser("Verzeichnis wählen");
	        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        final File file = new File(Statics.filePath);

	        fileChooser.setCurrentDirectory(file);
	        fileChooser.setFileFilter(new csvFileFilter());

	        int returnVal = fileChooser.showOpenDialog(Statics.getView());
	        Statics.filePath = fileChooser.getCurrentDirectory().toString();
	        
	        if (returnVal == 0 && fileChooser.getSelectedFile() != null){
	        	
	        	Statics.getView().onNewStatus("CSV Datei " + fileChooser.getSelectedFile().toString() + " erfolgreich geladen!", Color.GREEN, 5);
	        	//System.out.println(fileChooser.getSelectedFile().toString());
	        	CsvFileReader myFile = new CsvFileReader(fileChooser.getSelectedFile().getAbsolutePath());
	    		int[][] myValues = myFile.parse();
	    		if (myValues != null){
	    			JTable origDataTable = new JTable();
	    			MatrixDataTableModel origDataTableModel = new MatrixDataTableModel(myValues);
	    			origDataTable.setModel(origDataTableModel);

	    			JScrollPane origDataScrollPane = new JScrollPane(origDataTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    			//origDataScrollPane.setMinimumSize(new Dimension(300,300));
	    			origDataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    			int dividerLocation = Statics.getView().getOuterSplitPane().getDividerLocation();
	    			Statics.getView().getOuterSplitPane().setLeftComponent(origDataScrollPane);
	    			Statics.getView().getOuterSplitPane().setDividerLocation(dividerLocation);
	    			Statics.originalValues = myValues;
	    			
	    			for (Component c : Statics.getView().getLoadSavePanel().getComponents()){
		        		if (c instanceof JLabel){
			        		if (c.getName().equals("fileNameText")){
			        			((JLabel)c).setText(" Originaldatei: ");
			        		}
			        	} else if(c instanceof JTextField){
			        		if (c.getName().equals("fileNameTextField")){
			        			((JTextField)c).setText(fileChooser.getSelectedFile().getAbsolutePath());
			        			((JTextField)c).setVisible(true);
			        		}
			        		
			        	}
		        	}
	    			
	    		} else {
	    			//System.out.println("Fehler beim Einlesen der .csv Datei");
	    			Statics.getView().onNewStatus("Fehler beim Einlesen der .csv Datei", Color.red, 5);
	    			
	    		}
	        	
	        } else {
	        	
	        	return;
	        }
	        
		} else if (arg0.getActionCommand().equals(Statics.SAVE_FILE)){
			if (Statics.lastNullHandling == -1){
				return;
			}
			boolean withNullMode = false;
			
			//Falls beide Ergebnisse zur Verfuegung stehen, nachfragen welche gespeichert werden sollen
			if (Statics.lastNullHandling == 2){
				Object[] options = {"Ohne 0",
	                    "mit 0",
	                    "Abbrechen"};
				int n = JOptionPane.showOptionDialog(Statics.getView(),
					    "<html>Welche Ergebnisse m&ouml;chten Sie speichern?",
					    "Ergebnisse speichern",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[2]);
				if (n == 0){
					withNullMode = false;
				} else if (n == 1){
					withNullMode = true;
				} else {
					return;
				}
			} else if (Statics.lastNullHandling == 1){
				withNullMode = true;
			} else {
				withNullMode = false;
			}
			
			final JFileChooser fileChooser = new JFileChooser("Datei wählen");
	        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        final File file = new File(Statics.filePath);

	        fileChooser.setCurrentDirectory(file);
	        fileChooser.setFileFilter(new csvFileFilter());

	        int returnVal = fileChooser.showSaveDialog(Statics.getView());
	        Statics.filePath = fileChooser.getCurrentDirectory().toString();
	        // Wurde speichern geklickt
	        if (returnVal == 0){
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	// .csv ergaenzen?
	        	if (!fileName.endsWith(".csv")){
	        		fileName = fileName.concat(".csv");
	        	}
	        	
		        //Existiert die Datei bereits?
	        	if (new File(fileName).exists()){
	        		int overwrite = JOptionPane.showConfirmDialog(Statics.getView(),
	        			    "<html>Die Datei<br>" + fileName + "<br>existiert bereits<br>"
	        			    +"m&ouml;chten Sie sie &uuml;berschreiben?</html>",
	        			    "Datei existiert bereits",
	        			    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	        		if (overwrite == 1) {
	        			Statics.getView().onNewStatus("Speichern abgebrochen", Color.ORANGE, 5);
	        			return;
	        		}
	        	}
	        	
	        	//Speichern
	        	boolean success = false;
	        	
	        	SaveThreadRunner saveThreadRunner = new SaveThreadRunner(withNullMode, success, fileName);
	        	Thread saveThread = new Thread(saveThreadRunner);
	        	saveThread.start();

	        	
	        }
	        
			
		} else if (arg0.getActionCommand().equals(Statics.SAVE_LOG)){
			final JFileChooser fileChooser = new JFileChooser("Datei wählen");
	        fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
	        fileChooser.setApproveButtonText("Datei waehlen");
	        
	        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        final File file = new File(Statics.filePath);
	        
	        fileChooser.setCurrentDirectory(file);
	        fileChooser.setFileFilter(new htmlFileFilter());
	        fileChooser.repaint();
	        int returnVal = fileChooser.showSaveDialog(Statics.getView());
	        Statics.filePath = fileChooser.getCurrentDirectory().toString();
	        // Wurde speichern geklickt
	        if (returnVal == 0){
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	// .html ergaenzen?
	        	if (!fileName.endsWith(".html")){
	        		fileName = fileName.concat(".html");
	        	}
	        	
	        	for (Component c : Statics.getView().getLoadSavePanel().getComponents()){
	        		if (c instanceof JTextField){
		        		if (c.getName().equals("textFieldSaveDetailLog")){
		        			((JTextField)c).setText(fileName);
		        		}
		        	} else if(c instanceof JCheckBox){
		        		((JCheckBox) c).setSelected(true);
		        	}
	        	}
	        }
		}
	}
	
	

	/** 
	 * @author Ciprian Savu
	 * Interne Klasse, entscheidet welche Dateien im Datei-Auswahl-Dialog angezeigt werden sollen
	 *
	 */
	private class csvFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			return (f.isDirectory() || f.getName().toLowerCase().endsWith(".csv"));
		}

		@Override
		public String getDescription() {
			return ".csv - Dateien";
		}
		
	}
	
	/** 
	 * @author Ciprian Savu
	 * Interne Klasse, entscheidet welche Dateien im Datei-Auswahl-Dialog angezeigt werden sollen
	 *
	 */
	private class htmlFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			return (f.isDirectory() || f.getName().toLowerCase().endsWith(".html"));
		}

		@Override
		public String getDescription() {
			return ".html - Dateien";
		}
		
	}
	
	/**
	 * 
	 * @author Ciprian Savu
	 * Führt das eigentliche Speichern in einem Thread aus, damit die GUI währenddessen Events verarbeiten kann
	 *
	 */
	private class SaveThreadRunner implements Runnable{

		  private boolean withNullMode = false;
		  private boolean success = false; 
		  private String fileName = null;

		  public SaveThreadRunner(boolean withNullMode, boolean success, String fileName){
			this.withNullMode = withNullMode;
			this.success = success;
			this.fileName = fileName;
		  }

		  public void run(){
			  
			  Statics.getView().showProgressBar("Speichervorgang: 0 %", Statics.resultObjects.getColMatrixNullIgnore().lastElement().length);
			  //long start = System.currentTimeMillis();
				
			  if (!withNullMode){
				  success = CsvFileWriter.save(fileName,Statics.resultObjects.getColMatrixNullIgnore().lastElement());
			  } else {
				  success = CsvFileWriter.save(fileName,Statics.resultObjects.getColMatrixWithNegativeValues().lastElement());
			  }
			  //long end = System.currentTimeMillis();
			  //System.out.println(end-start);
				
			  Statics.getView().removeProgressBar();
			  if (success){
				  Statics.getView().onNewStatus("Erfolgreich gespeichert", Color.GREEN, 5);
			  } else {
				  Statics.getView().onNewStatus("Fehler beim Speichern der Datei", Color.GREEN, 5);
			  }
		  }

		}
	
}
