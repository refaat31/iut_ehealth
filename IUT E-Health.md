# IUT E-Health



IUT E-Health System is a healthcare management system made with the help of JAVA FX

  

# Setting up your environment

  - Download Java, JavaFX, SceneBuilder, IntelliJ, Jfoenix
  - for explicit steps, watch the required video in the [playlist](https://www.youtube.com/playlist?list=PLEs6yXAN0N5cqa4Yw0QOU8-ByQ13EG3jt)
 
# Warning
>DO NOT PUSH TO MASTER BRANCH IF THERE'S A CONFLICT. 
>RESOLVE THEM LOCALLY FIRST.


Create your own branch, work on that *locally* & push it back to your *remote* branch.
>just install Github Desktop, it'll make your life easier. Trust me.
# Getting your project up & running for development

> In IntelliJ, you need to configure the following stuff:
> 
- Go to Project Structure
 - Go to Modules

    Add --> JAR/directories ---> select mysql-connector .jar file
- Go to Lib

    Add --> Java --> select javafx lib file path
Add --> Java --> jfoenix .jar file
- Run once, then go to Edit Configurations :


    --module-path "LIB PATH OF YOUR JAVAFX without quotes" --add-modules javafx.controls,javafx.fxml --add-opens java.base/java.lang.reflect=ALL-UNNAMED

* Also setup Jfoenix in SceneBuilder for use



# Versions of softwares/libraries used :
- Java SDK Version 14.0.1
- jfoenix 9.0.10
- JavaFX SDK Version 14.0.1
- SceneBuilder 11.0.0





