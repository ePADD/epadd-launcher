Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		Given I open ePADD
		And I wait for 20 sec
		And I navigate to "http://localhost:9099/epadd/browse-top"

		##### lexicon testing
		Given I click on xpath element "//div/a[@href='lexicon']"
		Then CSS element "span.field-value" should contain "Lexicon Hits"

		Given I click on CSS element "#edit-lexicon"
		Then I verify that I am on page <LexiconEditURL>
		And I navigate back

		# mark family messages do-not-transfer
		Given I click on "Family"
		And I switch to the "Family" tab
		Then I check for 320 messages on the page
		Given I mark all messages "Do not transfer"
		And I close tab

		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchiveLocation  |emailExportSplitLocation  |LexiconURL  |LexiconEditURL  |queryGeneratorPage  |editCorrespondentsPage |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchiveLocation"|"emailExportSplitLocation"|"LexiconURL"|"LexiconEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	
