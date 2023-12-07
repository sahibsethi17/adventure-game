package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import observer.BackButtonObserver;
import observer.TextButtonObserver;
import observer.VBoxObserver;

public class ContrastView {
    private AdventureGameView adventureGameView;
    private Button closeWindowButton, labelButton, mainBackButtonButton, mainTextButtonButton, vboxButton, gridPaneButton;

    private Button scrollPaneButton;
    private ListView colourlist; //to hold colours

    private Button changeColourButton; //to select colour

    Label colourLabel;

    ColorPicker colourPicker;

    private VBox box;
    /**
     * Constructor for ContrastView
     */
    ContrastView(AdventureGameView adventureGameView){


        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        VBoxObserver vboxObserver = new VBoxObserver(dialogVbox, adventureGameView.vboxStyle);



        colourPicker = new ColorPicker();
        colourPicker.setPrefSize(200, 50);

        colourLabel = new Label("Select a colour");
        colourLabel.setId("ColourLabel"); // DO NOT MODIFY ID
        colourLabel.setStyle("-fx-text-fill: #e8e6e3;");
        colourLabel.setFont(new Font(16));




        labelButton = new Button("Change Labels");
        labelButton.setId("LabelButton"); // DO NOT MODIFY ID
        labelButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        labelButton.setPrefSize(300, 50);
        labelButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(labelButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
        addLabelEvent();

        vboxButton = new Button("Change VBoxes");
        vboxButton.setId("VBoxButton"); // DO NOT MODIFY ID
        vboxButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        vboxButton.setPrefSize(300, 50);
        vboxButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(vboxButton, "VBox Button", "This is a button to change the VBox", "Use this button to change VBox Colours");
        addVBoxEvent();

        mainBackButtonButton = new Button("Change Button Background");
        mainBackButtonButton.setId("MainButtonButton");
        mainBackButtonButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        mainBackButtonButton.setPrefSize(300, 50);
        mainBackButtonButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(mainBackButtonButton, "Main Button Button", "This is a button to change the Main Buttons", "Use this button to change Main Button Colours");
        addMainBackButtonEvent();

        mainTextButtonButton = new Button("Change Button Text");
        mainTextButtonButton.setId("MainTextButtonButton");
        mainTextButtonButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        mainTextButtonButton.setPrefSize(300, 50);
        mainTextButtonButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(mainTextButtonButton, "Main Text Button Button", "This is a button to change the Main Text Buttons", "Use this button to change Main Text Button Colours");
        addMainTextButtonEvent();

        gridPaneButton = new Button("Change GridPanes");
        gridPaneButton.setId("GridPaneButton"); // DO NOT MODIFY ID
        gridPaneButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        gridPaneButton.setPrefSize(300, 50);
        gridPaneButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(gridPaneButton, "GridPane Button", "This is a button to change the GridPanes", "Use this button to change GridPane Colours");
        addGridPaneEvent();

        scrollPaneButton = new Button("Change ScrollPanes");
        scrollPaneButton.setId("ScrollPaneButton"); // DO NOT MODIFY ID
        scrollPaneButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        scrollPaneButton.setPrefSize(300, 50);
        scrollPaneButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(scrollPaneButton, "ScrollPane Button", "This is a button to change the ScrollPanes", "Use this button to change ScrollPane Colours");
        addScrollPaneEvent();

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        closeWindowButton.setPrefSize(300, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");


        VBox buttonBox = new VBox(10, colourLabel, colourPicker, labelButton, mainBackButtonButton, mainTextButtonButton, vboxButton, gridPaneButton, scrollPaneButton, closeWindowButton);
        buttonBox.setAlignment(Pos.CENTER);
        VBoxObserver vboxObserver2 = new VBoxObserver(buttonBox, adventureGameView.vboxStyle);

        dialogVbox.getChildren().add(buttonBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 600);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    /**
     * Adds an event to the button to change the colour of labels
     */

    public void addLabelEvent(){
        labelButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {

                if (observer instanceof observer.LabelObserver) {

                    observer.update(color);
                }
            }
            adventureGameView.labelStyle = "-fx-text-fill: " + color + ";";
        });
    }
    /**
     * Adds an event to the button to change the colour of buttons
     */
    public void addMainBackButtonEvent(){

        mainBackButtonButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;
            adventureGameView.MainBackButtonStyle = "-fx-background-color: " + color + ";";
            String color2 = adventureGameView.MainTextButtonStyle + adventureGameView.MainBackButtonStyle;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {
                if (observer instanceof BackButtonObserver) {
                    observer.update(color2);
                }
            }
        });

    }
    /**
     * Adds an event to the button to change the colour of buttons
     */

    public void addMainTextButtonEvent(){

        mainTextButtonButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;

            adventureGameView.MainTextButtonStyle = "-fx-text-fill: " + color + ";";

            String color2 = adventureGameView.MainTextButtonStyle + adventureGameView.MainBackButtonStyle;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {
                if (observer instanceof TextButtonObserver) {
                    observer.update(color2);
                }
            }
        });

    }
    /**
     * Adds an event to the button to change the colour of vboxes
     */

    public void addVBoxEvent(){

        vboxButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {
                if (observer instanceof observer.VBoxObserver) {
                    observer.update(color);
                }
            }
            adventureGameView.vboxStyle = "-fx-background-color: " + color + ";";
        });
    }
    /**
     * Adds an event to the button to change the colour of gridpanes
     */
    public void addGridPaneEvent(){

        gridPaneButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {
                if (observer instanceof observer.GridPaneObserver) {
                    observer.update(color);
                }
            }
            adventureGameView.gridPaneStyle = "-fx-background-color: " + color + ";";
        });
    }
    /**
     * Adds an event to the button to change the colour of scrollpanes
     */

    public void addScrollPaneEvent(){

        scrollPaneButton.setOnAction(e -> {
            String color = colorToHex(colourPicker.getValue());
            color = "#"+ color;
            for (observer.NodeObserver observer : adventureGameView.getObservers()) {
                if (observer instanceof observer.ScrollPaneObserver) {
                    observer.update(color);
                }
            }
            adventureGameView.scrollPaneStyle = "-fx-background: " + color + ";";
        });
    }
    private String colorToHex(Color color) {
        String hex1;
        String hex2;

        hex1 = Integer.toHexString(color.hashCode()).toUpperCase();

        switch (hex1.length()) {
            case 2:
                hex2 = "000000";
                break;
            case 3:
                hex2 = String.format("00000%s", hex1.substring(0,1));
                break;
            case 4:
                hex2 = String.format("0000%s", hex1.substring(0,2));
                break;
            case 5:
                hex2 = String.format("000%s", hex1.substring(0,3));
                break;
            case 6:
                hex2 = String.format("00%s", hex1.substring(0,4));
                break;
            case 7:
                hex2 = String.format("0%s", hex1.substring(0,5));
                break;
            default:
                hex2 = hex1.substring(0, 6);
        }
        return hex2;
    }




}
