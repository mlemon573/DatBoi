import java.awt.*;

class DLine extends DShape
{
   private boolean invertX = false;
   private boolean invertY = false;

   public DLine()
   {
      super.setModel(new DLineModel());
   }

   /*
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
   */

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.drawLine(
            !invertX ? getX() : getX() + getWidth(),
            !invertY ? getY() : getY() + getHeight(),
            !invertX ? getX() + getWidth() : getX(),
            !invertY ? getY() + getHeight() : getY());
   }

   public boolean getInvertX()
   {
      return invertX;
   }

   public void setInvertX(boolean invertX)
   {
      this.invertX = invertX;
   }

   public boolean getInvertY()
   {
      return invertY;
   }

   public void setInvertY(boolean invertY)
   {
      this.invertY = invertY;
   }
}