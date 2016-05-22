import java.awt.*;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

class Client extends Thread
{
   private Canvas canvas;

   private String host;
   private int port;

   public Client(String host, int port)
   {
      this.host = host;
      this.port = port;
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
         Socket serverSocket = new Socket(host, port);
         ObjectInputStream ois = new ObjectInputStream(serverSocket.getInputStream());
         String objString = (String) ois.readObject();
         ByteArrayInputStream ba = new ByteArrayInputStream(objString.getBytes());
         XMLDecoder decoder = new XMLDecoder(ba);
         DShapeModel[] models = (DShapeModel[]) decoder.readObject();
         decoder.close();
         for (DShapeModel model : models) {canvas.addShape(model);}

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
                  {applyServerUpdate(i, newModel);}
               }
            }
            catch (Exception ex) {ex.printStackTrace();}
         }
      }
      catch (Exception e) {e.printStackTrace();}
   }

   public synchronized void applyServerUpdate(int cmdCode, DShapeModel newModel)
   {
      DShape old = canvas.getShapeByID(newModel.getID());
      if (cmdCode != 1 && old == null)
      {
         System.out.println("Sync Failed!");
      }
      else if (cmdCode == 1)
      {
         canvas.addShape(newModel);
      }
      else if (cmdCode == 2)
      {
         canvas.setSelected(old);
         canvas.removeSelected();
      }
      else if (cmdCode == 3)
      {
         canvas.setSelected(old);
         canvas.moveToFront();
      }
      else if (cmdCode == 4)
      {
         canvas.setSelected(old);
         canvas.moveToBack();
      }
      else if (cmdCode == 5)
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