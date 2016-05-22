import javax.swing.*;
import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Canvas extends JPanel implements ModelListener, Serializable
{
   static String default_port = "8001";
   static String default_host = "127.0.0.1";
   String[] cmdList = new String[]{"", "Add", "Remove", "Front", "Back", "Change"};
   private List<DShape> shapes;
   private DShape selected;
   private int sKnob;
   private int id;
   private DataTable dataTable;
   private List<ObjectOutputStream> outputStreamList;
   private String mode;
   private Client client;
   private Server server;

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
      outputStreamList = new ArrayList<>();
      id = 1;
   }

   public void setDataTable(DataTable dataTable)
   {
      this.dataTable = dataTable;
   }

   public void addShape(DShape shape)
   {
      if (shape == null) {return;}
      shapes.add(shape);
      shape.addListener(this);
      shape.setID(id++);
      selected = shape;
      repaint();
      sendToAllRemotes(1, shape.getModel());
      dataTable.addNewRow(shape.getModel());
   }

   public void addShape(DShapeModel model)
   {
      if (model == null) {return;}
      addShape(DShape.getDShapeFromModel(model));
   }

   public void removeSelected()
   {
      if (shapes.contains(selected))
      {
         shapes.remove(selected);
         sendToAllRemotes(2, selected.getModel());
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
      sendToAllRemotes(3, selected.getModel());
      dataTable.moveRowUp(selected.getModel());
   }

   public void moveToBack()
   {
      int i = getSelectedIndex();
      if (i == -1) {return;}
      DShape shape = shapes.remove(i);
      shapes.add(0, shape);
      repaint();
      sendToAllRemotes(4, selected.getModel());
      dataTable.moveRowDown(selected.getModel());
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
      sendToAllRemotes(5, selected.getModel());
      dataTable.updateRow(selected.getModel());
   }

   public void moveSelected(int dx, int dy)
   {
      if (selected != null)
      {
         selected.moveBy(dx, dy);
         repaint();
         sendToAllRemotes(5, selected.getModel());
         dataTable.updateRow(selected.getModel());
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

   public DShape getShapeByID(int id)
   {
      for (DShape shape : getShapesList()) {if (shape.getID() == id) {return shape;}}
      return null;
   }

   public synchronized void applyServerUpdate(int cmdCode, DShapeModel newModel)
   {
      DShape old = getShapeByID(newModel.getID());
      if (cmdCode != 1 && old == null)
      {
         System.out.println("Sync Failed!");
      }
      else if (cmdCode == 1)
      {
         addShape(newModel);
      }
      else if (cmdCode == 2)
      {
         setSelected(old);
         removeSelected();
      }
      else if (cmdCode == 3)
      {
         setSelected(old);
         moveToFront();
      }
      else if (cmdCode == 4)
      {
         setSelected(old);
         moveToBack();
      }
      else if (cmdCode == 5)
      {
         setSelected(old);
         removeSelected();
         addShape(newModel);
         selected.setID(old.getID());
      }
   }

   public synchronized void sendToAllRemotes(int cmdIndex, DShapeModel target)
   {
      if (!"Server".equals(mode)) {return;}
      OutputStream output = new ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(output);

      encoder.writeObject(target);
      encoder.close();
      String xmlString = output.toString();
      Iterator<ObjectOutputStream> osIterator = outputStreamList.iterator();
      while (osIterator.hasNext())
      {
         ObjectOutputStream out = osIterator.next();
         try
         {
            out.writeObject(cmdList[cmdIndex]);
            out.flush();
            out.writeObject(xmlString);
            out.flush();
         }
         catch (Exception e) {e.printStackTrace();}
      }
   }

   public synchronized void sendAllToRemote(ObjectOutputStream out)
   {
      OutputStream output = new ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(output);
      DShapeModel[] models = new DShapeModel[shapes.size()];
      for (int i = 0; i < shapes.size(); i++) {models[i] = shapes.get(i).getModel();}
      encoder.writeObject(models);
      encoder.close();
      String xmlString = output.toString();
      try
      {
         out.writeObject(xmlString);
         out.flush();
      }
      catch (Exception e) {e.printStackTrace();}
   }

   public void startServer()
   {
      mode = "Server";
      String result = JOptionPane.showInputDialog("Run server on port", default_port);
      if (result != null)
      {
         System.out.println("server: start");
         server = new Server(Integer.parseInt(result));
         server.start();
      }
   }

   public void startClient()
   {
      mode = "Client";
      String result = JOptionPane.showInputDialog("Connect to host:port",
            default_host + ":" + default_port);
      if (result != null)
      {
         String[] res = result.split(":");
         System.out.println("client: start");
         client = new Client(res[0], Integer.parseInt(res[1]));
         client.start();
      }
   }

   private class Client extends Thread
   {
      private String host;
      private int port;

      private Client(String host, int port)
      {
         this.host = host;
         this.port = port;
      }

      @Override
      public void run()
      {
         try
         {
            Socket serverSocket = new Socket(host, port);
            ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
            String objString = (String) ois.readObject();
            ByteArrayInputStream ba = new ByteArrayInputStream(objString.getBytes());
            XMLDecoder decoder = new XMLDecoder(ba);
            DShapeModel[] models = (DShapeModel[]) decoder.readObject();
            decoder.close();
            for (DShapeModel model : models) {addShape(model);}

            while (true)
            {
               String cmd = (String) ois.readObject();
               objString = (String) ois.readObject();
               decoder = new XMLDecoder(new ByteArrayInputStream(objString.getBytes()));
               DShapeModel newModel = (DShapeModel) decoder.readObject();
               decoder.close();

               for (int i = 1; i < cmdList.length; i++)
               {if (cmd.equals(cmdList[i])) {applyServerUpdate(i, newModel);}}
            }
         }
         catch (Exception e) {e.printStackTrace();}
      }
   }

   private class Server extends Thread
   {
      private int port;

      Server(int port)
      {
         this.port = port;
      }

      @Override
      public void run()
      {
         try
         {
            ServerSocket server = new ServerSocket(port);

            while (true)
            {
               Socket toClient = server.accept();
               System.out.println("server: client connected");
               ObjectOutputStream newOut =
                     new ObjectOutputStream(toClient.getOutputStream());
               sendAllToRemote(newOut);
               outputStreamList.add(newOut);
            }
         }
         catch (Exception e) {e.printStackTrace();}
      }
   }
}