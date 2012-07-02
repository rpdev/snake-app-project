package mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.EnumMap;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import mapeditor.Frame.K;

@SuppressWarnings("serial")
class InformationPanel extends JPanel{
	
	private EnumMap<K, String> values = new EnumMap<>(K.class);

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
					values.put(k, Integer.toString(((Number) f.getValue()).intValue()));
				} else
					values.put(k, ftf.getText());
			}
		});
		return ftf;
	}
}
