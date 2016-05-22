import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DLine extends DShape
{
   private boolean invertX = false;
   private boolean invertY = false;

   public DLine()
   {
      super.setModel(new DLineModel());
   }

   @Override
   public List<Rectangle> getKnobs()
   {
      int baseX = getX() - (KNOB_SIZE / 2);
      int baseY = getY() - (KNOB_SIZE / 2);
      int width = getWidth();
      int height = getHeight();
      List<Rectangle> knobs = new ArrayList<>();
      knobs.add(invertX != invertY ? null : new Rectangle(
            baseX - 1, baseY, KNOB_SIZE, KNOB_SIZE));
      knobs.add(invertX == invertY ? null : new Rectangle(
            baseX - 1 + width, baseY, KNOB_SIZE, KNOB_SIZE));
      knobs.add(invertX == invertY ? null : new Rectangle(
            baseX - 1, baseY + height, KNOB_SIZE, KNOB_SIZE));
      knobs.add(invertX != invertY ? null : new Rectangle(
            baseX - 1 + width, baseY + height, KNOB_SIZE, KNOB_SIZE));
      return knobs;
   }

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.drawLine(
            !invertX ? getX() : getX() + getWidth(),
            !invertY ? getY() : getY() + getHeight(),
            !invertX ? getX() + getWidth() : getX(),
            !invertY ? getY() + getHeight() : getY());
   }

   public void invertX()
   {
      invertX = !invertX;
   }

   public void invertY()
   {
      invertY = !invertY;
   }
}
