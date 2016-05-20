import java.awt.Graphics;
 
public class DRect extends DShape {
    public DRect() {
        super.setModel(new DRectModel());
    }
 
    public void draw(Graphics g) {
        g.setColor(this.getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
