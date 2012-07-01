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
			g2.fillOval(e.x, e.y, e.diameter, e.diameter);
			if(e.getMark()){
				Stroke s = g2.getStroke();
				g2.setStroke(new BasicStroke(3f));
				Color tmp = g.getColor();
				g2.setColor(Color.RED);
				
				g2.drawOval(e.x, e.y, e.diameter, e.diameter);
				g2.setColor(tmp);
				g2.setStroke(s);
			}
		}
	}
}
