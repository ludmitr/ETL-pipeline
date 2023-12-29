package io.etl_pipeline;

import io.etl_pipeline.common.Constants;
import io.etl_pipeline.extract.WebScraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ETLApplication {
    public static void main(String[] args) {
//        Constants constVars = new Constants();
//        WebScraper scraper = new WebScraper();
//        scraper.scrape(constVars.getYad2ForSale());
        // Set the location of the ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        // Configure options for headless Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // Applicable for Windows OS only
        options.addArguments("--no-sandbox"); // Bypass OS security model, required by Docker
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems

        // Initialize the driver with the configured options
        WebDriver driver = new ChromeDriver();

        try {
            // Open the webpage
            driver.get("YOUR_WEBPAGE_URL");

            // Find the input box and enter the text
            WebElement inputBox = driver.findElement(By.name("topArea,area,city,neighborhood,street,x_g,y_g"));
            inputBox.sendKeys("הרצליה");

            // Wait for the dropdown and select the correct option
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[data-v-7745abf2]")))
                    .click();

            // Find and click the search button
            driver.findElement(By.cssSelector("button[data-test-id='searchButton']")).click();

            // Add any additional actions or parsing with Jsoup here
            // ...

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the driver
            driver.quit();
        }
    }
}
