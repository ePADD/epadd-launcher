Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		
        Given I open ePADD
        And I navigate to "http://localhost:9099/epadd/browse-top"
		And I wait for 20 sec

	    # upload images
		And I click on CSS element "#more-options"
		And I wait for 1 sec
		And I click on "Set Images"

		And I enter <profilePhoto> into input field with name "profilePhoto"
		And I enter <bannerImage> into input field with name "bannerImage"
		And I enter <landingPhoto> into input field with name "landingPhoto"
		Then I take full page screenshot called "set-images"
		And I click on CSS element "#upload-btn"

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

		And I click on link containing "Correspondents"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "All Correspondents"

		Given I click on CSS element "td > a"
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
		And I click on link containing "Persons"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Person entities"

		Given I click on element having css "td > span"
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
		And I click on link containing "Organizations"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Organisation entities"

		Given I click on element having css "td > span"
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
		And I click on link containing "Locations"
		And I wait for 2 sec
		Then CSS element "span.field-name" should have value "Location entities"

		Given I click on element having css "td > span"
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
		Given I click on element having css "td > a"
		Then some messages should be displayed in another tab
		And I navigate back

		# non-docs
		Given I click on "Other attachments"
		Then CSS element "span.field-value" should contain "Other Attachments"
		Then I click on element having css "td > a"
		And some messages should be displayed in another tab
		Then I navigate back

		##### lexicon testing
		Given I click on "Lexicon search"
		Then CSS element "span.field-value" should contain "Lexicon Hits"

		# check that the lexicon edit page is alive
		Given I click on CSS element "#edit-lexicon"
		Then I verify that I am on page <LexiconEditURL>
		And I navigate back

	 	# mark family messages do-not-transfer
		Given I click on "Family"
		And I switch to the "Family" tab
		Then I check for 320 messages on the page
		Given I mark all messages "Do not transfer"
		And I close tab

		# export from appraisal
		Then I click on "Export"
		Then I click on "Do not transfer"
		And I verify the total number of emails not to be transfered having css "span.field-value" 
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='lexicon']"
		Then I click on element having css "button.btn-default > i"
		Then I take full page screenshot called "lexicon graph"
		Then I navigate back
		And I verify that I am on page <LexiconURL>
		Then I select "Sensitive" option by text from dropdown having id "lexiconName"
		Then I click on xpath element "//*[text() = 'Job']"
		And I switch to Job page and verify highlighted text having css "span.hilitedTerm.rounded" and email number "//div[@id='pageNumbering']"
		Then I click on "Browse"
		Then I click on xpath element "//div/a[@href='browse?sensitive=true']"

		Then page title "Date:" should be displayed having css "td.muted"
		Then I navigate back
		Then I click on "Search"
		And I enter search text "floridaText" in textfield having xpath "//input[@name='term']"
		Then I click on xpath element "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "florida"
		Then I click on "Search"
		And I enter search text "kidcareText" in textfield having xpath "//input[@name='term']"
		Then I click on xpath element "//input[@name='searchType']"
		Then I click on xpath element "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "kidcare"
		Then I click on "Search"
		And I enter search text "paragraph" in textfield having xpath "//textarea[@id='refText']"
		Then I click on xpath element "//button[@name='Go']"
		And I verify that I am on page <queryGeneratorPage>
		Then I verify the number of highlighted texts having css "span.muse-highlight"
		Then I verify the number of underlined texts having css "span.muse-NER-name"
		Then I click on "Search"
		And I enter search text "newUserName" in textfield having xpath "//input[@name='term']"
		Then I click on xpath element "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "Peter Chan"
		Then I click on "Search"
		And I enter search text "budgetText" in textfield having xpath "//input[@name='term']"
		Then I click on xpath element "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budget"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		Then I click on "Search"
		And I enter search text "budgetText" in textfield having xpath "//input[@name='term']"
		Then I click on xpath element "//input[@name='searchType']"
		Then I click on xpath element "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budgetWithSubject"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
		Then I click on xpath element "//a[@href='edit-correspondents']"
		And I verify that I am on page <editCorrespondentsPage>
		Then I edit the address book having id "text" 
		Then I click on xpath element "//button[@type='submit']"
		And I verify that I am on <browserTopPage>
		Then I verify the updated profile text having css "div.profile-text"
		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
		Then I click on xpath element "//a[@href='edit-correspondents']"
		Then I revert back the correspondent values in id "text"
		Then I click on xpath element "//button[@type='submit']"
		Then I verify the profile text having css "div.profile-text" has reverted
		Then I click on "Export"
		Then I click on xpath element "//button[contains(.,'Export')]"
		And I enter <emailExportLocation> into input field with name "dir"
		Then I click on xpath element "//button[contains(.,'Export')]"
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
		Then I click on element having css "div.landing-img"
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
		
		
	
