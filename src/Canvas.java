import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel {
	private List<DShape> shapes;
	private DShape selected;
	private int selectedX;
	private int selectedY;

	public Canvas() {
		this(400, 400);
	}

	public Canvas(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(true);
		this.setBackground(Color.white);
		shapes = new ArrayList<DShape>();
	}
	
	public void addShape(DShape shape) {
		shapes.add(shape);
		selected = shape;
	}

	public void clear() {
		shapes.clear();
		selected = null;
		repaint();
	}

	public void setSelected(DShape shape) {
		selected = shape;
	}

	public DShape getSelected() {
		return this.selected;
	}

	public DShape findShape(int x, int y) {
		return null;
	}

	public void moveShape(DShape shape, int newX, int newY) {

	}

	public void saveAsPNG(File fileToSave) {

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
