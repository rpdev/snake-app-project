package se.chalmers.snake.mapeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.io.File;
import java.util.EnumMap;
import java.util.Map.Entry;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;

import se.chalmers.snake.mapeditor.LoadSaveMapXML.Data;
import se.chalmers.snake.mapeditor.LoadSaveMapXML.SquareData;
import se.chalmers.snake.mapeditor.Square.Dir;

class SnakeMapEditor {
	private final MainPanel mainPanel = new MainPanel(this, 35, 35);
	private final JFrame frame;
	private Square markedSquare;

	private SnakeMapEditor() {
		frame = createFrame();
	}

	public static void main(String[] args) {
		new SnakeMapEditor();
	}

	private JFrame createFrame() {
		JFrame frame = new JFrame("WARNING: No safety checks, only developers!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new LeftSidePanel(this), BorderLayout.WEST);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(new RightSidePanel(this), BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	enum Settings{
		LEVELNAME("Level Name:"),
		LEVELDESCRIPTION("Level Description:"),
		LEVELDIFFICULY("Level Number:"),
		MAPSIZEX("X:"),
		MAPSIZEY("Y:"),
		COLLECTIBLE("Collectables:"),
		CIRCLERADIE("Circle radie:");

		final String name;

		private Settings(String name) {
			this.name = name;
		}
	}

	void generate(final int rows, final int columns, final int radie) {
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				mainPanel.fill(rows, columns, radie);
				frame.pack();
			}
		});
	}

	void draw(Dir dir, int length) {
		markedSquare.markPath(dir, length);
	}

	void draw(Dir dir) {
		markedSquare.markFullPath(dir);
	}

	void setMarkedSquare(Square square) {
		if(markedSquare != null)
			markedSquare.removeMark();
		markedSquare = square;
		square.setMark();
	}

	void loadMap() {
		final File f = openFileChooser(frame);
		if (f != null) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					Data d = LoadSaveMapXML.getInstance().loadMap(f);
					int rows = Integer.parseInt(d.data.get(Settings.MAPSIZEX)),
					columns = Integer.parseInt(d.data.get(Settings.MAPSIZEY)),
					radie =	Integer.parseInt(d.data.get(Settings.CIRCLERADIE));
					mainPanel.fill(rows, columns, radie);
					for(SquareData s : d.squares)
						mainPanel.markSquare(s.id);
					frame.pack();
				}
			});
		}
	}

	void saveMap(EnumMap<Settings, JTextComponent> values) {
		EnumMap<Settings, String> data = new EnumMap<Settings, String>(Settings.class);
		for(Entry<Settings, JTextComponent> e : values.entrySet())
			data.put(e.getKey(), e.getValue().getText());
		LoadSaveMapXML.getInstance().saveMap(data, mainPanel.getSquares());
	}
	
	private File openFileChooser(Component parent){
		return openFileChooser(parent, JFileChooser.FILES_ONLY);
	}
	
	private File openFileChooser(Component parent, int mode){
		JFileChooser fc = new JFileChooser("./");
		fc.setFileSelectionMode(mode);
		int choice = fc.showOpenDialog(parent);
		if(choice == JFileChooser.APPROVE_OPTION)
			return fc.getSelectedFile();
		return null;
	}
}
