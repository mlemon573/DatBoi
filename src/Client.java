import java.awt.*;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Class that handles takes in information from the server and displays it.
 */
class Client extends Thread
{
   // Client elements.
   private Canvas canvas;
   private String host;
   private int port;

   /**
    * Constructor for the Client.
    *
    * @param host - the host to connection to.
    * @param port - the port on which we are connection.
    */
   public Client(String host, int port)
   {
      this.host = host;
      this.port = port;
   }

   /**
    * Setter method for the Canvas.
    *
    * @param canvas - the canvas to initialize.
    */
   public void setCanvas(Canvas canvas)
   {
      this.canvas = canvas;
   }

   /**
    * Runs the client by reading in and displaying data through a stream.
    */
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
         for (DShapeModel model : models)
         {
            canvas.addShape(model);
         }

         while (true)
         {
            try
            {
               String cmd = (String) ois.readObject();
               objString = (String) ois.readObject();
               decoder = new XMLDecoder(new ByteArrayInputStream(objString.getBytes()));
               DShapeModel newModel = (DShapeModel) decoder.readObject();
               decoder.close();

               for (int i = 1; i < Canvas.cmdList.length; i++)
               {
                  if (cmd.equals(Canvas.cmdList[i]))
                  {
                     applyServerUpdate(i, newModel);
                  }
               }
            }
            catch (Exception ex)
            {
               System.out.println("Server disconnected.");
               break;
               // ex.printStackTrace();
            }
         }
      }
      catch (Exception e)
      {
         System.out.println("There is no server to connect to on this port.");
         return;
         // e.printStackTrace();
      }
   }

   /**
    * Applies updates that are read in from the server.
    *
    * @param cmdCode  - the type of change that has occurred.
    * @param newModel - the new shape model that has been resulted.
    */
   private synchronized void applyServerUpdate(int cmdCode, DShapeModel newModel)
   {
      DShape old = canvas.getShapeByID(newModel.getID());
      if (cmdCode != 1 && old == null)
      {
         System.out.println("Sync Failed!");
      }
      else if (cmdCode == 1) // A shape has been added on the server.
      {
         canvas.addShape(newModel); // Display in client.
      }
      else if (cmdCode == 2) // A shape has been removed on the server.
      {
         canvas.setSelected(old);
         canvas.removeSelected(); // Display in client.
      }
      else if (cmdCode == 3) // A shape has been moved to the front.
      {
         canvas.setSelected(old);
         canvas.moveToFront(); // Display in client.
      }
      else if (cmdCode == 4) // A shape has been moved to the back.
      {
         canvas.setSelected(old);
         canvas.moveToBack(); // Display in client.
      }
      else if (cmdCode == 5) // A shape has been moved, resized, or had its'
      // color change.
      {
         canvas.setSelected(old);
         Rectangle bounds = newModel.getBounds();
         int x = (int) bounds.getX();
         int y = (int) bounds.getY();
         int width = (int) bounds.getWidth();
         int height = (int) bounds.getHeight();
         old.setBounds(x, y, width, height);
         old.setColor(newModel.getColor());
         DShapeModel oldModel = old.getModel();
         if (oldModel instanceof DLineModel && newModel instanceof DLineModel)
         {
            ((DLineModel) oldModel).setInvertX(((DLineModel) newModel).getInvertX());
            ((DLineModel) oldModel).setInvertY(((DLineModel) newModel).getInvertY());
         }
         if (oldModel instanceof DTextModel && newModel instanceof DTextModel)
         {
            ((DTextModel) oldModel).setText(((DTextModel) newModel).getText());
            ((DTextModel) oldModel).setFont(((DTextModel) newModel).getFont());
         }
      }
   }
}
