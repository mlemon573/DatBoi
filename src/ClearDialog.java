import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A dialog screen for controlling whether or not the Canvas is cleared of all contents.
 */
public class ClearDialog extends JDialog
{
   //GUI elements.
   private JPanel contentPane;
   private JButton buttonOK;
   private JButton buttonCancel;

   private Canvas canvas;
   private DataTable dataTable;

   /**
    * Constructor for the Clear Dialog.
    * @param canvas -  the canvas to be cleared of all elements.
    * @param dataTable - the data table to be cleared of all elements.
     */
   public ClearDialog(Canvas canvas, DataTable dataTable)
   {
      $$$setupUI$$$();
      this.canvas = canvas;
      this.dataTable = dataTable;

      setTitle("Clear Screen");
      setContentPane(contentPane);
      setModal(true); //Can't click behind the Clear Dialog frame.
      getRootPane().setDefaultButton(buttonOK);

      buttonOK.addActionListener(e -> onOK());
      buttonCancel.addActionListener(e -> onCancel());

      setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter()
      {
         public void windowClosing(WindowEvent e) {onCancel();}
      });

      contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

   }

   /**
    * Clears the canvas, resets the data table of all elements.
    */
   private void onOK()
   {
      canvas.clear();
      canvas.repaint();
      dataTable.reset();
      dispose();
   }

   /**
    * Closes the clear dialog frame.
    */
   private void onCancel()
   {
      dispose();
   }

   /**
    * Method generated by IntelliJ IDEA GUI Designer
    * >>> IMPORTANT!! <<<
    * DO NOT edit this method OR call it in your code!
    */
   private void $$$setupUI$$$()
   {
      contentPane = new JPanel();
      contentPane.setLayout(new GridBagLayout());
      final JPanel panel1 = new JPanel();
      panel1.setLayout(new GridBagLayout());
      GridBagConstraints gbc;
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.anchor = GridBagConstraints.EAST;
      gbc.fill = GridBagConstraints.VERTICAL;
      gbc.insets = new Insets(5, 5, 5, 5);
      contentPane.add(panel1, gbc);
      buttonOK = new JButton();
      buttonOK.setText("OK");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(0, 0, 0, 5);
      panel1.add(buttonOK, gbc);
      buttonCancel = new JButton();
      buttonCancel.setText("Cancel");
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(0, 5, 0, 0);
      panel1.add(buttonCancel, gbc);
      final JPanel panel2 = new JPanel();
      panel2.setLayout(new GridBagLayout());
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.insets = new Insets(5, 5, 0, 5);
      contentPane.add(panel2, gbc);
      final JLabel label1 = new JLabel();
      label1.setText("Do you want to clear the canvas?");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      panel2.add(label1, gbc);
      final JLabel label2 = new JLabel();
      label2.setText("Your work will be deleted.");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      panel2.add(label2, gbc);
   }

   /** @noinspection ALL */
   public JComponent $$$getRootComponent$$$() { return contentPane; }
}
