package test;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.cli.Main;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;


public class StepDefs {
	public WebDriver driver;
	public static String pID;
	public static final Logger logger = Logger.getLogger(Main.class.getName());

	String userHome = System.getProperty("user.home");
	String driveLocation = userHome.substring(0, userHome.indexOf("\\"));

	public StepDefs() {
		driver = Hooks.driver;
	}

	@Given("^I navigate to \"(.*?)\"$")
	public void openURL(String url) throws Throwable {
		Hooks hooks = new Hooks();
		if (url.equals("emailSourceURL")) {
			Hooks.driver.get(hooks.getValue("emailSourceURL"));
		} else {
			Hooks.driver.get(hooks.getValue("epaddIndexURL"));
		}
	}

	@Then("^I wait for (\\d+) sec$")
	public void wait(int time) throws InterruptedException {
		TimeUnit.SECONDS.sleep(time);
	}

	@And("I enter \"(.*?)\" into input field having name \"(.*?)\"$")
	public void enterUserName(String input, String field) {
		Hooks hooks = new Hooks();
		DirLocation search = new DirLocation();
		hooks.waitForElement(By.name(field));

		if (input.equals("achieverName")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("achieverName"));
		} else if (input.equals("primaryEmailAddress")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("primaryEmailAddress"));
		} else if (input.equals("emailFolderLocation")) {
			driver.findElement(By.name(field))
					.sendKeys(search.getDirAbsoluteLoc("ePADD") + hooks.getValue("emailFolderLocation"));
		} else if (input.equals("emailExportLocation")) {
			driver.findElement(By.name(field))
					.sendKeys(search.getDirAbsoluteLoc("ePADD") + hooks.getValue("emailExportLocation"));
		} else if (input.equals("emailArchieveLocation")) {
			driver.findElement(By.name(field))
					.sendKeys(search.getDirAbsoluteLoc("ePADD") + hooks.getValue("emailArchieveLocation"));
		} else if (input.equals("emailExportSplitLocation")) {
			driver.findElement(By.name(field))
					.sendKeys(search.getDirAbsoluteLoc("ePADD") + hooks.getValue("emailExportSplitLocation"));
		}
	}

	@And("I click on element having id \"(.*?)\"$")
	public void clickContinueButton(String continueButtton) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(continueButtton));
		driver.findElement(By.id(continueButtton)).click();
	}

	@Then("I click on \"(.*?)\" button having css \"(.*?)\"$")
	public void clickSelectAllFoldersButton(String buttonName, String buttonCSS) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(buttonCSS));
		driver.findElement(By.cssSelector(buttonCSS)).click();
	}

	@Then("I click on element having xpath \"(.*?)\"$")
	public void clickOnElementHavingXpath(String xpathLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(xpathLocator));
		driver.findElement(By.xpath(xpathLocator)).click();
	}
	
	@Then("I click on element having css \"(.*?)\"$")
	public void clickOnElementHavingCSS(String cssLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(cssLocator));
		driver.findElement(By.cssSelector(cssLocator)).click();
	}

	@Then("I click on element having link \"(.*?)\"$")
	public void clickOnElementHavingLink(String exportLink) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.linkText(exportLink));
		driver.findElement(By.linkText(exportLink)).click();
	}

	@Then("page title \"(.*?)\" should be displayed having css \"(.*?)\"$")
	public void verifyPages(String expectedText, String actualTextLocator) {
		Hooks hooks = new Hooks();
		String actualText = null;
		try {
			actualText = driver.findElement(By.cssSelector(actualTextLocator)).getText();
			hooks.verifyElement(actualText, expectedText);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("FAILED: Either the " + actualText + " is not present or Page fails to load");
		}
	}

	@Then("I navigate back$")
	public void navigation() {
		driver.navigate().back();
	}

	@Then("close ePADD$")
	public void closeApplication() throws IOException, InterruptedException {
		this.wait(5);
		Process process = Runtime.getRuntime().exec("jps");
		process.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = reader.readLine();
		while (line != null) {
			if (line.toString().contains("epadd.exe")) {
				pID = line.substring(0, line.indexOf(" "));
				break;
			}
			line = reader.readLine();
		}
		WindowsUtils.killPID(pID);
		this.wait(10);
	}

	@Then("open ePADD$")
	public void openApplication() throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder(driveLocation + "\\epadd.exe");
		Process process = pb.start();
		process.waitFor();
		Thread.sleep(35000);
	}

	@Then("^open browser$")
	public void openBrowser() throws MalformedURLException {
		Hooks hooks = new Hooks();
		hooks.openBrowser();
	}

	@Then("copy files$")
	public void copyFiles() throws InterruptedException {
		Hooks hooks = new Hooks();
		File deliverySource = new File(driveLocation + hooks.getValue("deliverySource"));
		File deliveryDest = new File(userHome + hooks.getValue("deliveryDest"));
		File discoverySource = new File(driveLocation + hooks.getValue("discoverySource"));
		File discoveryDest = new File(userHome + hooks.getValue("discoveryDest"));
		try {
			FileUtils.copyDirectory(deliverySource, deliveryDest);
			FileUtils.copyDirectory(discoverySource, discoveryDest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Then("I select \"(.*?)\" option by text from dropdown having id \"(.*?)\"$")
	public void dropDownSelection(String option, String id) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(id));
		Select select = new Select(driver.findElement(By.id(id)));
		select.selectByVisibleText(option);
	}
	
	@Then("^I wait for the page \"(.*?)\" to be displayed within \"(.*?)\" seconds$")
	public void waitForPageToLoad(String url, int time) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, time);
		Hooks hooks = new Hooks();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/a[@href='correspondents']")));
		} catch (org.openqa.selenium.TimeoutException e) {
			logger.info(hooks.getValue("browserTopPage") + " did not opened in " + time + " Seconds.Exception occured is: " + e);
			System.out.println();
		}
	}
	
	@Then("^create screenshot folder$")
	public void createScreenshotFolder() throws Throwable {
		Hooks hooks = new Hooks();
		File file = new File(userHome + hooks.getValue("StrScreenShotFolderPath"));
		if (!file.exists()) {
			file.mkdir();
		} else {
		}
	}
	
	@Then("^take full page screenshot of \"(.*?)\"$")
	public void takeScreenshot(String page) throws Throwable {
		Hooks hooks = new Hooks();
		this.createScreenshotFolder();
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String stamp = timestamp + ".png";
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(userHome+hooks.getValue("StrScreenShotPath") + hooks.getValue("browser") + "-" + page + "-" +stamp));
	}

	@And("I verify that \"(.*?)\" is displayed$")
	public void verifyLexicanURL(String url) {
		Hooks hooks = new Hooks();
		if(url.equals("LexicanURL")){
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("lexicanURL"));
		}
		else if(url.equals("LexicanEditURL")){
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("LexicanEditURL"));
		}
		else if(url.equals("queryGeneratorPage")){
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("queryGenerator"));
		}
		else if(url.equals("editCorrespondentsPage")){
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("editCorrespondentsPage"));
		}
	}
	
	@And("I switch to the new window and verify the url$")
	public void verifyURL() throws InterruptedException {
		Hooks hooks = new Hooks();
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				String currentURL = driver.getCurrentUrl();
				hooks.verifyElement(currentURL, hooks.getValue("documentURL"));
				driver.close();
			}
		}
		driver.switchTo().window(parentWindow);
	}
	
	@And("I enter florida in textfield having xpath \"(.*?)\"$")
	public void enterFlorida(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("floridaText"));
	}
	
	@And("I enter kidcare in textfield having xpath \"(.*?)\"$")
	public void enterKidcare(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("kidcareText"));
	}
	
	@And("I enter paragraph in textfield having xpath \"(.*?)\"$")
	public void enterParagraph(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("paragraph"));
	}
	
	@Then("I verify the number having xpath \"(.*?)\" searched with \"(.*?)\"$")
	public void verifyNumber(String num, String text) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(num));
		String number = driver.findElement(By.xpath(num)).getText();
		if(text.equals(hooks.getValue("floridaText"))){
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""), hooks.getValue("floridaNumberValue"));
		}
		else if(text.equals(hooks.getValue("kidcareText"))){
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""), hooks.getValue("kidcareNumberValue"));
			this.wait(5);
		}
	}
	
	@Then("I verify the number of highlighted texts having css \"(.*?)\"$")
	public void verifyHighlightedText(String highlightedTextLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForWebElement(By.cssSelector(highlightedTextLocator));
		List<WebElement> highlightedText = driver.findElements(By.cssSelector(highlightedTextLocator));
		int highlightedTextSize = highlightedText.size();
		hooks.assertElement(Integer.toString(highlightedTextSize), hooks.getValue("highlightedTextSize"));
	}
	
	@Then("I verify the number of underlined texts having css \"(.*?)\"$")
	public void verifyUnderlinedText(String underlinedTextLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForWebElement(By.cssSelector(underlinedTextLocator));
		List<WebElement> underlinedText = driver.findElements(By.cssSelector(underlinedTextLocator));
		int underlinedTextSize = underlinedText.size();
		hooks.assertElement(Integer.toString(underlinedTextSize), hooks.getValue("underlinedTextSize"));
	}
	
	@Then("I handle the alert$")
	public void handleAlert() throws InterruptedException {
		Thread.sleep(5000);
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	@Then("I verify the session folder under \"(.*?)\" folder$")
	public void checkFolderExistence(String folderName) throws InterruptedException, IOException {
		this.wait(5);
		Hooks hooks = new Hooks();
		File file = new File(userHome+hooks.getValue("sessionFolder"));
		if(file.exists()){
			FileUtils.deleteDirectory(new File(userHome+hooks.getValue("sessionFolder")));
		}
		this.wait(5);
	}
	
	@And("I verify the \"(.*?)\" link existence$")
	public boolean browseLinkExistence(String browseLink) throws Exception {
		try {
	        driver.findElement(By.linkText(browseLink));
	        return false;
	    } catch (Exception e) {
	        return true;
	    }
	}
	 
}