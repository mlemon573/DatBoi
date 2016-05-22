public class DTextModel extends DShapeModel
{
   /**
    *
    */
   private static final long serialVersionUID = -586348795511193029L;
   private String text;
   private String font;

   public DTextModel()
   {
      super();
      this.text = "";
      this.font = null;
   }

   public String getText()
   {
      return this.text;
   }

   public void setText(String text)
   {
      this.text = text;
      notifyListeners();
   }

   public String getFont()
   {
      return this.font;
   }

   public void setFont(String font)
   {
      this.font = font;
      notifyListeners();
   }
}
