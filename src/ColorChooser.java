import javax.swing.*;
import java.awt.*;

public class ColorChooser
{
   private JPanel colorPanel;
   private JColorChooser colorChooser;
   private JButton confirmButton;
   private JButton cancelButton;

   private DShape selectedShape;

   {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
      $$$setupUI$$$();
   }

   private ColorChooser(DShape selectedShape)
   {
      this.selectedShape = selectedShape;
      confirmButton.addActionListener(e -> saveColor());
      cancelButton.addActionListener(e -> close(colorPanel));
   }

   public static void createFrame(DShape selectedShape)
   {
      JFrame frame = new JFrame();
      frame.setContentPane(new ColorChooser(selectedShape).colorPanel);
      frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   private void saveColor()
   {
      selectedShape.getModel().setColor(colorChooser.getColor());
      close(colorPanel);
   }

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
