import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Whiteboard extends JFrame {
	private static JFileChooser fileChooser;
	// gui elements
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
	private Canvas canvas;
	// menu elements
	private JMenu menuFile;
	private JMenuItem menuFileNew;
	private JMenuItem menuFileOpen;
	private JMenu menuFileSave;
	private JMenuItem menuFileSavePng;
	private JMenuItem menuFileSaveXml;
	private JMenuItem menuFileExit;
	// instance variables
	private int clickedX;
	private int clickedY;
	private boolean resizing = false;
	private boolean moving = false;
	private int saveState;
	private int openState;

	private Whiteboard() {
		buildMenu();
		buildButtonListeners();
	}

	public static void main(String[] args) {
		Whiteboard whiteboard = new Whiteboard();
		whiteboard.setTitle("Whiteboard");
		whiteboard.setContentPane(whiteboard.whiteboardPanel);
		whiteboard.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		whiteboard.pack();
		whiteboard.setLocationRelativeTo(null);
		whiteboard.setVisible(true);
	}

	private void buildMenu() {
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

	private void buildButtonListeners() {
		setColorButton.addActionListener(e -> setSelectedColor());
		menuFileSavePng.addActionListener(e -> savePng());
		menuFileSaveXml.addActionListener(e -> saveXml());
		rectButton.addActionListener(e -> addRect());
		ovalButton.addActionListener(e -> addOval());
		lineButton.addActionListener(e -> addLine());
		textButton.addActionListener(e -> addText());
		menuFileOpen.addActionListener(e -> openFile());
		menuFileNew.addActionListener(e -> clearCanvas());
		moveToFrontButton.addActionListener(e -> canvas.moveToFront());
		moveToBackButton.addActionListener(e -> canvas.moveToBack());
		removeShapeButton.addActionListener(e -> canvas.removeSelected());
		fontBox.addActionListener(e -> setSelectedFont());
		canvas.addMouseListener(new CanvasListener());
		canvas.addMouseMotionListener(new CanvasMotionListener());
	}

	private void clearCanvas() {
		JFrame ensureOption = new JFrame("Clear Screen");
		Box labelBox = new Box(BoxLayout.Y_AXIS);
		Box buttonsBox = new Box(BoxLayout.X_AXIS);
		JLabel warning = new JLabel("Do you want to clear the canvas? Your work will be deleted.");
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");
		buttonsBox.add(yesButton);
		buttonsBox.add(noButton);
		labelBox.add(warning);
		labelBox.add(buttonsBox);
		for (Component comp : labelBox.getComponents()) {
			((JComponent) comp).setAlignmentX(Box.CENTER_ALIGNMENT);
		}
		ensureOption.add(labelBox);
		ensureOption.pack();
		ensureOption.setLocationRelativeTo(null);
		ensureOption.setVisible(true);

		yesButton.addActionListener(e -> {
			canvas.clear();
			canvas.repaint();
			ensureOption.dispose();
		});
		noButton.addActionListener(e -> {
			ensureOption.dispose();
		});
	}

	private void savePng() {
		canvas.setSelected(null);
		canvas.repaint();
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save As...");
		saveState = fileChooser.showSaveDialog(Whiteboard.this);

		if (saveState == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			String desiredSaveLocation = fileToSave.getAbsolutePath();
			int canvasWidth = canvas.getSize().width;
			int canvasHeight = canvas.getSize().height;
			BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
			canvas.paint(image.createGraphics());
			File imageFile = new File(desiredSaveLocation + ".png");
			try {
				imageFile.createNewFile();
				ImageIO.write(image, "png", imageFile);
			} catch (Exception ex) {
				System.out.println("File cannot be saved.");
			}
			fileChooser.setEnabled(false);
		}
	}

	private void openFile() {
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open file");
		openState = fileChooser.showOpenDialog(Whiteboard.this);

		if (openState == JFileChooser.APPROVE_OPTION) {
			File fileToOpen = fileChooser.getSelectedFile().getAbsoluteFile();
			if (fileToOpen != null) {
				try {
					XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(new FileInputStream(fileToOpen)));
					DShapeModel[] models = (DShapeModel[]) xmlIn.readObject();
					xmlIn.close();
					canvas.clear();
					for (DShapeModel model : models) {
						canvas.addShapeFromModel(model);
					}
				} catch (Exception e) {
					System.out.println("File cannot be opened.");
				}
			}
			fileChooser.setEnabled(false);
		}
	}

	private void saveXml() {
		fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save As...");
		saveState = fileChooser.showSaveDialog(Whiteboard.this);

		if (saveState == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				XMLEncoder xmlOut = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
				DShape[] shapes = canvas.getShapesList().toArray(new DShape[0]);
				DShapeModel[] models = new DShapeModel[shapes.length];
				for (int i = 0; i < shapes.length; i++) {
					models[i] = shapes[i].getModel();
				}
				xmlOut.writeObject(models);
				xmlOut.close();
			} catch (Exception ex) {
				System.out.println();
			}
			fileChooser.setEnabled(false);
		}
	}

	private void findShape(int x, int y) {
		canvas.setSelected(canvas.findShape(x, y));
	}

	private void setSelectedColor() {
		ColorChooser.createFrame(canvas.getSelected());
	}

	private void setSelectedFont() {
		if (canvas.getSelected() instanceof DText) {
			DText text = (DText) canvas.getSelected();
			text.setFont(fontBox.getSelectedItem().toString());
			Rectangle2D bounds = text.getFont().getStringBounds(text.getText(),
					new FontRenderContext(new AffineTransform(), true, true));
			int width = (int) bounds.getWidth();
			int height = (int) bounds.getHeight();
			text.setBounds(text.getX(), text.getY(), width, height + 9);
		}
	}

	private void addRect() {
		DRect rect = new DRect();
		int width = 50;
		int height = 50;
		int x = (int) ((canvas.getWidth() - width) * Math.random());
		int y = (int) ((canvas.getHeight() - height) * Math.random());
		rect.setBounds(x, y, width, height);
		canvas.addShape(rect);
	}

	private void addOval() {
		DOval oval = new DOval();
		int width = 50;
		int height = 50;
		int x = (int) ((canvas.getWidth() - width) * Math.random());
		int y = (int) ((canvas.getHeight() - height) * Math.random());
		oval.setBounds(x, y, width, height);
		canvas.addShape(oval);
	}

	private void addLine() {

	}

	private void addText() {
		if (textBox.getText().isEmpty()) {
			return;
		}
		DText text = new DText();
		text.setFont(fontBox.getSelectedItem().toString());
		Rectangle2D bounds = text.getFont().getStringBounds(textBox.getText(),
				new FontRenderContext(new AffineTransform(), true, true));
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();
		int x = (int) ((canvas.getWidth() - width) * Math.random());
		int y = (int) ((canvas.getHeight() - height) * Math.random());
		text.setBounds(x, y, width, height + 9);
		text.setText(textBox.getText());
		canvas.addShape(text);
	}

	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT
	 * edit this method OR call it in your code!
	 */
	private void $$$setupUI$$$() {
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
		fontBox.addItem("Default");
		for (String font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
			fontBox.addItem(font);
		}
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

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return whiteboardPanel;
	}

	private class CanvasListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (canvas.getSelected() != null) {
				clickedX = e.getX();
				clickedY = e.getY();
				Rectangle knob = canvas.findKnob(clickedX, clickedY);
				if (knob != null) {
					resizing = true;
					moving = false;
				} else {
					findShape(e.getX(), e.getY());
					if (canvas.getSelected() != null) {
						resizing = false;
						moving = true;
					}
				}
			} else {
				findShape(e.getX(), e.getY());
			}
		}
	}

	private class CanvasMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (canvas.getSelected() != null) {
				int dx = e.getX() - clickedX;
				int dy = e.getY() - clickedY;
				clickedX = e.getX();
				clickedY = e.getY();
				if (moving) {
					canvas.moveSelected(dx, dy);
				}
				if (resizing) {
					canvas.resizeSelected(e.getX(), e.getY());
				}
			}
		}
	}
}
