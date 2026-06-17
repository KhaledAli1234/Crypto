public class Task1 {

    public String caesarEncrypt(String t, int shift) {
        String result = "";
        for (int i = 0; i < t.length(); i++) {
            char ch = t.charAt(i);

            if (Character.isLetter(ch)) {
                char c = Character.isUpperCase(ch) ? 'A' : 'a';
                ch = (char) ((ch - c + shift) % 26 + c);
            }
            result += ch;
        }
        return result;
    }


    public String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - shift);
    }

    public void caesarAttack(String ent) {
        for (int i = 0; i < 26; i++) {
            String decrypted = caesarDecrypt(ent, i);
            System.out.println(i + ":" + decrypted);
        }
    }

    public String affineEncrypt(String text, int a, int b) {
        if (!isCoprime(a, 26)) {
            System.out.println("a must be coprime with 26");
            return "";
        }

        String result = "";

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (Character.isLetter(ch)) {
                char c = Character.isUpperCase(ch) ? 'A' : 'a';
                int x = ch - c;
                char en = (char) ((a * x + b) % 26 + c);
                result += en;
            } else {
                result += ch;
            }
        }
        return result;
    }

    public String affineDecrypt(String text, int a, int b) {
        String result = "";
        int in = modInverse(a, 26);

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (Character.isLetter(ch)) {
                char c = Character.isUpperCase(ch) ? 'A' : 'a';
                int y = ch - c;
                char de = (char) ((in * (y - b + 26)) % 26 + c);
                result += de;
            } else {
                result += ch;
            }
        }
        return result;
    }

    private int modInverse(int a, int m) {
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1)
                return i;
        }
        return 1;
    }

    private int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    private boolean isCoprime(int a, int m) {
        return gcd(a, m) == 1;
    }
}
