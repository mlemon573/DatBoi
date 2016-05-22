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

   public void setInvertX(boolean invertX)
   {
      this.invertX = invertX;
   }

   public boolean getInvertY()
   {
      return invertY;
   }

   public void setInvertY(boolean invertY)
   {
      this.invertY = invertY;
   }
}
