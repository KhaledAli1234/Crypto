import javax.swing.*;

public class AESAppGUI {

    public static void start(){
        JFrame f=new JFrame("AES ENC/DEC");
        f.setSize(650,450);
        f.setLayout(null);

        JLabel keyLabel=new JLabel("Key");
        keyLabel.setBounds(20,10,200,20);

        JLabel inputLabel=new JLabel("Input");
        inputLabel.setBounds(20,80,200,20);

        JTextField key=new JTextField("Enter Key");
        key.setBounds(20,30,300,30);

        JTextField input=new JTextField("Enter Input");
        input.setBounds(20,100,300,30);

        String[] types={"Text","Hex","Binary"};
        JComboBox<String> typeBox=new JComboBox<>(types);
        typeBox.setBounds(350,100,120,30);

        JButton enc=new JButton("Encrypt");
        enc.setBounds(20,140,140,30);

        JButton dec=new JButton("Decrypt");
        dec.setBounds(180,140,140,30);

        JTextArea out=new JTextArea();
        JScrollPane sp=new JScrollPane(out);
        sp.setBounds(20,200,600,200);

        f.add(keyLabel); f.add(inputLabel);
        f.add(key); f.add(input);
        f.add(typeBox);
        f.add(enc); f.add(dec);
        f.add(sp);

        enc.addActionListener(e -> {
            byte[] k = normalize(getBytes(key.getText(),
                    typeBox.getSelectedItem().toString()));
            byte[] d = normalize(getBytes(input.getText(),
                    typeBox.getSelectedItem().toString()));
            byte[] r = AESCore.encrypt(d, k);
            out.setText(toHex(r));
        });

        dec.addActionListener(e -> {
            try {
                String inputHex = input.getText().replaceAll("\\s+", "");
                String keyHex   = key.getText().replaceAll("\\s+", "");
                byte[] d = normalize(hexToBytes(inputHex));
                byte[] k = normalize(hexToBytes(keyHex));
                byte[] r = AESCore.decrypt(d, k);
                out.setText(toHex(r));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Invalid Input!");
            }
        });
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    static byte[] normalize(byte[] in){
        byte[] r=new byte[16];
        for(int i=0;i<16;i++)
            r[i]= i<in.length ? in[i] : 0;
        return r;
    }

    static byte[] getBytes(String s,String type){
        if(type.equals("Text")) return s.getBytes();
        if(type.equals("Hex")) return hexToBytes(s);
        return binaryToBytes(s);
    }

    static byte[] hexToBytes(String hex){
        hex=hex.replaceAll("\\s+","");
        byte[] b=new byte[hex.length()/2];
        for(int i=0;i<b.length;i++)
            b[i]=(byte)Integer.parseInt(hex.substring(2*i,2*i+2),16);
        return b;
    }

    static byte[] binaryToBytes(String bin){
        bin=bin.replaceAll("\\s+","");
        int len=(bin.length()+7)/8;
        byte[] b=new byte[len];
        for(int i=0;i<len;i++){
            int start=Math.max(0,bin.length()-(i+1)*8);
            b[len-i-1]=(byte)Integer.parseInt(bin.substring(start,bin.length()-i*8),2);
        }
        return b;
    }

    static String toHex(byte[] b){
        StringBuilder sb=new StringBuilder();
        for(byte x:b)
            sb.append(String.format("%02x",x));
        return sb.toString();
    }
}



