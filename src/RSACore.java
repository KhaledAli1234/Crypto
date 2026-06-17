import java.math.BigInteger;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class RSACore {

    private static final SecureRandom random = new SecureRandom();

    public static class RSAKeyPair {
        public BigInteger n;
        public BigInteger e;
        public BigInteger d;

        public RSAKeyPair(BigInteger n, BigInteger e, BigInteger d) {
            this.n = n;
            this.e = e;
            this.d = d;
        }
    }

    public static RSAKeyPair generateKeys() {

        BigInteger p = BigInteger.probablePrime(512, random);
        BigInteger q = BigInteger.probablePrime(512, random);

        while (p.equals(q)) {
            q = BigInteger.probablePrime(512, random);
        }

        BigInteger n   = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE)
                .multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(65537);
        while (!phi.gcd(e).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.TWO);
        }

        BigInteger d = e.modInverse(phi);

        return new RSAKeyPair(n, e, d);
    }

    public static String encryptBytes(byte[] data, BigInteger e, BigInteger n) {

        if (data == null || data.length == 0)
            throw new IllegalArgumentException("Input data cannot be empty.");

        BigInteger msg = new BigInteger(1, data);

        if (msg.compareTo(n) >= 0)
            throw new IllegalArgumentException(
                    "Message is too large for this RSA key.\n");

        BigInteger cipher = msg.modPow(e, n);
        return cipher.toString(10);
    }
    public static String encryptText(String message, BigInteger e, BigInteger n) {

        if (message == null || message.trim().isEmpty())
            throw new IllegalArgumentException("Message cannot be empty.");

        return encryptBytes(message.getBytes(StandardCharsets.UTF_8), e, n);
    }


    public static byte[] decryptBytes(String cipherText, BigInteger d, BigInteger n) {

        if (cipherText == null || cipherText.trim().isEmpty())
            throw new IllegalArgumentException("Cipher text cannot be empty.");

        cipherText = cipherText.trim();

        if (!cipherText.matches("[0-9]+"))
            throw new IllegalArgumentException(
                    "Cipher text must contain decimal digits only.\n" );

        BigInteger cipher = new BigInteger(cipherText);

        if (cipher.compareTo(n) >= 0)
            throw new IllegalArgumentException(
                    "Cipher value is larger than the modulus.\n");

        BigInteger msg  = cipher.modPow(d, n);
        byte[]     data = msg.toByteArray();

        if (data.length > 1 && data[0] == 0) {
            byte[] trimmed = new byte[data.length - 1];
            System.arraycopy(data, 1, trimmed, 0, trimmed.length);
            data = trimmed;
        }

        return data;
    }

    public static String decryptText(String cipherText, BigInteger d, BigInteger n) {
        return new String(decryptBytes(cipherText, d, n), StandardCharsets.UTF_8);
    }
}