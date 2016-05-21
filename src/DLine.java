import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class DLine extends DShape
{
   public DLine()
   {
      super.setModel(new DLineModel());
   }

   @Override
   List<Rectangle> getKnobs()
   {
      List<Rectangle> knobs = new ArrayList<>();
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1,
            getY() - (KNOB_SIZE / 2), KNOB_SIZE, KNOB_SIZE));
      knobs.add(null);
      knobs.add(null);
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1 + getWidth(),
            getY() - (KNOB_SIZE / 2) + getHeight(), KNOB_SIZE, KNOB_SIZE));
      return knobs;
   }

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.drawLine(getX(), getY(), getX() + getWidth(), getY() + getHeight());
   }
}