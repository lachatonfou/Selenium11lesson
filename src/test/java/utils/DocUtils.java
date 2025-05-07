package utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;

public class DocUtils {
    public static boolean searchText(String filePath, String textToFind) {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            boolean textFound = false;

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                if (paragraph.getText().contains(textToFind)) {
                    textFound = true;
                    break;
                }
            }
            if (textFound) {
                System.out.println("The document contains the specified text.");
                return true;
            } else {
                System.out.println("The document does not contain the specified text.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
