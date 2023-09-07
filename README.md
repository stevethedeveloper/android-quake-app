android-quakes-app
==================

This is an app demonstrating many Android concepts.

Features:

-Compatible with all versions of Android back to version 2.2 for maximum audience 

-Compatible with any size screen, including all phones and tablets

-Utilizes a different layout for tablets to make use of entire screen

-Pulls data from USGS

-Displays data in a list

-Provides a filter so you can display only what quakes you want

-Displays a detail screen for each quake

-Maps all quakes currently displayed in the list

-Preferences allow you to set your default filter

-Refresh data at any time

-Links to the USGS website for even more details

<br /><br /><br /><br />




Technical:

-This project was created to demonstrate multiple techniques and features of Android and Java

-Support libraries - works on 2.2 and above

-Multiple device - master/detail for tablets, standard for phones

-Fragments - uses fragment design pattern as all apps should

-Abstract class - for code reuse when setting up Fragments in Activities

-AsyncTask - for fetching data on a separate thread than UI

-JSON - parses JSON returned from the USGS API

-Dialog Fragment - displays dialog and sets data, target for processing data

-Action bar - for various actions and settings

-Run once display - on initial load and updates

-Mapping - testing for Play Store access and displaying map with multiple markers

-Java patterns, including inner classes

-No fragments are defined in XML, for maximum flexibility
