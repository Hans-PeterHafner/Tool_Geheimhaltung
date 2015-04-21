package matrixManipulation.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import matrixManipulation.view.OptionChooserPanels;
import matrixManipulation.core.Statics;
/**
 * @author Ciprian Savu
 * Diese bestimmt, was angezeigt wird, wenn man die Überlagerungsoptionen umschaltet
 */
public class overlayOptionsActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		/** 
		 * Alpha und Beta vor dem Ändern der Optionen retten
		 */
		if (Statics.getView().getOptionsPanels() != null) {
			Object aObject = Statics.getView().getOptionsPanels().getComponent(Statics.getView().getOptionsPanels().getComponentCount() -1);
			System.out.println(aObject);
			if (aObject instanceof JScrollPane){
				JScrollPane jsp = (JScrollPane)aObject;
				JPanel outerPanel = (JPanel)jsp.getViewport().getView();
				for (Component c : outerPanel.getComponents()){
					// Panel?
					if (c instanceof JPanel){
						for (Component ic : ((JPanel) c).getComponents()){
							if (ic instanceof JTextField){
								if (ic.getName().equals("alphaValue")){
									double alphaValue = Double.parseDouble(((JTextField) ic).getText());
									Statics.alpha = alphaValue;
								} else if (ic.getName().equals("betaValue")){
									double betaValue = Double.parseDouble(((JTextField) ic).getText());
									Statics.beta = betaValue;
								}
							}
						} 
					}
				}
			}
		}
		
		
		/**
		 * Optionen ändern
		 */
		if (Statics.getView().getComboBox().getSelectedIndex() == 0){

			Statics.getView().getOptionsPanels().remove(Statics.getView().getOptionsPanels().getComponentCount() -1);
			OptionChooserPanels.optionsPane = null;
			OptionChooserPanels.optionsPane = OptionChooserPanels.getcyclicDiagonalOverlayOptions();
			Statics.getView().getOptionsPanels().add(OptionChooserPanels.optionsPane);
			Statics.getView().getOptionsPanels().revalidate();
			Statics.getView().getOptionsPanels().repaint();
		} else {

			Statics.getView().getOptionsPanels().remove(Statics.getView().getOptionsPanels().getComponentCount() -1);
			OptionChooserPanels.optionsPane = null;
			OptionChooserPanels.optionsPane = OptionChooserPanels.getPermutantOverlayOptions();
			Statics.getView().getOptionsPanels().add(OptionChooserPanels.optionsPane);
			Statics.getView().getOptionsPanels().revalidate();
			Statics.getView().getOptionsPanels().repaint();		}
	}

}
