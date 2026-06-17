import javax.swing.*;

public class MyGUI {
    public static void start(){

        Task2 c = new Task2();
        JFrame f = new JFrame("Crypto Lab");

        JLabel textLabel = new JLabel("Text:");
        textLabel.setBounds(40,5,100,20);
        JTextField text = new JTextField();
        text.setBounds(40,30,350,30);

        JLabel keyLabel = new JLabel("Key:");
        keyLabel.setBounds(40,55,100,20);
        JTextField key = new JTextField();
        key.setBounds(40,80,350,30);

        JLabel typeLabel = new JLabel("Cipher Type:");
        typeLabel.setBounds(40,110,100,20);
        String[] types = {
                "Playfair",
                "Hill",
                "Vigenere",
                "OneTimePad",
                "RailFence",
                "RowTransposition"
        };
        JComboBox<String> box = new JComboBox<>(types);
        box.setBounds(40,130,200,30);

        JButton enc = new JButton("Encrypt");
        enc.setBounds(40,180,120,40);

        JButton dec = new JButton("Decrypt");
        dec.setBounds(200,180,120,40);

        JTextArea out = new JTextArea();
        out.setEditable(false);
        JScrollPane sp = new JScrollPane(out);
        sp.setBounds(40,240,350,120);


        enc.addActionListener(e->{
            try{
                String t = text.getText();
                String k = key.getText();
                String type = box.getSelectedItem().toString();

                if(type.equals("Playfair"))
                    out.setText(c.playfairEncrypt(t,k));
                else if(type.equals("Hill"))
                    out.setText(c.hillEncrypt(t,k));
                else if(type.equals("Vigenere"))
                    out.setText(c.vigenereEncrypt(t,k));
                else if(type.equals("OneTimePad"))
                    out.setText(c.oneTimePadEncrypt(t,k));
                else if(type.equals("RailFence"))
                    out.setText(c.railFenceEncrypt(t,Integer.parseInt(k)));
                else
                    out.setText(c.rowTranspositionEncrypt(t,k));
            }catch(Exception ex){
                ex.printStackTrace();
                out.setText("Error: Invalid input or key!");
            }
        });


        dec.addActionListener(e->{
            try{
                String t = text.getText();
                String k = key.getText();
                String type = box.getSelectedItem().toString();

                if(type.equals("Playfair"))
                    out.setText(c.playfairDecrypt(t,k));
                else if(type.equals("Hill"))
                    out.setText(c.hillDecrypt(t,k));
                else if(type.equals("Vigenere"))
                    out.setText(c.vigenereDecrypt(t,k));
                else if(type.equals("RailFence"))
                    out.setText(c.railFenceDecrypt(t,Integer.parseInt(k)));
                else
                    out.setText(c.rowTranspositionDecrypt(t,k));
            }catch(Exception ex){
                ex.printStackTrace();
                out.setText("Error: Invalid input or key!");
            }
        });

        f.add(textLabel); f.add(text);
        f.add(keyLabel); f.add(key);
        f.add(typeLabel); f.add(box);
        f.add(enc); f.add(dec); f.add(sp);

        f.setSize(450,420);
        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
