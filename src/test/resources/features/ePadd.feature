Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		
        Given I open ePADD
        And I navigate to "http://localhost:9099/epadd/browse-top"
		And I wait for 10 sec

	    # upload images
		And I click on element having id "more-options"
		And I wait for 1 sec
		And I click on element having link "Set Images"

		And I enter <profilePhoto> into input field with name "profilePhoto"
		And I enter <bannerImage> into input field with name "bannerImage"
		And I enter <landingPhoto> into input field with name "landingPhoto"
		Then take full page screenshot called "set-images"
		And I click on element having id "upload-btn"

		Given I navigate to "http://localhost:9099/epadd/browse-top"
		Then take full page screenshot called "browse-top"

		# now check the # of attachments
		Given I wait for 5 sec
		Then element with CSS selector "#nImageAttachments" should have value <expectedImageAttachments>
		And element with CSS selector "#nDocAttachments" should have value <expectedDocAttachments>
		And element with CSS selector "#nOtherAttachments" should have value <expectedOtherAttachments>

		##### correspondents and entities

		# correspondents
		Given I click on element having link "Browse"

		And I click on element with xpath "//div/a[@href='correspondents']"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should have value "All Correspondents"

		Given I click on element having css "td > a"
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Given I wait for 2 sec
		And I click on element having css "button.btn-default > i"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should have value "Top correspondents graph"
		Then take full page screenshot called "correspondents-graph"
		Given I navigate back
		And I wait for 2 sec
		Then I navigate back
		And I wait for 2 sec

		# persons
		Given I click on element with xpath "//div/a[@href='entities?type=en_person']"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should have value "Person entities"

		Given I click on element having css "td > span"
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on element having css "button.btn-default > i"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should contain "Top entities graph"
		Then take full page screenshot called "person-graph"

		Given I navigate back
		And I wait for 2 sec
		Then I navigate back
		And I wait for 2 sec

		# orgs
		Given I click on element with xpath "//div/a[@href='entities?type=en_org']"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should have value "Organisation entities"

		Given I click on element having css "td > span"
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on element having css "button.btn-default > i"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should contain "Top entities graph"
		Then take full page screenshot called "org-graph"

		Given I navigate back
		And I wait for 2 sec
		Then I navigate back
		And I wait for 2 sec

		# locs
		Given I click on element with xpath "//div/a[@href='entities?type=en_loc']"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should have value "Location entities"

		Given I click on element having css "td > span"
		And I wait for 2 sec
		Then some messages should be displayed in another tab

		Then I click on element having css "button.btn-default > i"
		And I wait for 2 sec
		Then element with CSS selector "span.field-name" should contain "Top entities graph"
		Then take full page screenshot called "loc-graph"

		Given I navigate back
		And I wait for 2 sec
		Then I navigate back
		And I wait for 2 sec

		##### attachments #######
		# images
		Then I click on element with xpath "//div/a[@href='image-attachments']"
		Then take full page screenshot called "image-attachment"
		Then I navigate back

		# docs
		Then I click on element with xpath "//div/a[@href='attachments?type=doc']"
		Then element with CSS selector "span.field-value" should contain "Document Attachments"
		Then I click on element having css "td > a"
		And some messages should be displayed in another tab
		Then I navigate back

		# non-docs
		Then I click on element with xpath "//div/a[@href='attachments?type=nondoc']"
		Then element with CSS selector "span.field-value" should contain "Other Attachments"
		Then I click on element having css "td > a"
		And some messages should be displayed in another tab
		Then I navigate back

		##### lexicon testing
		Given I click on element with xpath "//div/a[@href='lexicon']"
		Then element with CSS selector "span.field-value" should contain "Lexicon Hits"

		Given I click on element with id "edit-lexicon"
		Then I verify that I am on page <LexiconEditURL>
		Given I navigate back
		Then I click on element with xpath "//*[text() = 'Family']"
		And I switch to Family page and click on id "doNotTransfer" and id "applyToAll" and verify email number "//div[@id='pageNumbering']" 
		Then I click on element having link "Export"
		Then I click on element having link "Do not transfer" 
		And I verify the total number of emails not to be transfered having css "span.field-value" 
		Then I click on element having link "Browse"
		Then I click on element with xpath "//div/a[@href='lexicon']"
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot called "lexicon graph"
		Then I navigate back
		And I verify that I am on page <LexiconURL>
		Then I select "Sensitive" option by text from dropdown having id "lexiconName"
		Then I click on element with xpath "//*[text() = 'Job']"
		And I switch to Job page and verify highlighted text having css "span.hilitedTerm.rounded" and email number "//div[@id='pageNumbering']"
		Then I click on element having link "Browse"
		Then I click on element with xpath "//div/a[@href='browse?sensitive=true']"

		Then page title "Date:" should be displayed having css "td.muted"
		Then I navigate back
		Then I click on element having link "Search"
		And I enter search text "floridaText" in textfield having xpath "//input[@name='term']"
		Then I click on element with xpath "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "florida"
		Then I click on element having link "Search"
		And I enter search text "kidcareText" in textfield having xpath "//input[@name='term']"
		Then I click on element with xpath "//input[@name='searchType']"
		Then I click on element with xpath "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "kidcare"
		Then I click on element having link "Search"	
		And I enter search text "paragraph" in textfield having xpath "//textarea[@id='refText']"
		Then I click on element with xpath "//button[@name='Go']"
		And I verify that I am on page <queryGeneratorPage>
		Then I verify the number of highlighted texts having css "span.muse-highlight"
		Then I verify the number of underlined texts having css "span.muse-NER-name"
		Then I click on element having link "Search"
		And I enter search text "newUserName" in textfield having xpath "//input[@name='term']"
		Then I click on element with xpath "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "Peter Chan"
		Then I click on element having link "Search"	
		And I enter search text "budgetText" in textfield having xpath "//input[@name='term']"
		Then I click on element with xpath "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budget"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		Then I click on element having link "Search"
		And I enter search text "budgetText" in textfield having xpath "//input[@name='term']"
		Then I click on element with xpath "//input[@name='searchType']"
		Then I click on element with xpath "//button[@onclick='handle_click()']"
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budgetWithSubject"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		Then I click on element with xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element with xpath "//a[@href='edit-correspondents']"
		And I verify that I am on page <editCorrespondentsPage>
		Then I edit the address book having id "text" 
		Then I click on element with xpath "//button[@type='submit']"
		And I verify that I am on <browserTopPage>
		Then I verify the updated profile text having css "div.profile-text"
		Then I click on element with xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element with xpath "//a[@href='edit-correspondents']"
		Then I revert back the correspondent values in id "text"
		Then I click on element with xpath "//button[@type='submit']"
		Then I verify the profile text having css "div.profile-text" has reverted
		Then I click on element having link "Export"
		Then I click on element with xpath "//button[contains(.,'Export')]"
		And I enter <emailExportLocation> into input field with name "dir"
		Then I click on element with xpath "//button[contains(.,'Export')]"
		Then I click on element with xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element with xpath "//a[@id='settings']"
		Then I click on element with xpath "//button[@id='delete-archive']"
		Then I handle the alert
		Then I verify the session folder under "epadd-appraisal/user" folder
		And I verify the "Browse" link existence 
		And I navigate to <epaddIndexURL>
		And I verify the name "name" field on email source page 
		Then I click on element with xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element with xpath "//a[@id='settings']"
		Then I select "PROCESSING" option by text from dropdown having id "mode-select"
		Then I click on element with xpath "//a[@href='import']"
		And I enter <emailArchiveLocation> into input field with name "sourceDir"
		Then I click on element having id "gobutton"
		Then I wait for 5 sec
		Then I click on element having css "div.landing-img"
		Then I click on element having id "enter"
		Then I click on element with xpath "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on element with xpath "//div/a[@href='assign-authorities?type=correspondent']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on element with xpath "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on element with xpath "//div/a[@href='assign-authorities?type=person']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on element with xpath "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on element with xpath "//div/a[@href='assign-authorities?type=org']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then I click on element with xpath "//a[contains(.,'Authorities')]"
		Then I wait for 5 sec
		Then I click on element with xpath "//div/a[@href='assign-authorities?type=places']"
		Then I wait for 5 sec
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I wait for 5 sec 
		Then I click on element with xpath "//a[contains(.,'Export')]"
		Then I click on element with xpath "//button[contains(.,'Export')]"
		And I enter <emailExportSplitLocation> into input field with name "dir"
		Then I click on element with xpath "//button[contains(.,'Export')]"
		Then copy files
		Then I click on element with xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element with xpath "//a[@id='settings']"
		Then I select "DISCOVERY" option by text from dropdown having id "mode-select"
		Then I click on element with xpath "//div[@class='archive-card']/div"
		Then I click on element having id "enter"
		Then I click on element with xpath "//div/a[@href='correspondents']"
		Then page title "All Correspondents" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		Then I click on element with xpath "//div/a[@href='entities?type=en_person']"
		Then page title "Person entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		Then I click on element with xpath "//div/a[@href='entities?type=en_org']"
		Then page title "Organisation entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		Then I click on element with xpath "//div/a[@href='entities?type=en_loc']"
		Then page title "Location entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		Then I close ePADD

		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchiveLocation  |emailExportSplitLocation  |LexiconURL  |LexiconEditURL  |queryGeneratorPage  |editCorrespondentsPage |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchiveLocation"|"emailExportSplitLocation"|"LexiconURL"|"LexiconEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	
