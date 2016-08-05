Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		# expects epadd to be already running
		Given I navigate to "http://localhost:9099/epadd/browse-top"

	 ##############  search
#		And I navigate to "http://localhost:9099/epadd/search-query"
#		And I enter "florida" into input field with name "term"
#		And I click on button "Search"
#		And I wait for 10 sec
#		Then I check for > 400 messages on the page
#		Then I navigate back
#	 # we can't check that Florida is highlighted because the first hit is inside an attachment!
#	 # And I check that "Florida" is highlighted
#
#		And I navigate to "http://localhost:9099/epadd/search-query"
#		And I enter "kidcare" into input field with name "term"
#		And I click on button "Search"
#		Then I check for > 20 messages on the page
#		And I wait for 10 sec
#		And I check that "Kidcare" is highlighted
#		Then I navigate back

	  # check query generator
		And I navigate to "http://localhost:9099/epadd/search-query"
		And I click on "Query Generator"
		And I enter <searchParagraph> into input field with name "refText"
#		And I click on button "Search"
                Given I find CSS element "div > button[name="Go"]" and click on it
		Then I verify that I am on page "http://localhost:9099/epadd/query-generator"
	 # this takes a while, so give it some time
		And I wait for 30 sec
##		Then I check for > 0 highlights on the page
##		And I check that "Latin American" is highlighted
		Examples:
		|emailSourceURL  |
		|"emailSourceURL"|
	
