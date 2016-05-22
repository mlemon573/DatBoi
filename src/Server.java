import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server extends Thread
{
   private Canvas canvas;
   private List<ObjectOutputStream> outputStreamList;

   private int port;

   Server(int port)
   {
      this.port = port;
      outputStreamList = new ArrayList<>();
   }

   public void setCanvas(Canvas canvas)
   {
      this.canvas = canvas;
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
            sendAllToClient(newOut);
            outputStreamList.add(newOut);
         }
      }
      catch (Exception e) {e.printStackTrace();}
   }

   public synchronized void sendAllToClient(ObjectOutputStream out)
   {
      OutputStream output = new ByteArrayOutputStream();
      XMLEncoder encoder = new XMLEncoder(output);
      List<DShape> shapes = canvas.getShapesList();
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

   public synchronized void sendToAllClients(int cmdIndex, DShapeModel target)
   {
      if (!"Server".equals(canvas.getMode())) {return;}
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
            out.writeObject(Canvas.cmdList[cmdIndex]);
            out.flush();
            out.writeObject(xmlString);
            out.flush();
         }
         catch (Exception e) {e.printStackTrace();}
      }
   }
}