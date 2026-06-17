public class CaesarCipher {

    public String encrypt(String text, int shift) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                ch = (char)((ch - base + shift) % 26 + base);
            }
            result += ch;
        }
        return result;
    }

    public String decrypt(String text, int shift) {
        return encrypt(text, 26 - shift);
    }


    public String caesarEncrypt(String text, int shift){
        String r="";
        for(char ch:text.toCharArray()){
            if(Character.isLetter(ch)){
                r += (char)((Character.toUpperCase(ch)-'A'+shift%26+26)%26+'A');
            }else r+=ch;
        }
        return r;
    }

    public String caesarDecrypt(String text, int shift){
        return caesarEncrypt(text,-shift);
    }

    public String monoEncrypt(String text, String key){
        text = text.toLowerCase();
        key = key.toUpperCase();
        String r = "";
        for(char c : text.toCharArray()){
            int i = Character.toLowerCase(c) - 'a';
            if(i >= 0 && i < 26)
                r += key.charAt(i);
            else
                r += c;
        }
        return r;
    }

    public String monoDecrypt(String text, String key){
        text = text.toUpperCase();
        key = key.toUpperCase();
        String r = "";
        for(char c : text.toCharArray()){
            int i = key.indexOf(c);
            if(i != -1)
                r += (char)('A' + i);
            else
                r += c;
        }
        return r;
    }
}
