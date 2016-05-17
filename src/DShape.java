import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DShape {
	private DShapeModel model;

	public DShape() {

	}

	public int getX() {
		return model.getX();
	}

	public void setX(int x) {
		model.setX(x);
	}

	public int getY() {
		return model.getY();
	}

	public void setY(int y) {
		model.setY(y);
	}

	public int getWidth() {
		return model.getWidth();
	}

	public void setWidth(int width) {
		model.setWidth(width);
	}

	public int getHeight() {
		return model.getHeight();
	}

	public void setHeight(int height) {
		model.setHeight(height);
	}

	public Color getColor() {
		return model.getColor();
	}

	public void setColor(Color color) {
		model.setColor(color);
	}

	public int getID() {
		return model.getID();
	}

	public void setID(int id) {
		model.setID(id);
	}

	public void setBounds(int x, int y, int width, int height) {
		model.setBounds(x, y, width, height);
	}

	public Rectangle getBounds() {
		return (Rectangle) model.getBounds().clone();
	}

	public boolean equals(DShape shape) {
		return shape != null && this.model.equals(shape.getModel());
	}

	public void moveBy(int dx, int dy) {
		this.model.moveBy(dx, dy);
	}

	public DShapeModel getModel() {
		return this.model;
	}

	public void setModel(DShapeModel model) {
		this.model = model;
	}

	public void addListener(ModelListener listener) {
		model.addListener(listener);
	}

	abstract void draw(Graphics g);

	protected List<Point> getKnobs() {
		List<Point> knobs = new ArrayList<Point>();
		knobs.add(new Point(getX(), getY()));
		knobs.add(new Point(getX() + getWidth(), getY()));
		knobs.add(new Point(getX(), getY() + getHeight()));
		knobs.add(new Point(getX() + getWidth(), getY() + getHeight()));
		return knobs;
	}
}

/*
 * class DRect extends DShape { public DRect() { super.setModel(new
 * DRectModel()); }
 * 
 * public void draw(Graphics g) { g.setColor(this.getColor());
 * g.fillRect(getX(), getY(), getWidth(), getHeight()); } }
 */

class DOval extends DShape {
	public DOval() {
		super.setModel(new DOvalModel());
	}

	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.fillOval(getX(), getY(), getWidth(), getHeight());
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
		super.setModel(new DTextModel());
	}

	public void setText(String text) {
		DTextModel model = (DTextModel) getModel();
		model.setText(text);
	}

	public String getText() {
		return ((DTextModel) getModel()).getText();
	}

	public void setFont(String font) {
		((DTextModel) getModel()).setFont(font);
	}

	public Font getFont() {
		return ((DTextModel) getModel()).getFont();
	}

	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.setFont(getFont());
		g.drawString(getText(), getX(), getY() + getHeight() - 6);
	}
}
