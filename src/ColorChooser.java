import javax.swing.*;
import java.awt.*;

/**
 * A frame that contains ways to alter and choose colors of the user's choice.
 */
public class ColorChooser
{
   //GUI elements
   private JPanel colorPanel;
   private JColorChooser colorChooser;
   private JButton confirmButton;
   private JButton cancelButton;

   //The selected shape to change the color of on the canvas.
   private DShape selectedShape;

   /**
    * Constructor for the ColorChooser.
    *
    * @param selectedShape - the shape to change the color of.
    */
   private ColorChooser(DShape selectedShape)
   {
      $$$setupUI$$$();
      this.selectedShape = selectedShape;
      confirmButton.addActionListener(e -> saveColor());
      cancelButton.addActionListener(e -> close(colorPanel));
   }

   /**
    * Creates the frame of color chooser panel.
    *
    * @param selectedShape - the shape to change the color of.
    */
   public static void createFrame(DShape selectedShape)
   {
      JFrame frame = new JFrame();
      frame.setContentPane(new ColorChooser(selectedShape).colorPanel);
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null); //sets the Color Chooser to the middle of
      // one's screen.
      frame.setVisible(true);
   }

   /**
    * Sets the color of the selected shape to the color that the user chose.
    */
   private void saveColor()
   {
      selectedShape.getModel().setColor(colorChooser.getColor());
      close(colorPanel);
   }

   /**
    * Closes the color chooser frame.
    *
    * @param child - the color chooser frame.
    */
   private void close(JPanel child)
   {
      SwingUtilities.getWindowAncestor(child).dispose();
   }

   /**
    * Method generated by IntelliJ IDEA GUI Designer
    * >>> IMPORTANT!! <<<
    * DO NOT edit this method OR call it in your code!
    */
   private void $$$setupUI$$$()
   {
      colorPanel = new JPanel();
      colorPanel.setLayout(new GridBagLayout());
      colorChooser = new JColorChooser();
      GridBagConstraints gbc;
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      colorPanel.add(colorChooser, gbc);
      final JPanel panel1 = new JPanel();
      panel1.setLayout(new GridBagLayout());
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      colorPanel.add(panel1, gbc);
      confirmButton = new JButton();
      confirmButton.setText("Confirm");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      panel1.add(confirmButton, gbc);
      cancelButton = new JButton();
      cancelButton.setText("Cancel");
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      panel1.add(cancelButton, gbc);
   }

   /** @noinspection ALL */
   public JComponent $$$getRootComponent$$$() { return colorPanel; }
}
