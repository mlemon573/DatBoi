import java.awt.Font;
import java.awt.Shape;
import java.awt.Graphics;

public class DText extends DShape {

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

	public String getFont() {
		return ((DTextModel) getModel()).getFont();
	}
	
	public Font computeFont(Graphics g) {
		Font font = g.getFont();
		int maxHeight = getModel().getHeight();
		int prevSize = font.getSize();
		for (double size = 1.0; ; size = (size * 1.10) + 1) {
			font = new Font(getFont(), Font.PLAIN, (int) size);
			int height = g.getFontMetrics(font).getHeight();
			if (height < maxHeight) {
				prevSize = (int) size;
			} else {
				font = new Font(font.getName(), Font.PLAIN, prevSize);
				return font;
			}
		}
	}

	public void draw(Graphics g) {
		g.setColor(this.getColor());
		g.setFont(new Font(getFont(), Font.PLAIN, 1));
		g.setFont(computeFont(g));
		Shape clip = g.getClip();
		g.setClip(clip.getBounds().createIntersection(getBounds()));
		g.drawString(getText(), getX(), getY() + getHeight() - g.getFontMetrics().getDescent());
		g.setClip(clip);
	}
}
