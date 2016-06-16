Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		Given I open ePADD
		And I wait for 2 sec
		And I navigate to "http://localhost:9099/epadd/browse-top"

		#sensitive

	##############  search
	 # edit address book
		And I find CSS element "#more-options" and click on it
		And I wait for 1 sec
		And I click on "Edit Correspondents"

		Then I verify that I am on page "http://localhost:9099/epadd/edit-correspondents"
		Given I add "Peter Chan111" to the address book
		And I click on "Save"
		Then I verify that I am on page "http://localhost:9099/epadd/browse-top"
		Then CSS element "div.profile-text" should contain "Peter Chan111"

		Examples:
		|emailSourceURL  |achieverName  |primaryEmailAddress  |emailFolderLocation  |emailExportLocation  |emailArchiveLocation  |emailExportSplitLocation  |LexiconURL  |LexiconEditURL  |queryGeneratorPage  |editCorrespondentsPage |browserTopPage  |epaddIndexURL  |
		|"emailSourceURL"|"achieverName"|"primaryEmailAddress"|"emailFolderLocation"|"emailExportLocation"|"emailArchiveLocation"|"emailExportSplitLocation"|"LexiconURL"|"LexiconEditURL"|"queryGeneratorPage"|"editCorrespondentsPage"|"browserTopPage"|"epaddIndexURL"|
		
		
	
