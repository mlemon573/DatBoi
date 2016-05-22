/**
 * Model class for the DText class.
 * To keep track of all the numerical information and properties of the respective shape.
 * To set the Text and Font of DText appropriately.
 */
public class DTextModel extends DShapeModel
{
   //DText elements
   private String text;
   private String font;

   /**
    * Constructor for the DTextModel.
    */
   public DTextModel()
   {
      super();
      this.text = "";
      this.font = null;
   }

   /**
    * Getter method for the text of DTextModel.
    *
    * @return this.text - the text of the DTextModel.
    */
   public String getText()
   {
      return this.text;
   }

   /**
    * Setter method for DTextModel.
    *
    * @param text - the text to set.
    */
   public void setText(String text)
   {
      this.text = text;
      notifyListeners(); //Notifies there has been a change.
   }

   /**
    * Getter method for the font of DTextModel.
    *
    * @return thisfont - the font of the DTextModel.
    */
   public String getFont()
   {
      return this.font;
   }

   /**
    * Setter method for the font of the DTextModel.
    *
    * @param font - the text to set.
    */
   public void setFont(String font)
   {
      this.font = font;
      notifyListeners(); //Notifies there has been a change.
   }
}
