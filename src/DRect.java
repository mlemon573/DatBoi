import java.awt.*;

/**
 * Class to handle the drawing of the oval shape.
 */
public class DRect extends DShape
{
   /**
    * Constructor for DRect.
    */
   public DRect()
   {
      super.setModel(new DRectModel());
   }

   /**
    * Method to draw the rectangle with a respective position, size, and color.
    *
    * @param g - the graphic to draw.
    */
   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.fillRect(getX(), getY(), getWidth(), getHeight());
   }
}
