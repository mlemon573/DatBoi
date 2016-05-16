import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public abstract class DShape {
	private DShapeModel model;

	public DShape() {

	}

	public void setX(int x) {
		model.setX(x);
	}

	public int getX() {
		return model.getX();
	}

	public void setY(int y) {
		model.setY(y);
	}

	public int getY() {
		return model.getY();
	}

	public void setWidth(int width) {
		model.setWidth(width);
	}

	public int getWidth() {
		return model.getWidth();
	}

	public void setHeight(int height) {
		model.setHeight(height);
	}

	public int getHeight() {
		return model.getHeight();
	}

	public void setColor(Color color) {
		model.setColor(color);
	}

	public Color getColor() {
		return model.getColor();
	}

	public void setID(int id) {
		model.setID(id);
	}

	public int getID() {
		return model.getID();
	}

	public void setBounds(int x, int y, int width, int height) {
		model.setBounds(x, y, width, height);
	}

	public Rectangle getBounds() {
		return model.getBounds();
	}

	public boolean equals(DShape shape) {
		return this.model.equals(shape.getModel());
	}

	public void setModel(DShapeModel model) {
		this.model = model;
	}

	public DShapeModel getModel() {
		return this.model;
	}

	abstract void draw(Graphics g);

	protected List<Point> getKnobs() {
		Rectangle r = model.getBounds();
		List<Point> knobs = new ArrayList<Point>();
		knobs.add(new Point((int) (r.getX()), (int) (r.getY())));
		knobs.add(new Point((int) (r.getX() + r.getWidth()), (int) r.getY()));
		knobs.add(new Point((int) (r.getX()), (int) (r.getY() + r.getHeight())));
		knobs.add(new Point((int) (r.getX() + r.getWidth()), (int) (r.getY() + r.getHeight())));
		return knobs;
	}
}

class DRect extends DShape {
	public DRect() {
		super.setModel(new DRectModel());
	}

	public void draw(Graphics g) {
		g.setColor(this.getColor());
	}
}

class DOval extends DShape {
	public DOval() {
		super.setModel(new DOvalModel());
	}

	public void draw(Graphics g) {

	}
}

class DLine extends DShape {
	public DLine() {
		super.setModel(new DLineModel());
	}

	public void draw(Graphics g) {

	}
}

class DText extends DShape {
	public DText() {
		super.setModel(new DLineModel());
	}

	public void draw(Graphics g) {

	}
}
