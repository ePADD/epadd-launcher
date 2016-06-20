Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		# expects epadd to be already running
	 # do the indexing
		Given I navigate to "http://localhost:9099/epadd/email-sources"
		And I enter "Jeb Bush into input field with name "name"
		And I enter jeb@jeb.org into input field with name "alternateEmailAddrs"
		And I enter <emailFolderLocation> into input field with name "mboxDir2"
		And I click on "Continue"
		Then I wait for button "Select all folders" to be displayed within 20 seconds
		Then I click on "Select all folders"
		Then I click on "Continue"

		Then I wait for the page <browserTopPage> to be displayed within 240 seconds

		And I navigate to "http://localhost:9099/epadd/browse-top"
		And I wait for 20 sec

		Then I verify the folder <appraisalSessionsDir> exists
		Examples:
		|emailSourceURL  |
		|"emailSourceURL"|
	
