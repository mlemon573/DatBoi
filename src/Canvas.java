import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the canvas (drawing panel). Relates information to the
 * server when changes are made to the canvas.
 */
public class Canvas extends JPanel implements ModelListener, Serializable
{
   // Canvas elements
   static String default_port = "39587";
   static String default_host = "127.0.0.1";
   static String[] cmdList = new String[]{"", "Add", "Remove", "Front", "Back", "Change"};
   private List<DShape> shapes;
   private DShape selected;
   private int sKnob;
   private int id;
   private DataTable dataTable;
   private String mode = "";
   private Client client;
   private Server server;

   /**
    * Constructor for Canvas. Carries a default size.
    */
   public Canvas()
   {
      this(400, 400);
   }

   /**
    * Constructor for Canvas.
    *
    * @param width  - the height of the canvas.
    * @param height - the width of the canvas,
    */
   public Canvas(int width, int height)
   {
      this.setPreferredSize(new Dimension(width, height));
      this.setOpaque(true);
      this.setBackground(Color.white);
      shapes = new ArrayList<>();
      id = 1;
   }

   /**
    * Method to handle the painting of a specific graphic.
    *
    * @param g - the graphic to manipulate.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      for (DShape shape : shapes)
      {
         shape.draw(g);
         if (shape.equals(selected) && !"Client".equals(mode))
         {
            drawKnobs(g);
         }
      }
   }

   /**
    * Draws the knobs with respect to a shape.
    *
    * @param g - the graphic to draw.
    */
   public void drawKnobs(Graphics g)
   {
      if (selected != null)
      {
         for (Point p : selected.getKnobs())
         {
            if (p != null)
            {
               g.setColor(Color.WHITE);
               g.fillRect((int) p.getX(), (int) p.getY(), DShape.KNOB_SIZE, DShape
                     .KNOB_SIZE);

               g.setColor(Color.BLACK);
               g.drawRect((int) p.getX(), (int) p.getY(), DShape.KNOB_SIZE, DShape
                     .KNOB_SIZE);
            }
         }
      }
   }

   /**
    * Method to start the Server,
    */
   void startServer()
   {
      String result = JOptionPane.showInputDialog("Run server on port", default_port);
      if (result != null)
      {
         mode = "Server";
         System.out.println("server: start");
         server = new Server(Integer.parseInt(result));
         server.setCanvas(this);
         server.start();
      }
   }

   /**
    * Method to start the Client Handling.
    *
    * @param whiteboard - the whiteboard to invoke.
    */
   void startClient(Whiteboard whiteboard)
   {
      String result = JOptionPane.showInputDialog("Connect to host:port",
            default_host + ":" + default_port);
      if (result != null)
      {
         mode = "Client";
         String[] res = result.split(":");
         System.out.println("client: start");
         client = new Client(res[0], Integer.parseInt(res[1]));
         client.setCanvas(this);
         client.start();
         whiteboard.disableAllDaThings();
      }
   }

   /**
    * Method to find a shape at a specific x and y coordinate.
    *
    * @param x - the x coordinate.
    * @param y - the y coordinate.
    * @return shapes.get(i) - the shape that is at a particular index.
    */
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

   /**
    * Method to find the knob at a specific x and y coordinate.
    *
    * @param x - the x coordinate.
    * @param y - the y coordinate.
    * @return the knob that exists at the given coordinates or none.
    */
   public Point findKnob(int x, int y)
   {
      for (Point knob : selected.getKnobs())
      {
         if (knob != null && x >= knob.getX() && x <= knob.getX() + DShape.KNOB_SIZE
               && y >= knob.getY() && y <= knob.getY() + DShape.KNOB_SIZE)
         {
            return knob;
         }
      }
      return null;
   }

   /**
    * Adds a newly constructed shape to the canvas.
    *
    * @param shape - the shape to add to the canvas.
    */
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
      if (server != null)
      {
         server.sendToAllClients(1, shape.getModel());
      }
      dataTable.addNewRow(shape.getModel());
   }

   /**
    * Helper to add a shape to the canvas.
    *
    * @param model - the model to add.
    */
   public void addShape(DShapeModel model)
   {
      if (model == null)
      {
         return;
      }
      addShape(DShape.getDShapeFromModel(model));
   }

   /**
    * Moves the shape to the highest layer of the shapes that share a bounds.
    */
   public void moveToFront()
   {
      int i = getSelectedIndex();
      if (i == -1)
      {
         return;
      }
      shapes.add(shapes.remove(i));
      repaint();
      if (server != null)
      {
         server.sendToAllClients(3, selected.getModel());
      }
      dataTable.moveRowUp(selected.getModel());
   }

   /**
    * Moves the shape to the lowest layer of the shapes that share a bounds.
    */
   public void moveToBack()
   {
      int i = getSelectedIndex();
      if (i == -1)
      {
         return;
      }
      shapes.add(0, shapes.remove(i));
      repaint();
      if (server != null)
      {
         server.sendToAllClients(4, selected.getModel());
      }
      dataTable.moveRowDown(selected.getModel());
   }

   /**
    * Setter method to mark which knob has been selected.
    *
    * @param point - a point which represents a knob.
    */
   public void setSelectedKnob(Point point)
   {
      List<Point> knobs = selected.getKnobs();
      boolean set = false;
      for (int i = 0; i < knobs.size(); i++)
      {
         Point p = knobs.get(i);
         if (p != null && p.equals(point))
         {
            setSelectedKnob(i);
            set = true;
         }
      }
      if (!set)
      {
         setSelectedKnob(-1);
      }
   }

   /**
    * Setter method to mark which knob of a shape has been selecyed.
    *
    * @param i - the numerical value that represents a specific knob.
    */
   private void setSelectedKnob(int i)
   {
      sKnob = i;
   }

   /**
    * Getter method to return the currently selected shape.
    *
    * @return this.selected - the selected DShape.
    */
   public DShape getSelected()
   {
      return this.selected;
   }

   /**
    * Setter method to mark the shape as selected.
    *
    * @param shape - the shape to set to selected.
    */
   public void setSelected(DShape shape)
   {
      selected = shape;
      repaint();
   }

   /**
    * Getter method to find the selected shape's index.
    *
    * @return i - the index of the shape if it exists.
    */
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

   /**
    * Method to move the selected shape by a given amount,
    *
    * @param dx - the amount the x value has changed.
    * @param dy - the amount the y value has changed.
    */
   public void moveSelected(int dx, int dy)
   {
      if (selected != null)
      {
         selected.moveBy(dx, dy);
      }
   }

   /**
    * Method to set the bounds of a shape.
    *
    * @param dx - the amount the x value has changed.
    * @param dy - the amount the y value has changed.
    */
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
         if (dx > 0)
         {
            sKnob += 1;
         }
         if (dx < 0)
         {
            sKnob -= 1;
         }
         newX += newWidth;
         newWidth = -newWidth;
         if (selected.getClass().equals(DLine.class))
         {
            ((DLine) selected).invertX();
         }
      }
      if (newHeight < 0)
      {
         if (dy > 0)
         {
            sKnob += 2;
         }
         if (dy < 0)
         {
            sKnob -= 2;
         }
         newY += newHeight;
         newHeight = -newHeight;
         if (selected.getClass().equals(DLine.class))
         {
            ((DLine) selected).invertY();
         }
      }
      selected.setBounds(newX, newY, newWidth, newHeight);
   }

   /**
    * Removes the selected shape and decrements the unique numerical value for
    * the next shape. Sends this information to the clients.
    */
   public void removeSelected()
   {
      if (shapes.contains(selected))
      {
         shapes.remove(selected);
         if (server != null)
         {
            server.sendToAllClients(2, selected.getModel());
         }
         dataTable.removeRow(selected.getModel());
         selected = null;
         repaint();
         id--;
      }
   }

   /**
    * Clears the shapes and repaints the canvas.
    */
   public void clear()
   {
      shapes.clear();
      selected = null;
      id = 1;
      repaint();
   }

   /**
    * Setter method for the data table.
    *
    * @param dataTable - the data table to construct.
    */
   public void setDataTable(DataTable dataTable)
   {
      this.dataTable = dataTable;
   }

   /**
    * Getter method for the mode.
    *
    * @return mode - the mode property that a model has.
    */
   public String getMode()
   {
      return mode;
   }

   /**
    * Getter method for a List<DShape>.
    *
    * @return shapes - a list of all shapes.
    */
   public List<DShape> getShapesList()
   {
      return shapes;
   }

   /**
    * Getter method for a DShape.
    *
    * @param id - the unique numerical value of a shape.
    * @return shape - the shape to return if it exists.
    */
   public DShape getShapeByID(int id)
   {
      for (DShape shape : getShapesList())
      {
         if (shape.getID() == id)
         {
            return shape;
         }
      }
      return null;
   }

   /**
    * Method to listen for a change to the model.
    *
    * @param model - the model who has been changed.
    */
   @Override
   public void modelChanged(DShapeModel model)
   {
      if (server != null)
      {
         server.sendToAllClients(5, model);
      }
      dataTable.updateRow(model);
      repaint();
   }
}
