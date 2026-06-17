import javax.swing.*;
import java.io.*;
import java.nio.file.Files;

public class AESGui {


    private static final String DEFAULT_FILE =
            System.getProperty("user.dir") + File.separator + "data.txt";

    public static void start() {

        JFrame f = new JFrame("AES Key Expansion");
        f.setSize(650, 500);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel keyLabel = new JLabel("Key (Text / Hex / Binary):");
        keyLabel.setBounds(20, 20, 250, 20);

        JTextField keyField = new JTextField();
        keyField.setBounds(20, 45, 450, 25);

        String[] types = {"Text", "Hex", "Binary"};
        JComboBox<String> box = new JComboBox<>(types);
        box.setBounds(120, 75, 100, 25);

        JLabel typeLabel = new JLabel("Input Type:");
        typeLabel.setBounds(20, 75, 100, 20);

        JButton loadFile = new JButton("Load");
        loadFile.setBounds(250, 75, 100, 25);

        JButton expandBtn = new JButton("Expand");
        expandBtn.setBounds(20, 110, 120, 30);

        JButton saveFile = new JButton("Save");
        saveFile.setBounds(160, 110, 120, 30);

        JTextArea output = new JTextArea();
        output.setEditable(false);
        JScrollPane sp = new JScrollPane(output);
        sp.setBounds(20, 150, 600, 300);

        f.add(keyLabel); f.add(keyField);
        f.add(typeLabel); f.add(box);
        f.add(loadFile); f.add(expandBtn); f.add(saveFile); f.add(sp);

        File defaultFile = new File(DEFAULT_FILE);

        try {
            if (!defaultFile.exists()) {
                defaultFile.createNewFile();
            } else {
                keyField.setText(new String(Files.readAllBytes(defaultFile.toPath())).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadFile.addActionListener(e -> {
            try {
                keyField.setText(new String(Files.readAllBytes(defaultFile.toPath())).trim());
                JOptionPane.showMessageDialog(f, "Loaded from data.txt");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error loading file!");
            }
        });


        expandBtn.addActionListener(e -> {
            try {
                String input = keyField.getText().trim();
                String type = box.getSelectedItem().toString();

                byte[] keyBytes;

                if (type.equals("Text"))
                    keyBytes = normalize(input.getBytes("UTF-8"));
                else if (type.equals("Hex"))
                    keyBytes = normalize(hexToBytes(input));
                else
                    keyBytes = normalize(binaryToBytes(input));

                int[][] w = AESKeyExpansion.keyExpansion(keyBytes);
                output.setText(format(w));

            } catch (Exception ex) {
                output.setText("Error!");
            }
        });

        saveFile.addActionListener(e -> {
            try (FileWriter fw = new FileWriter(defaultFile)) {
                fw.write(output.getText());
                JOptionPane.showMessageDialog(f,
                        "Saved in:\n" + defaultFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error saving file!");
            }
        });

        f.setVisible(true);
    }



    public static byte[] normalize(byte[] input) {
        byte[] res = new byte[16];
        for (int i = 0; i < 16; i++) {
            if (i < input.length) res[i] = input[i];
            else res[i] = 0x00;
        }
        return res;
    }

    public static byte[] hexToBytes(String hex) {
        hex = hex.replaceAll("\\s+", "");
        if (hex.length() % 2 != 0) hex = "0" + hex;
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < b.length; i++)
            b[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        return b;
    }

    public static byte[] binaryToBytes(String bin) {
        bin = bin.replaceAll("\\s+", "");
        int len = (bin.length() + 7) / 8;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            int start = bin.length() - (i + 1) * 8;
            int end = bin.length() - i * 8;
            start = Math.max(start, 0);
            b[len - i - 1] = (byte) Integer.parseInt(bin.substring(start, end), 2);
        }
        return b;
    }

    public static String format(int[][] w) {
        StringBuilder sb = new StringBuilder();
        for (int round = 0; round < 11; round++) {
            for (int row = 0; row < 4; row++) {
                int i = round * 4 + row;
                sb.append(String.format("%02X %02X %02X %02X\n",
                        w[i][0], w[i][1], w[i][2], w[i][3]));
            }
            sb.append(" | Round ").append(round).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        start();
    }
}