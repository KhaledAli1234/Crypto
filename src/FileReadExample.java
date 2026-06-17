import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReadExample {
    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("data.txt")));
            System.out.println("المحتوى داخل الملف:\n" + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}