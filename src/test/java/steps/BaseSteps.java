package steps;

import extensions.AllureExtension;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@ExtendWith(AllureExtension.class)
public class BaseSteps {
    private static WebDriver driver;
    protected AllureSteps allureSteps = new AllureSteps();

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
