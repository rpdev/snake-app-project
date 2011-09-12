package se.chalmers.snake.mapeditor;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		
		Insets space = new Insets(15, 0, 0, 0), noSpace = new Insets(0, 0, 0, 0);

		this.add(new JLabel("Draw direction:"), c);
		c.gridy++;
		final ButtonGroup directionGroup = new ButtonGroup();
		this.add(createDirectionPanel(snakeMapEditor, directionGroup), c);
		
		c.gridy++; c.insets = space;
		this.add(new JLabel("Snake"), c);
		c.gridy++;
		final JCheckBox drawSnakeBox = new JCheckBox("Place snake head");
		this.add(drawSnakeBox, c);
		c.gridy++; c.gridwidth = 1; c.insets = noSpace;
		this.add(new JLabel("Snake Length:"), c);
		c.gridx++;
		final SpinnerNumberModel snakeLengthSpinner = new SpinnerNumberModel(2, 2, 100, 1);
		this.add(new JSpinner(snakeLengthSpinner), c);
		c.gridx = 0; c.gridy++; c.gridwidth = 10;
		JButton drawSnake = new JButton("Draw snake");
		this.add(drawSnake, c);
		
		c.gridy++; c.insets = space;
		this.add(new JLabel("Draw length:"), c);
		c.gridy++; c.gridwidth = 1; c.insets = noSpace;
		final JCheckBox drawLengthBox = new JCheckBox("Length:");
		this.add(drawLengthBox, c);
		c.gridx++;
		final SpinnerNumberModel drawLengthSpinner = new SpinnerNumberModel(2, 2, 100, 1);
		this.add(new JSpinner(drawLengthSpinner), c);
		
		c.gridy++; c.gridx = 0; c.gridwidth = 10;
		JButton draw = new JButton("Draw");
		this.add(draw, c);
		
		drawSnakeBox.addChangeListener(new ChangeListener() {			
			@Override
			public void stateChanged(ChangeEvent e) {
				snakeMapEditor.activateSnakeDraw(drawSnakeBox.isSelected());
			}
		});
		
		drawSnake.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {					
					@Override
					public void run() {
						snakeMapEditor.drawSnake(Dir.valueOf(directionGroup.getSelection().getActionCommand()), snakeLengthSpinner.getNumber().intValue());
					}
				});
			}
		});
		
		draw.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {					
					@Override
					public void run() {
						if(drawLengthBox.isSelected())
							snakeMapEditor.draw(Dir.valueOf(directionGroup.getSelection().getActionCommand()), drawLengthSpinner.getNumber().intValue());
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
