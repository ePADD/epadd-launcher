Feature: ePadd

	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		Given I navigate to <emailSourceURL>
		And I enter <achieverName> into input field having name "name"
		And I enter <primaryEmailAddress> into input field having name "alternateEmailAddrs"
		And I enter <emailFolderLocation> into input field having name "mboxDir1"            
		And I click on element having id "gobutton"
		Then I click on "Select All Folder" button having css "button.btn-default.select_all_button"
		And I click on element having id "gobutton"
		Then I wait for the page <browserTopPage> to be displayed within "360" seconds
		
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having link "Set Images"
		Then I upload the image having id "profilePhoto"
		Then I upload the image having id "landingPhoto"
		Then I upload the image having id "bannerImage"
		And I click on element having id "upload-btn"
		Then I click on element having css "button.btn.btn-cta"
		Then take full page screenshot of "Image Upload"
		
		Then I click on element having link "Browse"
		Then I click on element having xpath "//div/a[@href='correspondents']"
		Then I click on element having css "td > a"	
		And I switch to the new window and verify the url
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot of "correspondents graph"
		Then I navigate back
		Then page title "All Correspondents" should be displayed having css "span.field-value"
		Then I navigate back
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_person']"
		Then I click on element having css "td > span"	
		And I switch to the new window and verify the url
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot of "person graph"
		Then I navigate back
		Then page title "Person entities" should be displayed having css "span.field-name"
		Then I navigate back
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_org']"
		Then I click on element having css "td > span"
		And I switch to the new window and verify the url
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot of "organisation graph"
		Then I navigate back
		Then page title "Organisation entities" should be displayed having css "span.field-name"
		Then I navigate back
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_loc']"
		Then I click on element having css "td > span"
		And I switch to the new window and verify the url
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot of "location graph"
		Then I navigate back
		Then page title "Location entities" should be displayed having css "span.field-name"
		Then I navigate back
		
		Then value "image" should be displayed having xpath "//div/a[@href='image-attachments']"
		Then I click on element having xpath "//div/a[@href='image-attachments']"
		Then value "image" should be displayed having css "span.field-value"
		Then page title "unique attachments" should be displayed having css "span.field-value"
		Then I wait for 5 sec
		Then take full page screenshot of "images attachment"
		Then I navigate back
		
		Then value "document" should be displayed having xpath "//div/a[@href='attachments?type=doc']"
		Then I click on element having xpath "//div/a[@href='attachments?type=doc']"
		Then value "document" should be displayed having css "span.field-value"
		Then page title "Document Attachments" should be displayed having css "span.field-value"
		Then I click on element having css "td > a"
		And I switch to the new window and verify the url
		Then I navigate back
		
		Then value "other" should be displayed having xpath "//div/a[@href='attachments?type=nondoc']"
		Then I click on element having xpath "//div/a[@href='attachments?type=nondoc']"
		Then value "other" should be displayed having css "span.field-value"
		Then page title "Other Attachments" should be displayed having css "span.field-value"
		Then I click on element having css "td > a"
		And I switch to the new window and verify the url
		Then I navigate back
		
		Then I click on element having xpath "//div/a[@href='lexicon']"
		Then page title "Lexicon Hits" should be displayed having css "span.field-value"
		Then I click on element having xpath "//button[@id='edit-lexicon']"
		And I verify that <LexicanEditURL> is displayed
		Then I navigate back
		Then I click on element having xpath "//*[text() = 'Family']"
		And I switch to Family page and click on id "doNotTransfer" and id "applyToAll" and verify email number "//div[@id='pageNumbering']" 
		Then I click on element having link "Export"
		Then I click on element having link "Do not transfer" 
		And I verify the total number of emails not to be transfered having css "span.field-value" 
		Then I click on element having link "Browse"
		Then I click on element having xpath "//div/a[@href='lexicon']"
		Then I click on element having css "button.btn-default > i"
		Then take full page screenshot of "lexican graph"
		Then I navigate back
		And I verify that <LexicanURL> is displayed
		Then I select "Sensitive" option by text from dropdown having id "lexiconName"
		Then I click on element having xpath "//*[text() = 'Job']"
		And I switch to Job page and verify highlighted text having css "span.hilitedTerm.rounded" and email number "//div[@id='pageNumbering']"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='browse?sensitive=true']"
		Then page title "Date:" should be displayed having css "td.muted"
		Then I navigate back
		
		Then I click on element having link "Search"
		And I enter florida in textfield having xpath "//input[@name='term']"
		Then I click on element having xpath "//button[@onclick='handle_click()']" 
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "florida"
		Then I click on element having link "Search"
		And I enter kidcare in textfield having xpath "//input[@name='term']"
		Then I click on element having xpath "//input[@name='searchType']"
		Then I click on element having xpath "//button[@onclick='handle_click()']" 
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "kidcare"
		Then I click on element having link "Search"
		And I enter paragraph in textfield having xpath "//textarea[@id='refText']"
		Then I click on element having xpath "//button[@name='Go']"
		And I verify that <queryGeneratorPage> is displayed
		Then I verify the number of highlighted texts having css "span.muse-highlight"
		Then I verify the number of underlined texts having css "span.muse-NER-name"
		Then I click on element having link "Search"
		And I enter Peter Chan in textfield having xpath "//input[@name='term']"
		Then I click on element having xpath "//button[@onclick='handle_click()']" 
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "Peter Chan"
		Then I click on element having link "Search"
		And I enter budget in textfield having xpath "//input[@name='term']"
		Then I click on element having xpath "//button[@onclick='handle_click()']" 
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budget"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		Then I click on element having link "Search"
		And I enter budget in textfield having xpath "//input[@name='term']"
		Then I click on element having xpath "//input[@name='searchType']"
		Then I click on element having xpath "//button[@onclick='handle_click()']" 
		Then I verify the number having xpath "//div[@id='pageNumbering']" searched with "budgetWithSubject"
		And I verify the highlighted "Budget" text having css "span.hilitedTerm.rounded"
		
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@href='edit-correspondents']"
		And I verify that <editCorrespondentsPage> is displayed
		Then I edit the address book having id "text" 
		Then I click on element having xpath "//button[@type='submit']" 
		And I verify that <browserTopPage> is displayed
		Then I verify the updated profile text having css "div.profile-text"
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@href='edit-correspondents']"
		Then I revert back the correspondent values in id "text"
		Then I click on element having xpath "//button[@type='submit']"
		Then I verify the profile text having css "div.profile-text" has reverted
		
		Then I click on element having link "Export"
		Then I click on element having xpath "//button[contains(.,'Export')]"
		And I enter <emailExportLocation> into input field having name "dir"
		Then I click on element having xpath "//button[contains(.,'Export')]"
		
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@id='settings']"
		Then I click on element having xpath "//button[@id='delete-archive']"
		Then I handle the alert
		Then I verify the session folder under "epadd-appraisal/user" folder
		And I verify the "Browse" link existence 
		And I navigate to <epaddIndexURL>
		And I verify the name "name" field on email source page 
				
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@id='settings']"
		Then I select "PROCESSING" option by text from dropdown having id "mode-select"
		Then I click on element having xpath "//a[@href='import']"
		And I enter <emailArchieveLocation> into input field having name "sourceDir"
		Then I click on element having id "gobutton"
		Then I click on element having css "div.landing-img"
		Then I click on element having id "enter"
		Then I click on element having xpath "//a[contains(.,'Authorities')]"
		
		Then I click on element having xpath "//div/a[@href='assign-authorities?type=correspondent']"
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='assign-authorities?type=person']"
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='assign-authorities?type=org']"
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='assign-authorities?type=places']"
		Then page title "Assign authority records" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//a[contains(.,'Export')]"
		Then I click on element having xpath "//button[contains(.,'Export')]"
		And I enter <emailExportSplitLocation> into input field having name "dir"
		Then I click on element having xpath "//button[contains(.,'Export')]"
		Then copy files
		
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@id='settings']"
		Then I select "DISCOVERY" option by text from dropdown having id "mode-select"
		Then I click on element having xpath "//div[@class='archive-card']/div"
		Then I click on element having id "enter"
		
		Then I click on element having xpath "//div/a[@href='correspondents']"
		Then page title "All Correspondents" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_person']"
		Then page title "Person entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_org']"
		Then page title "Organisation entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_loc']"
		Then page title "Location entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		Then close ePADD
		Then open ePADD
		
		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchieveLocation  |emailExportSplitLocation  |LexicanURL  |LexicanEditURL  |queryGeneratorPage  |editCorrespondentsPage  |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchieveLocation"|"emailExportSplitLocation"|"LexicanURL"|"LexicanEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	@ePADD	
	Scenario Outline: ePadd (Delivery Module)
		Given I navigate to <epaddIndexURL>
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@id='settings']"
		Then I select "DELIVERY" option by text from dropdown having id "mode-select"
		Then I click on element having xpath "//div[@class='archive-card']/div"
		Then I click on element having id "enter"
		
		Then I click on element having xpath "//div/a[@href='correspondents']"
		Then page title "All Correspondents" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_person']"
		Then page title "Person entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_org']"
		Then page title "Organisation entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='entities?type=en_loc']"
		Then page title "Location entities" should be displayed having css "span.field-name"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='image-attachments']"
		Then page title "unique attachments" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='attachments?type=doc']"
		Then page title "Document Attachments" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='attachments?type=nondoc']"
		Then page title "Other Attachments" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then I click on element having xpath "//div/a[@href='lexicon']"
		Then page title "Lexicon Hits" should be displayed having css "span.field-value"
		Then I click on element having link "Browse"
		
		Then close ePADD
		Then open ePADD
		
		Examples:
		|epaddIndexURL  |
		|"epaddIndexURL"|
		
		
	@ePADD
	Scenario Outline: ePadd (Appraisal)
		Given I navigate to <emailSourceURL>
		And I enter <epaddAchieverName> into input field having name "name"
		And I enter <epaddPrimaryEmailAddress> into input field having name "alternateEmailAddrs"
		And I enter <epaddEmailAddress> into input field having name "loginName0"
		And I enter <epaddPassword> into input field having name "password0"
		And I click on element having id "gobutton"
		Then I click on "Select All Folder" button having css "button.btn-default.select_all_button"
		And I click on element having id "go-button"
		Then I wait for the page <browserTopPage> to be displayed within "240" seconds
		
		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
		Then I click on element having xpath "//a[@id='settings']"
		Then I click on element having xpath "//button[@id='delete-archive']"
		Then I handle the alert
		Then I verify the session folder under "epadd-appraisal/user" folder
		And I verify the "Browse" link existence 
		And I navigate to <epaddIndexURL>
		And I verify the name "name" field on email source page 
		
		And I enter <epaddAchieverName> into input field having name "name"
		And I enter <epaddPrimaryEmailAddress> into input field having name "alternateEmailAddrs"
		And I enter <epaddEmailAddress> into input field having name "loginName0"
		And I enter <epaddPassword> into input field having name "password0"
		And I click on element having id "gobutton"
		Then I click on "Select All Folder" button having css "button.btn-default.select_all_button"
		And I provide from date in textfield having id "from"
		And I provide to date in textfield having id "to"
		And I click on element having id "go-button"
		Then I wait for the page <browserTopPage> to be displayed within "240" seconds
		
		Then close ePADD
		
		Examples:
		|emailSourceURL  |epaddAchieverName  |epaddPrimaryEmailAddress  |epaddEmailAddress  |epaddPassword  |browserTopPage  |
		|"emailSourceURL"|"epaddAchieverName"|"epaddPrimaryEmailAddress"|"epaddEmailAddress"|"epaddPassword"|"browserTopPage"|
		
		