import java.util.*;
import java.awt.*;

import javax.swing.*;

public class Canvas extends JPanel {
	private ArrayList<DShape> shapes;
	private DShape highlighted;
	private int highlightedX;
	private int highlightedY;
	
	public Canvas() {
		this(300, 300);
	}

	public Canvas(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(true);
		this.setBackground(Color.white);
	}

	public void clear() {
		shapes.clear();
		highlighted = null;
		repaint();
	}
	
	public void setHighlighted(DShape shape) {
		highlighted = shape;
	}
	
	public DShape getHighlighted() {
		return this.highlighted;
	}
	
	public DShape findShape(int x, int y) {
		return null;
	}

	public void moveShape(DShape shape, int newX, int newY) {
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
		}
	}

	public static void main(String... args) {
		JFrame a = new JFrame();
		Canvas b = new Canvas();
		a.add(b);
		a.pack();
		a.setVisible(true);
	}
}
