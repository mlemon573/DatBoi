import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel implements ModelListener
{
   private List<DShape> shapes;
   private DShape selected;
   private int selectedX;
   private int selectedY;
   private int id;

   public Canvas()
   {
      this(400, 400);
   }

   public Canvas(int width, int height)
   {
      this.setPreferredSize(new Dimension(width, height));
      this.setOpaque(true);
      this.setBackground(Color.white);
      shapes = new ArrayList<>();
      id = 1;
   }

   public void addShape(DShape shape)
   {
      shapes.add(shape);
      shape.addListener(this);
      shape.setID(id++);
      selected = shape;
      repaint();
   }

   public void removeSelected()
   {
      if (shapes.contains(selected))
      {
         shapes.remove(selected);
         selected = null;
         repaint();
      }
   }

   public void clear()
   {
      shapes.clear();
      selected = null;
      repaint();
   }

   public DShape getSelected()
   {
      return this.selected;
   }

   public void setSelected(DShape shape)
   {
      selected = shape;
   }

   public List<DShape> getShapesList()
   {
      return shapes;
   }

   public DShape findShape(int x, int y)
   {
      return null;
   }

   public void moveShape(DShape shape, int newX, int newY)
   {

   }

   @Override
   public void modelChanged(DShapeModel model)
   {
      repaint();
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      for (DShape shape : shapes) {shape.draw(g);}
   }
}
