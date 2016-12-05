package test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StepDefs {
	public WebDriver driver;
    public static String BASE_DIR;
    public static String BROWSER;

    public static String DEFAULT_BASE_DIR = System.getProperty ("user.home") + File.separator + "epadd-test";
	public static File newScreenshotFolder;
	private static Log log = LogFactory.getLog(Tester.class);
	static Properties VARS;
	public static final Logger logger = Logger.getLogger(Tester.class.getName());
	public static String EPADD_TEST_PROPS_FILE = System.getProperty("user.home") + File.separator + "epadd.test.properties";

	String userHome = System.getProperty("user.home");
	String opsystem = System.getProperty("os.name");
	Process epaddProcess = null;
	Stack<String> tabStack = new Stack<>();

	private Hooks hooks;
	private String screenshotsDir;

    public boolean runningOnMac() { return System.getProperty("os.name").startsWith("Mac"); }

    public StepDefs() {
		VARS = new Properties();

		File f = new File(EPADD_TEST_PROPS_FILE);
		if (f.exists() && f.canRead()) {
			log.info("Reading configuration from: " + EPADD_TEST_PROPS_FILE);
			try {
				InputStream is = new FileInputStream(EPADD_TEST_PROPS_FILE);
				VARS.load(is);
			} catch (Exception e) {
				print_exception("Error reading epadd properties file " + EPADD_TEST_PROPS_FILE, e, log);
			}
		} else {
			log.warn("ePADD properties file " + EPADD_TEST_PROPS_FILE + " does not exist or is not readable");
		}

        for (String key: VARS.stringPropertyNames()) {
            String val = System.getProperty (key);
            if (val != null && val.length() > 0)
                VARS.setProperty(key, val);
        }

        BASE_DIR = VARS.getProperty("epadd.test.dir");
        if (BASE_DIR == null)
            BASE_DIR = DEFAULT_BASE_DIR;

        new File(BASE_DIR).mkdirs();
        screenshotsDir = BASE_DIR + File.separator + "screenshots";
        new File(screenshotsDir).mkdirs();

        logger.info ("Base dir for this test run is: " + BASE_DIR);
        hooks = new Hooks();

    }

    public static String stackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter(0);
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        return sw.getBuffer().toString();
    }

    public static void print_exception(String message, Throwable t, Log log)
    {
        String trace = stackTrace(t);
        String s = message + "\n" + t.toString() + "\n" + trace;
        if (log != null)
            log.warn(s);
        System.err.println(s);
    }

	// @Given("^I navigate to \"(.*?)\"$")
	public void openURL(String url) {
        driver.navigate().to(url);
	}

	// @Given("^I wait for (\\d+) sec$")
	public void waitFor(int time) throws InterruptedException {
		TimeUnit.SECONDS.sleep(time);
	}

	// @Given("^I enter (.*) into input field with name \"(.*?)\"$")
	public void enterValueInInputField( String fieldName, String inputValue) throws InterruptedException {
		inputValue = resolveValue(inputValue);
		try {
			WebElement inputField = driver.findElement(By.name(fieldName));
			inputField.sendKeys(inputValue);
		} catch (Exception e) {
			throw new RuntimeException ("Unable to find an input field to enter value in: (" + inputValue + ") " + "field: " + fieldName + " page: " + driver.getCurrentUrl());
		}
	}

	// @Then("I navigate back$")
	public void navigateBack() {
		driver.navigate().back();
	}

	// @Then("I close ePADD$")
	public void closeEpadd() throws IOException, InterruptedException {
		if (epaddProcess == null)
			return;
		epaddProcess.destroy();
	}

	// @Given("I open ePADD$")
	public void openEpadd(String mode) throws IOException, InterruptedException {
		// we'll always launch using epadd-standalone.jar

		String errFile = System.getProperty("java.io.tmpdir") + File.separator + "epadd-test.err.txt";
		String outFile = System.getProperty("java.io.tmpdir") + File.separator + "epadd-test.out.txt";
        String cmd = VARS.getProperty ("cmd");
        if (cmd == null) {
            log.warn ("Please confirm cmd in epadd.test.properties");
            throw new RuntimeException ("no command to start epadd");
        }

        cmd = "java -Depadd.mode=" + mode +  " -Depadd.base.dir=" + BASE_DIR + " " + cmd;
        cmd = cmd + " --no-browser-open"; // we'll open our own browser
        ProcessBuilder pb = new ProcessBuilder(cmd.split(" "));

//		ProcessBuilder pb = new ProcessBuilder("java", "-Xmx2g", "-jar", "epadd-standalone.jar", "--no-browser-open");
		pb.redirectError(new File(errFile));
		pb.redirectOutput(new File(outFile));
		logger.info ("Sending epadd output to: " + outFile);
		epaddProcess = pb.start();
		logger.info ("Started ePADD");
	}

	// @Then("CSS element \"(.*)\" should have value (.*)$")
	public void verifyEquals(String selector, String expectedValue) {
		expectedValue = resolveValue(expectedValue);
		String actualText = driver.findElement(By.cssSelector(selector)).getText();
	    
		if (!actualText.equals(expectedValue)) {
			logger.warn ("ACTUAL text for CSS selector " + selector + ": " + actualText + " EXPECTED: " + expectedValue);
			throw new RuntimeException();
		}
		logger.info ("Found expected text for CSS selector " + selector + ": " + actualText);
		
	}

	// @Then("CSS element \"([^\"]*)\" should contain (.*)$")
	public void verifyContains(String selector, String expectedValue) {
		expectedValue = resolveValue(expectedValue);
		String actualText = driver.findElement(By.cssSelector(selector)).getText();
		actualText = actualText.toLowerCase();
		expectedValue = expectedValue.toLowerCase();
		if (!actualText.contains(expectedValue)) {
			logger.warn ("ACTUAL text for CSS selector " + selector + ": " + actualText + " EXPECTED TO CONTAIN: " + expectedValue);
			throw new RuntimeException();
		}
		logger.info ("Found expected text for CSS selector " + selector + ": " + actualText);
	}


	// @Then("CSS element \"([^\"]*)\" should start with a number > 0")
	public void verifyStartsWithNumberGT0(String selector) {
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

	// @Then("^open browser$")
	public void openBrowser() throws MalformedURLException {
        try {
            // String consoleOutputFile = this.getValue("browserConsoleOutputFile");
            // System.setProperty("webdriver.log.file", consoleOutputFile + "-" + this.getValue("browser") + ".txt");

            BROWSER = VARS.getProperty ("browser");

            if (BROWSER == null)
                BROWSER = "chrome";
            if ("firefox".equalsIgnoreCase(BROWSER)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equalsIgnoreCase(BROWSER)) {
                if (runningOnMac()) {
                    String macDriver = VARS.getProperty ("webdriver.chrome.driver");
                    if (macDriver == null)
                        macDriver = "/Users/hangal/workspace/epadd-launcher/src/test/resources/chromedriver";
                    System.setProperty("webdriver.chrome.driver", macDriver);
                } else {
                    System.err.println ("WARNING!!!! Chrome driver is only specified for Mac?");
                }
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--always-authorize-plugins=true"); // to allow flash - c.f. http://stackoverflow.com/questions/28804247/how-to-enable-plugin-in-chrome-browser-through-capabilities-using-web-driver
                driver = new ChromeDriver(options);
            } else if ("ie".equalsIgnoreCase(BROWSER)) {
                driver = new InternetExplorerDriver();
            }
            driver.manage().deleteAllCookies();
            driver.manage().window().maximize();
        } catch (Exception e) {
            print_exception("Error opening browser", e, log);
        }
    }

    public void closeBrowser() {
		driver.close();
	}

	// @Then("^I take full page screenshot called \"(.*?)\"$")
	public void takeScreenshot(String pageName) throws IOException {
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		String stamp = timestamp + ".png";
		Dimension saved = driver.manage().window().getSize();
//		driver.manage().window().setSize(new Dimension(1280, 2000));
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(screenshotsDir + File.separator + BROWSER + "-" + pageName + "-" + stamp));
//		driver.manage().window().setSize(saved);
	}

	public void visitAndTakeScreenshot(String url) throws IOException, InterruptedException {
		visitAndTakeScreenshot(url, 1);
	}

	public void visitAndTakeScreenshot(String url, int waitSecs) throws IOException, InterruptedException {
        openURL (url);
        int idx = url.lastIndexOf ("/");
        String page = (idx >= 0) ? url.substring (idx+1) : url;
		Thread.sleep (waitSecs * 1000);
        takeScreenshot(page);
    }

	// @Then("I verify that I am on page \"(.*?)\"$")
	public void verifyURL(String expectedURL) {
		expectedURL = resolveValue(expectedURL);
		String currentURL = driver.getCurrentUrl();
		if (!currentURL.contains(expectedURL))
			throw new RuntimeException("Expected URL: " + expectedURL + " actual URL: " + currentURL);
	}

	// @Given("I find CSS element \"(.*)\" and click on it$")
	public void clickOnCSS(String cssSelector) throws InterruptedException {
		// this could hit any element with the text! e.g. a button, an a tag, or even a td tag!
		driver.findElement(By.cssSelector(cssSelector)).click();
	}

	// @Then("I find CSS element \"(.*)\" and verify that it contains \"(.*)\"$")
	public void verifyCSSContains(String cssSelector, String expectedText) throws InterruptedException {
		cssSelector = resolveValue(cssSelector);
		expectedText = resolveValue(expectedText);
		// this could hit any element with the text! e.g. a button, an a tag, or even a td tag!
		String elementText = driver.findElement(By.cssSelector(cssSelector)).getText();
		if (!elementText.contains(expectedText)) {
			throw new RuntimeException("Expected CSS element " + cssSelector + " to contain " + expectedText + " but it has " + elementText);
		}
		logger.info("CSS element " + cssSelector + " has value " + elementText + " and contains " + expectedText);
	}

	public void clickOn(String linkText) throws InterruptedException {
		clickOn ("", linkText);
	}

	// will click on the link with the exact linkText if available; if not, on a link containing linkText
	// linkText is case insensitive
	// can use as:
	// I click on "Search" --> searches button, link, td tags with this text (or their sub-elements), in that order
	// or
	// I click on button "Search"
	// @Given("I click on (.*) *\"(.*?)\"$")
	public void clickOn(String elementType, String linkText) throws InterruptedException {
		elementType = elementType.trim(); // required because linkText might come as "button " due to regex matching above
		linkText = resolveValue(linkText);
		linkText = linkText.toLowerCase();
		WebElement e = null;

		// we'll look for linkText in a few specific tags, in this defined order
		// sometimes the text we're looking for is under a further element, like <a><p>...</p></a>
		String searchOrderEType[] = (elementType.length() != 0) ? new String[]{elementType, elementType + "//*"} : new String[]{"button","div//button","div//*", "a", "td", "button//*","a//*", "td//*","span"};

		// prefer to find an exact match first if possible
		// go in order of searchOrderEtype
		// be careful to ignore invisible elements
		// be case-insensitive
		for (String s: searchOrderEType) {
			String xpath = "//" + s + "[translate(text(),  'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '" + linkText + "')]";
			try { e = driver.findElement(By.xpath(xpath)); } catch (Exception e1) { } // ignore the ex, we'll try to find a link containing it
			if (e != null && !e.isDisplayed())
				e = null; // doesn't count if the element is not visible
			if (e != null)
				break;
		}

		// no exact match? try to find a contained match, again in order of searchOrderEtype
		if (e == null) {
			for (String s: searchOrderEType) {
				String xpath = "//" + s + "[contains(translate(text(),  'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + linkText + "')]";
				try { e = driver.findElement(By.xpath(xpath)); } catch (Exception e1) { } // ignore the ex, we'll try to find a link containing it
				if (e != null && !e.isDisplayed())
					e = null; // doesn't count if the element is not visible
				if (e != null)
					break;
			}
		}

		// ok, we we have an element to click on?
		if (e != null) {
			// color the border red of the selected element to make it easier to understand what is happening
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].style.border = '2px solid red';", e);
			logger.info ("Clicking on (" + e.getTagName() + ") containing " + linkText);
			waitFor(2);
			e.click(); // seems to be no way of getting text of a link through CSS
			waitFor(2); // always wait for 1 sec after click
		} else
			throw new RuntimeException ("Unable to find an element to click on: (" + elementType + ") " + linkText + " page: " + driver.getCurrentUrl());
	}

	// @Then("^I wait for the page (.*?) to be displayed within (\\d+) seconds$")
	public void waitForPageToLoad(String url, int time) {
		url = resolveValue(url);
		long startMillis = System.currentTimeMillis();
		WebDriverWait wait = new WebDriverWait(driver, time);
		try {
			wait.until(ExpectedConditions.urlMatches(url));
		} catch (org.openqa.selenium.TimeoutException e) {
			throw new RuntimeException (url + " did not open in " + time + " seconds. Exception occurred: ", e);
		}

		logger.info ("Page " + url + " loaded in " + (System.currentTimeMillis() - startMillis) + "ms");
	}

	// waits for button containing the given buttonText to appear within time seconds
	// @Then("^I wait for button (.*?) to be displayed within (\\d+) seconds$")
	public void waitForButton(String buttonText, int time) {
		buttonText = resolveValue(buttonText);
		
		long startMillis = System.currentTimeMillis();
		WebDriverWait wait = new WebDriverWait(driver, time);
		try {
			buttonText = buttonText.toLowerCase();
			String xpath = "//*[contains(translate(text(),  'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + buttonText + "')]";

			driver.findElement(By.xpath(xpath)).getText();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))); // case insensitive match! see
		} catch (org.openqa.selenium.TimeoutException e) {
			throw new RuntimeException ("Button text" + buttonText + " was not found in " + time + " seconds. Exception occured: ", e);
		}

		logger.info ("Button " + buttonText + " clickable in " + (System.currentTimeMillis() - startMillis) + "ms");
	}

	// @Given("I find xpath element \"(.*)\" and click on it$")
	public void clickOnElementHavingXpath(String xpathLocator) {
		//hooks.waitForElement(By.xpath(xpathLocator));
		driver.findElement(By.xpath(xpathLocator)).click();
	}

	private int nMessagesOnBrowsePage() {
		String num = driver.findElement(By.id("pageNumbering")).getText();
		// num will be "x/y", e.g. something like 123/312. Extract the "312" part of it
		String totalNumberOfEmails = num.substring(num.indexOf("/")).replace("/", "");
		int n = -1;
		try { n = Integer.parseInt(totalNumberOfEmails); } catch (Exception e) { }
		return n;
	}

	// @Then("^I check for ([<>]*) *(\\d+) messages on the page$")
	public void checkMessagesOnBrowsePage(String relation, int nExpectedMessages) {
		relation = relation.trim();
		int nActualMessages = nMessagesOnBrowsePage();
		logger.info ("checking for " + relation + " " + nExpectedMessages + " messages, got " + nActualMessages);
		if ("".equals(relation) && !(nActualMessages == nExpectedMessages))
			throw new RuntimeException("Expected " + nExpectedMessages + " found " + nActualMessages);
		if (">".equals(relation) && !(nActualMessages > nExpectedMessages))
			throw new RuntimeException("Expected >" + nExpectedMessages + " found " + nActualMessages);
		if ("<".equals(relation) && !(nActualMessages < nExpectedMessages))
			throw new RuntimeException("Expected <" + nExpectedMessages + " found " + nActualMessages);
	}

	// @Then("I check for ([<>]*) *(\\d+) highlights on the page")
	public void checkHighlights(String relation, int nExpectedHighlights) {
		Collection<WebElement> highlights = driver.findElements(By.cssSelector(".muse-highlight"));
		highlights.addAll(driver.findElements(By.cssSelector(".hilitedTerm"))); // could be either of these classes used for highlighting
		int nHighlights = highlights.size();

		logger.info ("checking for " + relation + " " + nExpectedHighlights + " messages, got " + nHighlights);
		if ("".equals(relation) && !(nHighlights == nExpectedHighlights))
			throw new RuntimeException("Expected " + nExpectedHighlights + " found " + nHighlights);
		if (">".equals(relation) && !(nHighlights > nExpectedHighlights))
			throw new RuntimeException("Expected >" + nExpectedHighlights + " found " + nHighlights);
		if ("<".equals(relation) && !(nHighlights < nExpectedHighlights))
			throw new RuntimeException("Expected <" + nExpectedHighlights + " found " + nHighlights);
	}

	// @And("I check that \"(.*)\" is highlighted")
	public void checkHighlighted(String termToBeHighighted) {
		Collection<WebElement> highlights = driver.findElements(By.cssSelector(".muse-highlight"));
		highlights.addAll(driver.findElements(By.cssSelector(".hilitedTerm"))); // could be either of these classes used for highlighting
		for (WebElement e: highlights)
			if (termToBeHighighted.equals(e.getText())) {
				logger.info ("highlighted term " + termToBeHighighted + " found");
				return;
			}
		String message = "highlighted term " + termToBeHighighted + " not found!";
		logger.warn (message);
		throw new RuntimeException(message);
	}

		// check for some messages in another tab, then close it
	// @Then("some messages should be displayed in another tab$")
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

	// @Given("I switch to the \"(.*)\" tab$")
	public void switchToTab(String title) throws InterruptedException {
        title = title.toLowerCase();
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
                String tabTitle = driver.getTitle();
                if (tabTitle == null)
                    continue;
                tabTitle = tabTitle.toLowerCase();
				if (title.equals(tabTitle)) {
					tabStack.push(parentWindow);
					return;
				}
			}
		}
		logger.warn ("Error: tab with title " + title + " not found!");
		// title not found? return to parentWindow
		driver.switchTo().window(parentWindow);
	}

	// @Given("I close tab")
	public void closeTab() throws InterruptedException {
		driver.close();
		// need to explicitly switch to last window, otherwise driver will stop working
		if (tabStack.size() > 0) {
			String s = tabStack.pop();
			driver.switchTo().window(s);
		}
	}

	// @Given("I switch to the previous tab")
	public void switchTabBack() throws InterruptedException {
		if (tabStack.size() < 1) {
			logger.warn ("Warning: trying to pop tab stack when it is empty!");
			return;
		}

		String lastWindow = tabStack.pop();
		switchToTab (lastWindow);
	}

	// @Given("I mark all messages \"Do not transfer\"")
	public void markDNT() throws InterruptedException {
		WebElement e = driver.findElement(By.id("doNotTransfer"));

		if (!e.getAttribute("class").contains("flag-enabled")) {
			driver.findElement(By.id("doNotTransfer")).click();
			waitFor(1);
		}

		driver.findElement(By.id("applyToAll")).click();
	}

	// @Given("I set dropdown \"(.*?)\" to \"(.*?)\"$")
	public void dropDownSelection(String cssSelector, String value) throws InterruptedException {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].style.border = '2px solid red';", element);
		Select select = new Select(element);
        waitFor (2);
        select.selectByVisibleText(value);
	}

	// @Then("I add \"(.*)\" to the address book$")
	public void editAddressBook(String newName) throws InterruptedException {
		WebElement e = driver.findElement(By.cssSelector("#text"));
		e.click();
		// go to the top of the textbox
		// on windows, we need home key, on mac, we need cmd-up.
		// probably doesn't hurt to send both ?
		// this is not working on chrome-mac currently!
		e.sendKeys(Keys.HOME);
		e.sendKeys(Keys.COMMAND, Keys.UP);
		e.sendKeys(Keys.chord(Keys.COMMAND, Keys.ARROW_UP));
		e.sendKeys(Keys.chord(Keys.COMMAND, Keys.ARROW_UP));
		e.sendKeys(Keys.chord(Keys.META, Keys.UP));
		e.sendKeys(Keys.chord(Keys.META, Keys.ARROW_UP));

		// one line down, and type in the new name, followed by enter
		e.sendKeys(Keys.DOWN);
		e.sendKeys(newName); // add the new name as the first row
		e.sendKeys(Keys.ENTER);
	}

	// @Then("I confirm the alert$")
	public void handleAlert() throws InterruptedException {
		Thread.sleep(5000);
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	// @Then("I verify the folder (.*) does not exist$")
	public void checkFolderDoesNotExist(String folderName) throws InterruptedException, IOException {
		folderName = resolveValue(folderName);
		if (new File(folderName).exists()) {
			throw new RuntimeException ("Folder " + folderName + " is not expected to exist, but it does!");
		}
		logger.info ("Good, folder " + folderName + " does not exist");
	}

	// @Then("I verify the folder (.*) exists$")
	public void checkFolderExists(String folderName) throws InterruptedException, IOException {
		folderName = resolveValue(folderName);
		if (!new File(folderName).exists()) {
			throw new RuntimeException ("Folder " + folderName + " is expected to exist, but it does not!");
		}
		logger.info ("Good, folder " + folderName + " exists");
	}

	/////////////////////// REVIEWED UPTIL HERE - SGH /////////////////////////////////////////////

	// @Then("copy files$")
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



	// @Then("^create folder$")
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

	// @And("I switch to Job page and verify highlighted text having css \"(.*?)\" and email number \"(.*?)\"$")
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

	// @Then("I upload the image having id \"(.*?)\"$")
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

	// @Then("I verify the updated profile text having css \"(.*?)\"$")
	public void verifyUpdatedProfile(String profileLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(profileLocator));
		String profileName = driver.findElement(By.cssSelector(profileLocator)).getText();
		hooks.verifyElementPresence(profileName, hooks.getValue("newUserName"));
	}

	// @Then("I revert back the correspondent values in id \"(.*?)\"$")
	public void revertCorrespondentsValues(String addressBookLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.id(addressBookLocator));
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.CONTROL, Keys.HOME);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.DOWN);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.SHIFT, Keys.END);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.id(addressBookLocator)).sendKeys(Keys.BACK_SPACE);
	}

	// @Then("I verify the profile text having css \"(.*?)\" has reverted$")
	public void verifyRevertedProfile(String profileLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.cssSelector(profileLocator));
		String profileName = driver.findElement(By.cssSelector(profileLocator)).getText();
		hooks.verifyElement(profileName, hooks.getValue("achieverName"));
	}

	// @And("I verify the name \"(.*?)\" field on email source page$")
	public void verifyEmailSourcePage(String textfieldLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(textfieldLocator));
	}

	// @And("I provide from date in textfield having id \"(.*?)\"$")
	public void fromDateValue(String dateLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(dateLocator));
		driver.findElement(By.id(dateLocator)).sendKeys(hooks.getValue("fromDate"));
	}

	// @And("I provide to date in textfield having id \"(.*?)\"$")
	public void toDateValue(String dateLocator) throws InterruptedException {
		Hooks hooks = new Hooks();
		hooks.waitForElement(By.name(dateLocator));
		driver.findElement(By.id(dateLocator)).sendKeys(hooks.getValue("toDate"));
	}

	// if the value is <abc> then we read the value of property abc in the hook. otherwise we use it as is.
	public String resolveValue(String s) {
		if (s == null)
			return null;
		s = s.trim(); // strip spaces before and after
		if (s.startsWith("<") && s.endsWith(">"))
			s = VARS.getProperty(s.substring(1, s.length()-1));
		if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) // strip quotes -- if "abc", simply make it abc
			s = s.substring(1, s.length()-1);
		return s;
	}

}
