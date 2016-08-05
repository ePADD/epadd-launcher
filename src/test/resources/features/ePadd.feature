Feature: ePadd
	@ePADD
	Scenario Outline: ePadd (Appraisal, Processing, Discovery Module)
		Given I navigate to "http://localhost:9099/epadd/browse-top"
                
#	   # do the indexing
#		Given I navigate to "http://localhost:9099/epadd/email-sources"
#		And I enter "Jeb Bush" into input field with name "name"
#		And I enter jeb@jeb.org into input field with name "alternateEmailAddrs"
#		And I enter <emailFolderLocation> into input field with name "mboxDir2"
#		And I click on "Continue"
#		Then I wait for button "Select all folders" to be displayed within 20 seconds
#		Then I click on "Select all folders"
#		Then I click on "Continue"

#		Then I wait for the page <browserTopPage> to be displayed within 240 seconds


# upload images
        And I find CSS element "#more-options" and click on it
	And I click on "Set Images"

	And I enter <profilePhoto> into input field with name "profilePhoto"
	And I enter <bannerImage> into input field with name "bannerImage"
	And I enter <landingPhoto> into input field with name "landingPhoto"
	Then I take full page screenshot called "set-images"
	And I click on button "Upload"

	Given I navigate to "http://localhost:9099/epadd/browse-top"
	Then I take full page screenshot called "browse-top"

	#now check the # of attachments
	Then CSS element "#nImageAttachments" should have value 136
	And CSS element "#nDocAttachments" should have value 289
        And I wait for 2 sec 
	And CSS element "#nOtherAttachments" should have value 70

 ##correspondents and entities

	#correspondents
	Given I click on "Browse"

       #And I click on "Correspondents"
        Given I find CSS element "a[href='correspondents']" and click on it
        Then CSS element "span.field-name" should have value "All Correspondents"

	Given I find CSS element "td > a" and click on it
	Then some messages should be displayed in another tab

	Then I click on "Go To Graph View"
	Then CSS element "span.field-name" should have value "Top correspondents graph"
	Then I take full page screenshot called "correspondents-graph"

	Then I click on "Go To Table View"
        #persons
	Given I click on "Browse"
       #And I click on "Persons"
               Given I find CSS element "a[href='entities?type=en_person']" and click on it
	       And I wait for 2 sec
#	       Then CSS element "span.field-name" should have value "Person entities"

#	       Given I find CSS element "td > span" and click on it
#	       And I wait for 2 sec
#	       Then some messages should be displayed in another tab

#	Then I click on "Go To Graph View"
#		And I wait for 2 sec
#		Then CSS element "span.field-name" should contain "Top entities graph"
#		Then I take full page screenshot called "person-graph"

#		Then I click on "Go To Table View"
#		And I wait for 2 sec
                Then I navigate back

#		# orgs
#		Given I click on "Browse"
#		And I click on "Organizations"
#                Given I find CSS element "a[href='entities?type=en_org']" and click on it
#		And I wait for 2 sec
#		Then CSS element "span.field-name" should have value "Organisation entities"
#
#		Given I find CSS element "td > span" and click on it
#		And I wait for 2 sec
#		Then some messages should be displayed in another tab
#
#		Then I click on "Go To Graph View"
#		And I wait for 2 sec
#		Then CSS element "span.field-name" should contain "Top entities graph"
#		Then I take full page screenshot called "org-graph"
#
#		Then I click on "Go To Table View"
#		And I wait for 2 sec
#
#		# locs
#		Given I click on "Browse"
#		And I click on "Locations"
#                Given I find CSS element "a[href='entities?type=en_loc']" and click on it
#		And I wait for 2 sec
#		Then CSS element "span.field-name" should have value "Location entities"
#
#		Given I find CSS element "td > span" and click on it
#		And I wait for 2 sec
#		Then some messages should be displayed in another tab
#
#		Then I click on "Go To Graph View"
#		And I wait for 2 sec
#		Then CSS element "span.field-name" should contain "Top entities graph"
#		Then I take full page screenshot called "loc-graph"
#
#		Then I click on "Go To Table View"
#		And I wait for 2 sec
#
		##### attachments #######
		# images
		Given I click on "Browse"
#		Then I click on "Image attachments"
                Given I find CSS element "a[href='image-attachments']" and click on it
		Then I take full page screenshot called "image-attachments"

#		# docs
		Given I navigate back
                And I wait for 2 sec
#		And I click on "Document attachments"
                Given I find CSS element "a[href='attachments?type=doc']" and click on it
		Then CSS element "span.field-value" should contain "Document Attachments"
		Given I find CSS element "td > a" and click on it
		Then some messages should be displayed in another tab
		And I navigate back
#
#		# non-docs
#		Given I click on "Other attachments"
                And I wait for 2 sec
                Given I find CSS element "a[href='attachments?type=nondoc']" and click on it
		Then CSS element "span.field-value" should contain "Other Attachments"
		Given I find CSS element "td > a" and click on it
                And I wait for 2 sec
		And some messages should be displayed in another tab
		Then I navigate back

	     ##### lexicon testing
#		Given I click on "Lexicon search"
                And I wait for 2 sec
                Given I find CSS element "a[href='lexicon']" and click on it
		And I verify that I am on page "http://localhost:9099/epadd/lexicon"
		Then CSS element "span.field-value" should contain "Lexicon Hits"

#	 # check that the lexicon edit page is alive
                And I wait for 2 sec
		Given I click on "View/Edit Lexicon"
		Then I verify that I am on page "http://localhost:9099/epadd/edit-lexicon?lexicon=general"
		And I navigate back

#	  # mark family messages do-not-transfer
                And I wait for 2 sec
		Given I click on "Family"
		And I switch to the "family" tab
		Then I check for 485 messages on the page
		Given I mark all messages "Do not transfer"
		And I close tab

#	 # verify the dnt got applied
                And I wait for 2 sec
		Given I click on "Export"
		And I click on "Do not transfer"
		Then CSS element "span.field-value" should start with a number > 0

		Given I click on "Browse"
#		And I click on "Lexicon search"
                Given I find CSS element "a[href='lexicon']" and click on it
		And I click on "Go To Graph View"
		Then I take full page screenshot called "lexicon-graph"
		And I navigate back
                And I wait for 2 sec

#		# try out the sensitive lexicon
##		Then I set dropdown "#lexiconName" to "Sensitive"
##		And I click on "Job"
##		And I switch to the "Job" tab
##		Then I check for 400 messages on the page
##		And I close tab

#		# check sensitive messages
		Given I navigate to "http://localhost:9099/epadd/browse-top"
#		And I click on "Sensitive messages"
                Given I find CSS element "a[href='browse?sensitive=true']" and click on it
#		# we should see 8 messages, with the first one having an SSN
		And I check for > 7 messages on the page
		And I check for > 0 highlights on the page
	 	Given I navigate back

#		##############  search
		And I click on "Search"
		And I enter "florida" into input field with name "term"
		And I click on button "Search"
		Then I check for > 400 messages on the page
		# we can't check that Florida is highlighted because the first hit is inside an attachment!
		# And I check that "Florida" is highlighted

		And I click on "Search"
		And I enter "kidcare" into input field with name "term"
		And I click on button "Search"
		Then I check for > 20 messages on the page
		And I check that "Kidcare" is highlighted
		And I navigate back

	  # check query generator
		Given I click on "Search"
                And I wait for 2 sec 
		And I navigate to "http://localhost:9099/epadd/search-query"
		And I click on "Query Generator"
		And I enter <searchParagraph> into input field with name "refText"
		And I wait for 5 sec
#		And I click on button "Search"
                Given I find CSS element "div > button[name="Go"]" and click on it
		Then I verify that I am on page "http://localhost:9099/epadd/query-generator"
		# this takes a while, so give it some time
		And I wait for 30 sec
##	        Then I check for > 0 highlights on the page
##		And I check that "Latin American" is highlighted

		# edit address book
	 # this step is not working properly on mac+chrome
#		And I find CSS element "#more-options" and click on it
#		And I click on "Edit Correspondents"
#		 Then I verify that I am on page "http://localhost:9099/epadd/edit-correspondents"
#		 Given I add "Peter Chan" to the address book
#		 And I click on "Save"
#		 Then I verify that I am on page "http://localhost:9099/epadd/browse-top"
#		 And I find CSS element "span.fieldValue" and verify that it contains "Peter Chan"

		# TODO: should also click on the other links on the export page (transfer-with-restrict, messages-to-transfer

		#export from appraisal
##		Then I click on "Export"
##		Then I click on button "Export"
##		And I enter <emailExportLocation> into input field with name "dir"
##		Then I click on button "Export"

		# delete the archive and confirm the dir got deleted
##		And I find CSS element "#more-options" and click on it
##		Then I click on "Settings"
##		Then I click on "Delete Archive"
##		Then I confirm the alert

##		Then I verify the folder <appraisalSessionsDir> does not exist

		# after deletion, going to indexpage should redirect us to email-sources
#		Given I navigate to <epaddIndexURL>
#		Then I verify that I am on page "http://localhost:9099/epadd/email-sources"

		####################  Verified upto here -- SGH #######################

		# switch to processing mode
		##      And I find CSS element "#more-options" and click on it
##		Then I click on "Settings"
##
##		Then I select "PROCESSING" option by text from dropdown having id "mode-select"
##		Then I click on "Import"
##
##		And I enter <emailArchiveLocation> into input field with name "sourceDir"
##		Then I click on element having id "gobutton"
##		Then I wait for 5 sec
##		Given I find CSS element "div.landing-img" and click on it
##		Then I click on element having id "enter"
##		Then I click on xpath element "//a[contains(.,'Authorities')]"
##		Then I wait for 5 sec
##		Then I click on xpath element "//div/a[@href='assign-authorities?type=correspondent']"
##		Then I wait for 5 sec
##		Then page title "Assign authority records" should be displayed having css "span.field-value"
##		Then I wait for 5 sec
##		Then I click on xpath element "//a[contains(.,'Authorities')]"
##		Then I wait for 5 sec
##		Then I click on xpath element "//div/a[@href='assign-authorities?type=person']"
##		Then I wait for 5 sec
##		Then page title "Assign authority records" should be displayed having css "span.field-value"
##		Then I wait for 5 sec
##		Then I click on xpath element "//a[contains(.,'Authorities')]"
##		Then I wait for 5 sec
##		Then I click on xpath element "//div/a[@href='assign-authorities?type=org']"
##		Then I wait for 5 sec
##		Then page title "Assign authority records" should be displayed having css "span.field-value"
##		Then I wait for 5 sec
##		Then I click on xpath element "//a[contains(.,'Authorities')]"
##		Then I wait for 5 sec
##		Then I click on xpath element "//div/a[@href='assign-authorities?type=places']"
##		Then I wait for 5 sec
##		Then page title "Assign authority records" should be displayed having css "span.field-value"
##		Then I wait for 5 sec 
##		Then I click on xpath element "//a[contains(.,'Export')]"
##		Then I click on xpath element "//button[contains(.,'Export')]"
##		And I enter <emailExportSplitLocation> into input field with name "dir"
##		Then I click on xpath element "//button[contains(.,'Export')]"
##		Then copy files
##		Then I click on xpath element "//img[@src='images/header-menuicon.svg']"
##		Then I click on xpath element "//a[@id='settings']"
##
##		Then I select "DISCOVERY" option by text from dropdown having id "mode-select"
##		Then I click on xpath element "//div[@class='archive-card']/div"
##		Then I click on element having id "enter"
##		Then I click on xpath element "//div/a[@href='correspondents']"
##		Then page title "All Correspondents" should be displayed having css "span.field-value"
##		Then I click on "Browse"
##		Then I click on xpath element "//div/a[@href='entities?type=en_person']"
##		Then page title "Person entities" should be displayed having css "span.field-name"
##		Then I click on "Browse"
##		Then I click on xpath element "//div/a[@href='entities?type=en_org']"
##		Then page title "Organisation entities" should be displayed having css "span.field-name"
##		Then I click on "Browse"
##		Then I click on xpath element "//div/a[@href='entities?type=en_loc']"
##		Then page title "Location entities" should be displayed having css "span.field-name"
##		Then I click on "Browse"
##		Then I close ePADD

		Examples:
		|emailSourceURL  |
		|"emailSourceURL"|
##
##	@ePADD
##	Scenario Outline: ePadd (Delivery Module)
##		Given I navigate to <epaddIndexURL>
##		Then I click on element having xpath "//img[@src='images/header-menuicon.svg']"
##		Then I click on element having xpath "//a[@id='settings']"
##		Then I select "DELIVERY" option by text from dropdown having id "mode-select"
##		Then I click on element having xpath "//div[@class='archive-card']/div"
##		Then I click on element having id "enter"
##
##		Then I click on element having xpath "//div/a[@href='correspondents']"
##		Then page title "All Correspondents" should be displayed having css "span.field-value"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='entities?type=en_person']"
##		Then page title "Person entities" should be displayed having css "span.field-name"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='entities?type=en_org']"
##		Then page title "Organisation entities" should be displayed having css "span.field-name"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='entities?type=en_loc']"
##		Then page title "Location entities" should be displayed having css "span.field-name"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='image-attachments']"
##		Then page title "unique attachments" should be displayed having css "span.field-value"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='attachments?type=doc']"
##		Then page title "Document Attachments" should be displayed having css "span.field-value"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='attachments?type=nondoc']"
##		Then page title "Other Attachments" should be displayed having css "span.field-value"
##		Then I click on element having link "Browse"
##
##		Then I click on element having xpath "//div/a[@href='lexicon']"
##		Then page title "Lexicon Hits" should be displayed having css "span.field-value"
##		Then I click on element having link "Browse"
##
##		Then close ePADD
##		Then open ePADD
##
##		Examples:
##			|epaddIndexURL  |
##			|"epaddIndexURL"|
##
