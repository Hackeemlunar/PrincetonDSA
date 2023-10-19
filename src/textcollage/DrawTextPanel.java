package textcollage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.xml.stream.events.StartDocument;

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
    boolean animationToggle;

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

            JMenu animationMenu = new JMenu("Animation");
            optionsMenu.setMnemonic('A');
            menuBar.add(animationMenu);

            JMenuItem start = new JMenuItem("Start...");
            start.addActionListener(menuHandler);
            JMenuItem stop = new JMenuItem("Stop...");
            stop.addActionListener(menuHandler);
            animationMenu.add(start);
            animationMenu.add(stop);

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
            case "Start...":
                animationToggle = true;
                animations();
                break;
            case "Stop...":
                animationToggle = false;
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
        strings.clear();
        File file = fileChooser.getInputFile();
        fileChooser.getInputFile(this, "Select File to Open");
        DrawTextItem item;
        try (Scanner input = new Scanner(file)){
            String line = input.nextLine();
            String[] word = line.split("-");
            System.out.println(word[0]);
            canvas.setBackground(new Color(Integer.parseInt(word[0]), Integer.parseInt(word[1]),
                    Integer.parseInt(word[2]))); // Sets the background of the image
            // Get the data of the image stored as text. Each line represent one image
            while (input.hasNext()) {
                line = input.nextLine();
                item = parsePicture(line);
                strings.add(item);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        } finally {
            canvas.repaint();
        }

    }

    private static DrawTextItem parsePicture(String line) {
        String[] words = line.split("-");
        String textString = words[0];
        double magnification = Double.parseDouble(words[1]);
        double rotationAngle = Double.parseDouble(words[2]);
        double backgroundTransparency = Double.parseDouble(words[3]);
        int red = Integer.parseInt(words[4]);
        int green = Integer.parseInt(words[5]);
        int blue = Integer.parseInt(words[6]);
        int xcoord = Integer.parseInt(words[7]);
        int ycoord = Integer.parseInt(words[8]);
        return drawTextItem(textString,magnification,rotationAngle,backgroundTransparency,
                red,green,blue,xcoord,ycoord);
    }

    private static DrawTextItem drawTextItem(String textString, double magnification, double rotationAngle,
                                             double backgroundTransparency, int red, int green,
                                             int blue, int xcoord, int ycoord) {
        var item = new DrawTextItem(textString);
        Color color = new Color(red,green,blue);
        item.setTextColor(color);
        item.setX(xcoord);
        item.setY(ycoord);
        item.setMagnification(magnification);
        item.setRotationAngle(rotationAngle);
        item.setBackgroundTransparency(backgroundTransparency);
        return item;
    }

    private void saveFile() {
        File saveFile = fileChooser.getOutputFile(this, "Select where to save the image");
        StringBuilder builder = new StringBuilder();
        try(PrintWriter writer = new PrintWriter(saveFile)) {
            String canvasBackground = canvas.getBackground().getRed() + "-"+canvas.getBackground().getBlue()+"-"+
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



    public void animations(){
        strings.clear();
        List<String> words = getFileContents();
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            String textString = words.get(random.nextInt(words.size()));
            createItems(random, textString);
            words.remove(textString);
        }

        while ( animationToggle) {
            String textString = words.get(random.nextInt(words.size()));
            createItems(random, textString);
            canvas.repaint();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }



    private void createItems(Random random, String textString) {
        double magnification = random.nextDouble(2.0);
        double rotationAngle = random.nextDouble(360.0);
        double backgroundTransparency = random.nextDouble(1.0);
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        int xcoord = canvas.getX() / random.nextInt(1,10);
        int ycoord = canvas.getY() / random.nextInt(1,10);
        DrawTextItem item = drawTextItem(textString,magnification,rotationAngle,backgroundTransparency,
                red,green,blue,xcoord,ycoord);

        Color color = new Color(random.nextInt(100,255), random.nextInt(150,255),
                random.nextInt(200,255));
        canvas.setBackground(color);

        strings.add(item);
    }

    // Gets the words in a file selected by a user for animations
    private List<String> getFileContents() {
        File file = fileChooser.getInputFile();
        fileChooser.getInputFile(this, "Select File to Open");
        DrawTextItem item;
        List<String> texts = new ArrayList<>(50);
        try (Scanner input = new Scanner(file)){
            String line = input.nextLine();
            while (input.hasNext()) {
                texts.addAll(Arrays.asList(line.split(" ")));
                line = input.nextLine();
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.toString());
        }
        return texts;
    }
}

