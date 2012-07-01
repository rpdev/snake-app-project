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
		add(setupFTF(new JTextField(), K.description), c); // desc
		c.gridy--;
		add(setupFTF(new JTextField(), K.name), c); // name
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
	
	private JTextField setupFTF(final JTextField ftf, final K k){
		ftf.setColumns((ftf instanceof JTextField) ? 10 : 5);
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
				if(ftf instanceof JFormattedTextField){
					JFormattedTextField f = (JFormattedTextField) ftf;
					values.put(k.toString(), Integer.toString(((Number) f.getValue()).intValue()));
				} else
					values.put(k.toString(), ftf.getText());
			}
		});
		return ftf;
	}
}
