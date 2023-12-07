package views;

import AdventureModel.*;
import AdventureModel.Moods.Mood;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.AccessibleRole;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Class NPCView.
 *
 * Loads Human or Troll NPC interactions.
 */
public class NPCView {
    private ImageView NPCImage; // ImageView to hold the NPC Image
    private NPC npc; // NPC of the NPC view
    private AdventureGameView adventureGameView; // the adventure game view
    AdventureGame model; // Model of the game
    GridPane dialogVbox = new GridPane(); // GridPane to hold the image and text of the NPC
    Label NPCLabel = new Label(); // Label which holds the text of the NPC
    HBox options = new HBox(); // HBox to hold all button options of the NPC
    Stage dialog = new Stage(); // Stage to render everything on



    /**
     * Constructor
     */
    public NPCView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        this.model = this.adventureGameView.model;
        this.npc = this.model.getPlayer().getCurrentRoom().getNPC();

        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);


        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle(adventureGameView.vboxStyle);

        updateScene("", this.npc.getName());

        options.setPadding(new Insets(20, 20, 20, 20));
        options.setSpacing(10);
        options.setAlignment(Pos.BOTTOM_CENTER);
        for (String s : this.npc.getOptions()) {
            Button b = new Button(s);
            b.setId(s);
            customizeButton(b, 200, 50);
            makeButtonAccessible(b, s, "This button is one of the options to select.", "This button is one of the options to select for the NPC.");
            b.setOnAction(e -> {
                if (this.npc instanceof Human) {
                    updateScene("", s);
                    Mood m = ((Human) this.npc).getMood();
                    if (s.equals("HIGH FIVE")) {
                        m.increaseOpinion(50);
                    }
                    else if (s.equals("FIST BUMP")) {
                        m.increaseOpinion(75);
                    }
                    else {
                        m.increaseOpinion(25);
                    }
                    m = m.update();
                    ((Human) this.npc).setMood(m); // updates mood based on interaction with Human NPC
                    ((Human) this.npc).updateText();
                }
                else if (this.npc instanceof Troll) {
                    if (s.equals(((Troll) this.npc).getCorrectOption())) {
                        updateScene(((Troll) this.npc).getWinningText(), this.npc.getName());
                        if (((Troll) this.npc).getObject() != null) {
                            AdventureObject a = ((Troll) this.npc).getObject();
                            ((Troll) this.npc).setObject(null);
                            this.model.getPlayer().inventory.add(a); // gives Player item back if it was stolen
                        }
                    }
                    else {
                        if (!this.model.getPlayer().inventory.isEmpty() && ((Troll) this.npc).getObject() == null) {
                            updateScene(((Troll) this.npc).getLosingText(), this.npc.getName());
                            Collections.shuffle(this.model.getPlayer().inventory);
                            AdventureObject o = this.model.getPlayer().inventory.remove(0);
                            ((Troll) this.npc).setObject(o); // Troll steals random item from Player
                        }
                        else {
                            updateScene(((Troll) this.npc).getAlternateLosingText(), this.npc.getName()); // Alternate losing text for when Player loses after first time
                        }
                    }
                }
                this.adventureGameView.updateItems();
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(event -> {
                    AchievementList.getInstance().checkCategory("mood", model);
                    ArrayList<String> ach = AchievementList.getInstance().getStringList();
                    this.adventureGameView.achievements = FXCollections.observableArrayList(ach);
                    dialog.close();
                });
                pause.play();
            });
            this.options.getChildren().add(b);
        }
        dialogVbox.getChildren().add(this.options);

        // Render everything
        var scene = new Scene(dialogVbox, 800, 700);
        scene.setFill(Color.BLACK);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.show();
    }

    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name        ARIA name
     * @param shortString ARIA accessible text
     * @param longString  ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }


    /**
     * updateScene
     * __________________________
     * <p>
     * Shows the NPC, and text below it.
     * If the textToDisplay parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * If filename is empty, displays NPC image.
     *
     * @param textToDisplay the text to display below the image.
     * @param filename filename which refers to image that may be displayed
     */
    public void updateScene(String textToDisplay, String filename) {

        if (filename.equals("")) {
            getImage(this.npc.getName());
        }
        else {
            getImage(filename);
        }
        formatText(textToDisplay); //format the text to display
        NPCLabel.setPrefWidth(500);
        NPCLabel.setPrefHeight(500);
        NPCLabel.setTextOverrun(OverrunStyle.CLIP);
        NPCLabel.setWrapText(true);
        VBox roomPane = new VBox(NPCImage, NPCLabel);

        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle(adventureGameView.vboxStyle);

        dialogVbox.getChildren().add(roomPane);
        dialog.sizeToScene();
    }

    /**
     * formatText
     * __________________________
     * <p>
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.npc.getText();
            NPCLabel.setText(roomDesc);
        } else NPCLabel.setText(textToDisplay);
        NPCLabel.setStyle(adventureGameView.labelStyle);
        NPCLabel.setFont(new Font("Arial", 16));
        NPCLabel.setAlignment(Pos.CENTER);



    }

    /**
     * getImage
     * __________________________
     * <p>
     * Get the image for the current NPC and place
     * it in the NPCImage
     */
    private void getImage(String filename) {

        String npcImage = this.model.getDirectoryName() + "/NPCPictures/" + filename + ".png";

        Image roomImageFile = new Image(npcImage);
        this.NPCImage = new ImageView(roomImageFile);
        this.NPCImage.setPreserveRatio(true);
        this.NPCImage.setFitWidth(400);
        this.NPCImage.setFitHeight(400);

        //set accessible text
        this.NPCImage.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        this.NPCImage.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        this.NPCImage.setFocusTraversable(true);
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
        inputButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
    }
}