import java.awt.*;

public class DOval extends DShape
{
   public DOval()
   {
      super.setModel(new DOvalModel());
   }

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.fillOval(getX(), getY(), getWidth(), getHeight());
   }
}