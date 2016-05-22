import java.awt.*;

/**
 * Class to handle the drawing and manipulation of text.
 */
public class DText extends DShape
{

   /**
    * Constructor for DText.
    */
   public DText()
   {
      super.setModel(new DTextModel());
   }

   /**
    * Getter method for DText.
    *
    * @return the text as a DTextModel.
    */
   public String getText()
   {
      return ((DTextModel) getModel()).getText();
   }

   /**
    * Setter method for DText
    *
    * @param text - the text to set for the model.
    */
   public void setText(String text)
   {
      DTextModel model = (DTextModel) getModel();
      model.setText(text);
   }

   /**
    * Getter method for DText.
    *
    * @return the font as a DTextModel.
    */

   public String getFont()
   {
      return ((DTextModel) getModel()).getFont();
   }

   /**
    * Setter method for DText.
    *
    * @param font - the font to set for the model.
    */
   public void setFont(String font)
   {
      ((DTextModel) getModel()).setFont(font);
   }

   /**
    * Method to compute the size that the font should have.
    *
    * @param g - the graphic to draw.
    * @return - the font to set for the model.
    */
   public Font computeFont(Graphics g)
   {
      Font font = g.getFont();
      int maxHeight = getModel().getHeight();
      int prevSize = font.getSize();
      for (double size = 1.0; ; size = (size * 1.10) + 1)
      {
         font = new Font(getFont(), Font.PLAIN, (int) size);
         int height = g.getFontMetrics(font).getHeight();
         if (height < maxHeight)
         {
            prevSize = (int) size;
         }
         else
         {
            font = new Font(font.getName(), Font.PLAIN, prevSize);
            return font;
         }
      }
   }

   /**
    * Method to draw DText on the canvas.
    *
    * @param g - the graphic to draw.
    */
   public void draw(Graphics g)
   {
      g.setColor(this.getColor());
      g.setFont(new Font(getFont(), Font.PLAIN, 1));
      g.setFont(computeFont(g));
      Shape clip = g.getClip();
      g.setClip(clip.getBounds().createIntersection(getBounds()));
      g.drawString(getText(), getX(), getY() + g.getFontMetrics().getAscent());
      g.setClip(clip);
   }
}
