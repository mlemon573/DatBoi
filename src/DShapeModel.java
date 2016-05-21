import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DShapeModel
{
   private int x;
   private int y;
   private int width;
   private int height;
   private Color color;
   private int id;
   private List<ModelListener> listeners;

   public DShapeModel()
   {
      this(0, 0, 0, 0, Color.GRAY, 0);
      listeners = new ArrayList<>();
   }

   public DShapeModel(int x, int y, int width, int height, int id)
   {
      this(x, y, width, height, Color.GRAY, 0);
   }

   public DShapeModel(int x, int y, int width, int height, Color color, int id)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   public void addListener(ModelListener listener)
   {
      listeners.add(listener);
   }

   public void notifyListeners()
   {
      for (ModelListener ml : listeners)
      {
         ml.modelChanged(this);
      }
   }

   public Color getColor()
   {
      return color;
   }

   public void setColor(Color color)
   {
      this.color = color;
      notifyListeners();
   }

   public int getX()
   {
      return x;
   }

   public void setX(int x)
   {
      this.x = x;
      notifyListeners();
   }

   public int getY()
   {
      return y;
   }

   public void setY(int y)
   {
      this.y = y;
      notifyListeners();
   }

   public int getWidth()
   {
      return this.width;
   }

   public void setWidth(int width)
   {
      this.width = width;
      notifyListeners();
   }

   public int getHeight()
   {
      return this.height;
   }

   public void setHeight(int height)
   {
      this.height = height;
      notifyListeners();
   }

   public int getID()
   {
      return id;
   }

   public void setID(int id)
   {
      this.id = id;
   }

   public void moveBy(int dx, int dy)
   {
      this.x += dx;
      this.y += dy;
      notifyListeners();
   }

   public void setXY(int x, int y)
   {
      this.x = x;
      this.y = y;
      notifyListeners();
   }

   public void setBounds(int x, int y, int width, int height)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      notifyListeners();
   }

   public Rectangle getBounds()
   {
      return (Rectangle) new Rectangle(x, y, width, height).clone();
   }

   public boolean equals(DShapeModel model)
   {
      if (id == model.getID())
      {
         return true;
      }
      return false;
   }
}