package se.chalmers.snake.mapeditor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import se.chalmers.snake.mapeditor.Square.Dir;

@SuppressWarnings("serial")
class MainPanel extends JPanel {
	private final GridBagConstraints c = new GridBagConstraints();
	private final SnakeMapEditor snakeMapEditor;
	private Square[] squares;

	MainPanel(SnakeMapEditor snakeMapEditor, int rows, int columns) {
		this.snakeMapEditor = snakeMapEditor;
		this.setLayout(new GridBagLayout());
		fillPanel(rows, columns, 10);
	}
	
	Square[] getSquares(){
		return squares.clone();
	}
	
	void markSquare(int id){
		squares[id].setFilled(true);
	}
		
	void fill(int rows, int columns, int radie) {
		removeAll();
		fillPanel(rows, columns, radie);
	}
	
	private void fillPanel(final int rows, final int columns, final int radie) {
		c.gridx = c.gridy = 0;
		Square[][] array = new Square[columns][rows];
		ArrayList<Square> squares = new ArrayList<Square>(columns*rows);
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				Square s = new Square(snakeMapEditor, radie, this, new Point(j,i));
				squares.add(s);
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
		squares.toArray(this.squares = new Square[squares.size()]);
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
