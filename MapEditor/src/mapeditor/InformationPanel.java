package mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.HashMap;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
class InformationPanel extends JPanel{
	
	private HashMap<String, String> values = new HashMap<>();

	InformationPanel(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0; c.anchor = GridBagConstraints.WEST;
		add(new JLabel("Game Speed"), c);
		c.gridx++;
		add(setupFTF(new JFormattedTextField(NumberFormat.getIntegerInstance()), K.gamespeed), c);
		c.gridy++;
		c.gridx--;
		add(new JLabel("Growth Speed"), c);
		c.gridx++;
		add(setupFTF(new JFormattedTextField(NumberFormat.getIntegerInstance()), K.growthspeed), c);
		
		c.gridy = 0;
		c.gridx++;
		c.insets.left = 5;
		add(new JLabel("Difficuly"), c);
		c.gridy++;
		add(new JLabel("Level Goal"), c);
		c.insets.left = 0;
		c.gridx++;
		add(setupFTF(new JFormattedTextField(NumberFormat.getIntegerInstance()), K.difficuly), c); // diff
		c.gridy--;
		add(setupFTF(new JFormattedTextField(NumberFormat.getIntegerInstance()), K.levelgoal), c); // lvl goal
		
		c.gridx++;
		c.gridy = 0;
		c.insets.left = 5;
		add(new JLabel("Name"), c);
		c.gridy++;
		add(new JLabel("Description"), c);
		c.insets.left = 0;
		c.gridx++;
		add(new JTextField(10), c); // desc
		c.gridy--;
		add(new JTextField(10), c); // name
	}
	
	private enum K{
		name, 
		description, 
		difficuly, 
		gamespeed, 
		growthspeed, 
		levelgoal;
		
		@Override
		public String toString(){
			return super.toString().toLowerCase();
		}
	}
	
	private JFormattedTextField setupFTF(final JFormattedTextField ftf, final K k){
		ftf.setColumns(5);
		ftf.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				u();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				u();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				u();
			}
			
			private void u(){
				values.put(k.toString(), Integer.toString(((Number) ftf.getValue()).intValue()));
			}
		});
		return ftf;
	}
}
