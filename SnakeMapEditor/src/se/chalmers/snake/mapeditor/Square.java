package se.chalmers.snake.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;

@SuppressWarnings("serial")
class Square extends JButton {
	private final EnumMap<Dir,Square> neighbors = new EnumMap<Dir, Square>(Dir.class);
	private final Point location;
	private volatile boolean filled = false, marked = false;

	enum Dir{
		NW(-1,-1),	N(0,-1),	NE(1,-1),
		W(-1,0),				E(1,0),
		SW(-1,1),	S(0,1),		SE(1,1);
		
		final int x,y;
		
		private Dir(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		Dir getOpposit(){
			switch(this){
			case NW: return SE;
			case N: return S;
			case NE: return SW;
			case W: return E;
			case E: return W;
			case SW: return NE;
			case S: return N;
			case SE: return NW;
			}
			throw new IllegalArgumentException();
		}
	}
	
	Square(final SnakeMapEditor snakeMapEditor, int radie, final MainPanel panel, Point point) {
		this.location = point;
		this.setPreferredSize(new Dimension(2 * radie, 2 * radie));
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				filled = !filled;
				EventQueue.invokeLater(new Runnable() {						
					@Override
					public void run() {
						snakeMapEditor.setMarkedSquare(Square.this);
						panel.repaint();
					}
				});
			}
		});
		/*this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				filled = !filled;
				EventQueue.invokeLater(new Runnable() {						
					@Override
					public void run() {
						panel.addSq(Square.this);
						panel.repaint();
					}
				});					
			}
		});*/
	}
	
	Point getLoc(){
		return location;
	}
	
	void setMark(){
		marked = true;
	}
	
	void removeMark(){
		marked = false;
	}
	
	boolean isFilled(){
		return filled;
	}
	
	void setFilled(boolean filled){
		this.filled = filled;
	}
	
	void markFullPath(Dir dir){
		if(!marked)
			filled = !filled;
		if(neighbors.containsKey(dir))
			neighbors.get(dir).markFullPath(dir);
		this.repaint();
	}
	
	void markPath(Dir dir, int length){
		if(!marked)
			filled = !filled;
		if(length > 0 && neighbors.containsKey(dir))
			neighbors.get(dir).markPath(dir, --length);
		this.repaint();
	}
	
	void linkNeighbors(Square neighbor, Dir dir){
		neighbors.put(dir, neighbor);
		neighbor.neighbors.put(dir.getOpposit(), this);
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(filled){
			Dimension d = this.getPreferredSize();
			g.setColor( marked ? Color.BLUE : Color.GREEN);
			g.fillOval(0, 0, d.width - 1 , d.height - 1);
		}
		super.paintComponents(g);
	}
}