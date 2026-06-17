import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExample {
    public static void main(String[] args) {
        try {
            File file = new File("data.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            writer.write("ده مثال على كتابة نص في data.txt\n");
            writer.write("ممكن تضيف اكتر من سطر\n");
            writer.close();

            System.out.println("تم حفظ الملف في: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}