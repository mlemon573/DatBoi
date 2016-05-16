import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Whiteboard extends JFrame
{
   //GUI elements
   private JPanel whiteboardPanel;
   private JButton rectButton;
   private JButton ovalButton;
   private JButton lineButton;
   private JButton textButton;
   private JButton setColorButton;
   private JTextField textBox;
   private JComboBox fontBox;
   private JButton moveToFrontButton;
   private JButton moveToBackButton;
   private JButton removeShapeButton;
   private JMenuBar menuBar;
   //menu elements
   private JMenu menuFile;
   private JMenuItem menuFileNew;
   private JMenuItem menuFileOpen;
   private JMenu menuFileSave;
   private JMenuItem menuFileSavePng;
   private JMenuItem menuFileSaveXml;
   private JMenuItem menuFileExit;
   private static JFileChooser	fileChooser;
   private int saveState;
   //instance variables
   private DShape selectedShape;

   {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
      $$$setupUI$$$();
   }

   private Whiteboard()
   {
      buildMenu();
      setColorButton.addActionListener(e -> ColorChooser.createFrame(selectedShape));
   }

   public static void main(String[] args)
   {
      Whiteboard whiteboard = new Whiteboard();
      whiteboard.setTitle("Whiteboard");
      whiteboard.setContentPane(whiteboard.whiteboardPanel);
      whiteboard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      whiteboard.pack();
      whiteboard.setLocationRelativeTo(null);
      whiteboard.setVisible(true);
   }

   private void buildMenu()
   {
      menuFile = new JMenu("File");
      menuFile.setMnemonic('f');
      menuFileNew = new JMenuItem("New");
      menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
      menuFileNew.setMnemonic('n');
      menuFileOpen = new JMenuItem("Open");
      menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
      menuFileOpen.setMnemonic('o');
      menuFileSave = new JMenu("Save");
      menuFileSave.setMnemonic('s');
      menuFileSavePng = new JMenuItem("PNG");
      menuFileSavePng.setMnemonic('p');
      menuFileSaveXml = new JMenuItem("XML");
      menuFileSaveXml.setMnemonic('x');
      menuFileSave.add(menuFileSavePng);
      menuFileSave.add(menuFileSaveXml);
      menuFileExit = new JMenuItem("Exit");
      menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
      menuFileExit.setMnemonic('e');
      menuFile.add(menuFileNew);
      menuFile.add(menuFileOpen);
      menuFile.addSeparator();
      menuFile.add(menuFileSave);
      menuFile.addSeparator();
      menuFile.add(menuFileExit);
      menuBar.add(menuFile);
   }

   private void buildButtonListeners {
   menuFileSavePng.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         fileChooser = new JFileChooser();
         fileChooser.setDialogTitle("Save As...");
         saveState = fileChooser.showSaveDialog(null);

         if(saveState == fileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            canvas.save(fileToSave);

            String desiredSaveLocation = fileToSave.getAbsolutePath();
            JPanel panel = canvas;
            BufferedImage image = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_RGB);
            panel.paint(image.createGraphics());
            File imageFile = new File(desiredSaveLocation + ".png");
            try {
               imageFile.createNewFile();
               ImageIO.write(image, "png", imageFile);
            }
            catch(Exception ex){ System.out.println("File cannot be saved");}
            
            fileChooser.disable();
         }
      }
   });

   }
   /**
    * Method generated by IntelliJ IDEA GUI Designer
    * >>> IMPORTANT!! <<<
    * DO NOT edit this method OR call it in your code!
    */
   private void $$$setupUI$$$()
   {
      whiteboardPanel = new JPanel();
      whiteboardPanel.setLayout(new BorderLayout(0, 0));
      whiteboardPanel.setAutoscrolls(false);
      final Canvas canvas1 = new Canvas();
      canvas1.setLayout(new BorderLayout(0, 0));
      canvas1.setPreferredSize(new Dimension(400, 400));
      whiteboardPanel.add(canvas1, BorderLayout.CENTER);
      final JPanel panel1 = new JPanel();
      panel1.setLayout(new BorderLayout(0, 0));
      whiteboardPanel.add(panel1, BorderLayout.WEST);
      final JPanel panel2 = new JPanel();
      panel2.setLayout(new GridBagLayout());
      panel1.add(panel2, BorderLayout.NORTH);
      final JPanel panel3 = new JPanel();
      panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      GridBagConstraints gbc;
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel3, gbc);
      final JLabel label1 = new JLabel();
      label1.setText("Add");
      label1.setVerticalAlignment(0);
      panel3.add(label1);
      rectButton = new JButton();
      rectButton.setText("Rect");
      panel3.add(rectButton);
      ovalButton = new JButton();
      ovalButton.setText("Oval");
      panel3.add(ovalButton);
      lineButton = new JButton();
      lineButton.setText("Line");
      panel3.add(lineButton);
      textButton = new JButton();
      textButton.setText("Text");
      panel3.add(textButton);
      final JPanel panel4 = new JPanel();
      panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel4, gbc);
      setColorButton = new JButton();
      setColorButton.setText("Set Color");
      panel4.add(setColorButton);
      final JPanel panel5 = new JPanel();
      panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel5, gbc);
      textBox = new JTextField();
      textBox.setPreferredSize(new Dimension(150, 31));
      panel5.add(textBox);
      fontBox = new JComboBox();
      panel5.add(fontBox);
      final JPanel panel6 = new JPanel();
      panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel6, gbc);
      moveToFrontButton = new JButton();
      moveToFrontButton.setText("Move To Front");
      panel6.add(moveToFrontButton);
      moveToBackButton = new JButton();
      moveToBackButton.setText("Move To Back");
      panel6.add(moveToBackButton);
      removeShapeButton = new JButton();
      removeShapeButton.setText("Remove Shape");
      panel6.add(removeShapeButton);
      menuBar = new JMenuBar();
      whiteboardPanel.add(menuBar, BorderLayout.NORTH);
   }

   /** @noinspection ALL */
   public JComponent $$$getRootComponent$$$() { return whiteboardPanel; }
}
