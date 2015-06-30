package Startup;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import matrixManipulation.view.View;
import de.htw.pim.sg.gui.MainLauncher;
import de.htw.sg.safe.gui.SafeClusterPanel;

public class StatistischeGeheimhaltung {

	public static JFrame mainFrame = new JFrame();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mainFrame = new JFrame();
		//mainFrame.setContentPane(this);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(900, 600));
		mainFrame.setResizable(true);
		mainFrame.pack();
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		mainFrame.setTitle("Statistische Geheimhaltung");
		
		/* Setze Systemeigenes Look n Feel (damit die Anwendung unter Windwos aussieht wie eine typische 
		 * Windows Anwendung, unter Linux wie eine typische Linux Anwendung, etc...)
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// Frames fuer Implementierungsabhaengige Verfahre erschaffen
		
		JPanel micro = new JPanel();
		JPanel matrix = new JPanel();
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Microdaten", micro);
		tabs.addTab("Matrixmanipulation", matrix);
		tabs.addTab("Safe-Clustering", new SafeClusterPanel(mainFrame));
		
		mainFrame.add(tabs);
		mainFrame.setVisible(true);
		

		new MainLauncher(micro);
		new View(matrix);
	}

}
