package se.chalmers.snake.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import se.chalmers.snake.mapeditor.SnakeMapEditor.Settings;

@SuppressWarnings("serial")
class SidePanel extends JPanel {
	private final static int WIDTH = 150;
	private EnumMap<Settings, JTextComponent> values = new EnumMap<Settings, JTextComponent>(Settings.class);  
	
	SidePanel(SnakeMapEditor snakeMapEditor) {
		createPanel(snakeMapEditor);
	}

	private void createPanel(final SnakeMapEditor snakeMapEditor) {
		this.setLayout(new BorderLayout());
		
		this.add(createSettingsPanel(snakeMapEditor), BorderLayout.CENTER);
		
		JButton generate = new JButton("Generate"), exit = new JButton("Exit");
		generate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				snakeMapEditor.generate(values);
			}
		});
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttons.add(generate); buttons.add(exit);
		this.add(buttons, BorderLayout.SOUTH);
	}

	private JPanel createSettingsPanel(SnakeMapEditor snakeMapEditor) {
		// Basic setup
		JPanel panel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0; c.gridwidth = 2; c.anchor =  GridBagConstraints.NORTHWEST; // to be sure
		
		// level title
		panel.add(new JLabel(Settings.LevelName.name), c);
		c.gridy++;
		JTextField levelName = new JTextField(10);
		panel.add(levelName, c);
		values.put(Settings.LevelName,levelName);
		
		// description
		c.gridy++;
		panel.add(new JLabel(Settings.LevelDescription.name), c);
		c.gridy++;
		JTextArea levelInfo = new JTextArea();
		levelInfo.setWrapStyleWord(true);
		levelInfo.setLineWrap(true);
		values.put(Settings.LevelDescription, levelInfo);
		JScrollPane sc = new JScrollPane(levelInfo);
		sc.setPreferredSize(new Dimension(WIDTH, 50));
		panel.add(sc, c);
		
		// level
		c.gridy++;
		panel.add(new JLabel(Settings.LevelDifficuly.name), c);
		c.gridy++;
		JTextField level = new JTextField(10);
		values.put(Settings.LevelDifficuly, level);
		panel.add(level, c);
		
		// size
		c.gridy++;
		panel.add(new JLabel("Map Size:"), c);
		// size X
		c.gridy++;
		JPanel panelX = new JPanel(new FlowLayout());
		panelX.add(new JLabel(Settings.MapSizeX.name), c);
		JTextField mapX = new JTextField(5);
		values.put(Settings.MapSizeX, mapX);
		panelX.add(mapX);
		panel.add(panelX, c);
		// size Y
		c.gridy++;
		JPanel panelY = new JPanel(new FlowLayout());
		panelY.add(new JLabel(Settings.MapSizeY.name));
		JTextField mapY = new JTextField(5);
		values.put(Settings.MapSizeY, mapY);
		panelY.add(mapY);
		panel.add(panelY, c);
		
		// circle radie
		c.gridy++;
		panel.add(new JLabel(Settings.CircleRadie.name), c);
		c.gridy++;
		JTextField circleRadie = new JTextField(10);
		values.put(Settings.CircleRadie, circleRadie);
		panel.add(circleRadie, c);
		
		
		// Collectible
		c.gridy++; c.gridx = 0;
		panel.add(new JLabel(Settings.Collectible.name), c);
		c.gridy++;
		JTextField collect = new JTextField(10);
		values.put(Settings.Collectible, collect);
		panel.add(collect, c);
		
		c.gridy++;
		final JButton calc = new JButton("Calc");
		calc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						final int rows = Integer.parseInt(values.get(Settings.MapSizeX).getText());
						final int columns = Integer.parseInt(values.get(Settings.MapSizeY).getText());
						final int radie = Integer.parseInt(values.get(Settings.CircleRadie).getText());
						calc.setText("W:"+(rows*radie*2) + " H:" + (columns*radie*2));
					}
				});
			}
		});
		panel.add(calc, c);
		
		return panel;
	}
}
