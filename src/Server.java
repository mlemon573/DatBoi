import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Server class that handles changes. Utilizes threads for utilizing changes/sending
 * them to the clients.
 */
class Server extends Thread
{
   //Server elements.
   private Canvas canvas;
   private List<ObjectOutputStream> outputStreamList;
   private int port;

   /**
    * Constructor for the server.
    *
    * @param port - the port to set the server onto.
    */
   Server(int port)
   {
      this.port = port;
      outputStreamList = new ArrayList<>();
   }

   /**
    * Setter method to set the canvas with respect to the server.
    *
    * @param canvas - the canvas to utilize in the server.
    */
   public void setCanvas(Canvas canvas)
   {
      this.canvas = canvas;
   }

   /**
    * Method that runs the server, allows connection, and sends changes to its' clients.
    */
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

   /**
    * Sends changes to the client.
    *
    * @param out - the data bits to send.
    */
   private synchronized void sendAllToClient(ObjectOutputStream out)
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

   /**
    * Sends changes to the client.
    *
    * @param cmdIndex - the type of changed that has occured.
    * @param target   - the shape that has been changed.
    */
   synchronized void sendToAllClients(int cmdIndex, DShapeModel target)
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
