import java.awt.Font;
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
 
    public Font getFont() {
        return ((DTextModel) getModel()).getFont();
    }
 
    public void draw(Graphics g) {
        g.setColor(this.getColor());
        g.setFont(getFont());
        g.drawString(getText(), getX(), getY() + getHeight() - 6);
    }
}
