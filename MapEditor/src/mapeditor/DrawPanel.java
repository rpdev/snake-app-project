package mapeditor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import mapeditor.Frame.DotData;
import mapeditor.Frame.SnakeData;

@SuppressWarnings("serial")
class DrawPanel extends JPanel {
	private final Frame frame;
	
	DrawPanel(final Frame frame, Dimension dim){
		this.frame = frame;
		setPreferredSize(dim);
		setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
		addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent me) {
				frame.addDot(me.getPoint());
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;
		for(DotData e : frame.getDots()){
			int dim = e.getDiameter();
			if(e instanceof SnakeData)
				g2.setColor(Color.GREEN);
			g2.fillOval(e.x-dim/2, e.y-dim/2, dim, dim);
			if( e instanceof SnakeData){
				g2.drawLine(
						e.x, 
						e.y, 
						(int) (e.x + dim/2 * frame.getSnakeSegments() * Math.cos(Math.toRadians(((SnakeData) e).getRot()))), 
						(int) (e.y + dim/2 * frame.getSnakeSegments() * Math.sin(Math.toRadians(((SnakeData) e).getRot()))));
				g2.setColor(Color.BLACK);
			}
			if(e.getMark()){
				Stroke s = g2.getStroke();
				g2.setStroke(new BasicStroke(3f));
				Color tmp = g.getColor();
				g2.setColor(Color.RED);
				
				g2.drawOval(e.x-dim/2, e.y-dim/2, dim, dim);
				g2.setColor(tmp);
				g2.setStroke(s);
			}
		}
	}
}
