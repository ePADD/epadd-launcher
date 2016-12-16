package edu.stanford.epadd.test;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Alert;

import java.io.*;

/**
 * Created by hangal on 11/29/16.
 */
public class Tester {

    private static Log log = LogFactory.getLog(Tester.class);
    public static String BASE_URL = "http://localhost:9099/epadd/";
    StepDefs browser;

    private static Options getOpt()
    {
        // create the Options
        // consider a local vs. global (hosted) switch. some settings will be disabled if its in global mode
        Options options = new Options();
        options.addOption( "ai", "import", false, "check appraisal import");
        options.addOption( "vap", "visit-all-pages", false, "visit all pages and check that they are alive (appraisal mode)");
        options.addOption( "si", "set-images", false, "set archive images");
        options.addOption( "t", "test", false, "self-test mode");

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
        options.addOption( "del", "delete-archive", false, "delete archive when done");

        //	options.addOption( "ns", "no-shutdown", false, "no auto shutdown");
        return options;
    }

    public void appraisalImport() throws InterruptedException, IOException {
        browser.updateTestStatus ("Checking import email functionality");

        browser.openURL(BASE_URL + "email-sources");
        browser.enterValueInInputField("name", "<name>");
        browser.enterValueInInputField("alternateEmailAddrs", "<emailAddress>");
        browser.enterValueInInputField("mboxDir2", browser.resolveValue("<data.dir>") + File.separator + "mbox1");
        browser.enterValueInInputField("emailSource2", "src1");
        browser.clickOn("", " Add another folder");
        browser.enterValueInInputField("mboxDir3", browser.resolveValue("<data.dir>") + File.separator + "mbox2");
        browser.enterValueInInputField("emailSource3", "src2");

        browser.clickOn("", "Continue");
        browser.waitForButton("Select all folders", 20 /* seconds */);
        browser.clickOnCSS("#selectall0");
        browser.clickOnCSS("#selectall1");
        browser.clickOn("", "Continue");
        browser.waitForPageToLoad("<browserTopPage>", 240);
        setImages();
    }

    public void setImages() throws InterruptedException, IOException {
        browser.updateTestStatus ("Checking profile images...");

        browser.openURL(BASE_URL + "browse-top");
        browser.clickOnCSS ("#more-options");
        browser.clickOn ("Set Images");
        browser.enterValueInInputField("profilePhoto", browser.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "profilePhoto.png");
        browser.enterValueInInputField("bannerImage", browser.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "bannerImage.png");
        browser.enterValueInInputField("landingPhoto", browser.resolveValue("<data.dir>") + File.separator + "Jeb Bush Images" + File.separator + "landingPhoto.png");
        browser.clickOn ("button", "Upload");
        browser.clickOn ("button", "Back");
        browser.takeScreenshot("profile-images");
    }


    public void basicChecks() throws IOException, InterruptedException {
        browser.updateTestStatus ("Checking different pages");

        browser.visitAndTakeScreenshot(BASE_URL + "debug");
        browser.visitAndTakeScreenshot(BASE_URL + "settings");
        browser.visitAndTakeScreenshot(BASE_URL + "about");
        browser.visitAndTakeScreenshot(BASE_URL + "report");
        browser.visitAndTakeScreenshot(BASE_URL + "error");
        checkNumberOfAttachments();
    }

    private void checkCorrespondents() throws InterruptedException, IOException {
        browser.updateTestStatus ("Checking correspondents");

        browser.clickOnCSS("a[href='correspondents']");
        browser.verifyEquals ("span.field-name", "All Correspondents");
        browser.clickOnCSS ("td > a");
        browser.someMessagesShouldBeDisplayed();
        browser.clickOn ("Go to Graph View");
        browser.verifyEquals ("span.field-name", "Top correspondents graph");
        browser.takeScreenshot("correspondents-graph");
    }

    private void checkNumberOfAttachments() {
        browser.openURL(BASE_URL + "browse-top");
        browser.verifyEquals ("#nImageAttachments", "136");
        browser.verifyEquals ("#nDocAttachments", "289");
        browser.verifyEquals ("#nOtherAttachments", "70");
    }

    private void checkAttachments() throws InterruptedException, IOException {
        browser.updateTestStatus ("Checking attachments");

        browser.clickOn ("Browse");
        browser.clickOn ("Image attachments");
     //   browser.clickOnCSS("a[href='image-attachments']");
        browser.takeScreenshot("image-attachments");

        browser.navigateBack();
        browser.waitFor (2);
        browser.clickOn("", "Document attachments");
//        browser.clickOnCSS ("a[href='attachments?type=doc']");
        browser.verifyContains ("span.field-value", "Document attachments");
        browser.clickOnCSS ("td > a");
        browser.waitFor (2);
        browser.someMessagesShouldBeDisplayed();

        browser.navigateBack();
        browser.waitFor (2);
        browser.clickOn("Other attachments");
   //     browser.clickOnCSS ("a[href='attachments?type=nondoc']");
        browser.verifyContains ("span.field-value", "Other attachments");
        browser.clickOnCSS ("td > a");
        browser.waitFor (2);
        browser.someMessagesShouldBeDisplayed();
        browser.navigateBack();
    }

    public void visitAllPages() throws InterruptedException, IOException {

        // all pages in web.xml can be included here!

        browser.visitAndTakeScreenshot(BASE_URL + "/set-images");
        // needs proper POST: browser.visitAndTakeScreenshot(BASE_URL + "/upload-images");
        browser.visitAndTakeScreenshot(BASE_URL + "correspondents");
        browser.visitAndTakeScreenshot(BASE_URL + "browse-top");
        browser.visitAndTakeScreenshot(BASE_URL + "search-query");
        browser.visitAndTakeScreenshot(BASE_URL + "advanced-search");
        browser.visitAndTakeScreenshot(BASE_URL + "entities?type=en_person");
        browser.visitAndTakeScreenshot(BASE_URL + "entities?type=en_loc");
        browser.visitAndTakeScreenshot(BASE_URL + "entities?type=en_org");
        browser.visitAndTakeScreenshot(BASE_URL + "by-folder");
        browser.visitAndTakeScreenshot(BASE_URL + "browse-finetypes");
        browser.visitAndTakeScreenshot(BASE_URL + "lexicon");
        browser.visitAndTakeScreenshot(BASE_URL + "edit-lexicon?lexicon=general");
        browser.visitAndTakeScreenshot(BASE_URL + "multi-search?term=award&term=prize&term=medal&term=fellowship&term=certificate&");
        browser.visitAndTakeScreenshot(BASE_URL + "image-attachments");
        browser.visitAndTakeScreenshot(BASE_URL + "attachments?type=doc");
        browser.visitAndTakeScreenshot(BASE_URL + "attachments?type=nondoc");
        browser.visitAndTakeScreenshot(BASE_URL + "graph?view=people");
        browser.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_person");
        browser.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_loc");
        browser.visitAndTakeScreenshot(BASE_URL + "graph?view=entities&type=en_org");

        browser.visitAndTakeScreenshot(BASE_URL + "edit-correspondents");
        browser.visitAndTakeScreenshot(BASE_URL + "query-generator?refText=John Ellis Jeb Bush Sr. (born February 11, 1953) is an American businessman and politician who served as the 43rd Governor of Florida from 1999 to 2007", 5);
        browser.visitAndTakeScreenshot(BASE_URL + "export");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review?type=transferWithRestrictions");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review?type=doNotTransfer");

        browser.visitAndTakeScreenshot(BASE_URL + "export-mbox");

        // processing mode:
        browser.visitAndTakeScreenshot(BASE_URL + "collections");
        browser.visitAndTakeScreenshot(BASE_URL + "import");
        browser.visitAndTakeScreenshot(BASE_URL + "collection-detail");
        browser.visitAndTakeScreenshot(BASE_URL + "edit-accession");
        browser.visitAndTakeScreenshot(BASE_URL + "assignauthorities-top");
        browser.visitAndTakeScreenshot(BASE_URL + "assignauthorities");
        browser.visitAndTakeScreenshot(BASE_URL + "export-processing");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review-processing");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review-processing?type=deliverWithRestrictions");
        browser.visitAndTakeScreenshot(BASE_URL + "export-review-processing?type=doNotDeliver");

        // delivery
        browser.visitAndTakeScreenshot(BASE_URL + "review-cart");
    }



    public void checkSensitiveMessages() throws InterruptedException {
        browser.updateTestStatus ("Checking sensitive message search");

        browser.openURL(BASE_URL + "browse-top");
        browser.clickOn("Sensitive messages");
        browser.checkMessagesOnBrowsePage(">", 7);
        browser.checkHighlights (">", 0);
        browser.navigateBack();
    }

    public void checkLexicons() throws InterruptedException, IOException {
        browser.updateTestStatus("Checking lexicon functionality");
        // graph
        browser.openURL(BASE_URL + "browse-top");

        // test if lexicon page opens
        browser.clickOn ("Lexicon Search");
        browser.waitFor (1);
        browser.verifyContains("span.field-value", "Lexicon Hits");
        browser.waitFor (1);

        // test lexicon graph view
        browser.clickOn ("Go To Graph View");
        browser.takeScreenshot("lexicon-graph");
        browser.navigateBack();
        browser.waitFor(1);

        // test edit-lexicon
        browser.clickOnCSS ("#edit-lexicon");
        browser.waitFor (1);
        browser.verifyURL("/epadd/edit-lexicon?lexicon=general");
        browser.waitFor(1);
        browser.navigateBack();

        // test create lexicon
        browser.clickOnCSS ("#create-lexicon");
        browser.enterPrompt("TestLexicon");
        browser.waitFor (1);
        browser.verifyURL("/epadd/edit-lexicon?lexicon=TestLexicon");
        browser.waitFor(1);
        browser.navigateBack();
    }

    /** currently checks do not transfer only */
    public void checkFlags() throws InterruptedException {
        browser.updateTestStatus ("Checking message flags");

        browser.openURL(BASE_URL + "browse-top");
        browser.clickOn ("Lexicon Search");
        browser.dropDownSelection ("#lexiconName", "Sensitive");
        browser.waitFor (2);
        browser.clickOn ("Health");
        browser.switchToTab ("health");
        browser.waitFor (5);
        browser.checkMessagesOnBrowsePage("", 393);
        browser.markDNT();
        browser.closeTab();
        browser.clickOn ("Export");
        browser.clickOn ("Do not transfer");
        browser.verifyStartsWithNumberGT0("span.field-value");
    }

    public void checkSearch() throws InterruptedException {
        browser.updateTestStatus ("Checking Search functionality");
        browser.clickOn("Search");
        browser.enterValueInInputField("term", "florida");
        browser.clickOn ("button", "search");
        browser.checkMessagesOnBrowsePage(">", 400);
        // we can't check that Florida is highlighted because the first hit is inside an attachment!
        browser.clickOn("Search");
        browser.enterValueInInputField("term", "kidcare");
        browser.clickOn ("button", "search");
        browser.checkMessagesOnBrowsePage(">", 20);
        browser.checkHighlighted ("Kidcare");
        browser.navigateBack();
    }

    public void checkQueryGenerator () throws InterruptedException {
        browser.updateTestStatus ("Checking Query Generator");

        browser.clickOn("Search");
        browser.clickOn("Query Generator");
        browser.enterValueInInputField("refText", "John Ellis Jeb Bush Sr. (born February 11, 1953) is an American businessman and politician who served as the 43rd Governor of Florida from 1999 to 2007\", 5);\n");
        browser.clickOn ("button", "search");
        browser.clickOnCSS ("div > button[name=\"Go\"]");
        browser.verifyURL (BASE_URL + "query-generator");
        browser.waitFor (10);
        browser.checkHighlights (">", 0);
        browser.checkHighlighted ("Florida");
    }

    public void checkEditAddressBook() throws InterruptedException {
        browser.updateTestStatus ("Checking editing of address book");

        browser.openURL(BASE_URL + "browse-top");
        browser.clickOnCSS("#more-options");
        browser.clickOn ("Edit Correspondents");
        browser.verifyURL (BASE_URL + "edit-correspondents");
        browser.editAddressBook ("Peter Chan");
        browser.clickOn ("Save");
        browser.verifyURL(BASE_URL + "browse-top");
        browser.verifyContains (".profile-text", "Peter Chan");
    }

    public void checkAppraisalExport() throws InterruptedException {
        browser.updateTestStatus ("Checking export from appraisal");

        String appraisalExportDir = StepDefs.BASE_DIR + File.separator + "appraisal-export";
        new File(appraisalExportDir).mkdirs();

        browser.clickOn("Export");
        browser.clickOn ("button", "Export");
        browser.enterValueInInputField("dir", StepDefs.BASE_DIR + File.separator + "appraisal-export");
        browser.clickOn ("button", "Export");
    }

    public void deleteArchive () throws InterruptedException {
        browser.updateTestStatus ("Checking archive deletion");

        browser.clickOnCSS("#more-options");
        browser.clickOn ("Settings");
        browser.clickOn ("Delete Archive");
        browser.confirmAlert();
        browser.openURL(BASE_URL + "browse-top");
        browser.verifyURL(BASE_URL + "email-sources"); // should redirect to email-sources
    }

    public void doAppraisal(String args[]) throws IOException, InterruptedException, ParseException {
        try {
            Options options = getOpt();
            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);
            browser = new StepDefs();
            browser.openBrowser();
           browser.openEpadd("appraisal");

            Thread.sleep(15000); // wait for it to startup and load the archive if needed
            if (cmd.hasOption("import")) {
                appraisalImport();
            }


          //  checkFlags();

//            checkLexicons();
            checkEditAddressBook();
            basicChecks();
            checkCorrespondents();
            checkAttachments();
            checkSensitiveMessages();
            checkSearch();
            checkQueryGenerator();

            checkAppraisalExport();

            if (cmd.hasOption("delete-archive"))
                deleteArchive();

            if (cmd.hasOption("visit-all-pages")) {
                // visit all pages, take screenshot
                visitAllPages();
            }

            browser.closeEpadd();
            browser.closeBrowser();
        } catch (Exception e) {
            log.warn (e);
            e.printStackTrace(System.out);
            if (browser != null) {
                browser.testStatus = "Test failed! " + e.getMessage();
                browser.testStatusColor = "red";
                browser.updateTestStatus();
//              browser.waitFor(5);
//              browser.closeBrowser();
            }
        }
    }

    public void doIt(String args[]) throws IOException, InterruptedException, ParseException {
        doAppraisal(args);
    }

    public static void main (String args[]) throws InterruptedException, IOException, ParseException {
        new Tester().doIt(args);
    }
}
