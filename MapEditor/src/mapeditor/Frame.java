package mapeditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private Frame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(new OptionPanel(), BorderLayout.EAST);
		pack();
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new Frame();
	}

}
