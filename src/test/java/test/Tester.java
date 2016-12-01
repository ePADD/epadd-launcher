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

    public void appraisalImport() throws InterruptedException, IOException {
        test.openURL(BASE_URL + "email-sources");
        test.enterValueInInputField("name", "<name>");
        test.enterValueInInputField("alternateEmailAddrs", "<emailAddress>");
        test.enterValueInInputField("mboxDir2", test.resolveValue("<data.dir>") + File.separator + "mbox1");
        test.enterValueInInputField("emailSource2", "src1");
        test.clickOn("", " Add another folder");
        test.enterValueInInputField("mboxDir3", test.resolveValue("<data.dir>") + File.separator + "mbox2");
        test.enterValueInInputField("emailSource3", "src2");

        test.clickOn("", "Continue");
        test.waitForButton("Select all folders", 20 /* seconds */);
        test.clickOnCSS("#selectall0");
        test.clickOnCSS("#selectall1");
        test.clickOn("", "Continue");
        test.waitForPageToLoad("<browserTopPage>", 240);
        setImages();
    }

    public void setImages() throws InterruptedException, IOException {
        test.openURL(BASE_URL + "browse-top");
        test.clickOnCSS ("#more-options");
        test.clickOn ("Set Images");
        test.enterValueInInputField("profilePhoto", test.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "profilePhoto.png");
        test.enterValueInInputField("bannerImage", test.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "bannerImage.png");
        test.enterValueInInputField("landingPhoto", test.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "landingPhoto.png");
        test.clickOn ("button", "Upload");
        test.clickOn ("button", "Back");
        test.takeScreenshot("profile-images");
    }

    private void checkNumberOfAttachments() {
        test.openURL(BASE_URL + "browse-top");
        test.verifyEquals ("#nImageAttachments", "136");
        test.verifyEquals ("#nDocAttachments", "289");
        test.verifyEquals ("#nOtherAttachments", "70");
    }

    public void basicChecks() throws IOException, InterruptedException {
        test.visitAndTakeScreenshot(BASE_URL + "debug");
        test.visitAndTakeScreenshot(BASE_URL + "settings");
        test.visitAndTakeScreenshot(BASE_URL + "about");
        test.visitAndTakeScreenshot(BASE_URL + "report");
        test.visitAndTakeScreenshot(BASE_URL + "error");
        checkNumberOfAttachments();
    }

    private void checkCorrespondents() throws InterruptedException, IOException {
        test.clickOnCSS("a[href='correspondents']");
        test.verifyEquals ("span.field-name", "All Correspondents");
        test.clickOnCSS ("td > a");
        test.someMessagesShouldBeDisplayed();
        test.clickOn ("Go to Graph View");
        test.verifyEquals ("span.field-name", "Top correspondents graph");
        test.takeScreenshot("correspondents-graph");
    }

    private void checkAttachments() throws InterruptedException, IOException {
        test.clickOn ("Browse");
        test.clickOn ("Image attachments");
     //   test.clickOnCSS("a[href='image-attachments']");
        test.takeScreenshot("image-attachments");

        test.navigateBack();
        test.waitFor (2);
        test.clickOn("", "Document attachments");
//        test.clickOnCSS ("a[href='attachments?type=doc']");
        test.verifyContains ("span.field-value", "Document attachments");
        test.clickOnCSS ("td > a");
        test.waitFor (2);
        test.someMessagesShouldBeDisplayed();

        test.navigateBack();
        test.waitFor (2);
        test.clickOn("Other attachments");
   //     test.clickOnCSS ("a[href='attachments?type=nondoc']");
        test.verifyContains ("span.field-value", "Other attachments");
        test.clickOnCSS ("td > a");
        test.waitFor (2);
        test.someMessagesShouldBeDisplayed();
        test.navigateBack();
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
        options.addOption( "si", "set-images", false, "set archive images");

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

        basicChecks();
        checkCorrespondents();
        checkAttachments();

        if (cmd.hasOption("visit-all-pages")) {
            // visit all pages, take screenshot
            visitAllPages();
        }

        test.closeEpadd();
        test.closeBrowser();
    }

    public static void main (String args[]) throws InterruptedException, IOException, ParseException {
        new Tester().doIt(args);
    }
}
