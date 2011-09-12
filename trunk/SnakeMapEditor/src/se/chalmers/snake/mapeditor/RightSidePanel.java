package se.chalmers.snake.mapeditor;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import se.chalmers.snake.mapeditor.Square.Dir;

@SuppressWarnings("serial")
class RightSidePanel extends JPanel {
	
	RightSidePanel(SnakeMapEditor snakeMapEditor){
		createPanel(snakeMapEditor);
	}

	private void createPanel(final SnakeMapEditor snakeMapEditor) {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0; c.gridwidth = 10; c.anchor = GridBagConstraints.WEST;
		
		this.add(new JLabel("Draw direction:"), c);
		
		c.gridy++;
		final ButtonGroup directionGroup = new ButtonGroup();
		this.add(createDirectionPanel(snakeMapEditor, directionGroup), c);
		
		c.gridy++;
		this.add(new JLabel("Draw length:"), c);
		
		c.gridy++; c.gridwidth = 1;
		final JCheckBox lengthBox = new JCheckBox("Length:");
		this.add(lengthBox, c);
		c.gridx++;
		final SpinnerNumberModel spinner = new SpinnerNumberModel(2, 2, 1000, 1);
		this.add(new JSpinner(spinner), c);
		
		c.gridy++; c.gridx = 0; c.gridwidth = 10;
		JButton draw = new JButton("Draw");
		this.add(draw, c);
		
		draw.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {					
					@Override
					public void run() {
						if(lengthBox.isSelected())
							snakeMapEditor.draw(Dir.valueOf(directionGroup.getSelection().getActionCommand()), spinner.getNumber().intValue());
						else
							snakeMapEditor.draw(Dir.valueOf(directionGroup.getSelection().getActionCommand()));
					}
				});
			}
		});
	}

	private JPanel createDirectionPanel(SnakeMapEditor snakeMapEditor, ButtonGroup directionGroup) {
		JPanel panel = new JPanel(new GridLayout(3,3));
		for(Dir d : Dir.values()){
			JRadioButton r = new JRadioButton(d.toString());
			r.setActionCommand(d.toString());
			directionGroup.add(r);
			panel.add(r);
			if(d == Dir.W)
				panel.add(new JLabel());
		}
		return panel;
	}
}
