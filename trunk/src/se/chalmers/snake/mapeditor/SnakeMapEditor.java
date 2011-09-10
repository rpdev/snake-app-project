package se.chalmers.snake.mapeditor;

import java.awt.BorderLayout;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

class SnakeMapEditor {
	private SidePanel sidePanel;
	private MainPanel mainPanel;

	private SnakeMapEditor() {
		sidePanel = new SidePanel(this);
		mainPanel = new MainPanel(this, 600, 400);
		createFrame();
	}

	public static void main(String[] args) {
		new SnakeMapEditor();
	}

	private void createFrame() {
		JFrame frame = new JFrame("Map Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(sidePanel, BorderLayout.WEST);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

	enum Settings{
		LevelName("Level Name:"),
		LevelDescription("Level Description:"),
		LevelDifficuly("Level Number:"),
		MapSizeX("X:"),
		MapSizeY("Y:"),
		Collectible("Collectables:"), CircleRadie("Circle radie:");

		final String name;

		private Settings(String name) {
			this.name = name;
		}
	}

	void generate(EnumMap<Settings, JTextComponent> values) {
		final int width = Integer.parseInt(values.get(Settings.MapSizeX).getText());
		final int height = Integer.parseInt(values.get(Settings.MapSizeY).getText());
		final int radie = Integer.parseInt(values.get(Settings.CircleRadie).getText());
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				mainPanel.fill(height, width, radie);
			}
		})).start();
	}
}
