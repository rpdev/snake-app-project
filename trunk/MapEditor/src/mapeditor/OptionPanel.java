package mapeditor;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
class OptionPanel extends JPanel {

	OptionPanel(Frame frame){
		setupPanel(frame);
	}

	private void setupPanel(final Frame frame) {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		add(new JLabel("Map Size", JLabel.CENTER), c);
		JPanel p = new JPanel(new FlowLayout());
		p.add(new JLabel("X:"));
		final JFormattedTextField xx;
		p.add(xx = new JFormattedTextField(NumberFormat.getIntegerInstance()));
		xx.setValue(200);
		xx.setColumns(5);
		c.gridy++;
		add(p, c);
		p = new JPanel(new FlowLayout());
		p.add(new JLabel("Y:"));
		final JFormattedTextField yy;
		p.add(yy = new JFormattedTextField(NumberFormat.getIntegerInstance()));
		yy.setValue(200);
		yy.setColumns(5);
		c.gridy++;
		add(p, c);
		JButton b = new JButton("GENERATE");
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.generate(((Number) xx.getValue()).intValue(),((Number)  yy.getValue()).intValue());
			};
		});
		c.gridy++;
		add(b, c);
		
		p = new JPanel(new FlowLayout());
		p.add(new JLabel("DIAMETER:"));
		final JSlider slid = new JSlider(10, 200, 10);
		slid.setToolTipText(Integer.toString(10));
		slid.setPaintLabels(true);
		slid.setPaintTicks(true);
		slid.setPaintTrack(true);
		slid.setMinorTickSpacing(10);
		slid.setMajorTickSpacing(40);
		slid.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent ce) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						slid.setToolTipText(Long.toString(slid.getValue()));
						frame.setRadius(slid.getValue());
					}
				});
			}
		});
		p.add(slid);
		c.gridy++;
		add(p, c);
		
		c.gridy++;
		final JCheckBox player = new JCheckBox("Place snake");
		player.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				frame.setSnakeActive(player.isSelected());
			}
		});
		final SpinnerNumberModel spinNum = new SpinnerNumberModel(4, 1, 10, 1);
		JSpinner segments = new JSpinner(spinNum);
		segments.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				frame.setSnakeSegments(spinNum.getNumber().intValue());
			}
		});
		final JSlider rot = new JSlider(0, 360, 0);
		rot.setToolTipText(Integer.toString(0));
		rot.setPaintTicks(true);
		rot.setPaintLabels(true);
		rot.setPaintTrack(true);
		rot.setMinorTickSpacing(10);
		rot.setMajorTickSpacing(60);
		rot.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				rot.setToolTipText(Integer.toString(rot.getValue()));
				frame.setRotation(rot.getValue());
			}
		});
		add(new JLabel("PLAYER", JLabel.CENTER), c);
		c.gridy++;
		add(player, c);
		c.gridy++;
		p = new JPanel(new FlowLayout());
		p.add(new JLabel("Segments"));
		p.add(segments);
		add(p, c);
		c.gridy++;
		p = new JPanel(new FlowLayout());
		p.add(new JLabel("Rotation"));
		p.add(rot);
		add(p, c);
	}
}
