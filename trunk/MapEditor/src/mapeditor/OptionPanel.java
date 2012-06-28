package mapeditor;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel {

	OptionPanel(){
		setupPanel();
	}

	private void setupPanel() {
		setLayout(new GridLayout(0, 1,0,0));
		add(new JLabel("Map Size", JLabel.CENTER));
		JPanel x = new JPanel(new FlowLayout());
		x.add(new JLabel("X:"));
		x.add(new JTextField(10));
		add(x);
		JPanel y = new JPanel(new FlowLayout());
		y.add(new JLabel("Y:"));
		y.add(new JTextField(10));
		add(y);
		add(new JButton("GENERATE"));
		
		JPanel s = new JPanel(new FlowLayout());
		final JLabel value = new JLabel("5");
		s.add(new JLabel("RADIUS:"));
		final JSlider slid = new JSlider(1, 100, 5);
		slid.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent ce) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						value.setText(Integer.toString(slid.getValue()));
					}
				});
			}
		});
		s.add(slid);
		s.add(value);
		add(s);
	}
}
