import java.awt.*;

public class DText extends DShape
{

   public DText()
   {
      super.setModel(new DTextModel());
   }

   public String getText()
   {
      return ((DTextModel) getModel()).getText();
   }

   public void setText(String text)
   {
      DTextModel model = (DTextModel) getModel();
      model.setText(text);
   }

   public Font getFont()
   {
      return ((DTextModel) getModel()).getFont();
   }

   public void setFont(String font)
   {
      ((DTextModel) getModel()).setFont(font);
   }

   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.setFont(getFont());
      g.drawString(getText(), getX(), getY() + getHeight() - 6);
   }
}