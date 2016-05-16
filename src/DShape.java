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

   public Rectangle getBounds()
   {
      return shapeModel.getBounds();
   }

   public List<Point> getKnobs()
   {
      Rectangle r = shapeModel.getBounds();
      return new ArrayList();
   }
}
