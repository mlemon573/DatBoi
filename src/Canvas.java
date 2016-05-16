import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel
{
   private List<DShape> shapes;
   private DShape highlighted;
   private int highlightedX;
   private int highlightedY;

   public Canvas()
   {
      this(400, 400);
   }

   public Canvas(int width, int height)
   {
      super();
      shapes = new ArrayList<>();
      this.setPreferredSize(new Dimension(width, height));
      this.setOpaque(true);
      this.setBackground(Color.white);
   }

   public static void main(String... args)
   {
      JFrame a = new JFrame();
      Canvas b = new Canvas();
      a.add(b);
      a.pack();
      a.setVisible(true);
   }

   public void clear()
   {
      shapes.clear();
      highlighted = null;
      repaint();
   }

   public DShape getHighlighted()
   {
      return this.highlighted;
   }

   public void setHighlighted(DShape shape)
   {
      highlighted = shape;
   }

   public DShape findShape(int x, int y)
   {
      return null;
   }

   public void moveShape(DShape shape, int newX, int newY)
   {

   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      for (DShape shape : shapes)
      {
         shape.draw(g);
      }
   }
}
