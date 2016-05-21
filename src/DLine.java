import java.awt.*;

class DLine extends DShape
{
   public DLine()
   {
      super.setModel(new DLineModel());
   }

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.drawLine(getX(), getY(), getX() + getWidth(), getY() + getHeight());
   }
}