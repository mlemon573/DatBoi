import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DShapeModel
{
   private static final int DEFAULT_X = 10;
   private static final int DEFAULT_Y = 10;
   private static final int DEFAULT_WIDTH = 20;
   private static final int DEFAULT_HEIGHT = 20;
   //elements that all shapes have.
   private int x;
   private int y;
   private int width;
   private int height;
   private Color color;
   private int id;
   private List<ModelListener> listeners;

   /**
    * Constructor for DShapeModel.
    */
   public DShapeModel()
   {
      this(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.GRAY);
      listeners = new ArrayList<>();
   }

   /**
    * Constructor for DShapeModel.
    *
    * @param x      - the x coordinate.
    * @param y      - the y coordinate.
    * @param width  - the width of the shape.
    * @param height - the height of the shape.
    */
   public DShapeModel(int x, int y, int width, int height)
   {
      this(x, y, width, height, Color.GRAY);
   }

   /**
    * Constructor for the DShapeModel.
    *
    * @param x      - the x coordinate.
    * @param y      - the y coordinate.
    * @param width  - the width of the shape.
    * @param height - the height of the shape.
    * @param color  - the color of the shape.
    */
   public DShapeModel(int x, int y, int width, int height, Color color)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   /**
    * Adds a listener to the model.
    *
    * @param listener - the ModelListener to add.
    */
   public void addListener(ModelListener listener)
   {
      listeners.add(listener);
   }

   /**
    * Notifies the listeners of a change.
    */
   public void notifyListeners()
   {
      for (ModelListener ml : listeners)
      {
         ml.modelChanged(this);
      }
   }

   /**
    * Getter method for the Color.
    *
    * @return - the color of the shape.
    */
   public Color getColor()
   {
      return color;
   }

   /**
    * Setter method for the color.
    *
    * @param color - the color of the shape to set.
    */
   public void setColor(Color color)
   {
      this.color = color;
      notifyListeners();
   }

   /**
    * Getter method for the x coordinate.
    *
    * @return x - the x coordinate.
    */
   public int getX()
   {
      return x;
   }

   /**
    * Setter method for the x coordinate.
    *
    * @param x - the x coordinate.
    */
   public void setX(int x)
   {
      this.x = x;
      notifyListeners();
   }

   /**
    * Getter method for the y value.
    *
    * @return y - the y coordinate.
    */
   public int getY()
   {
      return y;
   }

   /**
    * Setter method for the y coordinate.
    *
    * @param y - the y coordinate.
    */
   public void setY(int y)
   {
      this.y = y;
      notifyListeners();
   }

   /**
    * Getter method for the width of a shape.
    *
    * @return this.width - the width of the shape.
    */
   public int getWidth()
   {
      return this.width;
   }

   /**
    * Setter method for the width of a shape.
    *
    * @param width - the width of the shape.
    */
   public void setWidth(int width)
   {
      this.width = width;
      notifyListeners();
   }

   /**
    * Getter method for the height of a shape.
    *
    * @return height - the height of the shape.
    */
   public int getHeight()
   {
      return this.height;
   }

   /**
    * Setter method for the height of a shape.
    *
    * @param height - the height of the shape.
    */
   public void setHeight(int height)
   {
      this.height = height;
      notifyListeners();
   }

   /**
    * Getter method for the ID of a shape.
    *
    * @return id - the numerical representation of a shape.
    */
   public int getID()
   {
      return id;
   }

   /**
    * Setter method for the ID of a shape.
    */
   public void setID(int id)
   {
      this.id = id;
   }

   /**
    * Moves the shape with respective to new coordinates.
    *
    * @param dx - the amount to change in the x coordinates.
    * @param dy - the amount to change in the y coordinates.
    */
   public void moveBy(int dx, int dy)
   {
      this.x += dx;
      this.y += dy;
      notifyListeners();
   }

   /**
    * Setter method for the DShapeModel.
    *
    * @param x - the x coordinate.
    * @param y - the y coordinate.
    */
   public void setXY(int x, int y)
   {
      this.x = x;
      this.y = y;
      notifyListeners();
   }

   /**
    * Setter method for the DShapeModel.
    *
    * @param x      - the x coordinate.
    * @param y      - the y coordinate.
    * @param width  - the width of a shape.
    * @param height - the height of a shape.
    */
   public void setBounds(int x, int y, int width, int height)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      notifyListeners();
   }


   /**
    * Getter method for the DShapeModel.
    *
    * @return the space that a shape occupies.
    */
   public Rectangle getBounds()
   {
      return (Rectangle) new Rectangle(x, y, width, height).clone();
   }

   /**
    * Checks to see if it is the same model/
    *
    * @param model - the model to check validity of.
    * @return boolean - whether or not it is the model.
    */
   public boolean equals(DShapeModel model)
   {
      if (id == model.getID())
      {
         return true;
      }
      return false;
   }
}
