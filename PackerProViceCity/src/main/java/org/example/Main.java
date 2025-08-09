package org.example;
import org.example.PackerUnpacker.MainPacker;
import org.example.PackerUnpacker.MainUnpacker;


import com.formdev.flatlaf.FlatDarkLaf;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * =================================================================================
 * Main Class - The Entry Point of the Application
 * =================================================================================
 */
public class Main {
    public static void main(String[] args) {
        // It's best practice to run all Swing applications on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Load all resources first
            ResourceLoader.load();
            // Set up the main application frame
            new MainFrame().setVisible(true);
        });
    }
}

/**
 * =================================================================================
 * ResourceLoader - Utility Class
 * Handles loading all external assets like fonts, images, and sounds.
 * This centralizes resource management, making the code cleaner.
 * =================================================================================
 */
class ResourceLoader {
    public static Font pricedownFont;
    public static Image backgroundImage;
    private static Clip musicClip; // Centralized clip for background music control

    public static void load() {
        pricedownFont = loadFont("/Pricedown.otf");
        backgroundImage = loadImage("/live_background.gif");
    }

    private static Font loadFont(String path) {
        try (InputStream in = ResourceLoader.class.getResourceAsStream(path)) {
            if (in == null) throw new IOException("Font not found at: " + path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, in);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, 28); // Return a fallback font
        }
    }

    private static Image loadImage(String path) {
        URL imgUrl = ResourceLoader.class.getResource(path);
        if (imgUrl != null) {
            return new ImageIcon(imgUrl).getImage();
        } else {
            System.err.println("Error: Background image not found in resources folder: " + path);
            return null;
        }
    }

    public static void playSound(String path, float volume, Runnable onFinish) {
        try (InputStream in = ResourceLoader.class.getResourceAsStream("/" + path)) {
            if (in == null) { System.err.println("Sound not found: " + path); return; }
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(in)) {
                Clip clip = AudioSystem.getClip();
                // Add a listener that runs the onFinish action when the sound stops
                if (onFinish != null) {
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                            onFinish.run();
                        }
                    });
                }
                clip.open(audioIn);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playBackgroundMusic(String path, float volume) {
        try (InputStream in = ResourceLoader.class.getResourceAsStream("/" + path)) {
            if (in == null) { System.err.println("Music not found: " + path); return; }
            try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(in)) {
                musicClip = AudioSystem.getClip();
                musicClip.open(audioIn);
                FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    public static void startBackgroundMusic() {
        if (musicClip != null && !musicClip.isRunning()) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}

/**
 * =================================================================================
 * UIFactory - Utility Class
 * Creates styled UI components to ensure a consistent look and feel.
 * =================================================================================
 */
class UIFactory {
    public static JButton createViceCityButton(String text) {
        JButton button = new RoundedButton(text); // Use the new RoundedButton
        button.setFont(ResourceLoader.pricedownFont.deriveFont(28f));
        button.setForeground(Color.decode("#EFEFEF"));
        button.setFocusPainted(false);
        button.addActionListener(e -> ResourceLoader.playSound("onclick.wav", 0.9f, null));
        return button;
    }

    public static JTextField createViceCityTextField() {
        JTextField textField = new JTextField(30);
        textField.setBackground(new Color(0, 0, 0, 102)); // 40% opaque black
        textField.setForeground(Color.decode("#00FFFF")); // Cyan text
        textField.setCaretColor(Color.WHITE); // White cursor
        textField.setOpaque(false);
        Border line = BorderFactory.createLineBorder(Color.decode("#FF69B4"));
        Border padding = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        textField.setBorder(BorderFactory.createCompoundBorder(line, padding));
        return textField;
    }

    public static JLabel createShadowLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.decode("#00FFFF"));
        return label;
    }
}

/**
 * =================================================================================
 * MainFrame - The main window of the application
 * =================================================================================
 */
class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContentPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Vice City Packer Unpacker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // This line removes the window's title bar and borders.
        setUndecorated(true);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(900, 700);
        setLocationRelativeTo(null);

        ResourceLoader.playBackgroundMusic("vice_city_theme.wav", 0.7f);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        mainContentPanel.setOpaque(false);

        MenuPanel menuPanel = new MenuPanel(this);
        PackerPanel packerPanel = new PackerPanel(this);
        UnpackerPanel unpackerPanel = new UnpackerPanel(this);

        mainContentPanel.add(menuPanel, "MENU");
        mainContentPanel.add(packerPanel, "PACKER");
        mainContentPanel.add(unpackerPanel, "UNPACKER");

        backgroundPanel.add(mainContentPanel);
        setContentPane(backgroundPanel);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainContentPanel, panelName);
    }
}

/**
 * =================================================================================
 * BackgroundPanel - A custom JPanel that scales the background image to fit.
 * =================================================================================
 */
class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ResourceLoader.backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(ResourceLoader.backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        } else {
            // Fallback if the image fails to load, preventing a blank screen
            g.setColor(new Color(20, 20, 30)); // Dark retro blue
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

/**
 * =================================================================================
 * RoundedButton - A custom JButton with soft, rounded edges.
 * =================================================================================
 */
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255, 105, 180, 51)); // 20% opaque pink
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}


/**
 * =================================================================================
 * MenuPanel - The initial screen with main options
 * =================================================================================
 */
class MenuPanel extends JPanel {
    public MenuPanel(MainFrame mainFrame) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("PACKER / UNPACKER");
        titleLabel.setFont(ResourceLoader.pricedownFont.deriveFont(50f));
        titleLabel.setForeground(Color.decode("#FF69B4"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        JButton packButton = UIFactory.createViceCityButton("PACK FILES");
        packButton.addActionListener(e -> mainFrame.showPanel("PACKER"));
        add(packButton, gbc);

        JButton unpackButton = UIFactory.createViceCityButton("UNPACK FILES");
        unpackButton.addActionListener(e -> mainFrame.showPanel("UNPACKER"));
        add(unpackButton, gbc);

        JButton exitButton = UIFactory.createViceCityButton("EXIT");
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton, gbc);
    }
}

/**
 * =================================================================================
 * AbstractFunctionalPanel - A base class for Packer and Unpacker panels
 * =================================================================================
 */
abstract class AbstractFunctionalPanel extends JPanel {
    protected MainFrame mainFrame;
    protected JTextField field1;
    protected JTextField field2;
    protected JTextArea statusArea = new JTextArea(10, 50);
    protected JProgressBar progressBar = new JProgressBar();
    protected List<Component> componentsToDisable = new ArrayList<>();

    public AbstractFunctionalPanel(MainFrame frame, String type, String label1Text, String label2Text, String buttonText) {
        this.mainFrame = frame;
        setOpaque(false);
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel(type);
        titleLabel.setFont(ResourceLoader.pricedownFont.deriveFont(40f));
        titleLabel.setForeground(Color.decode("#00FFFF"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        field1 = UIFactory.createViceCityTextField();
        field2 = UIFactory.createViceCityTextField();

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END; fieldsPanel.add(UIFactory.createShadowLabel(label1Text), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START; fieldsPanel.add(field1, gbc);
        JButton browse1 = UIFactory.createViceCityButton("...");
        browse1.setFont(ResourceLoader.pricedownFont.deriveFont(16f));
        gbc.gridx = 2; gbc.weightx = 0; fieldsPanel.add(browse1, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END; fieldsPanel.add(UIFactory.createShadowLabel(label2Text), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.LINE_START; fieldsPanel.add(field2, gbc);
        JButton browse2 = UIFactory.createViceCityButton("...");
        browse2.setFont(ResourceLoader.pricedownFont.deriveFont(16f));
        gbc.gridx = 2; gbc.weightx = 0; fieldsPanel.add(browse2, gbc);

        JPanel statusWrapper = new JPanel(new BorderLayout());
        statusWrapper.setOpaque(false);
        Border lineBorder = BorderFactory.createLineBorder(Color.decode("#FF69B4"));
        statusWrapper.setBorder(BorderFactory.createTitledBorder(lineBorder, "ACTIVITY STATUS", TitledBorder.LEFT, TitledBorder.TOP, ResourceLoader.pricedownFont.deriveFont(16f), Color.decode("#FF69B4")));

        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        statusArea.setForeground(Color.decode("#33FF33"));
        statusArea.setBackground(new Color(0, 50, 20, 220));
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        statusWrapper.add(scrollPane, BorderLayout.CENTER);

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        statusWrapper.add(progressBar, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(fieldsPanel, BorderLayout.NORTH);
        centerPanel.add(statusWrapper, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JButton actionButton = UIFactory.createViceCityButton(buttonText);
        JButton backButton = UIFactory.createViceCityButton("BACK");
        backButton.addActionListener(e -> mainFrame.showPanel("MENU"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(actionButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupActions(browse1, browse2, actionButton);
        componentsToDisable.addAll(List.of(field1, field2, browse1, browse2, actionButton, backButton));
    }

    protected void toggleComponents(boolean enabled) {
        for (Component c : componentsToDisable) {
            c.setEnabled(enabled);
        }
    }

    protected void redirectSystemStreams(JTextArea logArea) {
        logArea.setText("");
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> logArea.append(String.valueOf((char) b)));
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    protected abstract void setupActions(JButton browse1, JButton browse2, JButton actionButton);
}

/**
 * =================================================================================
 * PackerPanel - The UI for the packing operation
 * =================================================================================
 */
class PackerPanel extends AbstractFunctionalPanel {
    public PackerPanel(MainFrame frame) {
        super(frame, "PACKER", "Select Directory to Pack:", "Save Packed File As:", "Start Packing");
    }

    @Override
    protected void setupActions(JButton browse1, JButton browse2, JButton actionButton) {
        browse1.addActionListener(e -> chooseDirectory(field1));
        browse2.addActionListener(e -> chooseSaveFile(field2));
        actionButton.addActionListener(e -> performPacking());
    }

    private void performPacking() {
        String dirName = field1.getText();
        String packName = field2.getText();
        if (dirName.isEmpty() || packName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a directory and a destination file.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        redirectSystemStreams(statusArea);

        new BackgroundTask(this) {
            @Override
            protected Void doInBackground() throws Exception {
                new MainPacker(packName, dirName).PacckingActivity();
                return null;
            }
        }.execute();
    }

    private void setNativeLookAndFeel() {
        try {
            // Use FlatLaf Dark for a modern, Windows 11-like dark theme
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void resetCustomLookAndFeel() {
        // This is a workaround to force all components to re-render with our custom styles
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    private void chooseDirectory(JTextField targetField) {
        setNativeLookAndFeel();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        resetCustomLookAndFeel();
    }

    private void chooseSaveFile(JTextField targetField) {
        setNativeLookAndFeel();
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        resetCustomLookAndFeel();
    }
}

/**
 * =================================================================================
 * UnpackerPanel - The UI for the unpacking operation
 * =================================================================================
 */
class UnpackerPanel extends AbstractFunctionalPanel {
    public UnpackerPanel(MainFrame frame) {
        super(frame, "UNPACKER", "Select Packed File:", "Unpack to Directory:", "Start Unpacking");
    }

    @Override
    protected void setupActions(JButton browse1, JButton browse2, JButton actionButton) {
        browse1.addActionListener(e -> chooseOpenFile(field1));
        browse2.addActionListener(e -> chooseDirectory(field2));
        actionButton.addActionListener(e -> performUnpacking());
    }

    private void performUnpacking() {
        String packName = field1.getText();
        String destDir = field2.getText();
        if (packName.isEmpty() || destDir.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a packed file and a destination directory.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        redirectSystemStreams(statusArea);

        new BackgroundTask(this) {
            @Override
            protected Void doInBackground() throws Exception {
                new MainUnpacker(packName, destDir).UnpackingActivity();
                return null;
            }
        }.execute();
    }

    private void setNativeLookAndFeel() {
        try {
            // Use FlatLaf Dark for a modern, Windows 11-like dark theme
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void resetCustomLookAndFeel() {
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    private void chooseDirectory(JTextField targetField) {
        setNativeLookAndFeel();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        resetCustomLookAndFeel();
    }

    private void chooseOpenFile(JTextField targetField) {
        setNativeLookAndFeel();
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            targetField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        resetCustomLookAndFeel();
    }
}

/**
 * =================================================================================
 * BackgroundTask - A generic SwingWorker for running operations
 * =================================================================================
 */
abstract class BackgroundTask extends SwingWorker<Void, Void> {
    private AbstractFunctionalPanel panel;

    public BackgroundTask(AbstractFunctionalPanel panel) {
        this.panel = panel;
        panel.toggleComponents(false);
        panel.progressBar.setVisible(true);
    }

    @Override
    protected void done() {
        panel.toggleComponents(true);
        panel.progressBar.setVisible(false);
        // Mute background music, play success sound, then resume background music
        ResourceLoader.stopBackgroundMusic();
        ResourceLoader.playSound("success.wav", 0.8f, () -> ResourceLoader.startBackgroundMusic());
    }
}