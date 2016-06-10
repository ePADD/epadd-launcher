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
import org.openqa.selenium.Keys;
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
	public static String totalNumberOfEmails;
	public static File newScreenshotFolder;
	public static File newLogsFolder;
	public static String pID;

	public static final Logger logger = Logger.getLogger(Main.class.getName());

	String userHome = System.getProperty("user.home");
	String driveLocation = userHome.substring(0, userHome.indexOf("\\"));
	String opsystem = System.getProperty("os.name");

	public static void main(String args[]) {
	//	String userHome = System.getProperty("user.home");
	

	}

	public StepDefs() {
		driver = Hooks.driver;
	}

	@Given("^I navigate to \"(.*?)\"$")
	public void openURL(String url) throws Throwable {
		int flag = '0';
		Hooks hooks = new Hooks();
		if (url.equals("emailSourceURL")) {
			Hooks.driver.get(hooks.getValue("emailSourceURL"));
			// this.wait(5);
			String strtext = driver.findElement(By.tagName("body")).getText();
			// System.out.println("text is:" + strtext);
			if (strtext.contains("Unable to connect")) {
				flag = '1';
				System.out.println("Error in opening epadd application");
				System.exit(0);
			}
		}
		if ((flag != '1') && (url.equals("epaddIndexURL"))) {
			Hooks.driver.get(hooks.getValue("epaddIndexURL"));
		}
		this.createFolder();
	}

	@Then("^I wait for (\\d+) sec$")
	public void wait(int time) throws InterruptedException {
		TimeUnit.SECONDS.sleep(time);
	}

	@And("I enter \"(.*?)\" into input field having name \"(.*?)\"$")
	public void enterUserName(String input, String field) throws InterruptedException {

		Hooks hooks = new Hooks();
	//	DirLocation search = new DirLocation();
		hooks.waitForElement(By.name(field));

		driver.findElement(By.name(field)).sendKeys(hooks.getValue(input));
	
		/*
		if (input.equals("achieverName")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("achieverName"));
		} else if (input.equals("primaryEmailAddress")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("primaryEmailAddress"));
		} else if (input.equals("emailFolderLocation")) {

			if (opsystem.contains("Windows")) {
				driver.findElement(By.name(field))
						.sendKeys(hooks.getValue("emailFolderLocation"));
			} else if (opsystem.contains("Linux")) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailFolderLocation"));
			} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailFolderLocation"));
			}

		} else if (input.equals("emailExportLocation")) {
			if (opsystem.contains("Windows")) {
				driver.findElement(By.name(field))
						.sendKeys(hooks.getValue("emailExportLocation"));
			} else if (opsystem.contains("Linux")) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailExportLocation"));
			} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailExportLocation"));
			}

		} else if (input.equals("emailArchiveLocation")) {
			if (opsystem.contains("Windows")) {
				driver.findElement(By.name(field))
						.sendKeys(hooks.getValue("emailArchiveLocation"));
			} else if (opsystem.contains("Linux")) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailArchiveLocation"));
			} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailArchiveLocation"));
			}

		} else if (input.equals("emailExportSplitLocation")) {
			if (opsystem.contains("Windows")) {
				driver.findElement(By.name(field))
						.sendKeys(hooks.getValue("emailExportSplitLocation"));
			} else if (opsystem.contains("Linux")) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailExportSplitLocation"));
			} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
				driver.findElement(By.name(field)).sendKeys(hooks.getValue("emailExportSplitLocation"));
			}

		} else if (input.equals("epaddAchieverName")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("epaddAchieverName"));
		} else if (input.equals("epaddPrimaryEmailAddress")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("epaddPrimaryEmailAddress"));
		} else if (input.equals("epaddEmailAddress")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("epaddEmailAddress"));
		} else if (input.equals("epaddPassword")) {
			driver.findElement(By.name(field)).sendKeys(hooks.getValue("epaddPassword"));
		}
		*/
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
		// System.out.println ("inside copy files function");
		Hooks hooks = new Hooks();
		File deliverySource = null;
		File discoverySource = null;
		File deliveryDest = null;
		File discoveryDest = null;

		if (opsystem.contains("Windows")) {
		//	String driveLocation = userHome.substring(0, userHome.indexOf("\\"));
			deliverySource = new File(hooks.getValue("deliverySource"));
			// System.out.println("drivelocation is: " + driveLocation);
		} else if (opsystem.contains("Linux")) {
			deliverySource = new File(hooks.getValue("deliverySource"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			deliverySource = new File(hooks.getValue("deliverySource"));
		}

		if (opsystem.contains("Windows")) {
			deliveryDest = new File(hooks.getValue("deliveryDest"));
			// System.out.println("userhome is: " + userHome);
		} else if (opsystem.contains("Linux")) {
			deliveryDest = new File(hooks.getValue("deliveryDest"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			deliveryDest = new File(hooks.getValue("deliveryDest"));
		}

		if (opsystem.contains("Windows")) {
	//		String driveLocation = userHome.substring(0, userHome.indexOf("\\"));
			discoverySource = new File(hooks.getValue("discoverySource"));
		} else if (opsystem.contains("Linux")) {
			discoverySource = new File(hooks.getValue("discoverySource"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			discoverySource = new File(hooks.getValue("discoverySource"));
		}

		if (opsystem.contains("Windows")) {
			discoveryDest = new File(hooks.getValue("discoveryDest"));
		} else if (opsystem.contains("Linux")) {
			discoveryDest = new File(hooks.getValue("discoveryDest"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			discoveryDest = new File(hooks.getValue("discoveryDest"));
		}

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
			logger.info(hooks.getValue("browserTopPage") + " did not opened in " + time
					+ " Seconds.Exception occured is: " + e);
		}
	}

	@Then("^create folder$")
	public void createFolder() throws Throwable {
		Hooks hooks = new Hooks();
		File screenshotFolder = null;
		File reportFolder = null;
		File logsFolder = null;

		if (opsystem.contains("Windows")) {
			screenshotFolder = new File(hooks.getValue("screenshotFolderPath"));
		} else if (opsystem.contains("Linux")) {
			screenshotFolder = new File(hooks.getValue("StrScreenShotFolderPath"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			screenshotFolder = new File(hooks.getValue("StrScreenShotFolderPath"));
		}

		if (opsystem.contains("Windows")) {
			reportFolder = new File(hooks.getValue("reportsFolderPath"));
		} else if (opsystem.contains("Linux")) {
			reportFolder = new File(hooks.getValue("reportsFolderPath"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			reportFolder = new File(hooks.getValue("reportsFolderPath"));
		}

		if (opsystem.contains("Windows")) {
			logsFolder = new File(hooks.getValue("logsFolderPath"));
		} else if (opsystem.contains("Linux")) {
			logsFolder = new File(hooks.getValue("logsFolderPath"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			logsFolder = new File(hooks.getValue("logsFolderPath"));
		}

		String timestamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());

		if (opsystem.contains("Windows")) {
			newScreenshotFolder = new File(hooks.getValue("screenshotCapturedFolderPath") + timestamp);
		} else if (opsystem.contains("Linux")) {
			newScreenshotFolder = new File(hooks.getValue("screenshotCapturedFolderPath") + timestamp);
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			newScreenshotFolder = new File(hooks.getValue("screenshotCapturedFolderPath") + timestamp);
		}

		if (!screenshotFolder.exists()) {
			screenshotFolder.mkdirs();
		}
		if (!reportFolder.exists()) {
			reportFolder.mkdirs();
		}
		if (!logsFolder.exists()) {
			logsFolder.mkdirs();
		}
		if (!newScreenshotFolder.exists()) {
			newScreenshotFolder.mkdirs();
		}
	}

	@Then("^take full page screenshot of \"(.*?)\"$")
	public void takeScreenshot(String page) throws Throwable {
		Hooks hooks = new Hooks();
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String stamp = timestamp + ".png";
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(newScreenshotFolder + hooks.getValue("ScreenShotPath")+ hooks.getValue("browser") + "-" + page + "-" + stamp));
		
	}

	@And("I verify that \"(.*?)\" is displayed$")
	public void verifyLexiconURL(String url) {
		Hooks hooks = new Hooks();
		String currentURL = driver.getCurrentUrl();
		hooks.verifyElement(currentURL, hooks.getValue(url));
		/*
		if (url.equals("LexiconURL")) {
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("LexiconURL"));
		} else if (url.equals("LexiconEditURL")) {
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("LexiconEditURL"));
		} else if (url.equals("queryGeneratorPage")) {
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("queryGenerator"));
		} else if (url.equals("editCorrespondentsPage")) {
			String currentURL = driver.getCurrentUrl();
			hooks.verifyElement(currentURL, hooks.getValue("editCorrespondentsPage"));
		}
		*/
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

	@And("I switch to Family page and click on id \"(.*?)\" and id \"(.*?)\" and verify email number \"(.*?)\"$")
	public void verifyURL(String doNotTransferLocator, String applyToAllLocator, String emailNumberLocator)
			throws InterruptedException {
		Hooks hooks = new Hooks();
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				String currentURL = driver.getCurrentUrl();
				hooks.verifyElement(currentURL, hooks.getValue("familyURL"));
				if (driver.findElement(By.id(doNotTransferLocator)).getAttribute("class").contains("flag-enabled")) {
				} else {
					driver.findElement(By.id(doNotTransferLocator)).click();
					driver.findElement(By.id(applyToAllLocator)).click();
				}
				String num = driver.findElement(By.xpath(emailNumberLocator)).getText();
				totalNumberOfEmails = num.substring(num.indexOf("/")).replace("/", "");
				driver.close();
			}
		}
		driver.switchTo().window(parentWindow);
	}

	@And("I switch to Job page and verify highlighted text having css \"(.*?)\" and email number \"(.*?)\"$")
	public void verifyJobPage(String highlightedTextLocator, String emailNumberLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				String currentURL = driver.getCurrentUrl();
				hooks.verifyElement(currentURL, hooks.getValue("jobURL"));
				String emailNumber = driver.findElement(By.xpath(emailNumberLocator)).getText();
				hooks.waitForElement(By.cssSelector(highlightedTextLocator));
				List<WebElement> highlightedText = driver.findElements(By.cssSelector(highlightedTextLocator));
				hooks.assertElement(highlightedText.get(1).getText(), hooks.getValue("jobPageHighlightedText"));
				hooks.assertElement(emailNumber.substring(emailNumber.indexOf("/")).replace("/", ""),
						hooks.getValue("jobPageEmailNumber"));
				driver.close();
			}
		}
		driver.switchTo().window(parentWindow);
	}

	@And("I verify the total number of emails not to be transfered having css \"(.*?)\"$")
	public void verifyEmailsNotToTransfer(String emailNumberLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(emailNumberLocator));
		hooks.assertElement(totalNumberOfEmails,
				driver.findElement(By.cssSelector(emailNumberLocator)).getText().replaceAll("\\D", ""));
	}

	@Given("^I enter search text \"([^\"]*)\" in textfield having xpath \"([^\"]*)\"$")
	public void enter_search_text(String searchtext, String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue(searchtext));
	}
	/*
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

	@And("I enter Peter Chan in textfield having xpath \"(.*?)\"$")
	public void enterText(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("newUserName"));
	}

	@And("I enter budget in textfield having xpath \"(.*?)\"$")
	public void enterBudgetText(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("budgetText"));
	}

	@And("I enter paragraph in textfield having xpath \"(.*?)\"$")
	public void enterParagraph(String textFieldLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(textFieldLocator));
		driver.findElement(By.xpath(textFieldLocator)).sendKeys(hooks.getValue("paragraph"));
	}

*/
	@Then("I verify the number having xpath \"(.*?)\" searched with \"(.*?)\"$")
	public void verifyNumber(String num, String text) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(num));
		String number = driver.findElement(By.xpath(num)).getText();
		if (text.equals(hooks.getValue("floridaText"))) {
			hooks.verifyFloridaNumber(number.substring(number.indexOf("/")).replace("/", ""),
					hooks.getValue("floridaNumberValue"));
		} else if (text.equals(hooks.getValue("kidcareText"))) {
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""),
					hooks.getValue("kidcareNumberValue"));
			this.wait(5);
		} else if (text.equals(hooks.getValue("Peter Chan"))) {
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""),
					hooks.getValue("peterchanNumberValue"));
			this.wait(5);
		} else if (text.equals(hooks.getValue("budget"))) {
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""),
					hooks.getValue("budgetNumberValue"));
			this.wait(5);
		} else if (text.equals(hooks.getValue("budgetWithSubject"))) {
			hooks.assertElement(number.substring(number.indexOf("/")).replace("/", ""),
					hooks.getValue("budgetWithSubjectNumberValue"));
			this.wait(5);
		}
	}

	@And("I verify the highlighted \"(.*?)\" text having css \"(.*?)\"$")
	public void verifyHighlightedText(String highlightedTextName, String highlightedTextLocator) {
		Hooks hooks = new Hooks();
		hooks.waitForWebElement(By.cssSelector(highlightedTextLocator));
		String highlightedText = driver.findElement(By.cssSelector(highlightedTextLocator)).getText();
		hooks.assertElement(highlightedTextName, highlightedText);
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
		File file = null;

		if (opsystem.contains("Windows")) {
			file = new File(hooks.getValue("sessionFolder"));
		} else if (opsystem.contains("Linux")) {
			file = new File(hooks.getValue("sessionFolder"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			file = new File(hooks.getValue("sessionFolder"));
		}

		if (file.exists()) {

			if (opsystem.contains("Windows")) {
				FileUtils.deleteDirectory(new File(hooks.getValue("sessionFolder")));
			} else if (opsystem.contains("Linux")) {
				FileUtils.deleteDirectory(new File(hooks.getValue("sessionFolder")));
			} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
				FileUtils.deleteDirectory(new File(hooks.getValue("sessionFolder")));
			}
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

	@Then("I upload the image having id \"(.*?)\"$")
	public void uploadImage(String imageLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(imageLocator));

		if (opsystem.contains("Windows")) {
			driver.findElement(By.id(imageLocator)).sendKeys(hooks.getValue("imageName"));
			driver.findElement(By.id(imageLocator)).sendKeys(hooks.getValue("imageName1"));
			driver.findElement(By.id(imageLocator)).sendKeys(hooks.getValue("imageName2"));
		} else if (opsystem.contains("Linux")) {
			driver.findElement(By.id(imageLocator)).sendKeys(hooks.getValue("imageName"));
		} else if ((opsystem.contains("MacOS")) || (opsystem.contains("OS X"))) {
			driver.findElement(By.id(imageLocator)).sendKeys(hooks.getValue("imageName"));
		}

	}

	@Then("I edit the address book having id \"(.*?)\"$")
	public void editAddressBook(String addressBookLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(addressBookLocator));
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.CONTROL, Keys.HOME);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.END);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.ENTER);
		driver.findElement(By.id(addressBookLocator)).sendKeys(hooks.getValue("newUserName"));
	}

	@Then("I verify the updated profile text having css \"(.*?)\"$")
	public void verifyUpdatedProfile(String profileLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(profileLocator));
		String profileName = driver.findElement(By.cssSelector(profileLocator)).getText();
		hooks.verifyElementPresence(profileName, hooks.getValue("newUserName"));
	}

	@Then("I revert back the correspondent values in id \"(.*?)\"$")
	public void revertCorrespondentsValues(String addressBookLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(addressBookLocator));
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.CONTROL, Keys.HOME);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.DOWN);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.SHIFT, Keys.END);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.BACK_SPACE);
	}

	@Then("I verify the profile text having css \"(.*?)\" has reverted$")
	public void verifyRevertedProfile(String profileLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(profileLocator));
		String profileName = driver.findElement(By.cssSelector(profileLocator)).getText();
		hooks.verifyElement(profileName, hooks.getValue("achieverName"));
	}

	@And("I verify the name \"(.*?)\" field on email source page$")
	public void verifyEmailSourcePage(String textfieldLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(textfieldLocator));
	}

	@Then("^value \"([^\"]*)\" should be displayed having xpath \"([^\"]*)\"$")
	public void verifyvalue(String strText, String actualText1) {
		String expectedvalue = null;
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.xpath(actualText1));
		if (strText.equals("document")) {
			expectedvalue = hooks.getValue("documentattachmentsvalue");
		} else if (strText.equals("image")) {
			expectedvalue = hooks.getValue("imageattachmentsvalue");
		} else if (strText.equals("other")) {
			expectedvalue = hooks.getValue("otherattachmentsvalue");
		}
		try {
			String strOrig = driver.findElement(By.xpath(actualText1)).getText();
			int intIndex = strOrig.indexOf(expectedvalue);
			if (intIndex == -1) {
				System.out.println("expectedvalue not found");
			} else {
				System.out.println("Found expectedvalue at index " + intIndex);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("FAILED: Either the " + expectedvalue + " is not present or Page fails to load");
		}
	}

	@Then("^value \"([^\"]*)\" should be displayed having css \"([^\"]*)\"$")
	public void verifyvaluebycss(String strText1, String actualText2) {
		String expectedvalue1 = null;
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(actualText2));

		if (strText1.equals("document")) {
			expectedvalue1 = hooks.getValue("documentattachmentsvalue1");
		} else if (strText1.equals("image")) {
			expectedvalue1 = hooks.getValue("imageattachmentsvalue1");
		} else if (strText1.equals("other")) {
			expectedvalue1 = hooks.getValue("otherattachmentsvalue1");
		}
		try {
			String strOrig1 = driver.findElement(By.cssSelector(actualText2)).getText();
			int intIndex1 = strOrig1.indexOf(expectedvalue1);
			if (intIndex1 == -1) {
				System.out.println("expectedvalue not found");
			} else {
				System.out.println("Found expectedvalue at index " + intIndex1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("FAILED: Either the " + expectedvalue1 + " is not present or Page fails to load");
		}
	}

	@And("I provide from date in textfield having id \"(.*?)\"$")
	public void fromDateValue(String dateLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(dateLocator));
		driver.findElement(By.id(dateLocator)).sendKeys(hooks.getValue("fromDate"));
	}

	@And("I provide to date in textfield having id \"(.*?)\"$")
	public void toDateValue(String dateLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(dateLocator));
		driver.findElement(By.id(dateLocator)).sendKeys(hooks.getValue("toDate"));
	}

}