package se.chalmers.snake.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
class Square extends JButton {
	private boolean filled = false;

	enum Dir{
		NW(-1,-1),	N(0,1),		NE(1,-1),
		W(-1,0),	C(0,0),		E(1,0),
		SW(-1,1),	S(0,-1),	SE(1,1);
		
		final int x,y;
		
		private Dir(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	Square(int radie, final MainPanel panel) {
		setSize(radie);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		/*this.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				filled = !filled;
				EventQueue.invokeLater(new Runnable() {						
					@Override
					public void run() {
						MainPanel.this.repaint();
					}
				});
			}
		});*/
		this.addMouseListener(new MouseAdapter() {
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
		});
	}
	
	void setFilled(boolean filled){
		this.filled = filled;
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(filled){
			Dimension d = this.getPreferredSize();
			g.setColor(Color.GREEN);
			g.fillOval(0, 0, d.width - 1 , d.height - 1);
		}
		super.paintComponents(g);
	}

	private void setSize(int radie) {
		this.setPreferredSize(new Dimension(2 * radie, 2 * radie));
	}
}