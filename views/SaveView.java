package views;

import AdventureModel.AchievementList;
import AdventureModel.AdventureGame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observer.LabelObserver;
import observer.VBoxObserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class SaveView.
 *
 * Saves Serialized adventure games.
 */
public class SaveView {

    static String saveFileSuccess = "Saved Adventure Game!!";
    static String saveFileExistsError = "Error: File already exists";
    static String saveFileNotSerError = "Error: File must end with .ser";

    String gameName;
    private Label saveFileErrorLabel = new Label("");
    private Label saveGameLabel = new Label(String.format("Enter name of file to save"));
    private TextField saveFileNameTextField = new TextField("");
    private Button saveGameButton;
    private Button closeWindowButton;

    private AdventureGameView adventureGameView;

    /**
     * Constructor
     */
    public SaveView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();
        gameName = this.adventureGameView.model.getName();


        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle(adventureGameView.vboxStyle);
        VBoxObserver vboxObserver = new VBoxObserver(dialogVbox, adventureGameView.vboxStyle);
        saveGameLabel.setId("SaveGame"); // DO NOT MODIFY ID


        saveFileErrorLabel.setId("SaveFileErrorLabel");

        saveFileNameTextField.setId("SaveFileNameTextField");
        saveGameLabel.setStyle(adventureGameView.labelStyle);
        saveGameLabel.setFont(new Font(16));
        LabelObserver labelObserver = new LabelObserver(saveGameLabel, adventureGameView.labelStyle);
        saveFileErrorLabel.setStyle(adventureGameView.labelStyle);
        saveFileErrorLabel.setFont(new Font(16));
        LabelObserver labelObserver2 = new LabelObserver(saveFileErrorLabel, adventureGameView.labelStyle);
        saveFileNameTextField.setStyle(adventureGameView.labelStyle);
        saveFileNameTextField.setFont(new Font(16));

        String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
        saveFileNameTextField.setText(gameName);

        saveGameButton = new Button("Save board");
        saveGameButton.setId("SaveBoardButton"); // DO NOT MODIFY ID
        saveGameButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        saveGameButton.setPrefSize(200, 50);
        saveGameButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(saveGameButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
        saveGameButton.setOnAction(e -> saveGame());

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle(adventureGameView.MainBackButtonStyle + adventureGameView.MainTextButtonStyle);
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");

        VBox saveGameBox = new VBox(10, saveGameLabel, saveFileNameTextField, saveGameButton, saveFileErrorLabel, closeWindowButton);
        saveGameBox.setAlignment(Pos.CENTER);
        VBoxObserver vboxObserver2 = new VBoxObserver(saveGameBox, adventureGameView.vboxStyle);

        dialogVbox.getChildren().add(saveGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Saves the Game
     * Save the game to a serialized (binary) file.
     * Get the name of the file from saveFileNameTextField.
     * Files will be saved to the Games/Saved directory.
     * If the file already exists, set the saveFileErrorLabel to the text in saveFileExistsError
     * If the file doesn't end in .ser, set the saveFileErrorLabel to the text in saveFileNotSerError
     * Otherwise, load the file and set the saveFileErrorLabel to the text in saveFileSuccess
     */
    private void saveGame() {
        String name = saveFileNameTextField.getText();
        if (!name.endsWith("ser")) {
            saveFileErrorLabel.setText(saveFileNotSerError);
            return;
        }
        File file = new File("Games/" + gameName + "/Saved");
        if (file.isDirectory()) {
            File[] items = file.listFiles();
            if (items != null) {
                for (File f : items) {
                    if (f.getName().equals(name)) {
                        saveFileErrorLabel.setText(saveFileExistsError);
                        return;
                    }
                }
            }
        }
        else {
            File file2 = new File(gameName + "/Saved");
            file2.mkdirs();

        }
        File file2 = new File(gameName + "/Saved/" + name);
        adventureGameView.model.saveModel(file2);
        saveFileErrorLabel.setText(saveFileSuccess);
        AchievementList.saveAchievements(adventureGameView.model);
    }


}

