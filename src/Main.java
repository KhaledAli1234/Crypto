import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Scanner sc = new Scanner(System.in);
//        Task2 c = new Task2();
//
//        System.out.println("1 Caesar");
//        System.out.println("2 Mono");
//        System.out.println("3 Playfair");
//        System.out.println("4 Hill");
//
//        int type = sc.nextInt();
//        sc.nextLine();
//
//        System.out.println("Enter text:");
//        String text = sc.nextLine();
//
//        System.out.println("Enter key:");
//        String key = sc.nextLine();
//
//        System.out.println("1 Encrypt  2 Decrypt");
//        int op = sc.nextInt();
//
//        String res="";
//
//        if(type==1){
//            int k=Integer.parseInt(key);
//            res=(op==1)?c.caesarEncrypt(text,k):c.caesarDecrypt(text,k);
//        }
//        else if(type==2){
//            res=(op==1)?c.monoEncrypt(text,key):c.monoDecrypt(text,key);
//        }
//        else if(type==3){
//            res=(op==1)?c.playfairEncrypt(text,key):c.playfairDecrypt(text,key);
//        }
//        else if(type==4){
//            res=(op==1)?c.hillEncrypt(text,key):c.hillDecrypt(text,key);
//        }
//
//        System.out.println("Result = "+res);
//
//        sc.close();
//

//        MyGUI.start();
//        AESGui.start();
//        AESAppGUI.start();
          RSAAppGUI.start();
//
//        byte[] key = {
//                0x2b,0x7e,0x15,0x16,
//                0x28,(byte)0xae,(byte)0xd2,(byte)0xa6,
//                (byte)0xab,(byte)0xf7,0x15,(byte)0x88,
//                0x09,(byte)0xcf,0x4f,0x3c
//        };
//
//        int[][] w = AESKeyExpansion.keyExpansion(key);
//
//        for (int i = 0; i < w.length; i++) {
//            System.out.printf("w[%02d]: %02x %02x %02x %02x\n",
//                    i, w[i][0], w[i][1], w[i][2], w[i][3]);
//
//            if ((i + 1) % 4 == 0) {
//                System.out.println("---- Round " + ((i + 1) / 4 - 1) + " ----");
//            }
//        }

//        Task1 crypto = new Task1();
//        String text = "Cat404-z";
//
//        String encrypted = crypto.caesarEncrypt(text, 28);
//        System.out.println("En: " + encrypted);
//        System.out.println("De: " + crypto.caesarDecrypt(encrypted, 1));
//
//        crypto.caesarAttack(encrypted);
//
//        String affineEnc = crypto.affineEncrypt(text, 5, 8);
//        System.out.println("Affine En: " + affineEnc);
//        System.out.println("Affine De: " +
//                crypto.affineDecrypt(affineEnc, 5, 8));
//
//        BigNumberHandler bigHandler = new BigNumberHandler();
//        FileHandler fileHandler = new FileHandler();
//
//        BigInteger bigSum = bigHandler.add("12345678901234567890", "98765432109876543210");
//        System.out.println("BigInteger Sum: " + bigSum);
//
//        fileHandler.writeFile("data.txt", "Here We Go");
//
//        String text = fileHandler.readFile("data.txt");
//        System.out.println("File content: " + text);
//
//
//        CaesarCipher cipher = new CaesarCipher();
//        String encrypted = cipher.encrypt(text, 3);
//        System.out.println("Encrypted text: " + encrypted);
//        String decrypted = cipher.decrypt(encrypted, 3);
//        System.out.println("Decrypted text: " + decrypted);


//        MyGUI gui = new MyGUI(cipher);
//        gui.showGUI();
    }
}
