import com.codeborne.selenide.SelenideElement;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.url;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.DragAndDropUtils.dragAndDropFile;


// у самого бони гарсиа есть простой тест загрузки https://github.com/bonigarcia/selenium-webdriver-java/blob/master/selenium-webdriver-junit5/src/test/java/io/github/bonigarcia/webdriver/jupiter/ch03/keyboard/UploadFileJupiterTest.java

public class UploadTests {
    @Test
    void uploadFileTest() throws IOException, InterruptedException {
        String filePath = "src/main/resources/text.txt";

        // Чтение содержимого файла в виде строки
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        // Используйте содержимое файла в вашем коде, например, вывод на экран
        System.out.println("Содержимое файла: " + content);

        // Получаем URL ресурса
        URL url = UploadTests.class.getClassLoader().getResource("text.txt");

        String absolutePath = null;
        if (url != null) {
            // Получаем абсолютный путь к файлу
            absolutePath = new File(url.getPath()).getAbsolutePath();
            System.out.println("Абсолютный путь к файлу: " + absolutePath);
        } else {
            System.out.println("Ресурс не найден.");
        }

        // Открываем страницу с формой загрузки файла
        open("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");

        // Находим элемент <input type="file"> по его атрибуту name
        SelenideElement fileInput = $("input[name='my-file']");

        // Загружаем файл, указывая абсолютный путь к файлу
        fileInput.uploadFile(new File(filePath));

        // Далее можно продолжить взаимодействие с элементами на странице или выполнять другие действия
        Thread.sleep(5000);
        WebElement submit = $(By.xpath("//button[text()='Submit']"));
        submit.click();
        Thread.sleep(5000);

        assertThat(url()).contains("text.txt");
    }

    @Test
    void uploadFileRealSiteTest() {
        String filePath = "src/main/resources/square_avatar.png";

        // Открываем страницу с формой загрузки файла
        open("https://photoaid.com/ru/ru/tools/greyscale");

        // Находим элемент <input type="file"> по его атрибуту name
        SelenideElement fileInput = $("input[type='file']");

        // Загружаем файл, указывая абсолютный путь к файлу
        fileInput.uploadFile(new File(filePath));

        // Далее можно продолжить взаимодействие с элементами на странице или выполнять другие действия
        SelenideElement downloadButton = $("a[download]");
        downloadButton.shouldBe(visible);
    }

    @Test
    void simpleDragAndDropTest() {
        // Открываем страницу с формой загрузки файла
        open("https://photoaid.com/ru/ru/tools/greyscale");
        // Путь к файлу
        File file = new File("src/main/resources/square_avatar.png");
        // Находим элемент куда нужно перетащить файл
        SelenideElement dragTarget = $(By.xpath("//section"));
        // Внутри метода JavaScript код для drag & drop
        dragAndDropFile(file, dragTarget);
        // Далее можно продолжить взаимодействие с элементами на странице или выполнять другие действия
    }

    @Test
    void apiUploadTest() {
        // URL API для загрузки изображения
        String apiUrl = "https://petstore.swagger.io/v2/pet/1/uploadImage";
        // Создаем объект File, который указывает на изображение для загрузки
        // Путь к файлу
        File file = new File("src/main/resources/square_avatar.png");
        // Отправляем POST-запрос на API с изображением
        Response response =
                given()
                        .header("accept", "application/json")
                        .contentType("multipart/form-data")
                        .multiPart("file", file, "image/jpeg") // Указываем тип содержимого файла
                        .when()
                        .post(apiUrl)
                        .then()
                        .statusCode(200)  // Проверяем, что запрос успешен
                        .extract()
                        .response();
        // Выводим ответ сервера
        System.out.println("Response: " + response.asString());
    }
}
