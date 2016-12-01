package test;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by hangal on 11/29/16.
 */
public class Tester {

    private static Log log = LogFactory.getLog(Tester.class);
    static Properties VARS;
    public static String BASE_URL = "http://localhost:9099/epadd/";
    public static String EPADD_TEST_PROPS_FILE = System.getProperty("user.home") + File.separator + "epadd.test.properties";

    public boolean runningOnMac() { return System.getProperty("os.name").startsWith("Mac"); }
    WebDriver driver;
    StepDefs test;



    // temporarily disable quits
//	@  After
    public void closeBrowser() {
        try {
            driver.quit();
        } catch (Exception e) {

        }
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

    /* time is in seconds */
    public void waitFor(int time) throws InterruptedException {
        TimeUnit.SECONDS.sleep(time);
    }

    /** if the value is <abc> then we read the value of property abc in the hook. otherwise we use it as is. */
    public String processValue(String s) {
        if (s == null)
            return null;
        s = s.trim(); // strip spaces before and after
        if (s.startsWith("<") && s.endsWith(">"))
            s = (String) VARS.get(s.substring(1, s.length()-1));
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) // strip quotes -- if "abc", simply make it abc
            s = s.substring(1, s.length()-1);
        return s;
    }

    public void enterValueInInputField(String fieldName, String inputValue) {
        inputValue = processValue(inputValue);
        try {
            WebElement inputField = driver.findElement(By.name(fieldName));
            inputField.sendKeys(inputValue);
        } catch (Exception e) {
            throw new RuntimeException ("Unable to find an input field to enter value in: (" + inputValue + ") " + "field: " + fieldName + " page: " + driver.getCurrentUrl());
        }
    }

    public void clickOn(String elementType, String linkText) throws InterruptedException {
        elementType = elementType.trim(); // required because linkText might come as "button " due to regex matching above
        linkText = processValue(linkText);
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
            log.info ("Clicking on (" + e.getTagName() + ") containing " + linkText);
            waitFor(2);
            e.click(); // seems to be no way of getting text of a link through CSS
            waitFor(2); // always wait for 1 sec after click
        } else
            throw new RuntimeException ("Unable to find an element to click on: (" + elementType + ") " + linkText + " page: " + driver.getCurrentUrl());
    }

    public void waitForButton(String buttonText, int time) {
        buttonText = processValue(buttonText);

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

        log.info ("Button " + buttonText + " clickable in " + (System.currentTimeMillis() - startMillis) + "ms");
    }

    public void waitForPageToLoad(String url, int time) {
        url = processValue(url);
        long startMillis = System.currentTimeMillis();
        WebDriverWait wait = new WebDriverWait(driver, time);
        try {
            wait.until(ExpectedConditions.urlMatches(url));
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new RuntimeException (url + " did not open in " + time + " seconds. Exception occurred: ", e);
        }

        log.info ("Page " + url + " loaded in " + (System.currentTimeMillis() - startMillis) + "ms");
    }

    public void appraisalImport() throws InterruptedException {
        test.openURL(BASE_URL + "email-sources");
        test.enterValueInInputField ("name", "<name>");
        test.enterValueInInputField ("alternateEmailAddrs", "<emailAddress>");
        test.enterValueInInputField ("mboxDir2", "<emailFolderLocationSrc1>");
        test.enterValueInInputField ("emailSource2", "src1");
        test.clickOn ("", " Add another folder");
        test.enterValueInInputField ("mboxDir3", "<emailFolderLocationSrc2>");
        test.enterValueInInputField ("emailSource3", "src2");

        test.clickOn ("", "Continue");
        test.waitForButton ("Select all folders", 20 /* seconds */);
        test.clickOn ("#selectall0");
        test.clickOn ("#selectall1");
        test.clickOn ("", "Continue");
        test.waitForPageToLoad ("<browserTopPage>", 240);
    }

    public void visitAllPages() throws InterruptedException, IOException {

        // all pages in web.xml can be included here!

        test.visitAndTakeScreenshot(BASE_URL + "/set-images");
        // needs proper POST: test.visitAndTakeScreenshot(BASE_URL + "/upload-images");
        test.visitAndTakeScreenshot(BASE_URL + "correspondents");
        test.visitAndTakeScreenshot(BASE_URL + "browse-top");
        test.visitAndTakeScreenshot(BASE_URL + "search-query");
        test.visitAndTakeScreenshot(BASE_URL + "advanced-search");
        test.visitAndTakeScreenshot(BASE_URL + "entities?type=en_person");
        test.visitAndTakeScreenshot(BASE_URL + "entities?type=en_loc");
        test.visitAndTakeScreenshot(BASE_URL + "entities?type=en_org");
        test.visitAndTakeScreenshot(BASE_URL + "by-folder");
        test.visitAndTakeScreenshot(BASE_URL + "browse-finetypes");
        test.visitAndTakeScreenshot(BASE_URL + "lexicon");
        test.visitAndTakeScreenshot(BASE_URL + "edit-lexicon?lexicon=general");
        test.visitAndTakeScreenshot(BASE_URL + "multi-search?term=award&term=prize&term=medal&term=fellowship&term=certificate&");
        test.visitAndTakeScreenshot(BASE_URL + "image-attachments");
        test.visitAndTakeScreenshot(BASE_URL + "attachments?type=doc");
        test.visitAndTakeScreenshot(BASE_URL + "attachments?type=nondoc");
        test.visitAndTakeScreenshot(BASE_URL + "graph?view=people");
        test.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_person");
        test.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_loc");
        test.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_org");

        test.visitAndTakeScreenshot(BASE_URL + "edit-correspondents");
        test.visitAndTakeScreenshot(BASE_URL + "query-generator?refText=John Ellis Jeb Bush Sr. (born February 11, 1953) is an American businessman and politician who served as the 43rd Governor of Florida from 1999 to 2007", 5);
        test.visitAndTakeScreenshot(BASE_URL + "export");
        test.visitAndTakeScreenshot(BASE_URL + "export-review");
        test.visitAndTakeScreenshot(BASE_URL + "export-review?type=transferWithRestrictions");
        test.visitAndTakeScreenshot(BASE_URL + "export-review?type=doNotTransfer");

        test.visitAndTakeScreenshot(BASE_URL + "export-mbox");
        test.visitAndTakeScreenshot(BASE_URL + "debug");
        test.visitAndTakeScreenshot(BASE_URL + "settings");
        test.visitAndTakeScreenshot(BASE_URL + "about");
        test.visitAndTakeScreenshot(BASE_URL + "report");
        test.visitAndTakeScreenshot(BASE_URL + "error");

        // processing mode:
        test.visitAndTakeScreenshot(BASE_URL + "collections");
        test.visitAndTakeScreenshot(BASE_URL + "import");
        test.visitAndTakeScreenshot(BASE_URL + "collection-detail");
        test.visitAndTakeScreenshot(BASE_URL + "edit-accession");
        test.visitAndTakeScreenshot(BASE_URL + "assignauthorities-top");
        test.visitAndTakeScreenshot(BASE_URL + "assignauthorities");
        test.visitAndTakeScreenshot(BASE_URL + "export-processing");
        test.visitAndTakeScreenshot(BASE_URL + "export-review-processing");
        test.visitAndTakeScreenshot(BASE_URL + "export-review-processing?type=deliverWithRestrictions");
        test.visitAndTakeScreenshot(BASE_URL + "export-review-processing?type=doNotDeliver");

        // delivery
        test.visitAndTakeScreenshot(BASE_URL + "review-cart");
    }

    private static Options getOpt()
    {
        // create the Options
        // consider a local vs. global (hosted) switch. some settings will be disabled if its in global mode
        Options options = new Options();
        options.addOption( "ai", "import", false, "check appraisal import");
        options.addOption( "vap", "visit-all-pages", false, "visit all pages and check that they are alive (appraisal mode)");
        options.addOption( "b", "browse", false, "check browse (appraisal mode)");
        options.addOption( "fl", "flags", false, "check flags (appraisal)");
        options.addOption( "as", "adv-search", false, "check advanced search (appraisal mode)");
        options.addOption( "f", "facets", false, "check facets (appraisal mode)");
        options.addOption( "lex", "lexicon", false, "check lexicon");
        options.addOption( "sens", "sensitive", false, "check sensitive messages");
        options.addOption( "s", "settings", false, "check settings");
        options.addOption( "ae", "export", false, "check expraisal export");
        options.addOption( "pi", "processing import", false, "check import into processing");
        options.addOption( "pe", "processing-export", false, "check export from processing");
        options.addOption( "ds", "discovery", false, "check discovery module");
        options.addOption( "dl", "delivery", false, "check delivery module");
        options.addOption( "del", "delete", false, "delete archive when done");

        //	options.addOption( "ns", "no-shutdown", false, "no auto shutdown");
        return options;
    }

    public void doIt(String args[]) throws IOException, InterruptedException, ParseException {
        Options options = getOpt();
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        test = new StepDefs();
        test.openEpadd("appraisal");

        test.openBrowser();
        Thread.sleep (15000); // wait for it to startup and load the archive if needed
        if (cmd.hasOption("import")) {
            appraisalImport();
        }

        if (cmd.hasOption("visit-all-pages")) {
            // visit all pages, take screenshot
            visitAllPages();
        }

        test.closeEpadd();
    }

    public static void main (String args[]) throws InterruptedException, IOException, ParseException {
        new Tester().doIt(args);
    }
}
