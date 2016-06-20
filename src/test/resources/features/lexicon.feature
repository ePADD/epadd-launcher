Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		Given I open ePADD
		And I wait for 2 sec
		And I navigate to "http://localhost:9099/epadd/browse-top"

		#sensitive


	 ##############  search
		And I click on "Search"
		And I enter "florida" into input field with name "term"
		And I click on button "Search"
		Then I check for > 400 messages on the page
		Given I navigate back

		And I click on "Search"
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
	 # this takes a while, so give it some time
		And I wait for 30 sec
		Then I check for > 0 highlights on the page
		And I check that "Latin American" is highlighted

	 # edit address book
		And I find CSS element "#more-options" and click on it
		And I click on "Edit Correspondents"

		Then I verify that I am on page "http://localhost:9099/epadd/edit-correspondents"
		Given I add "Peter Chan" to the address book
		And I click on "Save"
		Then I verify that I am on page "http://localhost:9099/epadd/browse-top"
		Then I verify that CSS element "div.profile-text" contains "Peter Chan"
		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchiveLocation  |emailExportSplitLocation  |LexiconURL  |LexiconEditURL  |queryGeneratorPage  |editCorrespondentsPage |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchiveLocation"|"emailExportSplitLocation"|"LexiconURL"|"LexiconEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	
