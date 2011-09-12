package se.chalmers.snake.mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.Comparator;
import java.util.TreeMap;

import javax.swing.JPanel;

import se.chalmers.snake.mapeditor.Square.Dir;

@SuppressWarnings("serial")
class MainPanel extends JPanel {
	private final GridBagConstraints c = new GridBagConstraints();
	private final SnakeMapEditor snakeMapEditor;
	private TreeMap<Point, Square> squares = new TreeMap<Point, Square>(new Comparator<Point>(){

		@Override
		public int compare(Point o1, Point o2) {
			if(o1.y == o2.y){
				if(o1.x == o2.x)
					return 0;
				return (o1.x < o2.x) ? -1 : 1;
			}
			return (o1.y < o2.y) ? -1 : 1;
		}
		
	});

	MainPanel(SnakeMapEditor snakeMapEditor, int rows, int columns) {
		this.snakeMapEditor = snakeMapEditor;
		this.setLayout(new GridBagLayout());
		fillPanel(rows, columns, 10);
	}
	
	Square[] getSquares(){
		return squares.values().toArray(new Square[squares.size()]);
	}
	
	void markSquare(boolean snake, int id, Point point){
		Square s = squares.get(point);
		if(snake)
			s.setSnakeMark(true);
		else
			s.setFilled(true);
	}
	
	void fill(int rows, int columns, int radie) {
		removeAll();
		fillPanel(rows, columns, radie);
	}
	
	private void fillPanel(final int rows, final int columns, final int radie) {
		c.gridx = c.gridy = 0;
		Square[][] array = new Square[columns][rows];
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				Point p = new Point(j,i);
				Square s = new Square(snakeMapEditor, radie, this, p);
				squares.put(p,s);
				setNeigbours(array, s, j, i);
				array[i][j] = s;
				if(i==0 || i==columns-1 || j==0 || j == rows -1)
					s.setBorderPainted(true);
				add(s, c);
				c.gridx++;
			}
			c.gridx = 0;
			c.gridy++;
		}
		// Create a star in the middle
		//for(Dir d : Dir.values())
		//	array[columns/2][rows/2].markPath(d,5);
	}
	
	/*
	 *  [0,0][0,1]
	 *  [1,0][1,1]
	 */
	private void setNeigbours(Square[][] array, Square s, int row, int column){
		final Dir[] dirArray = {Dir.W, Dir.NW, Dir.N, Dir.NE};
		for(Dir dir : dirArray){
			int r = row + dir.x, c = column + dir.y;
			if(r >= 0 && c >= 0 && r < array[0].length && c < array.length && array[c][r] != null)
				s.linkNeighbors(array[c][r], dir);
		}
	}
	
	/*
	 * Create a snake that follows the cursor. 
	 *
	private int length = 10;
	private final LinkedList<Square> sq = new LinkedList<Square>();
	void addSq(Square s){
		if(sq.size() <= length)
			sq.addFirst(s);
		else{
			sq.addFirst(s);
			Square squ = sq.removeLast();
			squ.setFilled(false);
			squ.repaint();
		}
	}*/
}
