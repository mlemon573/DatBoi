import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DShape
{
   static final int KNOB_SIZE = 9;
   private DShapeModel model;

   public DShape() {}

   public int getX()
   {
      return model.getX();
   }

   public void setX(int x)
   {
      model.setX(x);
   }

   public int getY()
   {
      return model.getY();
   }

   public void setY(int y)
   {
      model.setY(y);
   }

   public int getWidth()
   {
      return model.getWidth();
   }

   public void setWidth(int width)
   {
      model.setWidth(width);
   }

   public int getHeight()
   {
      return model.getHeight();
   }

   public void setHeight(int height)
   {
      model.setHeight(height);
   }

   public Color getColor()
   {
      return model.getColor();
   }

   public void setColor(Color color)
   {
      model.setColor(color);
   }

   public int getID()
   {
      return model.getID();
   }

   public void setID(int id)
   {
      model.setID(id);
   }

   public void setBounds(int x, int y, int width, int height)
   {
      model.setBounds(x, y, width, height);
   }

   public Rectangle getBounds()
   {
      return (Rectangle) model.getBounds().clone();
   }

   public boolean equals(DShape shape)
   {
      return shape != null && this.model.equals(shape.getModel());
   }

   public void moveBy(int dx, int dy)
   {
      this.model.moveBy(dx, dy);
   }

   public DShapeModel getModel()
   {
      return this.model;
   }

   public void setModel(DShapeModel model)
   {
      this.model = model;
   }

   public void addListener(ModelListener listener)
   {
      model.addListener(listener);
   }

   abstract void draw(Graphics g);

   List<Rectangle> getKnobs()
   {
      List<Rectangle> knobs = new ArrayList<>();
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1,
            getY() - (KNOB_SIZE / 2), KNOB_SIZE, KNOB_SIZE));
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1 + getWidth(),
            getY() - (KNOB_SIZE / 2), KNOB_SIZE, KNOB_SIZE));
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1,
            getY() - (KNOB_SIZE / 2) + getHeight(), KNOB_SIZE, KNOB_SIZE));
      knobs.add(new Rectangle(
            getX() - (KNOB_SIZE / 2) - 1 + getWidth(),
            getY() - (KNOB_SIZE / 2) + getHeight(), KNOB_SIZE, KNOB_SIZE));
      return knobs;
   }
}