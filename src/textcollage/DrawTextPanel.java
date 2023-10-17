package textcollage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * A panel that contains a large drawing area where strings
 * can be drawn. The strings are represented by objects of
 * type DrawTextItem. An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 */
public class DrawTextPanel extends JPanel {

    private ArrayList<DrawTextItem> strings = new ArrayList<DrawTextItem>(); // ArrayList to store multiple text items

    private Color currentTextColor = Color.BLACK; // Color applied to new strings.

    private Canvas canvas; // the drawing area.
    private JTextField input; // where the user inputs the string that will be added to the canvas
    private SimpleFileChooser fileChooser; // for letting the user select files
    private JMenuBar menuBar; // a menu bar with command that affect this panel
    private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
    private JMenuItem undoMenuItem; // the "Remove Item" command from the edit menu

    /**
     * An object of type Canvas is used for the drawing area.
     * The canvas simply displays all the DrawTextItems that
     * are stored in the ArrayList, strings.
     */
    private class Canvas extends JPanel {
        Canvas() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.LIGHT_GRAY);
            setFont(new Font("Serif", Font.BOLD, 24));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            for (DrawTextItem item : strings) {
                item.draw(g); // Draw each text item in the ArrayList
            }
        }
    }

    /**
     * An object of type MenuHandler is registered as the ActionListener
     * for all the commands in the menu bar. The MenuHandler object
     * simply calls doMenuCommand() when the user selects a command
     * from the menu.
     */
    private class MenuHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            doMenuCommand(evt.getActionCommand());
        }
    }

    /**
     * Creates a DrawTextPanel. The panel has a large drawing area and
     * a text input box where the user can specify a string. When the
     * user clicks the drawing area, the string is added to the drawing
     * area at the point where the user clicked.
     */
    public DrawTextPanel() {
        fileChooser = new SimpleFileChooser();
        undoMenuItem = new JMenuItem("Remove Item");
        undoMenuItem.setEnabled(false);
        menuHandler = new MenuHandler();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Text to add: "));
        input = new JTextField("Hello World!", 40);
        bottom.add(input);
        add(bottom, BorderLayout.SOUTH);
        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                doMousePress(e);
            }
        });
    }

    /**
     * This method is called when the user clicks the drawing area.
     * A new string is added to the drawing area. The center of
     * the string is at the point where the user clicked.
     *
     * @param e the mouse event that was generated when the user clicked
     */
    public void doMousePress(MouseEvent e) {
        String text = input.getText().trim();
        if (text.isEmpty()) {
            input.setText("Hello World!");
            text = "Hello World!";
        }
        DrawTextItem newItem = new DrawTextItem(text, e.getX(), e.getY());
        newItem.setTextColor(currentTextColor);
        strings.add(newItem); // Add the new text item to the ArrayList
        undoMenuItem.setEnabled(true);
        canvas.repaint();
    }

    /**
     * Returns a menu bar containing commands that affect this panel. The menu
     * bar is meant to appearat the top of the application window.
     *
     * @return the menu bar for this panel
     */
    public JMenuBar getMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();

            JMenu fileMenu = new JMenu("File");
            fileMenu.setMnemonic('F');
            menuBar.add(fileMenu);

            JMenuItem saveItem = new JMenuItem("Save...");
            saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
            saveItem.setMnemonic('S');
            saveItem.addActionListener(menuHandler);
            fileMenu.add(saveItem);

            JMenuItem openItem = new JMenuItem("Open...");
            openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
            openItem.setMnemonic('O');
            openItem.addActionListener(menuHandler);
            fileMenu.add(openItem);

            JMenu editMenu = new JMenu("Edit");
            editMenu.setMnemonic('E');
            menuBar.add(editMenu);

            undoMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
            undoMenuItem.setMnemonic('U');
            undoMenuItem.addActionListener(menuHandler);
            editMenu.add(undoMenuItem);

            JMenuItem clearItem = new JMenuItem("Clear");
            clearItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
            clearItem.setMnemonic('C');
            clearItem.addActionListener(menuHandler);
            editMenu.add(clearItem);

            JMenu optionsMenu = new JMenu("Options");
            optionsMenu.setMnemonic('P');
            menuBar.add(optionsMenu);

            JMenuItem colorItem = new JMenuItem("Text Color...");
            colorItem.setAccelerator(KeyStroke.getKeyStroke("ctrl T"));
            colorItem.setMnemonic('T');
            colorItem.addActionListener(menuHandler);
            optionsMenu.add(colorItem);
        }
        return menuBar;
    }

    /**
     * Carry out one of the commands from the menu bar.
     *
     * @param command the text of the menu command.
     */
    public void doMenuCommand(String command) {
        switch (command) {
            case "Save...":
                saveFile();
                break;
            case "Open...":
                openFile();
                break;
            case "Remove Item":
                removeItem();
                break;
            case "Clear":
                clearItems();
                break;
            case "Text Color...":
                changeColor();
                break;
        }

    }

    private void changeColor() {
        Color newColor = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
        if (newColor != null) {
            currentTextColor = newColor;
        }
    }

    private void clearItems() {
        strings.clear();
        undoMenuItem.setEnabled(false);
        canvas.repaint();
    }

    private void removeItem() {
        if (!strings.isEmpty()) {
            strings.remove(strings.size() - 1);
            canvas.repaint();
            if (strings.isEmpty()) {
                undoMenuItem.setEnabled(false);
            }
        }
    }

    private void openFile() {
        var fileChooser = new SimpleFileChooser();
        fileChooser.getInputFile(this, "Select File to Open");
    }

    private void saveFile() {
        File saveFile = fileChooser.getOutputFile(this, "Select where to save the image");
        StringBuilder builder = new StringBuilder();
        try(PrintWriter writer = new PrintWriter(saveFile)) {
            String canvasBackground = canvas.getBackground().getRed() + " "+canvas.getBackground().getBlue()+" "+
                    canvas.getBackground().getBlue()+"\n";
            writer.write(canvasBackground);
            for (DrawTextItem item : strings) {
                builder.append(item.getString());
                builder.append("-");
                builder.append(item.getMagnification() );
                builder.append("-");
                builder.append(item.getRotationAngle() );
                builder.append("-");
                builder.append(item.getBackgroundTransparency() );
                builder.append("-");
                builder.append(item.getTextColor().getRed());
                builder.append("-");
                builder.append(item.getTextColor().getGreen());
                builder.append("-");
                builder.append(item.getTextColor().getBlue());
                builder.append("-");
                builder.append(item.getX());
                builder.append("-");
                builder.append(item.getY());
                builder.append("\n");
                writer.write(builder.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

