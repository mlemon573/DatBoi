import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JPanel implements ModelListener, Serializable
{
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
    * Constructors
    */

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

   /**
    * Drawing Stuff
    */

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      for (DShape shape : shapes)
      {
         shape.draw(g);
         if (shape.equals(selected) && !"Client".equals(mode)) {drawKnobs(g);}
      }
   }

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
    * Networking stuff
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
    * Find stuff
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

   public Point findKnob(int x, int y)
   {
      for (Point knob : selected.getKnobs())
      {
         if (knob != null && x >= knob.getX() && x <= knob.getX() + DShape.KNOB_SIZE
               && y >= knob.getY() && y <= knob.getY() + DShape.KNOB_SIZE) {return knob;}
      }
      return null;
   }

   /**
    * Add shape
    */

   public void addShape(DShape shape)
   {
      if (shape == null) {return;}
      shapes.add(shape);
      shape.addListener(this);
      shape.setID(id++);
      selected = shape;
      repaint();
      if (server != null) {server.sendToAllClients(1, shape.getModel());}
      dataTable.addNewRow(shape.getModel());
   }

   public void addShape(DShapeModel model)
   {
      if (model == null) {return;}
      addShape(DShape.getDShapeFromModel(model));
   }

   /**
    * Move to front / back
    */

   public void moveToFront()
   {
      int i = getSelectedIndex();
      if (i == -1) {return;}
      shapes.add(shapes.remove(i));
      repaint();
      if (server != null) {server.sendToAllClients(3, selected.getModel());}
      dataTable.moveRowUp(selected.getModel());
   }

   public void moveToBack()
   {
      int i = getSelectedIndex();
      if (i == -1) {return;}
      shapes.add(0, shapes.remove(i));
      repaint();
      if (server != null) {server.sendToAllClients(4, selected.getModel());}
      dataTable.moveRowDown(selected.getModel());
   }

   /**
    * Select knob
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
      if (!set) {setSelectedKnob(-1);}
   }

   private void setSelectedKnob(int i)
   {
      sKnob = i;
   }

   /**
    * Selected getter / setter (and index)
    */

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

   /**
    * Move and Resize
    */

   public void moveSelected(int dx, int dy)
   {
      if (selected != null) {selected.moveBy(dx, dy);}
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

      selected.setBounds(newX, newY, newWidth, newHeight);
   }

   /**
    * Remove
    */

   public void removeSelected()
   {
      if (shapes.contains(selected))
      {
         shapes.remove(selected);
         if (server != null) {server.sendToAllClients(2, selected.getModel());}
         dataTable.removeRow(selected.getModel());
         selected = null;
         repaint();
         id--;
      }
   }

   public void clear()
   {
      shapes.clear();
      selected = null;
      repaint();
   }

   /**
    * Various getters and setters
    */

   public void setDataTable(DataTable dataTable)
   {
      this.dataTable = dataTable;
   }

   public String getMode()
   {
      return mode;
   }

   public List<DShape> getShapesList()
   {
      return shapes;
   }

   public DShape getShapeByID(int id)
   {
      for (DShape shape : getShapesList()) {if (shape.getID() == id) {return shape;}}
      return null;
   }

   /**
    * Model change listener
    */

   @Override
   public void modelChanged(DShapeModel model)
   {
      if (server != null) {server.sendToAllClients(5, model);}
      dataTable.updateRow(model);
      repaint();
   }
}