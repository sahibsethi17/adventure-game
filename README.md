# adventure-game
Adventure Game made with GUIs amongst 3 other members for a group project. Link to how project works below:
https://drive.google.com/file/d/1O3Vl6aWc7E-ZvlGO09kRN07ey63a417F/view?usp=sharing

When loading the project, the file to run must be the AdventureGameApp file. This file runs the main stage for our GUI components. Note JavaFX was used as an external library for this project. All .jar files in "javafx-sdk-21/lib/" must be in the external library. For the program to compile & run, JavaFX components must be added on the computer that the project is being run on. Also, the configurations for AdventureGameApp must be edited. To be specific, VM options must be added, with this line of code as our VM options:
--module-path "/path/to/javafx-sdk-21/lib/" --add-modules javafx.controls,javafx.fxml,javafx.media
