import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSAAppGUI {

    static RSACore.RSAKeyPair keys;

    public static void start() {

        keys = RSACore.generateKeys();

        JFrame frame = new JFrame("RSA Encrypt / Decrypt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(680, 620));

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        frame.setContentPane(root);

        JPanel keysPanel = new JPanel(new GridLayout(3, 1, 0, 6));
        keysPanel.setBorder(new TitledBorder("RSA Keys  (512-bit)"));

        JTextArea publicKeyArea  = makeReadOnlyArea(keys.e.toString());
        JTextArea privateKeyArea = makeReadOnlyArea(keys.d.toString());
        JTextArea modulusArea    = makeReadOnlyArea(keys.n.toString());

        keysPanel.add(labelledScroll("Public key  (e)",  publicKeyArea));
        keysPanel.add(labelledScroll("Private key  (d)", privateKeyArea));
        keysPanel.add(labelledScroll("Modulus  (n)",     modulusArea));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(new TitledBorder("Input"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets  = new Insets(4, 4, 4, 4);
        gc.fill    = GridBagConstraints.HORIZONTAL;

        JTextField inputField = new JTextField();
        String[]   formats    = {"Text", "Hex", "Binary"};
        JComboBox<String> formatBox = new JComboBox<>(formats);

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1.0;
        inputPanel.add(inputField, gc);
        gc.gridx = 1; gc.weightx = 0;
        inputPanel.add(formatBox, gc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        JButton encBtn      = new JButton("Encrypt");
        JButton decBtn      = new JButton("Decrypt");
        JButton copyBtn     = new JButton("Copy output");
        JButton clearBtn    = new JButton("Clear");
        JButton generateBtn = new JButton("Generate new keys");

        styleButton(encBtn,      new Color(0x2E7D32), Color.WHITE);
        styleButton(decBtn,      new Color(0x1565C0), Color.WHITE);
        styleButton(generateBtn, new Color(0x6A1B9A), Color.WHITE);

        btnPanel.add(encBtn);
        btnPanel.add(decBtn);
        btnPanel.add(copyBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(Box.createHorizontalStrut(20));
        btnPanel.add(generateBtn);

        JPanel outputPanel = new JPanel(new BorderLayout(0, 4));
        outputPanel.setBorder(new TitledBorder("Output"));

        JTextArea outputArea = new JTextArea(6, 40);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JLabel statusBar = new JLabel(" Ready");
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(3, 6, 3, 6)));
        statusBar.setFont(statusBar.getFont().deriveFont(Font.PLAIN, 11f));

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        centerPanel.add(inputPanel);
        centerPanel.add(btnPanel);
        centerPanel.add(outputPanel);

        root.add(keysPanel,    BorderLayout.NORTH);
        root.add(centerPanel,  BorderLayout.CENTER);
        root.add(statusBar,    BorderLayout.SOUTH);

        encBtn.addActionListener(e -> {
            try {
                BigInteger pub = new BigInteger(publicKeyArea.getText().trim());
                BigInteger mod = new BigInteger(modulusArea.getText().trim());

                byte[] data = parseInput(inputField.getText(),
                        formatBox.getSelectedItem().toString());

                String result = RSACore.encryptBytes(data, pub, mod);
                outputArea.setText(result);
                setStatus(statusBar, "Encrypted successfully.", false);

            } catch (Exception ex) {
                showError(frame, ex.getMessage());
                setStatus(statusBar, "Encryption failed: " + ex.getMessage(), true);
            }
        });

        decBtn.addActionListener(e -> {
            try {
                BigInteger pri = new BigInteger(privateKeyArea.getText().trim());
                BigInteger mod = new BigInteger(modulusArea.getText().trim());

                byte[] result = RSACore.decryptBytes(inputField.getText(), pri, mod);

                String format = formatBox.getSelectedItem().toString();
                if (format.equals("Text")) {
                    outputArea.setText(new String(result, StandardCharsets.UTF_8));
                } else if (format.equals("Hex")) {
                    outputArea.setText(toHex(result));
                } else {
                    outputArea.setText(toBinary(result));
                }

                setStatus(statusBar, "Decrypted successfully.", false);

            } catch (Exception ex) {
                showError(frame, ex.getMessage());
                setStatus(statusBar, "Decryption failed: " + ex.getMessage(), true);
            }
        });

        copyBtn.addActionListener(e -> {
            String text = outputArea.getText().trim();
            if (text.isEmpty()) {
                setStatus(statusBar, "Nothing to copy.", true);
                return;
            }
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(text), null);
            setStatus(statusBar, "Output copied to clipboard.", false);
        });

        clearBtn.addActionListener(e -> {
            inputField.setText("");
            outputArea.setText("");
            setStatus(statusBar, "Cleared.", false);
        });

        generateBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Generate new RSA keys?\nThe current keys will be lost.",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) return;

            generateBtn.setEnabled(false);
            setStatus(statusBar, "Generating keys…", false);


            SwingWorker<RSACore.RSAKeyPair, Void> worker = new SwingWorker<>() {
                @Override
                protected RSACore.RSAKeyPair doInBackground() {
                    return RSACore.generateKeys();
                }
                @Override
                protected void done() {
                    try {
                        keys = get();
                        publicKeyArea.setText(keys.e.toString());
                        privateKeyArea.setText(keys.d.toString());
                        modulusArea.setText(keys.n.toString());
                        outputArea.setText("");
                        setStatus(statusBar, "New keys generated.", false);
                    } catch (Exception ex) {
                        showError(frame, "Key generation failed: " + ex.getMessage());
                    } finally {
                        generateBtn.setEnabled(true);
                    }
                }
            };
            worker.execute();
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel labelledScroll(String label, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }
    private static JTextArea makeReadOnlyArea(String text) {
        JTextArea a = new JTextArea(text, 2, 40);
        a.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        a.setEditable(false);
        a.setLineWrap(true);
        return a;
    }

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }

    private static void setStatus(JLabel bar, String msg, boolean error) {
        bar.setText(" " + msg);
        bar.setForeground(error ? new Color(0xC62828) : new Color(0x1B5E20));
    }

    private static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static byte[] parseInput(String raw, String format) {
        return switch (format) {
            case "Hex"    -> hexToBytes(raw);
            case "Binary" -> binaryToBytes(raw);
            default       -> {
                if (raw == null || raw.isEmpty())
                    throw new IllegalArgumentException("Input cannot be empty.");
                yield raw.getBytes(StandardCharsets.UTF_8);
            }
        };
    }

    static byte[] hexToBytes(String hex) {
        hex = hex.replaceAll("\\s+", "");
        if (hex.isEmpty())
            throw new IllegalArgumentException("Hex input cannot be empty.");
        if (hex.length() % 2 != 0)
            throw new IllegalArgumentException("Hex string length must be even.");
        if (!hex.matches("[0-9a-fA-F]+"))
            throw new IllegalArgumentException("Hex input contains invalid characters.");

        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < b.length; i++)
            b[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        return b;
    }

    static byte[] binaryToBytes(String bin) {
        bin = bin.replaceAll("\\s+", "");
        if (bin.isEmpty())
            throw new IllegalArgumentException("Binary input cannot be empty.");
        if (!bin.matches("[01]+"))
            throw new IllegalArgumentException("Binary input must contain only 0 and 1.");
        if (bin.length() % 8 != 0)
            throw new IllegalArgumentException("Binary length must be a multiple of 8.");

        byte[] b = new byte[bin.length() / 8];
        for (int i = 0; i < b.length; i++)
            b[i] = (byte) Integer.parseInt(bin.substring(i * 8, i * 8 + 8), 2);
        return b;
    }

    static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte x : b) sb.append(String.format("%02X", x));
        return sb.toString();
    }

    static String toBinary(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte x : b)
            sb.append(String.format("%8s",
                    Integer.toBinaryString(x & 0xff)).replace(' ', '0'));
        return sb.toString();
    }
}