import java.awt.*;

public class DTextModel extends DShapeModel
{
   private String text;
   private Font font;

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

   public Font getFont()
   {
      return this.font;
   }

   public void setFont(String font)
   {
      this.font = new Font(font, Font.PLAIN, 14);
      notifyListeners();
   }
}