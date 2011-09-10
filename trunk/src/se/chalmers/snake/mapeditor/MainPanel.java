package se.chalmers.snake.mapeditor;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
class MainPanel extends JPanel {
	private final LinkedList<Square> sq = new LinkedList<Square>();
	private final GridBagConstraints c = new GridBagConstraints();
	private final HashMap<Location, Square> squares = new HashMap<Location, Square>();
	private static final int RADIE = 4;

	MainPanel(SnakeMapEditor snakeMapEditor, int width, int height) {
		this.setLayout(new GridBagLayout());
		int rows = (height-40)/(2*RADIE);
		int columns = (width-40)/(2*RADIE);
		fillPanel(rows, columns, RADIE);
	}
	
	private int length = 10;
	void addSq(Square s){
		if(sq.size() <= length)
			sq.addFirst(s);
		else{
			sq.addFirst(s);
			Square squ = sq.removeLast();
			squ.setFilled(false);
			squ.repaint();
		}
	}
		
	void fill(final int width, final int height, final int radie){
		//final int rows = (height-40)/(2*radie), columns = (width-40)/(2*radie);
		final int rows = height -1, columns = height -1;
		EventQueue.invokeLater(new Runnable() {			
			@Override
			public void run() {
				removeAll();
				fillPanel(rows, columns, radie);
				//repaint();
				SwingUtilities.windowForComponent(MainPanel.this).pack();
			}
		});
	}
	
	private void fillPanel(final int rows, final int columns, final int radie) {
		c.gridx = c.gridy = 0;
		//Square[][] array = new Square[columns][rows];
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				Square s = new Square(radie, this);
				squares.put(new Location(j, i), s);
				//setNeigbours(array, s, j, i);
				//array[i][j] = s;
				if(i==0 || i==columns-1 || j==0 || j == rows -1)
					s.setBorderPainted(true);
				add(s, c);
				c.gridx++;
			}
			c.gridx = 0;
			c.gridy++;
		}
	}
	
	/* Continue linking squares when time....
	 *  [0,0][0,1]
	 *  [1,0][1,1]
	 *
	private void setNeigbours(Square[][] array, Square s, int row, int column){
		
	}*/
	
	private class Location{
		private final int x,y;
		
		private Location(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		@Override
		public boolean equals(Object obj){
			if(obj instanceof Location){
				Location l = (Location) obj;
				if(l.x == x && l.y == y)
					return true;
			}
			return false;
		}
	}
}
