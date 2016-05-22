import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Whiteboard extends JFrame
{
   private static final long serialVersionUID = 4103330820691022210L;
   private static final String[] FONTS =
         GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
   private static JFileChooser fileChooser = new JFileChooser();
   // gui elements
   private JPanel whiteboardPanel;
   private JButton rectButton;
   private JButton ovalButton;
   private JButton lineButton;
   private JButton textButton;
   private JButton setColorButton;
   private JTextField textBox;
   private JComboBox<String> fontBox;
   private JButton moveToFrontButton;
   private JButton moveToBackButton;
   private JButton removeShapeButton;
   private JMenuBar menuBar;
   private Canvas canvas;
   private JTable tableContainer;
   private JScrollPane scrollPane;
   private JPanel isServerPanel;
   private JPanel isClientPanel;
   // menu elements
   private JMenu menuFile;
   private JMenuItem menuFileNew;
   private JMenuItem menuFileOpen;
   private JMenu menuFileSave;
   private JMenuItem menuFileSavePng;
   private JMenuItem menuFileSaveXml;
   private JMenuItem menuFileExit;
   private JMenu menuNetwork;
   private JMenuItem menuNetworkStartServer;
   private JMenuItem menuNetworkJoinServer;
   // instance variables
   private int clickedX;
   private int clickedY;
   private boolean resizing = false;
   private boolean moving = false;
   private int saveState;
   private int openState;
   private DataTable dataTable;
   private boolean dirty;

   private Whiteboard()
   {
      $$$setupUI$$$();
      canvas.setDataTable(dataTable);
      buildMenu();
      buildButtonListeners();
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
      menuFileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
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

      menuNetwork = new JMenu("Network");
      menuNetwork.setMnemonic('n');
      menuNetworkStartServer = new JMenuItem("Start Server");
      menuNetworkStartServer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_MASK));
      menuNetworkStartServer.setMnemonic('s');
      menuNetworkJoinServer = new JMenuItem("Join Server");
      menuNetworkJoinServer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
      menuNetworkJoinServer.setMnemonic('c');
      menuNetwork.add(menuNetworkStartServer);
      menuNetwork.add(menuNetworkJoinServer);
      menuBar.add(menuNetwork);
   }

   private void buildButtonListeners()
   {
      setColorButton.addActionListener(e -> setSelectedColor());
      menuFileSavePng.addActionListener(e -> savePng());
      menuFileSaveXml.addActionListener(e -> saveXml());
      rectButton.addActionListener(e -> addRect());
      ovalButton.addActionListener(e -> addOval());
      lineButton.addActionListener(e -> addLine());
      textButton.addActionListener(e -> addText());
      menuFileOpen.addActionListener(e -> openFile());
      menuFileNew.addActionListener(e -> clearCanvas());
      menuFileExit.addActionListener(e -> confirmExit());
      moveToFrontButton.addActionListener(e -> moveToFront());
      moveToBackButton.addActionListener(e -> moveToBack());
      removeShapeButton.addActionListener(e -> removeSelected());
      menuNetworkStartServer.addActionListener(e -> startServer());
      menuNetworkJoinServer.addActionListener(e -> startClient());
      textBox.addKeyListener(new TextChangeListener());
      fontBox.addActionListener(e -> setSelectedFont());
      canvas.addMouseListener(new CanvasListener());
      canvas.addMouseMotionListener(new CanvasMotionListener());
   }

   private void moveToFront()
   {
      canvas.moveToFront();
      dirty = true;
   }

   private void moveToBack()
   {
      canvas.moveToBack();
      dirty = true;
   }

   private void removeSelected()
   {
      canvas.removeSelected();
      dirty = true;
   }

   private void startServer()
   {
      isServerPanel.setVisible(true);
      canvas.startServer();
   }

   private void startClient()
   {
      isClientPanel.setVisible(true);
      canvas.startClient();
      disableAllDaThings();
   }

   private void disableAllDaThings()
   {
      rectButton.setEnabled(false);
      ovalButton.setEnabled(false);
      lineButton.setEnabled(false);
      textButton.setEnabled(false);
      setColorButton.setEnabled(false);
      textBox.setEnabled(false);
      fontBox.setEnabled(false);
      moveToFrontButton.setEnabled(false);
      moveToBackButton.setEnabled(false);
      removeShapeButton.setEnabled(false);
      canvas.setEnabled(false);
      tableContainer.setEnabled(false);
   }

   private void confirmExit()
   {
      ExitDialog dialog = new ExitDialog(this, dirty, canvas.getMode());
      dialog.pack();
      dialog.setLocationRelativeTo(null);
      dialog.setVisible(true);
   }

   private void clearCanvas()
   {
      ClearDialog dialog = new ClearDialog(canvas, dataTable);
      dialog.pack();
      dialog.setLocationRelativeTo(null);
      dialog.setVisible(true);
   }

   private void savePng()
   {
      canvas.setSelected(null);
      canvas.repaint();
      fileChooser.setDialogTitle("Save As...");
      saveState = fileChooser.showSaveDialog(Whiteboard.this);

      if (saveState == JFileChooser.APPROVE_OPTION)
      {
         File fileToSave = fileChooser.getSelectedFile();
         String desiredSaveLocation = fileToSave.getAbsolutePath();
         int canvasWidth = canvas.getSize().width;
         int canvasHeight = canvas.getSize().height;
         BufferedImage image =
               new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
         canvas.paint(image.createGraphics());
         File imageFile = new File(desiredSaveLocation + ".png");
         try
         {
            imageFile.createNewFile();
            ImageIO.write(image, "png", imageFile);
         }
         catch (Exception ex)
         {
            System.out.println("File cannot be saved.");
         }
         fileChooser.setEnabled(false);
      }
   }

   private void openFile()
   {
      fileChooser.setDialogTitle("Open file");
      openState = fileChooser.showOpenDialog(Whiteboard.this);

      if (openState == JFileChooser.APPROVE_OPTION)
      {
         File fileToOpen = fileChooser.getSelectedFile().getAbsoluteFile();
         if (fileToOpen != null)
         {
            try
            {
               XMLDecoder xmlIn =
                     new XMLDecoder(new BufferedInputStream(new FileInputStream(fileToOpen)));
               DShapeModel[] models = (DShapeModel[]) xmlIn.readObject();
               xmlIn.close();
               canvas.clear();
               for (DShapeModel model : models)
               {
                  canvas.addShape(model);
               }
            }
            catch (Exception e)
            {
               System.out.println("File cannot be opened.");
            }
         }
         fileChooser.setEnabled(false);
      }
   }

   private void saveXml()
   {
      fileChooser.setDialogTitle("Save As...");
      saveState = fileChooser.showSaveDialog(Whiteboard.this);

      if (saveState == JFileChooser.APPROVE_OPTION)
      {
         File file = fileChooser.getSelectedFile();
         try
         {
            XMLEncoder xmlOut =
                  new XMLEncoder(new BufferedOutputStream(new FileOutputStream(
                        file + ".xml")));
            DShape[] shapes = canvas.getShapesList().toArray(new DShape[0]);
            DShapeModel[] models = new DShapeModel[shapes.length];
            for (int i = 0; i < shapes.length; i++)
            {
               models[i] = shapes[i].getModel();
            }
            xmlOut.writeObject(models);
            xmlOut.close();
            dirty = false;
         }
         catch (Exception e) {e.printStackTrace();}
         fileChooser.setEnabled(false);
      }
   }

   private void findShape(int x, int y)
   {
      canvas.setSelected(canvas.findShape(x, y));
      textCheck();
   }

   private void setSelectedColor()
   {
      ColorChooser.createFrame(canvas.getSelected());
      dirty = true;
   }

   private void setSelectedFont()
   {
      if (canvas.getSelected() instanceof DText)
      {
         DText text = (DText) canvas.getSelected();
         text.setFont(fontBox.getSelectedItem().toString());
         text.setBounds(text.getX(), text.getY(), text.getWidth(), text.getHeight());
      }
      dirty = true;
   }

   private void addRect()
   {
      DRect rect = new DRect();
      rect.setBounds(DShape.DEFAULT_X, DShape.DEFAULT_Y, DShape.DEFAULT_WIDTH, DShape.DEFAULT_HEIGHT);
      canvas.addShape(rect);
      textCheck();
      dirty = true;
   }

   private void addOval()
   {
      DOval oval = new DOval();
      oval.setBounds(DShape.DEFAULT_X, DShape.DEFAULT_Y, DShape.DEFAULT_WIDTH, DShape.DEFAULT_HEIGHT);
      canvas.addShape(oval);
      textCheck();
      dirty = true;
   }

   private void addLine()
   {
      DLine line = new DLine();
      line.setBounds(DShape.DEFAULT_X, DShape.DEFAULT_Y, DShape.DEFAULT_WIDTH, DShape.DEFAULT_HEIGHT);
      canvas.addShape(line);
      textCheck();
      dirty = true;
   }

   private void addText()
   {
      if (textBox.getText().isEmpty()) {return;}
      DText text = new DText();
      text.setFont(fontBox.getSelectedItem().toString());
      text.setBounds(DShape.DEFAULT_X, DShape.DEFAULT_Y, DShape.DEFAULT_WIDTH, DShape.DEFAULT_HEIGHT);
      text.setText(textBox.getText());
      canvas.addShape(text);
      dirty = true;
   }

   private void textCheck()
   {
      if (!"Client".equals(canvas.getMode()) && (canvas.getSelected() == null
            || canvas.getSelected() instanceof DText))
      {
         textBox.setEnabled(true);
         fontBox.setEnabled(true);
      }
      else
      {
         textBox.setEnabled(false);
         fontBox.setEnabled(false);
      }
      if (canvas.getSelected() instanceof DText)
      {
         DText text = (DText) canvas.getSelected();
         textBox.setText(text.getText());
         for (int i = 1; i < FONTS.length; i++)
         {
            if (text.getFont().equals(FONTS[i]))
            {
               fontBox.setSelectedIndex(i);
               break;
            }
         }
      }
   }

   private void createUIComponents()
   {
      fontBox = new JComboBox<>();
      for (int i = 0; i < FONTS.length; i++)
      {
         fontBox.addItem(FONTS[i]);
         if (FONTS[i].equals("Dialog")) {fontBox.setSelectedIndex(i);}
      }
      dataTable = new DataTable();
      tableContainer = new JTable(dataTable);
      scrollPane = new JScrollPane();
      scrollPane.setViewportView(tableContainer);
   }

   /**
    * Method generated by IntelliJ IDEA GUI Designer
    * >>> IMPORTANT!! <<<
    * DO NOT edit this method OR call it in your code!
    */
   private void $$$setupUI$$$()
   {
      createUIComponents();
      whiteboardPanel = new JPanel();
      whiteboardPanel.setLayout(new BorderLayout(0, 0));
      whiteboardPanel.setAutoscrolls(false);
      canvas = new Canvas();
      canvas.setLayout(new BorderLayout(0, 0));
      canvas.setPreferredSize(new Dimension(400, 400));
      whiteboardPanel.add(canvas, BorderLayout.CENTER);
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
      panel4.setLayout(new GridBagLayout());
      panel3.add(panel4);
      isServerPanel = new JPanel();
      isServerPanel.setLayout(new GridBagLayout());
      isServerPanel.setVisible(false);
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      panel4.add(isServerPanel, gbc);
      final JLabel label2 = new JLabel();
      label2.setText("Server");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      isServerPanel.add(label2, gbc);
      isClientPanel = new JPanel();
      isClientPanel.setLayout(new GridBagLayout());
      isClientPanel.setVisible(false);
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      panel4.add(isClientPanel, gbc);
      final JLabel label3 = new JLabel();
      label3.setText("Client");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.WEST;
      isClientPanel.add(label3, gbc);
      final JPanel panel5 = new JPanel();
      panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel5, gbc);
      setColorButton = new JButton();
      setColorButton.setText("Set Color");
      panel5.add(setColorButton);
      final JPanel panel6 = new JPanel();
      panel6.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel6, gbc);
      textBox = new JTextField();
      textBox.setPreferredSize(new Dimension(150, 31));
      textBox.setText("Hello");
      panel6.add(textBox);
      panel6.add(fontBox);
      final JPanel panel7 = new JPanel();
      panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.fill = GridBagConstraints.BOTH;
      panel2.add(panel7, gbc);
      moveToFrontButton = new JButton();
      moveToFrontButton.setText("Move To Front");
      panel7.add(moveToFrontButton);
      moveToBackButton = new JButton();
      moveToBackButton.setText("Move To Back");
      panel7.add(moveToBackButton);
      removeShapeButton = new JButton();
      removeShapeButton.setText("Remove Shape");
      panel7.add(removeShapeButton);
      scrollPane.setHorizontalScrollBarPolicy(31);
      scrollPane.setPreferredSize(new Dimension(300, 200));
      scrollPane.setVisible(true);
      panel1.add(scrollPane, BorderLayout.CENTER);
      scrollPane.setViewportView(tableContainer);
      menuBar = new JMenuBar();
      whiteboardPanel.add(menuBar, BorderLayout.NORTH);
   }

   /** @noinspection ALL */
   public JComponent $$$getRootComponent$$$() { return whiteboardPanel; }

   private class CanvasListener extends MouseAdapter
   {
      @Override
      public void mousePressed(MouseEvent e)
      {
         clickedX = e.getX();
         clickedY = e.getY();
         if (canvas.getSelected() != null && !"Client".equals(canvas.getMode()))
         {
            Rectangle knob = canvas.findKnob(clickedX, clickedY);
            canvas.setSelectedKnob(knob);
            if (knob != null)
            {
               resizing = true;
               moving = false;
            }
            else
            {
               findShape(e.getX(), e.getY());
               resizing = false;
               moving = true;
            }
         }
         else {findShape(e.getX(), e.getY());}
      }
   }

   private class CanvasMotionListener extends MouseMotionAdapter
   {
      @Override
      public void mouseDragged(MouseEvent e)
      {
         if (canvas.getSelected() != null)
         {
            int dx = e.getX() - clickedX;
            int dy = e.getY() - clickedY;
            clickedX = e.getX();
            clickedY = e.getY();
            if (moving) {canvas.moveSelected(dx, dy);}
            if (resizing) {canvas.resizeSelected(dx, dy);}
            dirty = true;
         }
      }
   }

   private class TextChangeListener implements KeyListener
   {
      @Override
      public void keyPressed(KeyEvent arg0)
      {
      }

      @Override
      public void keyReleased(KeyEvent arg0)
      {
         DText text = (DText) canvas.getSelected();
         text.setText(textBox.getText());
         dirty = true;
      }

      @Override
      public void keyTyped(KeyEvent arg0)
      {
      }
   }
}
