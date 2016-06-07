package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks {
	public static WebDriver driver;
	public Logger logger = Logger.getLogger(Hooks.class);

    public static boolean runningOnMac() { return System.getProperty("os.name").startsWith("Mac"); }

	@Before
	/**
	 * Delete all cookies at the start of each scenario to avoid shared state
	 * between tests
	 */
	public void openBrowser() throws MalformedURLException {
		try {
			if (this.getValue("browser").equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			} else if (this.getValue("browser").equalsIgnoreCase("chrome")) {
                if (runningOnMac()) { 
                    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver-mac");
                } else { 
                    System.err.println ("WARNING!!!! Chrome driver is only specified for Mac?"); 
                }
				driver = new ChromeDriver();
			} else if (this.getValue("browser").equalsIgnoreCase("ie")) {
				driver = new InternetExplorerDriver();
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
		} catch (Exception e) {

		}
	}

	@After
	public void closeBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {

		}
	}

	public String getValue(String key) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return prop.getProperty(key);
	}

	protected void verifyElement(String actualValue, String expectedValue) {
		try {
			Assert.assertTrue(actualValue.contains(expectedValue));
		} catch (AssertionError ae) {
			logger.info("FAILED: " + ". ACTUAL: \"" + actualValue + "\", EXPECTED: \"" + expectedValue + "\"");
		}
	}
	
	protected void verifyElementPresence(String actualValue, String expectedValue) {
		Assert.assertTrue(actualValue.contains(expectedValue));
	}

	protected void assertElement(String actualValue, String expectedValue) {
		Assert.assertTrue(actualValue.equals(expectedValue));
	}

	public void waitForElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			logger.info("FAILED: " + by + "\", is not present \"");
		}
	}

	public void waitForWebElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			logger.info("FAILED: " + by + "\", is not present \"");
		}
	}
	
	protected void verifyFloridaNumber(String actualValue, String expectedValue)
	{
		Assert.assertTrue(Integer.parseInt(actualValue)>Integer.parseInt(expectedValue));
		
	}
}
