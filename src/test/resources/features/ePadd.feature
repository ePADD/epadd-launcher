Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		
        Given I open ePADD
        And I navigate to "http://localhost:9099/epadd/browse-top"
		And I wait for 20 sec

	    # upload images
	    And I find CSS element "#more-options" and click on it
		And I wait for 1 sec
		And I click on "Set Images"

		And I enter <profilePhoto> into input field with name "profilePhoto"
		And I enter <bannerImage> into input field with name "bannerImage"
		And I enter <landingPhoto> into input field with name "landingPhoto"
		Then I take full page screenshot called "set-images"
		And I click on "Upload"

		Given I navigate to "http://localhost:9099/epadd/browse-top"
		Then I take full page screenshot called "browse-top"

		# now check the # of attachments
		Given I wait for 5 sec
		Then CSS element "#nImageAttachments" should have value 136
		And CSS element "#nDocAttachments" should have value 289
		And CSS element "#nOtherAttachments" should have value 70

		##### correspondents and entities

		# correspondents
		Given I click on "Browse"

		And I click on "Correspondents"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "All Correspondents"

		Given I find CSS element "td > a" and click on it
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Given I wait for 2 sec
		Then I click on "Go To Graph View"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Top correspondents graph"
		Then I take full page screenshot called "correspondents-graph"

		Then I click on "Go To Table View"
		And I wait for 2 sec

		# persons
		Given I click on "Browse"
		And I click on "Persons"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Person entities"

		Given I find CSS element "td > span" and click on it
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on "Go To Graph View"
		And I wait for 2 sec
		Then CSS element "span.field-name" should contain "Top entities graph"
		Then I take full page screenshot called "person-graph"

		Then I click on "Go To Table View"
		And I wait for 2 sec

		# orgs
		Given I click on "Browse"
		And I click on "Organizations"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Organisation entities"

		Given I find CSS element "td > span" and click on it
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on "Go To Graph View"
		And I wait for 2 sec
		Then CSS element "span.field-name" should contain "Top entities graph"
		Then I take full page screenshot called "org-graph"

		Then I click on "Go To Table View"
		And I wait for 2 sec

		# locs
		Given I click on "Browse"
		And I click on "Locations"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Location entities"

		Given I find CSS element "td > span" and click on it
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on "Go To Graph View"
		And I wait for 2 sec
		Then CSS element "span.field-name" should contain "Top entities graph"
		Then I take full page screenshot called "loc-graph"

		Then I click on "Go To Table View"
		And I wait for 2 sec

		##### attachments #######
		# images
		Given I click on "Browse"
		Then I click on "Image attachments"
		Then I take full page screenshot called "image-attachments"

		# docs
		Given I navigate back
		And I click on "Document attachments"
		Then CSS element "span.field-value" should contain "Document Attachments"
		Given I find CSS element "td > a" and click on it
		Then some messages should be displayed in another tab
		And I navigate back

		# non-docs
		Given I click on "Other attachments"
		Then CSS element "span.field-value" should contain "Other Attachments"
		Given I find CSS element "td > a" and click on it
		And some messages should be displayed in another tab
		Then I navigate back

	     ##### lexicon testing
		Given I click on "Lexicon search"
		And I verify that I am on page "http://localhost:9099/epadd/lexicon"
		Then CSS element "span.field-value" should contain "Lexicon Hits"

	 # check that the lexicon edit page is alive
		Given I click on "View/Edit Lexicon"
		Then I verify that I am on page "http://localhost:9099/epadd/edit-lexicon?lexicon=general"
		And I navigate back

	  # mark family messages do-not-transfer
		Given I click on "Family"
		And I switch to the "Family" tab
		Then I check for 320 messages on the page
		Given I mark all messages "Do not transfer"
		And I close tab

	 # verify the dnt got applied
		Given I click on "Export"
		And I click on "Do not transfer"
		Then CSS element "span.field-value" should start with a number > 0

		Given I click on "Browse"
		And I click on "Lexicon search"
		And I click on "Go To Graph View"
		Then I take full page screenshot called "lexicon-graph"
		And I navigate back

		# try out the sensitive lexicon
		Then I set dropdown "#lexiconName" to "Sensitive"
		And I click on "Job"
		And I switch to the "Job" tab
		Then I check for 400 messages on the page
		And I close tab

		# check sensitive messages
		Then I click on "Sensitive messages"
		And I switch to Job page and verify highlighted text having css "span.hilitedTerm.rounded" and email number "//div[@id='pageNumbering']"
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='browse?sensitive=true']"
		And some messages should be displayed in another tab
		Then I navigate back

		##############  search
		Given I click on "Search"
		And I enter "florida" into input field with name "term"
		And I click on button "Search"
		Then I check for > 400 messages on the page
		And I check that "Florida" is highlighted
		And I navigate back

		Given I click on "Search"
		And I enter "kidcare" into input field with name "term"
		And I click on button "Search"
		Then I check for > 20 messages on the page
		And I check that "Kidcare" is highlighted
		And I navigate back

	  # check query generator
		Given I click on "Search"
		And I click on "Query Generator"
		And I enter <searchParagraph> into input field with name "refText"
		And I click on button "Search"
		Then I verify that I am on page "http://localhost:9099/epadd/query-generator"
		And I wait for 30 sec
		Then I check for > 0 highlights on the page
		And I check that "Latin American" is highlighted

		# edit address book
		And I find CSS element "#more-options" and click on it
		And I wait for 1 sec
		And I click on "Edit Correspondents"

		Then I verify that I am on page "http://localhost:9099/epadd/edit-correspondents"
		Given I add "Peter Chan" to the address book
		And I click on "Save"
		Then I verify that I am on page "http://localhost:9099/epadd/browse-top"
		Then I verify that CSS element "div.profile-text" contains "Peter Chan"

		#export from appraisal
		Then I click on "Export"
		Then I click on button "Export"
		And I enter <emailExportLocation> into input field with name "dir"
		Then I click on button "Export"

		# delete the archive
		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
		Then I click on xpath element "//a[@id='settings']"
		Then I click on xpath element "//button[@id='delete-archive']"
		Then I handle the alert
		Then I verify the session folder under "epadd-appraisal/user" folder
		And I verify the "Browse" link existence 
		And I navigate to <epaddIndexURL>
		And I verify the name "name" field on email source page 
		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
		Then I click on xpath element "//a[@id='settings']"
		Then I select "PROCESSING" option by text from dropdown having id "mode-select"
		Then I click on xpath element "//a[@href='import']"
		And I enter <emailArchiveLocation> into input field with name "sourceDir"
		Then I click on element having id "gobutton"
		Then I wait for 5 sec
		Given I find CSS element "div.landing-img" and click on it
		Then I click on element having id "enter"
		Then I click on xpath element "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on xpath element "//div/a[@href='assign-authorities?type=correspondent']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on xpath element "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on xpath element "//div/a[@href='assign-authorities?type=person']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on xpath element "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on xpath element "//div/a[@href='assign-authorities?type=org']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on xpath element "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on xpath element "//div/a[@href='assign-authorities?type=places']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec 
		Then I click on xpath element "//a[contains(.,'Export')]"
		Then I click on xpath element "//button[contains(.,'Export')]"
		And I enter <emailExportSplitLocation> into input field with name "dir"
		Then I click on xpath element "//button[contains(.,'Export')]"
		Then copy files
		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
		Then I click on xpath element "//a[@id='settings']"
		Then I select "DISCOVERY" option by text from dropdown having id "mode-select"
		Then I click on xpath element "//div[@class='archive-card']/div"
		Then I click on element having id "enter"
		Then I click on xpath element "//div/a[@href='correspondents']"
		Then page title "All Correspondents" should be displayed having css "span.field-value"
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='entities?type=en_person']"
		Then page title "Person entities" should be displayed having css "span.field-name"
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='entities?type=en_org']"
		Then page title "Organisation entities" should be displayed having css "span.field-name"
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='entities?type=en_loc']"
		Then page title "Location entities" should be displayed having css "span.field-name"
		Then I click on "Browse"
		Then I close ePADD

		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchiveLocation  |emailExportSplitLocation  |LexiconURL  |LexiconEditURL  |queryGeneratorPage  |editCorrespondentsPage |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchiveLocation"|"emailExportSplitLocation"|"LexiconURL"|"LexiconEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	
