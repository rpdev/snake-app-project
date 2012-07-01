package mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
class Frame extends JFrame {
	private ArrayList<DotData> dots = new ArrayList<>();
	private DrawPanel draw;
	private JPanel main = new JPanel();
	private DotList list = new DotList(this);
	private OptionPanel options = new OptionPanel(this);
	private InformationPanel info = new InformationPanel();
	private int radius = 10, snakeSeg = 4;
	private SnakeData snakeData;
	private boolean snake = false;

	private Frame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(options, BorderLayout.EAST);
		add(main, BorderLayout.CENTER);
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
					main.remove(draw);
				snakeData = null;
				dots.clear();
				list.clear();
				list.getPreferredSize().height = y;
				main.add(draw = new DrawPanel(Frame.this, new Dimension(x, y)), BorderLayout.CENTER);
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
		DotData data = null;
		if(snake && snakeData == null){
			snakeData = new SnakeData(point, radius, snakeSeg);
			data = snakeData;
		} else if(!snake)
			data = new DotData(point, radius);
		if(data != null){
			dots.add(data);
			list.addDot(data);
			list.repaint();
			draw.repaint();
		}
	}
	
	
	public static void main(String[] args) {
		new Frame();
	}

	void setRadius(int value) {
		radius = value;
		if(snake && snakeData != null){
			((DotData) snakeData).diameter = value;
			draw.repaint();
		}
	}

	class DotData {
		final int x, y;
		private int diameter;
		private boolean mark = false;
		
		private DotData(Point point, int diameter){
			x = point.x + diameter/2;
			y = point.y + diameter/2;
			this.diameter = diameter;
		}
		
		int getDiameter(){
			return diameter;
		}
		
		boolean getMark(){
			return mark;
		}
		
		@Override
		public String toString(){
			return "Dot{"+x+","+y+"}";
		}
	}
	
	class SnakeData extends DotData{
		private int rotation;
		
		private SnakeData(Point point, int diameter, int rotation){
			super(point, diameter);
			this.rotation = rotation;
		}
		
		int getRot(){
			return rotation;
		}
		
		@Override
		public String toString(){
			return "Snake{"+x+","+y+"}";
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
		if(data == snakeData)
			snakeData = null;
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

	void setRotation(int intValue) {
		if(snakeData != null){
			snakeData.rotation = intValue;
			draw.repaint();
		}
	}

	void setSnakeSegments(int intValue) {
		snakeSeg = intValue;
		draw.repaint();
	}
	
	int getSnakeSegments(){
		return snakeSeg;
	}

	void setSnakeActive(boolean selected) {
		snake = selected;
	}
}
