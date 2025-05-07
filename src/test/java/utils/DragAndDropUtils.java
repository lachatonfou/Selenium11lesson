package utils;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class DragAndDropUtils {
    public static void dragAndDropFile(File file, SelenideElement dragTarget) {
        SelenideElement fileInput = $(By.xpath("//input[@type = 'file']"));
        fileInput.uploadFile(file);

        dispatchFileDragAndDropEvent("dragenter", "document", fileInput);
        dispatchFileDragAndDropEvent("dragover", "document", fileInput);
        dispatchFileDragAndDropEvent("drop", dragTarget, fileInput);
    }

    private static void dispatchFileDragAndDropEvent(String eventName, Object to, SelenideElement fileInputId){
        String script =  "var files = arguments[0].files;" +
                "var items = [];" +
                "var types = [];" +
                "for (var i = 0; i < files.length; i++) {" +
                " items[i] = {kind: 'file', type: files[i].type};" +
                " types[i] = 'Files';" +
                "}" +
                "var event = document.createEvent('CustomEvent');" +
                "event.initCustomEvent(arguments[1], true, true, 0);" +
                "event.dataTransfer = {" +
                " files: files," +
                " items: items," +
                " types: types" +
                "};" +
                "arguments[2].dispatchEvent(event);";

        if (to instanceof String) {
            script = script.replace("arguments[2]", to.toString());
        } else {
            executeJavaScript(script,fileInputId, eventName, to);
        }
    }
}
