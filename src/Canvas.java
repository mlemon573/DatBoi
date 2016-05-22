import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel implements ModelListener, Serializable
{
   private List<DShape> shapes;
   private DShape selected;
   private int sKnob;
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
      if (shape == null)
      {
         return;
      }
      shapes.add(shape);
      shape.addListener(this);
      shape.setID(id++);
      selected = shape;
      repaint();
   }

   public void addShapeFromModel(DShapeModel model)
   {
      DShape shape = null;
      if (model instanceof DRectModel)
      {
         shape = new DRect();
      }
      else if (model instanceof DOvalModel)
      {
         shape = new DOval();
      }
      else if (model instanceof DLineModel)
      {
         shape = new DLine();
      }
      else if (model instanceof DTextModel)
      {
         shape = new DText();
      }
      if (shape != null && model != null)
      {
         shape.setModel(model);
         addShape(shape);
      }
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

   public void setSelectedKnob(Rectangle rect)
   {
      List<Rectangle> knobs = selected.getKnobs();
      boolean set = false;
      for (int i = 0; i < knobs.size(); i++)
      {
         Rectangle r = knobs.get(i);
         if (r != null && r.equals(rect))
         {
            setSelectedKnob(i);
            set = true;
         }
      }
      if (!set) {setSelectedKnob(-1);}
   }

   private void setSelectedKnob(int i)
   {
      sKnob = i;
   }

   public int getSelectedIndex()
   {
      for (int i = 0; i < shapes.size(); i++)
      {
         if (shapes.get(i).equals(selected))
         {
            return i;
         }
      }
      return -1;
   }

   public void moveToFront()
   {
      int i = getSelectedIndex();
      if (i == -1)
      {
         return;
      }
      DShape shape = shapes.remove(i);
      shapes.add(shape);
      repaint();
   }

   public void moveToBack()
   {
      int i = getSelectedIndex();
      if (i == -1)
      {
         return;
      }
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
         {
            return shapes.get(i);
         }
      }
      return null;
   }

   public Rectangle findKnob(int x, int y)
   {
      for (Rectangle knob : selected.getKnobs())
      {
         if (knob != null && x >= knob.getX() && x <= knob.getX() + DShape.KNOB_SIZE
               && y >= knob.getY() && y <= knob.getY() + DShape.KNOB_SIZE) {return knob;}
      }
      return null;
   }

   public void resizeSelected(int dx, int dy)
   {
      Rectangle bounds = selected.getBounds();
      int newX = (int) bounds.getX();
      int newY = (int) bounds.getY();
      int newWidth = (int) bounds.getWidth();
      int newHeight = (int) bounds.getHeight();

      switch (sKnob)
      {
         case 0:
            newX += dx;
            newWidth -= dx;
            newY += dy;
            newHeight -= dy;
            break;
         case 1:
            newWidth += dx;
            newY += dy;
            newHeight -= dy;
            break;
         case 2:
            newX += dx;
            newWidth -= dx;
            newHeight += dy;
            break;
         case 3:
            newWidth += dx;
            newHeight += dy;
            break;
      }
      if (newWidth < 0)
      {
         if (dx > 0) {sKnob += 1;}
         if (dx < 0) {sKnob -= 1;}
         newX += newWidth;
         newWidth = -newWidth;
         if (selected.getClass().equals(DLine.class)) {((DLine) selected).invertX();}
      }
      if (newHeight < 0)
      {
         if (dy > 0) {sKnob += 2;}
         if (dy < 0) {sKnob -= 2;}
         newY += newHeight;
         newHeight = -newHeight;
         if (selected.getClass().equals(DLine.class)) {((DLine) selected).invertY();}
      }

      /*
      System.out.printf("x: %d, %d, %d%n", dx, newX, newWidth);
      System.out.printf("y: %d, %d, %d%n%n", dy, newY, newHeight);
      */

      selected.setBounds(newX, newY, newWidth, newHeight);
      repaint();
   }

   public void moveSelected(int dx, int dy)
   {
      if (selected != null)
      {
         selected.moveBy(dx, dy);
         repaint();
      }
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
      if (selected != null)
      {
         for (Rectangle r : selected.getKnobs())
         {
            if (r != null)
            {
               g.setColor(Color.WHITE);
               g.fillRect((int) r.getX(), (int) r.getY(), DShape.KNOB_SIZE, DShape
                     .KNOB_SIZE);

               g.setColor(Color.BLACK);
               g.drawRect((int) r.getX(), (int) r.getY(), DShape.KNOB_SIZE, DShape
                     .KNOB_SIZE);
            }
         }
      }
   }
}
