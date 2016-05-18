import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Canvas extends JPanel implements ModelListener, Serializable {
	private List<DShape> shapes;
	private DShape selected;
	private int id;

	public Canvas() {
		this(400, 400);
	}

	public Canvas(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(true);
		this.setBackground(Color.white);
		shapes = new ArrayList<>();
		id = 1;
	}

	public void addShape(DShape shape) {
		if (shape == null) {
			return;
		}
		shapes.add(shape);
		shape.addListener(this);
		shape.setID(id++);
		selected = shape;
		repaint();
	}

	public void addShapeFromModel(DShapeModel model) {
		DShape shape = null;
		if (model instanceof DRectModel) {
			shape = new DRect();
		} else if (model instanceof DOvalModel) {
			shape = new DOval();
		} else if (model instanceof DLineModel) {
			shape = new DLine();
		} else if (model instanceof DTextModel) {
			shape = new DText();
		}
		if (shape != null && model != null) {
			shape.setModel(model);
			addShape(shape);
		}
	}

	public void removeSelected() {
		if (shapes.contains(selected)) {
			shapes.remove(selected);
			selected = null;
			repaint();
		}
	}

	public void clear() {
		shapes.clear();
		selected = null;
		repaint();
	}

	public DShape getSelected() {
		return this.selected;
	}

	public void setSelected(DShape shape) {
		selected = shape;
		repaint();
	}

	public int getSelectedIndex() {
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).equals(selected)) {
				return i;
			}
		}
		return -1;
	}

	public void moveToFront() {
		int i = getSelectedIndex();
		if (i == -1) {
			return;
		}
		DShape shape = shapes.remove(i);
		shapes.add(shape);
		repaint();
	}

	public void moveToBack() {
		int i = getSelectedIndex();
		if (i == -1) {
			return;
		}
		DShape shape = shapes.remove(i);
		shapes.add(0, shape);
		repaint();
	}

	public List<DShape> getShapesList() {
		return shapes;
	}

	public DShape findShape(int x, int y) {
		for (int i = shapes.size() - 1; i >= 0; i--) {
			DShape shape = shapes.get(i);
			if (x >= shape.getX() && x <= shape.getX() + shape.getWidth() && y >= shape.getY()
					&& y <= shape.getY() + shape.getHeight()) {
				return shapes.get(i);
			}
		}
		return null;
	}

	public Rectangle findKnob(int x, int y) {
		for (Rectangle knob : selected.getKnobs()) {
			if (x >= knob.getX() && x <= knob.getX() + 9 && y >= knob.getY() && y <= knob.getY() + 9) {
				return knob;
			}
		}
		return null;
	}
	
	public Rectangle findAnchor(Rectangle knob) {
		List<Rectangle> knobs = selected.getKnobs();
		for (int i = 0; i < knobs.size(); i++) {
			if (knobs.get(i).equals(knob)) {
				if (i == 0) {
					return knobs.get(knobs.size() - 1);
				} else if (i == 1) {
					return knobs.get(knobs.size() - 2);
				} else if (i == 2) {
					return knobs.get(knobs.size() - 3);
				} else {
					return knobs.get(knobs.size() - 4);
				}
			}
		}
		return null;
	}

	public void resizeSelected(int x, int y) {
		if (selected instanceof DLine) {
			return;
		}
		for (;;) {
			break;
		}
		int newX;
		int newY;
		int newWidth;
		int newHeight;
		// selected.setBounds(newX, newY, newWidth, newHeight);
		repaint();
	}

	public void moveSelected(int dx, int dy) {
		if (selected != null) {
			selected.moveBy(dx, dy);
			repaint();
		}
	}

	@Override
	public void modelChanged(DShapeModel model) {
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (DShape shape : shapes) {
			shape.draw(g);
			if (shape.equals(selected)) {
				drawKnobs(g);
			}
		}
	}

	public void drawKnobs(Graphics g) {
		if (selected != null) {
			List<Rectangle> knobs = selected.getKnobs();
			for (Rectangle r : knobs) {
				g.setColor(Color.WHITE);
				g.fillRect((int) r.getX(), (int) r.getY(), DShape.KNOB_SIZE, DShape.KNOB_SIZE);
				g.setColor(Color.BLACK);
				g.drawRect((int) r.getX(), (int) r.getY(), DShape.KNOB_SIZE, DShape.KNOB_SIZE);
			}
		}
	}
}
