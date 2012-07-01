package mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
class Frame extends JFrame {
	private ArrayList<DotData> dots = new ArrayList<>();
	private DrawPanel draw;
	private DotList list = new DotList(this);
	private OptionPanel options = new OptionPanel(this);
	private InformationPanel info = new InformationPanel();
	private int radius = 10;

	private Frame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(options, BorderLayout.EAST);
		add(new JScrollPane(list), BorderLayout.WEST);
		add(info, BorderLayout.SOUTH);
		pack();
		setVisible(true);
		setResizable(false);
	}
	
	void generate(final int x, final int y){
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if(draw != null)
					remove(draw);
				dots.clear();
				list.clear();
				list.getPreferredSize().height = y;
				add(draw = new DrawPanel(Frame.this, new Dimension(x, y)), BorderLayout.CENTER);
				draw.repaint();
				validate();
				pack();
			}
		});
	}
	
	int getRadius(){
		return radius;
	}
	
	void addDot(Point point){
		DotData data = new DotData(point, radius);
		dots.add(data);
		list.addDot(data);
		list.repaint();
		draw.repaint();
	}
	
	
	public static void main(String[] args) {
		new Frame();
	}

	void setRadius(int value) {
		radius = value;
	}

	class DotData {
		final int x, y, diameter;
		private boolean mark = false;
		
		private DotData(Point point, int diamter){
			point.x -= diamter/2;
			point.y -= diamter/2;
			x = point.x;
			y = point.y;
			this.diameter = diamter;
		}
		
		boolean getMark(){
			return mark;
		}
		
		@Override
		public String toString(){
			return "Dot{"+x+","+y+"}";
		}
	}

	void removeMark(DotData data) {
		data.mark = false;
		draw.repaint();
	}

	void setMark(DotData data) {
		data.mark = true;
		draw.repaint();
	}

	void removeDot(final DotData data) {
		dots.remove(data);
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				list.removeDot(data);
				repaint();
			}
		});
	}

	ArrayList<DotData> getDots() {
		return dots;
	}
}
