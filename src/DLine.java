import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle the drawing and manipulation of DLine.
 */
public class DLine extends DShape
{
   //DLine elements
   private boolean invertX = false;
   private boolean invertY = false;

   /**
    * Constructor for DLine.
    */
   public DLine()
   {
      super.setModel(new DLineModel());
   }

   /**
    * Overrides the getKnobs method of the DShape class to work for a DLine.
    *
    * @return knobs - the knobs used for a DLine.
    */
   @Override
   public List<Point> getKnobs()
   {
      this.invertX = ((DLineModel) this.getModel()).getInvertX();
      this.invertY = ((DLineModel) this.getModel()).getInvertY();
      int baseX = getX() - (KNOB_SIZE / 2);
      int baseY = getY() - (KNOB_SIZE / 2);
      int width = getWidth();
      int height = getHeight();
      List<Point> knobs = new ArrayList<>();
      knobs.add(invertX != invertY ? null : new Point(baseX - 1, baseY));
      knobs.add(invertX == invertY ? null : new Point(baseX - 1 + width, baseY));
      knobs.add(invertX == invertY ? null : new Point(baseX - 1, baseY + height));
      knobs.add(invertX != invertY ? null : new Point(baseX - 1 + width, baseY + height));
      return knobs;
   }

   /**
    * Method to draw the line with a a respective color, size, and position.
    *
    * @param g - the graphic to draw.
    */
   public void draw(Graphics g)
   {
      this.invertX = ((DLineModel) this.getModel()).getInvertX();
      this.invertY = ((DLineModel) this.getModel()).getInvertY();
      g.setColor(this.getColor());
      g.drawLine(
            !invertX ? getX() : getX() + getWidth(),
            !invertY ? getY() : getY() + getHeight(),
            !invertX ? getX() + getWidth() : getX(),
            !invertY ? getY() + getHeight() : getY());
   }

   /**
    * Inverts the model with respect to X coordinate.
    */
   public void invertX()
   {
      ((DLineModel) this.getModel()).invertX();
   }

   /**
    * Inverts the model with respect to the Y coordinate.
    */
   public void invertY()
   {
      ((DLineModel) this.getModel()).invertY();
   }
}
