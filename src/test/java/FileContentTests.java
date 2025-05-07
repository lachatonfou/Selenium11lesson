import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import utils.DocUtils;
import utils.ExcelUtils;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.ExcelUtils.printText;
import static utils.ImageComparisonUtils.compareImages;
import static utils.PdfUtils.readPdf;
import static utils.PdfUtils.savePdf;

public class FileContentTests {
    @Test
    void downloadAndCheckImageTest() throws IOException {
        // Указываем режим скачивания файлов
        Configuration.fileDownload = FileDownloadMode.FOLDER;
        // Указываем директорию для сохранения скачанных файлов
        Configuration.downloadsFolder = "src/main/resources";

        String filePath = "src/main/resources/square_avatar.png";
        File expectedImage = new File("src/main/resources/expected-square_avatar-greyscale.png");
        // Открываем страницу с формой загрузки файла
        open("https://photoaid.com/ru/ru/tools/greyscale");
        // Находим элемент <input type="file"> по его атрибуту name
        SelenideElement fileInput = $("input[type='file']");
        // Загружаем файл, указывая абсолютный путь к файлу
        fileInput.uploadFile(new File(filePath));
        // Находим элемент кнопки для скачивания
        SelenideElement downloadButton = $("a[download]");
        downloadButton.shouldBe(visible);
        // Кликаем по кнопке для скачивания и сохраняем файл
        File downloadedFile = downloadButton.download();
        // Проверяем, что файл был скачан успешно
        assertThat(downloadedFile).exists();
        File actualImage = downloadedFile.getAbsoluteFile();
        boolean isImagesEquals = compareImages(expectedImage, actualImage);
        assertTrue(isImagesEquals);
    }

    @Test
    void testDownloadHttpClient() {
        String endpoint = "https://alfabank.servicecdn.ru/site-upload/67/dd/356/zayavlenie-IZK.pdf";
        String fileName = "downloaded.pdf";

        Response response =
                given().
                        when().
                        get(endpoint).
                        then().
                        contentType("application/pdf").
                        statusCode(200).
                        extract().response();
        savePdf(response, fileName);
        String pdfText = readPdf(fileName);
        assertThat(pdfText).contains("Чтобы стать индивидуальным зарплатным клиентом,")
                .contains("нужно получать на карту зарплату от 30 000 ₽ в месяц");
    }

    @Test
    void checkDocTest() {
        String filePath = "src/main/resources/Lorem ipsum.docx";
        assertFalse(DocUtils.searchText(filePath, "Selenide"));
        assertTrue(DocUtils.searchText(filePath, "Lorem ipsum"));
    }

    @Test
    void checkExcelTest() {
        String filePath = "src/main/resources/Sheet.xlsx";
        printText(filePath);
        assertFalse(ExcelUtils.searchText(filePath, "Motorola"));
        assertTrue(ExcelUtils.searchText(filePath, "iPhone"));
    }

}
