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
      repaint();
   }

   public int getSelectedIndex()
   {
      for (int i = 0; i < shapes.size(); i++)
      {
         if (shapes.get(i).equals(selected)) {return i;}
      }
      return -1;
   }

   public void moveToFront()
   {
      int i = getSelectedIndex();
      if (i == -1) {return;}
      DShape shape = shapes.remove(i);
      shapes.add(shape);
      repaint();
   }

   public void moveToBack()
   {
      int i = getSelectedIndex();
      if (i == -1) {return;}
      DShape shape = shapes.remove(i);
      shapes.add(0, shape);
      repaint();
   }

   public List<DShape> getShapesList()
   {
      return shapes;
   }

   public DShape findShape(int x, int y)
   {
      for (int i = shapes.size() - 1; i >= 0; i--)
      {
         DShape shape = shapes.get(i);
         if (x >= shape.getX() && x <= shape.getX() + shape.getWidth()
               && y >= shape.getY() && y <= shape.getY() + shape.getHeight())
         {return shapes.get(i);}
      }
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
      for (DShape shape : shapes)
      {
         shape.draw(g);
         if (shape.equals(selected)) {drawKnobs(g);}
      }
   }

   public void drawKnobs(Graphics g)
   {
      int squareWidth = 10;
      if (selected != null)
      {
         List<Point> knobs = selected.getKnobs();
         for (Point p : knobs)
         {
            int x = (int) (p.getX() - squareWidth / 2) - 1;
            int y = (int) (p.getY() - squareWidth / 2);
            g.setColor(Color.WHITE);
            g.fillRect(x, y, squareWidth, squareWidth);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, squareWidth, squareWidth);
         }
      }
   }
}
