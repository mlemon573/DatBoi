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
