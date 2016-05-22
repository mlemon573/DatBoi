public class DLineModel extends DShapeModel
{
   private boolean invertX = false;
   private boolean invertY = false;

   public DLineModel()
   {
      super();
   }

   public void invertX()
   {
      invertX = !invertX;
   }

   public void invertY()
   {
      invertY = !invertY;
   }

   public boolean getInvertX()
   {
      return invertX;
   }

   public boolean getInvertY()
   {
      return invertY;
   }
}
