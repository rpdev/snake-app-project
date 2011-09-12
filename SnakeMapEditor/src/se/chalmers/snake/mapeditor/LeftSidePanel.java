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
class LeftSidePanel extends JPanel {
	private final static int WIDTH = 150;
	private EnumMap<Settings, JTextComponent> values = new EnumMap<Settings, JTextComponent>(Settings.class);  
	
	LeftSidePanel(SnakeMapEditor snakeMapEditor) {
		createPanel(snakeMapEditor);
	}

	private void createPanel(final SnakeMapEditor snakeMapEditor) {
		this.setLayout(new BorderLayout());
		
		this.add(createSettingsPanel(snakeMapEditor), BorderLayout.CENTER);
		
		JButton generate = new JButton("Generate"), exit = new JButton("Exit");
		generate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				(new Thread(new Runnable() {
					
					@Override
					public void run() {
						snakeMapEditor.generate(
								Integer.parseInt(values.get(Settings.MAPSIZEX).getText()),
								Integer.parseInt(values.get(Settings.MAPSIZEY).getText()),
								Integer.parseInt(values.get(Settings.CIRCLERADIE).getText())
						);
					}
				})).start();
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

	private JPanel createSettingsPanel(final SnakeMapEditor snakeMapEditor) {
		// Basic setup
		JPanel panel = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0; c.gridwidth = 2; c.anchor =  GridBagConstraints.NORTHWEST; // to be sure
		
		// level title
		panel.add(new JLabel(Settings.LEVELNAME.name), c);
		c.gridy++;
		JTextField levelName = new JTextField("Level 1",10);
		panel.add(levelName, c);
		values.put(Settings.LEVELNAME,levelName);
		
		// description
		c.gridy++;
		panel.add(new JLabel(Settings.LEVELDESCRIPTION.name), c);
		c.gridy++;
		JTextArea levelInfo = new JTextArea("Some info about this level.");
		levelInfo.setWrapStyleWord(true);
		levelInfo.setLineWrap(true);
		values.put(Settings.LEVELDESCRIPTION, levelInfo);
		JScrollPane sc = new JScrollPane(levelInfo);
		sc.setPreferredSize(new Dimension(WIDTH, 50));
		panel.add(sc, c);
		
		// level
		c.gridy++;
		panel.add(new JLabel(Settings.LEVELDIFFICULY.name), c);
		c.gridy++;
		JTextField level = new JTextField("1",10);
		values.put(Settings.LEVELDIFFICULY, level);
		panel.add(level, c);
		
		// size
		c.gridy++;
		panel.add(new JLabel("Map Size:"), c);
		// size X
		c.gridy++;
		JPanel panelX = new JPanel(new FlowLayout());
		panelX.add(new JLabel(Settings.MAPSIZEX.name), c);
		JTextField mapX = new JTextField("45",5);
		values.put(Settings.MAPSIZEX, mapX);
		panelX.add(mapX);
		panel.add(panelX, c);
		// size Y
		c.gridy++;
		JPanel panelY = new JPanel(new FlowLayout());
		panelY.add(new JLabel(Settings.MAPSIZEY.name));
		JTextField mapY = new JTextField("45",5);
		values.put(Settings.MAPSIZEY, mapY);
		panelY.add(mapY);
		panel.add(panelY, c);
		
		// circle radie
		c.gridy++;
		panel.add(new JLabel(Settings.CIRCLERADIE.name), c);
		c.gridy++;
		JTextField circleRadie = new JTextField("10",10);
		values.put(Settings.CIRCLERADIE, circleRadie);
		panel.add(circleRadie, c);
		
		
		// Collectible
		c.gridy++; c.gridx = 0;
		panel.add(new JLabel(Settings.COLLECTIBLE.name), c);
		c.gridy++;
		JTextField collect = new JTextField("10",10);
		values.put(Settings.COLLECTIBLE, collect);
		panel.add(collect, c);
		
		c.gridy++;
		final JButton calc = new JButton("Calc");
		panel.add(calc, c);
		calc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						final int rows = Integer.parseInt(values.get(Settings.MAPSIZEX).getText());
						final int columns = Integer.parseInt(values.get(Settings.MAPSIZEY).getText());
						final int radie = Integer.parseInt(values.get(Settings.CIRCLERADIE).getText());
						calc.setText("W:"+(rows*radie*2) + " H:" + (columns*radie*2));
					}
				});
			}
		});		
		
		c.gridy++;
		JButton save = new JButton("Save");
		panel.add(save, c);
		save.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				snakeMapEditor.saveMap(values);
			}
		});
		
		c.gridy++;
		JButton load = new JButton("Load");
		panel.add(load, c);
		load.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						snakeMapEditor.loadMap();
					}
				});
			}
		});
		
		return panel;
	}
}
