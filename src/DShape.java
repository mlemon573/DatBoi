import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DShape
{
   private DShapeModel shapeModel;

   DShape()
   {
      shapeModel = new DShapeModel();
   }

   DShape(int x, int y, int width, int height)
   {
      shapeModel = new DShapeModel(x, y, width, height);
   }

   DShape(int x, int y, int width, int height, Color color)
   {
      shapeModel = new DShapeModel(x, y, width, height, color);
   }

   public DShapeModel getShapeModel()
   {
      return shapeModel;
   }

   public void setShapeModel(DShapeModel shapeModel)
   {
      this.shapeModel = shapeModel;
   }

   abstract void draw(Graphics g);

   protected Rectangle getBounds()
   {
      return shapeModel.getBounds();
   }

   protected List<Point> getKnobs()
   {
      Rectangle r = shapeModel.getBounds();
      List<Point> knobs = new ArrayList<>();
      knobs.add(new Point((int) (r.getX()), (int) (r.getY())));
      knobs.add(new Point((int) (r.getX() + r.getWidth()), (int) r.getY()));
      knobs.add(new Point((int) (r.getX()), (int) (r.getY() + r.getHeight())));
      knobs.add(new Point((int) (r.getX() + r.getWidth()), (int) (r.getY()
            + r.getHeight())));
      return knobs;
   }
}

class DRect extends DShape
{
   public DRect()
   {
      super.setShapeModel(new DRectModel());
   }

   public void draw(Graphics g)
   {
      g.setColor(getShapeModel().getColor());
      Rectangle r = getBounds();
      int x = (int) r.getX();
      int y = (int) r.getY();
      int width = (int) r.getWidth();
      int height = (int) r.getHeight();
      g.drawRect(x, y, width, height);
   }
}