package mapeditor;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel {

	OptionPanel(Frame frame){
		setupPanel(frame);
	}

	private void setupPanel(final Frame frame) {
		setLayout(new GridLayout(0, 1,0,0));
		add(new JLabel("Map Size", JLabel.CENTER));
		JPanel x = new JPanel(new FlowLayout());
		x.add(new JLabel("X:"));
		final JFormattedTextField xx;
		x.add(xx = new JFormattedTextField(NumberFormat.getIntegerInstance()));
		xx.setValue(200);
		xx.setColumns(5);
		add(x);
		JPanel y = new JPanel(new FlowLayout());
		y.add(new JLabel("Y:"));
		final JFormattedTextField yy;
		y.add(yy = new JFormattedTextField(NumberFormat.getIntegerInstance()));
		yy.setValue(200);
		yy.setColumns(5);
		add(y);
		JButton b = new JButton("GENERATE");
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.generate(((Number) xx.getValue()).intValue(),((Number)  yy.getValue()).intValue());
			};
		});
		add(b);
		
		JPanel s = new JPanel(new FlowLayout());
		final JLabel value = new JLabel("10");
		s.add(new JLabel("DIAMETER:"));
		final JSlider slid = new JSlider(10, 200, 10);
		slid.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent ce) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						value.setText(Integer.toString(slid.getValue()));
						frame.setRadius(slid.getValue());
					}
				});
			}
		});
		s.add(slid);
		s.add(value);
		add(s);
	}
}
