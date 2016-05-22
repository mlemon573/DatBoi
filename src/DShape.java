import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DShape
{
   //Default values for a shape.
   static final int KNOB_SIZE = 9;
   private DShapeModel model;

   /**
    * Getter method for DShape.
    *
    * @param model - the model to check the type of.
    * @return shape - the type of DShapeModel.
    */
   public static DShape getDShapeFromModel(DShapeModel model)
   {
      DShape shape = null;
      if (model instanceof DRectModel) { shape = new DRect(); }
      else if (model instanceof DOvalModel) { shape = new DOval(); }
      else if (model instanceof DLineModel) { shape = new DLine(); }
      else if (model instanceof DTextModel) { shape = new DText(); }
      if (shape != null) {shape.setModel(model);}
      return shape;
   }

   /**
    * Getter method for DShape.
    *
    * @return the x coordinate of a given model.
    */
   public int getX()
   {
      return model.getX();
   }

   /**
    * Setter method for the x coordinate of a model.
    */
   public void setX(int x)
   {
      model.setX(x);
   }

   /**
    * Getter method for DShape.
    *
    * @return the y coordinate of a given model.
    */
   public int getY()
   {
      return model.getY();
   }

   /**
    * Setter method for the y coordinate of a model.
    */
   public void setY(int y)
   {
      model.setY(y);
   }

   /**
    * Getter method for DShape.
    *
    * @return the width of a given model.
    */
   public int getWidth()
   {
      return model.getWidth();
   }

   /**
    * Setter method for the width of a model.
    */
   public void setWidth(int width)
   {
      model.setWidth(width);
   }

   /**
    * Getter method for DShape.
    *
    * @return the height of a given model.
    */
   public int getHeight()
   {
      return model.getHeight();
   }

   /**
    * Setter method for the height of a model.
    */
   public void setHeight(int height)
   {
      model.setHeight(height);
   }

   /**
    * Getter method for DShape.
    *
    * @return the color of a given model.
    */
   public Color getColor()
   {
      return model.getColor();
   }

   /**
    * Setter method for the color of a model.
    */
   public void setColor(Color color)
   {
      model.setColor(color);
   }

   /**
    * Getter method for DShape.
    *
    * @return the ID of a given model.
    */
   public int getID()
   {
      return model.getID();
   }

   /**
    * Setter method for the ID of a model.
    */
   public void setID(int id)
   {
      model.setID(id);
   }

   /**
    * Setter method for the bounds of a model.
    *
    * @param x      - the x coordinate.
    * @param y      - the y coordinate.
    * @param width  - the width of the shape.
    * @param height - the height of the shape.
    */
   public void setBounds(int x, int y, int width, int height)
   {
      model.setBounds(x, y, width, height);
   }

   /**
    * Getter method for the bounds of a Rectangle.
    *
    * @return the bounds of the model casted as a rectangle.
    */
   public Rectangle getBounds()
   {
      return (Rectangle) model.getBounds().clone();
   }

   /**
    * Tests the equality of a shape to a model.
    *
    * @param shape - the shape to test the validity of.
    * @return whether or not the shape is not null and the models of the shape are equal.
    */
   public boolean equals(DShape shape)
   {
      return shape != null && this.model.equals(shape.getModel());
   }

   /**
    * Method to move the shape by a given amount.
    *
    * @param dx - the x coordinate amount to move the shape by.
    * @param dy - the y coordinate amount to move the shape by.
    */
   public void moveBy(int dx, int dy)
   {
      this.model.moveBy(dx, dy);
   }

   /**
    * Getter method for DShape.
    *
    * @return the model of a given shape.
    */
   public DShapeModel getModel()
   {
      return this.model;
   }

   /**
    * Sets the model of a shape to its' respective model type.
    */
   public void setModel(DShapeModel model)
   {
      this.model = model;
   }

   /**
    * Adds a listener to a model.
    *
    * @param listener - the listener to add to a model.
    */
   public void addListener(ModelListener listener)
   {
      model.addListener(listener);
   }

   /**
    * Method to draw a given shape.
    *
    * @param g - the graphic to draw.
    */
   abstract void draw(Graphics g);

   /**
    * Getter method for the DShape.
    *
    * @return - the knobs of the shape implemented as rectangles.
    */
   List<Point> getKnobs()
   {
      int baseX = getX() - (KNOB_SIZE / 2);
      int baseY = getY() - (KNOB_SIZE / 2);
      int width = getWidth();
      int height = getHeight();

      List<Point> knobs = new ArrayList<>();
      knobs.add(new Point(baseX - 1, baseY));
      knobs.add(new Point(baseX - 1 + width, baseY));
      knobs.add(new Point(baseX - 1, baseY + height));
      knobs.add(new Point(baseX - 1 + width, baseY + height));
      return knobs;
   }
}
