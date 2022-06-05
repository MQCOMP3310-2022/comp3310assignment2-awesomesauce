# COMP3310A2Y2022
Assignment 2 Starter code for COMP3310 - Wordle


# Changes made within codebase - 
* Code Changes
  * All JavaPMD errors from initial analysis have been corrected/fixed
  * The possible null conn in the SQLLiteConnectionManager has been safeguarded, as recommended on the LGTM 
  * Added a Logger for all console content within the application

* Additional Features
  * The resizing of the screen now allows the content to be transformed inside the game when player resizes. 
  * A scoreboard has been added onto the right side of the game that tracks a users "win streak" for the game.
  * After each round is concluded the scoreboard will display the correct answer for that round. 