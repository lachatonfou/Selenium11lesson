import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import steps.AllureSteps;
import steps.BaseSteps;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Download")
public class DownloadTest extends BaseSteps {

    @Test
    void testDownloadHttpClient() throws IOException {
        WebDriver driver = getDriver();
        getDriver().get("https://bonigarcia.dev/selenium-webdriver-java/download.html");

        WebElement pngLink = driver.findElement(By.xpath("//a[@download='webdrivermanager.png']"));
        pngLink.click();

        File pngFile = new File(".", "webdrivermanager.png");
        allureSteps.download(pngLink.getAttribute("href"), pngFile);
        assertThat(pngFile).exists();

        WebElement pdfLink = driver.findElement(By.xpath("//a[@download='webdrivermanager.pdf']"));
        File pdfFile = new File(".", "webdrivermanager.pdf");
        allureSteps.download(pdfLink.getAttribute("href"), pdfFile);
        assertThat(pdfFile).exists();
    }
}
