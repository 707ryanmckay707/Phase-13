# Phase-13
This application was developed with Java, JavaFX and MySQL and it was originally created for the final project of my second year Java course. 

## What is Phase-13?
Phase-13, is a (paper based) faith building program created by my old youth pastor. 
It is a thirteen week long program, composed of thirteen "Phases", where each phase is a week long. 
Phase 1 is the first week, Phase 2 is the second week, etc.
Each phase containes a number of challenges to help the participants grow in their faith, improve their self discipline, health and more.

## What is this application?
This application is a companion app to the Phase-13 program.
- It uses a local database to keep track of the users progress towards completing each of the Phases.
- It also uses a calendar system to keep track of which of the thirteen weeks the user is on, and whether they are able to move onto the next phase.

The application starts by asking what date the user would like to start the Phase-13 program on.
This is the date that will be used to mark when each phase starts.
So the first phase will start on the date the user entered, and the next phase will start on the same day of the following week, etc.
Once the user completes all of the challenges for a week, they will be able to move on, as long as the start date for next phase has been reached.

## Updates since the original submission
- Improved the performance of starting/restarting the Phase-13 program (not the same as starting up the application), from about 5 seconds to about .4 seconds
- Fixed the textfields for the completed TextFieldsChallenges from being editable after the application is restarted.
- Improved the readability of some of the large functions, and removed some unneeded conditional logic.