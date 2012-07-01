package mapeditor;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mapeditor.Frame.DotData;

@SuppressWarnings("serial")
class DotList extends JPanel {
	private final Dimension labelDim = new Dimension(120,15);
	private static final int size = 10;
	private final HashMap<DotData, DotObject> panels = new HashMap<>();
	private final GridBagConstraints c = new GridBagConstraints();
	private final Frame frame;
	private final JPanel panel = new JPanel(new GridBagLayout());
	
	DotList(Frame frame) {
		this.frame = frame;
		this.setPreferredSize(new Dimension(labelDim.width + size + 10, 50));
		setLayout(new BorderLayout());
		add(new JScrollPane(panel), BorderLayout.CENTER);
		c.gridx = c.gridy = 0;
		c.weightx = 1d;
		c.fill = GridBagConstraints.HORIZONTAL;
	}
	
	void addDot(DotData data){
		DotObject o = new DotObject(data, frame);
		panel.add(o, c);
		c.gridy++;
		panels.put(data, o);
		panel.revalidate();
	}
	
	void clear(){
		c.gridy = 0;
		panel.removeAll();
		panels.clear();
		panel.invalidate();
		panel.repaint();
	}
	
	void removeDot(DotData data){
		panel.remove(panels.remove(data));
		panel.invalidate();
	}
	
	private class DotObject extends JPanel{
		
		
		private DotObject(final DotData data, final Frame frame){
			this.setLayout(new FlowLayout());
			JLabel label = new JLabel(data.toString());
			add(label);
			add(new Cross(data, frame));
			addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					EventQueue.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							setBorder(null);
							repaint();
						}
					});
					frame.removeMark(data);
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					EventQueue.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
							repaint();
						}
					});
					frame.setMark(data);
				}
			});
		}
		
		private class Cross extends JPanel{
			private final BufferedImage image;
			
			private Cross(final DotData data, final Frame frame){
				setPreferredSize(new Dimension(size, size));
				BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = (Graphics2D) image.getGraphics();
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(3f));
				g2.drawLine(0, 0, image.getWidth(), image.getHeight());
				g2.drawLine(0, image.getHeight(), image.getWidth(), 0);
				this.image = image;
				addMouseListener(new MouseAdapter() {					
					@Override
					public void mouseClicked(MouseEvent me) {
						frame.removeDot(data);
					}
				});
			}
			
			@Override
			public void paintComponent(Graphics g){
				super.paintComponents(g);
				g.drawImage(image, 0, 0, null);
			}
		}
	}
}
