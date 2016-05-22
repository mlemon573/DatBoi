import java.awt.*;

/**
 * Class to handle the drawing of the oval shape.
 */
public class DOval extends DShape
{
   /**
    * Constructor for DOval.
    */
   public DOval()
   {
      super.setModel(new DOvalModel());
   }

   /**
    * Method to draw the oval with a respective position, size, and color.
    *
    * @param g - the graphic to draw.
    */
   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.fillOval(getX(), getY(), getWidth(), getHeight());
   }
}
