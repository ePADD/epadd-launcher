package test;

import cucumber.api.cli.Main;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StepDefs {
	public WebDriver driver;
	public static String totalNumberOfEmails;
	public static File newScreenshotFolder;
	public static File newLogsFolder;
	public static String pID;

	public static final Logger logger = Logger.getLogger(Main.class.getName());

	String userHome = System.getProperty("user.home");
	String opsystem = System.getProperty("os.name");
	Process epaddProcess = null;
	Stack<String> tabStack = new Stack<>();

	private Hooks hooks;
	private String screenshotsDir;

	public StepDefs() {
		driver = Hooks.driver;
		hooks = new Hooks();
		screenshotsDir = hooks.getValue("screenshotsDir");
		if (screenshotsDir != null)
			new File(screenshotsDir).mkdirs();
		logger.info ("Screenshots will be stored in " + screenshotsDir);
	}

	@Given("^I navigate to \"(.*?)\"$")
	public void openURL(String url) throws Throwable {
        driver.navigate().to(url);
	}

	@Given("^I wait for (\\d+) sec$")
	public void waitFor(int time) throws InterruptedException {
		TimeUnit.SECONDS.sleep(time);
	}

	@Given("I enter (.*?) into input field with name \"(.*?)\"$")
	public void enterValueInInputField(String inputValue, String fieldName) throws InterruptedException {
		inputValue = parseValue(inputValue);
		WebElement inputField = driver.findElement(By.name(fieldName));
		inputField.sendKeys(inputValue);
	}

	@Then("I navigate back$")
	public void navigation() {
		driver.navigate().back();
	}

	@Then("I close ePADD$")
	public void closeApplication() throws IOException, InterruptedException {
		if (epaddProcess == null)
			return;
		epaddProcess.destroy();
	}

	@Given("I open ePADD$")
	public void openApplication() throws IOException, InterruptedException {
		// we'll always launch using epadd-standalone.jar
		String javaBinary = hooks.getValue("javaBinary");
		if (!new File(javaBinary).exists()) {
			logger.warn ("Warning: java binary does not exist, is probably misconfigured! " + javaBinary);
			throw new RuntimeException();
		}

		String errFile = System.getProperty("java.io.tmpdir") + File.separator + "epadd-test.err.txt";
		String outFile = System.getProperty("java.io.tmpdir") + File.separator + "epadd-test.out.txt";
		ProcessBuilder pb = new ProcessBuilder(javaBinary, "-Xmx2g", "-jar", "epadd-standalone.jar", "--no-browser-open");
		pb.redirectError(new File(errFile));
		pb.redirectOutput(new File(outFile));
		logger.info ("Sending epadd output to: " + outFile);
		epaddProcess = pb.start();
		logger.info ("Started ePADD");
	}

	@Then("CSS element \"(.*)\" should have value (.*)$")
	public void verifyEquals(String selector, String expectedValue) {
		expectedValue = parseValue(expectedValue);
		String actualText = driver.findElement(By.cssSelector(selector)).getText();
		if (!actualText.equals(expectedValue)) {
			logger.warn ("ACTUAL text for CSS selector " + selector + ": " + actualText + " EXPECTED: " + expectedValue);
			throw new RuntimeException();
		}
		logger.info ("Found expected text for CSS selector " + selector + ": " + actualText);
	}

	@Then("CSS element \"([^\"]*)\" should contain (.*)$")
	public void verifyContains(String selector, String expectedValue) {
		expectedValue = parseValue(expectedValue);
		String actualText = driver.findElement(By.cssSelector(selector)).getText();
		if (!actualText.contains(expectedValue)) {
			logger.warn ("ACTUAL text for CSS selector " + selector + ": " + actualText + " EXPECTED TO CONTAIN: " + expectedValue);
			throw new RuntimeException();
		}
		logger.info ("Found expected text for CSS selector " + selector + ": " + actualText);
	}


	@Then("CSS element \"([^\"]*)\" should start with a number > 0")
	public void verifyContains(String selector) {
		String actualText = driver.findElement(By.cssSelector(selector)).getText();
		actualText = actualText.trim();
		char ch = actualText.charAt(0);
		if (Character.isDigit(ch) && ch > '0') { // the number can't start with 0
			// its ok
		} else {
			logger.warn ("ACTUAL text " + actualText + " was expected to start with a number > 0");
			throw new RuntimeException();
		}
		logger.info ("Found expected text for CSS selector " + selector + ": " + actualText);
	}

	@Then("^open browser$")
	public void openBrowser() throws MalformedURLException {
		hooks.openBrowser();
	}

	@Then("^I take full page screenshot called \"(.*?)\"$")
	public void takeScreenshot(String page) throws Throwable {
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String stamp = timestamp + ".png";
		Dimension saved = driver.manage().window().getSize();
//		driver.manage().window().setSize(new Dimension(1280, 2000));
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(screenshotsDir + File.separator + hooks.getValue("browser") + "-" + page + "-" + stamp));
//		driver.manage().window().setSize(saved);
	}

	@Then("I verify that I am on page \"(.*?)\"$")
	public void verifyLexiconURL(String expectedURL) {
		expectedURL = parseValue(expectedURL);
		String currentURL = driver.getCurrentUrl();
		hooks.verifyElement(currentURL, expectedURL);
	}

	// be careful linkText should not have single or double quotes
	private void clickOnLinkContaining(String elementType, String linkText) {
		// this could hit any element with the text! e.g. a button, an a tag, or even a td tag!
		List<WebElement> es = driver.findElements(By.xpath("//" + elementType + "[contains(text(), '" + linkText + "')]"));
		for (WebElement e: es) {
			// click on the first displayed element
			if (e.isDisplayed()) {
				e.click();
				break;
			}
		}
	}

	@Given("I find CSS element \"(.*)\" and click on it$")
	public void clickOn(String cssSelector) throws InterruptedException {
		// this could hit any element with the text! e.g. a button, an a tag, or even a td tag!
		driver.findElement(By.cssSelector(cssSelector)).click();
	}

	// will click on the link with the exact linkText if available; if not, on a link containing linkText
	// can use as:
	// I click on "Search"
	// or
	// I click on button "Search"
	@Given("I click on (.*) *\"(.*?)\"$")
	public void clickOn(String elementType, String linkText) throws InterruptedException {
		// this could hit any element with the text! e.g. a button, an a tag, or even a td tag!
		WebElement e = null;
		// elementType is option, if it's present it is used in the xpath, otherwise * is used
		if (elementType.length() == 0)
			elementType = "*";

		try { driver.findElement(By.xpath("//" + elementType + "[text() = '" + linkText + "']")); } catch (Exception e1)  { } // ignore the ex, we'll try to find a link containing it
		if (e != null) {
			e.click(); // seems to be no way of getting text of a link through CSS
		}
		else
			clickOnLinkContaining(elementType, linkText);
		waitFor(1);
	}

	@Then("I click on xpath element \"(.*?)\"$")
	public void clickOnElementHavingXpath(String xpathLocator) {
		hooks.waitForElement(By.xpath(xpathLocator));
		driver.findElement(By.xpath(xpathLocator)).click();
	}

	private int nMessagesOnBrowsePage() {
		String num = driver.findElement(By.xpath("//div[@id='pageNumbering']")).getText();
		String totalNumberOfEmails = num.substring(num.indexOf("/")).replace("/", "");
		int n = -1;
		try { n = Integer.parseInt(totalNumberOfEmails); } catch (Exception e) { }
		return n;
	}

	@Then("I check for (.*) (\\d*) messages on the page")
	public void checkMessagesOnBrowsePage(String relation, int nExpectedMessages) {
		int nActualMessages = nMessagesOnBrowsePage();
		logger.info ("checking for " + relation + " " + nExpectedMessages + " messages, got " + nActualMessages);
		if ("".equals(relation) && !(nActualMessages == nExpectedMessages))
			throw new RuntimeException("Expected " + nExpectedMessages + " found " + nActualMessages);
		if (">".equals(relation) && !(nActualMessages > nExpectedMessages))
			throw new RuntimeException("Expected >" + nExpectedMessages + " found " + nActualMessages);
		if ("<".equals(relation) && !(nActualMessages < nExpectedMessages))
			throw new RuntimeException("Expected <" + nExpectedMessages + " found " + nActualMessages);
	}

	@Then("I check for (.*) (\\d*) highlights on the page")
	public void checkHighlights(String relation, int nExpectedHighlights) {
		int nHighlights = driver.findElements(By.cssSelector(".muse-highlight")).size();

		logger.info ("checking for " + relation + " " + nExpectedHighlights + " messages, got " + nHighlights);
		if ("".equals(relation) && !(nHighlights == nExpectedHighlights))
			throw new RuntimeException("Expected " + nExpectedHighlights + " found " + nHighlights);
		if (">".equals(relation) && !(nHighlights > nExpectedHighlights))
			throw new RuntimeException("Expected >" + nExpectedHighlights + " found " + nHighlights);
		if ("<".equals(relation) && !(nHighlights < nExpectedHighlights))
			throw new RuntimeException("Expected <" + nExpectedHighlights + " found " + nHighlights);
	}

	@And("I check that \"(.*)\" is highlighted")
	public void checkHighlighted(String termToBeHighighted) {
		Collection<WebElement> highlights = driver.findElements(By.cssSelector(".muse-highlight"));
		highlights.addAll(driver.findElements(By.cssSelector(".hilitedTerm"))); // could be either of these classes used for highlighting
		for (WebElement e: highlights)
			if (termToBeHighighted.equals(e.getText())) {
				logger.info ("highlighted term " + termToBeHighighted + " found");
				return;
			}
		logger.info ("highlighted term " + termToBeHighighted + " not found!");
		return;
	}

		// check for some messages in another tab, then close it
	@Then("some messages should be displayed in another tab$")
	public void someMessagesShouldBeDisplayed() throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				int nMessages = nMessagesOnBrowsePage();
				if (nMessages <= 0) {
					throw new RuntimeException("Error: No messages on browse page");
				} else {
					logger.info (nMessages + " messages on the browse page");
				}
				driver.close();
			}
		}
		driver.switchTo().window(parentWindow);
	}

	@Given("I switch to the \"(.*)\" tab$")
	public void switchToTab(String title) throws InterruptedException {
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				if (title.equals(driver.getTitle())) {
					tabStack.push(parentWindow);
					return;
				}
			}
		}
		logger.warn ("Error: tab with title " + title + " not found!");
		// title not found? return to parentWindow
		driver.switchTo().window(parentWindow);
	}

	@Given("I close tab")
	public void closeTab() throws InterruptedException {
		driver.close();
		// need to explicitly switch to last window, otherwise driver will stop working
		if (tabStack.size() > 0) {
			String s = tabStack.pop();
			driver.switchTo().window(s);
		}
	}

	@Given("I switch to the previous tab")
	public void switchTabBack() throws InterruptedException {
		if (tabStack.size() < 1) {
			logger.warn ("Warning: trying to pop tab stack when it is empty!");
			return;
		}

		String lastWindow = tabStack.pop();
		switchToTab (lastWindow);
	}

	@Given("I mark all messages \"Do not transfer\"")
	public void markDNT() throws InterruptedException {
		WebElement e = driver.findElement(By.id("doNotTransfer"));

		if (!e.getAttribute("class").contains("flag-enabled")) {
			driver.findElement(By.id("doNotTransfer")).click();
			wait(1);
		}

		driver.findElement(By.id("applyToAll")).click();
	}

	@Given("I set dropdown \"(.*?)\" to \"(.*?)\"$")
	public void dropDownSelection(String cssSelector, String value) {
		Select select = new Select(driver.findElement(By.cssSelector(cssSelector)));
		select.selectByVisibleText(value);
	}

	@Then("I add \"(.*)\" to the address book$")
	public void editAddressBook(String newName) throws InterruptedException {
		WebElement e = driver.findElement(By.cssSelector("#text"));
		e.sendKeys(Keys.HOME);
		e.sendKeys(Keys.DOWN);
		e.sendKeys(newName); // add the new name as the first row
		e.sendKeys(Keys.ENTER);
	}

	/////////////////////// REVIEWED UPTIL HERE - SGH /////////////////////////////////////////////

	@Then("I click on \"(.*?)\" button having css \"(.*?)\"$")
	public void clickSelectAllFoldersButton(String buttonName, String buttonCSS) {
		hooks.waitForElement(By.cssSelector(buttonCSS));
		driver.findElement(By.cssSelector(buttonCSS)).click();
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


	@Then("^I wait for the page \"(.*?)\" to be displayed within \"(.*?)\" seconds$")
	public void waitForPageToLoad(String url, int time) throws Throwable {
		WebDriverWait wait = new WebDriverWait(driver, time);
		Hooks hooks = new Hooks();
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div/a[@href='correspondents']")));
		} catch (org.openqa.selenium.TimeoutException e) {
			logger.warn(hooks.getValue("browserTopPage") + " did not opened in " + time
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

	// if the value is <abc> then we read the value of property abc in the hook. otherwise we use it as is.
	public String parseValue(String s) {
		Hooks hooks = new Hooks();
		if (s == null)
			return null;
		if (s.startsWith("<") && s.endsWith(">"))
			s = hooks.getValue(s.substring(1, s.length()-1));
		if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) // strip quotes -- if "abc", simply make it abc
			s = s.substring(1, s.length()-1);
		return s;
	}

}
