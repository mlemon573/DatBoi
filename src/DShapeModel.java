import java.awt.*;

public class DShapeModel
{
   private int x;
   private int y;
   private int width;
   private int height;
   private Color color;

   public DShapeModel()
   {
      this(0, 0, 0, 0, Color.GRAY);
   }

   public DShapeModel(int x, int y, int width, int height)
   {
      this(x, y, width, height, Color.GRAY);
   }

   public DShapeModel(int x, int y, int width, int height, Color color)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.color = color;
   }

   public Color getColor()
   {
      return color;
   }

   public void setColor(Color color)
   {
      this.color = color;
   }

   public void moveBy(int dx, int dy)
   {
      x += dx;
      y += dy;
   }

   public void setXY(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public void setBounds(int x, int y, int width, int height)
   {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public Rectangle getBounds()
   {
      return new Rectangle(x, y, width, height);
   }

   public boolean equals(DShapeModel model)
   {
      if (getClass().equals(model.getClass()))
      {
         if (this.getBounds().equals(model.getBounds())
               && color.equals(model.getColor())) {return true;}
      }
      return false;
   }
}

class DRectModel extends DShapeModel
{
   public DRectModel()
   {
      super();
   }
}

class DOvalModel extends DShapeModel
{
   public DOvalModel()
   {
      super();
   }
}

class DLineModel extends DShapeModel
{
   public DLineModel()
   {
      super();
   }
}

class DTextModel extends DShapeModel
{
   public DTextModel()
   {
      super();
   }
}