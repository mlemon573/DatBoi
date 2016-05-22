/**
 * Model class for the DLine class.
 * To keep track of all the numerical information and properties of the respective shape.
 * To keep track of whether or not a line is inverted on the canvas and adjust it
 * relatively.
 */
public class DLineModel extends DShapeModel
{
   //Booleans to check for inversion on both tails of the line.
   private boolean invertX = false;
   private boolean invertY = false;

   /**
    * Constructor for the DLineModel.
    */
   public DLineModel()
   {
      super();
   }

   /**
    * Inverts the invertX boolean value.
    */
   public void invertX()
   {
      invertX = !invertX;
   }

   /**
    * Inverts the invertY boolean value.
    */
   public void invertY()
   {
      invertY = !invertY;
   }

   /**
    * Getter method for the invertX boolean.
    *
    * @return invertX - whether or not invertX is true or false.
    */
   public boolean getInvertX()
   {
      return invertX;
   }

   /**
    * Setter method for the invertX boolean.
    */
   public void setInvertX(boolean invertX)
   {
      this.invertX = invertX;
   }

   /**
    * Getter method for the invertY boolean.
    *
    * @return invertY - whether or not invertY is true or false.
    */
   public boolean getInvertY()
   {
      return invertY;
   }

   /**
    * Setter method for the invertY boolean.
    */
   public void setInvertY(boolean invertY)
   {
      this.invertY = invertY;
   }
}
