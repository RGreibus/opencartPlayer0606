import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestOpencart {
    WebDriver driver;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://opencart.demo.servername.co/");
    }


    @Test
    public void testSelectMP3Players() {
        driver.findElement(By.cssSelector("a[href*='category&path=34']")).click();
        driver.findElement(By.cssSelector("a[href*='category&path=34'] + div > a")).click();
        driver.findElement(By.cssSelector("button#list-view")).click();

        List<String> itemNames = readItemNamesFromFile("testdata.txt");

        for (String itemName : itemNames) {
            List<WebElement> items = driver.findElements(By.cssSelector(".product-layout .caption a"));
            boolean itemFound = false;

            for (WebElement item : items) {
                if (item.getText().equals(itemName)) {
                    assertTrue(item.isDisplayed(), "Item " + itemName + " is not displayed.");
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                fail("Item " + itemName + " does not exist in the list.");
            }
        }
    }

    private List<String> readItemNamesFromFile(String filePath) {
        List<String> itemNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                itemNames.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemNames;
    }
}