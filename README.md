# FamilyMapServerStudent
Instructions:
1.	Follow the instructions on github regarding how to clone this repository into a directory on your local machine.
2.	Open IntelliJ.
3.	Select File -> Open...
4.	Navigate to the newly cloned repository and select it.
5.	Allow IntelliJ a few minutes to download any dependencies and set up the project.
6.	Once it is done loading, try running the HelloWorld.java's main function. It is found in src/main/java/HelloWorld.java. If you get an error message like "release version 15 not supported," make sure you have version 15 of the Java JDK or of openjdk installed on your computer and that the Project SDK is set to that install of Java 15 (see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360010215699-Set-up-a-project-SDK- for how to set that). 
7.	Select Help -> Edit Custom VM Options and add the following line to the end of the file: `-Deditable.java.test.console=true` This is necessary to run the test driver properly.
8.	Select File -> Invalidate Caches / Restart
9.	Once IntelliJ finishes restarting, you should be good to go. Good luck!
