package views;

import AdventureModel.*;
import AdventureModel.Moods.Mood;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.AccessibleRole;
import observer.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize the model.
 */
public class AdventureGameView {

    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered

    Button saveButton, loadButton, helpButton, NPCButton, customButton, achButton; //buttons

    Boolean helpToggle = false; //is help on display?

    /**
     * A toggle which keeps track of whether the Visited Rooms interface is being displayed
     */
    Boolean roomToggle = false;

    Boolean colourToggle = false; // temp thing for now

    ArrayList<NodeObserver> observers = new ArrayList<>();

    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    String labelStyle = "-fx-text-fill: white;";

    String MainBackButtonStyle = "-fx-background-color: #17871b;";

    String MainTextButtonStyle = "-fx-text-fill: white;";
    String objButtonStyle = "-fx-text-fill: white;";
    String vboxStyle = "-fx-background-color: #000000;";
    String gridPaneStyle = "-fx-background-color: #000000;";
    String scrollPaneStyle = "-fx-background: #000000; -fx-background-color:transparent;";



    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing
    ArrayList<String> ach = AchievementList.getInstance().getStringList();
    ObservableList<String> achievements = FXCollections.observableArrayList(ach);

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("Adventure Game");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);

        VBoxObserver objectsInInventoryObserver = new VBoxObserver(objectsInInventory, vboxStyle);
        observers.add(objectsInInventoryObserver);

        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        VBoxObserver objectsInRoomObserver = new VBoxObserver(objectsInRoom, vboxStyle);
        observers.add(objectsInRoomObserver);

        LabelObserver roomDescLabelObserver = new LabelObserver(roomDescLabel, labelStyle);
        observers.add(roomDescLabelObserver);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));
        GridPaneObserver gridPaneObserver = new GridPaneObserver(gridPane, gridPaneStyle);
        observers.add(gridPaneObserver);


        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        TextButtonObserver saveTextButtonObserver = new TextButtonObserver(saveButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(saveTextButtonObserver);

        saveButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");

        BackButtonObserver BackButtonObserver = new BackButtonObserver(saveButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(BackButtonObserver);

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        TextButtonObserver loadTextButtonObserver = new TextButtonObserver(loadButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(loadTextButtonObserver);

        BackButtonObserver = new BackButtonObserver(loadButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(BackButtonObserver);

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 100, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        TextButtonObserver helpTextButtonObserver = new TextButtonObserver(helpButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(helpTextButtonObserver);
        BackButtonObserver = new BackButtonObserver(helpButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(BackButtonObserver);

        NPCButton = new Button("NPC");
        NPCButton.setId("NPC");
        customizeButton(NPCButton, 100, 50);
        makeButtonAccessible(NPCButton, "NPC Button", "This button displays an NPC.", "This button displays the NPC in the room.");
        NPCButton.setOnAction(e -> {
            gridPane.requestFocus();
            NPCView npcView = new NPCView(this);
        });

        TextButtonObserver NPCTextButtonObserver = new TextButtonObserver(NPCButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(NPCTextButtonObserver);
        BackButtonObserver = new BackButtonObserver(NPCButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(BackButtonObserver);

        customButton = new Button("Customize");
        customButton.setId("Customize");
        customizeButton(customButton, 100, 50);
        makeButtonAccessible(customButton, "Customize Button", "This button allows you to customize the game.", "This button allows you to customize the game. Click it to customize the game to your liking.");
        addCustomEvent();

        TextButtonObserver customTextButtonObserver = new TextButtonObserver(customButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(customTextButtonObserver);
        BackButtonObserver = new BackButtonObserver(customButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(BackButtonObserver);

        HBox topButtons = new HBox();
        achButton = new Button("Achievements");
        achButton.setId("Achievements");
        customizeButton(achButton, 150, 50);
        makeButtonAccessible(achButton, "Achievements Button", "This button shows your achievements", "This button shows the achievements for the game and those you've earned. Click to view.");
        addAchievementEvent();

        TextButtonObserver achTextButtonObserver = new TextButtonObserver(achButton, MainTextButtonStyle + MainBackButtonStyle);
        observers.add(achTextButtonObserver);
        BackButtonObserver achBackButtonObserver = new BackButtonObserver(achButton, MainTextButtonStyle  + MainBackButtonStyle);
        observers.add(achBackButtonObserver);

        topButtons.getChildren().addAll(saveButton, helpButton, customButton,  loadButton, achButton, NPCButton);

        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        LabelObserver objLabelObserver = new LabelObserver(objLabel, labelStyle);
        observers.add(objLabelObserver);

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        LabelObserver invLabelObserver = new LabelObserver(invLabel, labelStyle);
        observers.add(invLabelObserver);

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        LabelObserver commandLabelObserver = new LabelObserver(commandLabel, labelStyle);
        observers.add(commandLabelObserver);

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        this.model.getPlayer().getCurrentRoom().visit();

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);

        VBoxObserver textEntryObserver = new VBoxObserver(textEntry, vboxStyle);
        observers.add(textEntryObserver);

        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
        if (this.model.getPlayer().getCurrentRoom().getNPC() != null) {
            loadNPC();
            this.model.getPlayer().getCurrentRoom().getNPC().setVisited();
        }
    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the inputTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to inputTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            String x = inputTextField.getText().strip();
            if (event.getCode().toString().equals("ENTER")) {
                submitEvent(x);
                inputTextField.setText("");
            }
            else if (event.getCode().toString().equals("TAB")) {
                gridPane.requestFocus();
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        } else if (text.equalsIgnoreCase("ROOMS") || text.equalsIgnoreCase("R")) {
            showVisitedRooms();
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!
        // If player drops cake, Human NPC picks it up and mood increases.
        if (Objects.equals("YOU HAVE DROPPED:\n CAKE", output) && this.model.getPlayer().getCurrentRoom().getNPC() instanceof Human) {
            for (AdventureObject o : this.model.getPlayer().getCurrentRoom().objectsInRoom) {
                if (o.getName().equals("CAKE")) {
                    int x = o.getOpinionReward();
                    Mood m = ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).getMood();
                    m.increaseOpinion(x);
                    m = m.update();
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).setMood(m);
                    gridPane.requestFocus();
                    NPCView npcView = new NPCView(this);
                    npcView.updateScene("Thank you for the cake! This gift will be eaten after my dinner tonight for sure!", "");
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).updateText();
                    AchievementList.getInstance().checkCategory("mood", model);
                    AchievementList.updateScoreString();
                    ArrayList<String> ach = AchievementList.getInstance().getStringList();
                    this.achievements = FXCollections.observableArrayList(ach);
                    this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(o);
                    PauseTransition pause = new PauseTransition(Duration.seconds(4));
                    pause.setOnFinished(event -> {
                        npcView.dialog.close();
                    });
                    pause.play();
                    break;
                }
            }
        }
        // If player drops rotten cake, Human NPC picks it up and mood decreases.
        else if (Objects.equals("YOU HAVE DROPPED:\n ROTTEN_CAKE", output) && this.model.getPlayer().getCurrentRoom().getNPC() instanceof Human) {
            for (AdventureObject o : this.model.getPlayer().getCurrentRoom().objectsInRoom) {
                if (o.getName().equals("ROTTEN_CAKE")) {
                    int x = o.getOpinionReward();
                    Mood m = ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).getMood();
                    m.increaseOpinion(x);
                    m = m.update();
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).setMood(m);
                    gridPane.requestFocus();
                    NPCView npcView = new NPCView(this);
                    npcView.updateScene("What is this garbage!? It stinks! I'll be tossing this in the trash.", "");
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).updateText();
                    AchievementList.getInstance().checkCategory("mood", model);
                    AchievementList.updateScoreString();
                    ArrayList<String> ach = AchievementList.getInstance().getStringList();
                    this.achievements = FXCollections.observableArrayList(ach);
                    this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(o);
                    PauseTransition pause = new PauseTransition(Duration.seconds(4));
                    pause.setOnFinished(event -> {
                        npcView.dialog.close();
                    });
                    pause.play();
                    break;
                }
            }
        }
        // If player drops poop, Human NPC picks it up and mood decreases drastically.
        else if (Objects.equals("YOU HAVE DROPPED:\n POOP", output) && this.model.getPlayer().getCurrentRoom().getNPC() instanceof Human) {
            for (AdventureObject o : this.model.getPlayer().getCurrentRoom().objectsInRoom) {
                if (o.getName().equals("POOP")) {
                    int x = o.getOpinionReward();
                    Mood m = ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).getMood();
                    m.increaseOpinion(x);
                    m = m.update();
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).setMood(m);
                    gridPane.requestFocus();
                    NPCView npcView = new NPCView(this);
                    npcView.updateScene("I CAN'T BELIEVE THIS! WHAT IS THIS?! CRAP!!??", "");
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).updateText();
                    AchievementList.getInstance().checkCategory("mood", model);
                    AchievementList.updateScoreString();
                    ArrayList<String> ach = AchievementList.getInstance().getStringList();
                    this.achievements = FXCollections.observableArrayList(ach);
                    this.model.getPlayer().getCurrentRoom().objectsInRoom.remove(o);
                    PauseTransition pause = new PauseTransition(Duration.seconds(4));
                    pause.setOnFinished(event -> {
                        npcView.dialog.close();
                    });
                    pause.play();
                    break;
                }
            }
        }

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            this.model.getPlayer().getCurrentRoom().visit(); // Visit the room
            AchievementList.getInstance().checkCategory("secret", this.model);
            AchievementList.updateScoreString();
            ach = AchievementList.getInstance().getStringList();
            achievements = FXCollections.observableArrayList(ach);
            updateItems();
            if (this.model.getPlayer().getCurrentRoom().getNPC() instanceof Human) {
                if (!this.model.getPlayer().getCurrentRoom().getNPC().isVisited()) {
                    loadNPC();
                    ((Human) this.model.getPlayer().getCurrentRoom().getNPC()).setVisited();
                }
            }
            else if (this.model.getPlayer().getCurrentRoom().getNPC() instanceof Troll) {
                if (!this.model.getPlayer().getCurrentRoom().getNPC().isVisited()) {
                    loadNPC();
                    ((Troll) this.model.getPlayer().getCurrentRoom().getNPC()).setVisited();
                }
                if (this.model.getPlayer().getCurrentRoom().getNPC().isVisited() && ((Troll) this.model.getPlayer().getCurrentRoom().getNPC()).getObject() != null) {
                    loadNPC();
                }
            }
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            this.model.getPlayer().getCurrentRoom().visit(); // Visit the room
            updateItems();
            AchievementList.getInstance().checkCategory("completion", this.model);
            AchievementList.updateScoreString();
            ach = AchievementList.getInstance().getStringList();
            achievements = FXCollections.observableArrayList(ach);
            PauseTransition pause = new PauseTransition(Duration.seconds(30));
            pause.setOnFinished(event -> {
                Platform.exit();
                for (Achievement a: AchievementList.getInstance().aList) {
                    a.setStatus(0);
                }
                AchievementList.saveAchievements(this.model);
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            PauseTransition pause = new PauseTransition(Duration.seconds(6));
            pause.setOnFinished(event -> {
                submitEvent("FORCED");
            });
            pause.play();
            updateScene("");
            this.model.getPlayer().getCurrentRoom().visit(); // Visit the room            AchievementList.getInstance().checkCategory("secret", this.model);
            AchievementList.updateScoreString();
            ach = AchievementList.getInstance().getStringList();
            achievements = FXCollections.observableArrayList(ach);
            updateItems();
        }
    }

    /**
     * showVisitedRooms
     *
     * show a list of all rooms that the player has visited before.
     */
    private void showVisitedRooms() {
        if (!roomToggle) {
            ScrollPane scrollPane = new ScrollPane();
            VBox content = new VBox();
            Button button = new Button("Go Back");
            button.setId("Go Back");
            button.setOnAction(e -> {
                gridPane.requestFocus();
                gridPane.getChildren().remove(1,1);
                updateScene("");
                roomToggle = false;
            });
            customizeButton(button, 400, 50);
            makeButtonAccessible(button, "Go Back", "This button returns to the normal interface", "This button hides the visited rooms.");
            content.getChildren().add(button);
            Text text = new Text(this.model.getVisitedRoomsString());
            text.setFill(Color.WHITE);
            text.setFont(new Font("Arial", 14));

            TextButtonObserver buttonText = new TextButtonObserver(button, MainTextButtonStyle);
            observers.add(buttonText);
            BackButtonObserver backButtonText = new BackButtonObserver(button, MainBackButtonStyle);
            observers.add(backButtonText);
            TextObserver textObserver = new TextObserver(text, labelStyle);
            observers.add(textObserver);
            VBoxObserver contentObserver = new VBoxObserver(content, vboxStyle);
            observers.add(contentObserver);

            content.getChildren().add(text);
            content.setSpacing(10);
            scrollPane.setContent(content);
            scrollPane.setStyle("-fx-background: #000000; -fx-background-color:transparent;");

            ScrollPaneObserver scrollPaneObserver = new ScrollPaneObserver(scrollPane, scrollPaneStyle);
            observers.add(scrollPaneObserver);

            gridPane.add(scrollPane, 1, 1);
            roomToggle = true;
        }

    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        roomDescLabel.setText(model.player.getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {


        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);



        VBox roomPane = new VBox(roomImageView,roomDescLabel);



        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
//        roomPane.setStyle("-fx-background-color: #000000;");

        VBoxObserver roomPaneObserver = new VBoxObserver(roomPane, vboxStyle);
        observers.add(roomPaneObserver);

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
        if ((textToDisplay == null || textToDisplay.isBlank())) {
            articulateRoomDescription();
        }
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            else roomDescLabel.setText(roomDesc);
        } else roomDescLabel.setText(textToDisplay);
//        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";
        if (!this.objectsInRoom.getChildren().isEmpty()) {
            this.objectsInRoom.getChildren().subList(0, this.objectsInRoom.getChildren().size()).clear();
        }
        if (!this.objectsInInventory.getChildren().isEmpty()) {
            this.objectsInInventory.getChildren().subList(0, this.objectsInInventory.getChildren().size()).clear();
        }
        for (AdventureObject object : this.model.player.inventory) {
            String objectName = object.getName();
            Image image = new Image(this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg");
            Button button = new Button();
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageView.setAccessibleText(object.getDescription());
            imageView.setFocusTraversable(true);
            button.setGraphic(imageView);
            button.setId(objectName);
            button.setAccessibleText(object.getDescription());
            button.setOnAction(e -> {
                gridPane.requestFocus();
                submitEvent("drop " + objectName);
            });
            VBox s = new VBox();
            s.setAlignment(Pos.TOP_CENTER);

            VBoxObserver sObserver = new VBoxObserver(s, vboxStyle);
            observers.add(sObserver);



            s.getChildren().add(button);

            Label label = new Label(objectName);

            LabelObserver labelObserver = new LabelObserver(label, labelStyle);
            observers.add(labelObserver);

            s.getChildren().add(label);
            makeButtonAccessible(button, object.getName(), object.getDescription(), object.getDescription());

            this.objectsInInventory.getChildren().add(s);
        }

        for (AdventureObject object : this.model.player.getCurrentRoom().objectsInRoom) {
            String objectName = object.getName();
            Image image = new Image(this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg");
            Button button = new Button();
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imageView.setAccessibleText(object.getDescription());
            imageView.setFocusTraversable(true);
            button.setGraphic(imageView);
            button.setId(objectName);
            button.setAccessibleText(object.getDescription());
            button.setOnAction(e -> {
                gridPane.requestFocus();
                submitEvent("take " + objectName);
            });
            VBox s = new VBox();

            VBoxObserver sObserver = new VBoxObserver(s, vboxStyle);
            observers.add(sObserver);

            s.setAlignment(Pos.TOP_CENTER);



            s.getChildren().add(button);

            Label label = new Label(objectName);

            LabelObserver labelObserver = new LabelObserver(label, labelStyle);
            observers.add(labelObserver);

            s.getChildren().add(label);
            makeButtonAccessible(button, object.getName(), object.getDescription(), object.getDescription());
            this.objectsInRoom.getChildren().add(s);
        }

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
//        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);

        ScrollPaneObserver scOObserver = new ScrollPaneObserver(scO, scrollPaneStyle);
        observers.add(scOObserver);

        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
//        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");

        ScrollPaneObserver scIObserver = new ScrollPaneObserver(scI, scrollPaneStyle);
        observers.add(scIObserver);

        gridPane.add(scI,2,1);


    }

    /**
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        if (!helpToggle) {
            ScrollPane scrollPane = new ScrollPane();
            VBox content = new VBox();


            Button button = new Button("Hide Instructions");
            button.setId("Hide Instructions");
            button.setOnAction(e -> {
                gridPane.requestFocus();
                gridPane.getChildren().remove(1,1);
                updateScene("");
                helpToggle = false;
            });
            customizeButton(button, 400, 50);
            makeButtonAccessible(button, "Hide Instructions", "This button hides instructions.", "This button hides the instructions.");
            content.getChildren().add(button);
            Text text = new Text(this.model.getInstructions());
            text.setFill(Color.WHITE);
            text.setFont(new Font("Arial", 14));
            TextObserver textObserver = new TextObserver(text, labelStyle);
            observers.add(textObserver);

            TextButtonObserver textButtonObserver = new TextButtonObserver(button, MainTextButtonStyle + MainBackButtonStyle);
            observers.add(textButtonObserver);
            BackButtonObserver backButtonObserver = new BackButtonObserver(button, MainTextButtonStyle + MainBackButtonStyle);
            observers.add(backButtonObserver);

            content.getChildren().add(text);
            content.setSpacing(10);

            scrollPane.setContent(content);
            scrollPane.setStyle(scrollPaneStyle);
            VBoxObserver contentObserver = new VBoxObserver(content, vboxStyle);
            observers.add(contentObserver);
            ScrollPaneObserver scrollPaneObserver = new ScrollPaneObserver(scrollPane, scrollPaneStyle);
            observers.add(scrollPaneObserver);

            gridPane.add(scrollPane, 1, 1);


            helpToggle = true;
        }
    }



    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    public void addAchievementEvent() {
        achButton.setOnAction(e -> {
            gridPane.requestFocus();
            AchievementsView achView = new AchievementsView(this, achievements);
        });

    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }
    /**
     * This method handles the event related to the
     * custom button.
     */
    public void addCustomEvent() {
        customButton.setOnAction(e -> {
//            setColour();
            gridPane.requestFocus();
            ContrastView customView = new ContrastView(this);
        });
    }
    /**
     * Getter for observers
     */
    public ArrayList<NodeObserver> getObservers(){
        return observers;
    }

    public void loadNPC() {
        if (this.model.getPlayer().getCurrentRoom().getNPC() != null) {
            gridPane.requestFocus();
            NPCView npcView = new NPCView(this);
        }
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}
