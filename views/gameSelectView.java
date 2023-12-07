package views;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.AdventureGameStart;

import java.io.File;

/**
 * Class gameSelectView.
 *
 * Finds games and allows user to pick one.
 */
public class gameSelectView {
    private AdventureGameStart adventureGameStart;

    private ListView<String> GameList;
    Label selectGameLabel;

    private Button selectGameButton, closeWindowButton;



    public gameSelectView(AdventureGameStart adventureGameStart) {
        this.adventureGameStart = adventureGameStart;
        loadUI();

    }
    /**
     * Loads the UI for the game select view.
     */
    public void loadUI(){
        final Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(adventureGameStart.stage);

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        selectGameLabel = new Label("");
        selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
        selectGameLabel.setStyle("-fx-text-fill: #e8e6e3;");

        GameList = new ListView<>(); //to hold all the game names
        GameList.setId("GameList");
        GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getGames(GameList);

        selectGameButton = new Button("Change Game");
        selectGameButton.setId("ChangeGame");
        // AdventureGameStart.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");
        selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectGameButton.setPrefSize(200, 50);
        selectGameButton.setOnAction(e -> {
            selectGame(selectGameLabel, GameList);


        });

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton");
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setOnAction(e -> stage.close());

        VBox vbox = new VBox(10, selectGameLabel, GameList, selectGameButton, closeWindowButton);
        dialogVbox.getChildren().add(vbox);
        GameList.setPrefHeight(200);


        stage.setScene(new Scene(dialogVbox, 400, 400));
        stage.show();

    }
    /**
     * Gets the games from the games folder.
     * @param GameList the list of games
     */
    private void getGames(ListView<String> GameList) {
        File folder = new File("Games");
        File[] games = folder.listFiles();
        if (games != null) {
            for (File game : games) {
                if (game.isDirectory()) {
                    this.GameList.getItems().add(game.getName());
                }
            }
        }

    }
    /**
     * Selects a game from the list of games.
     * @param selectGameLabel the label to tell the user what game was loaded.
     * @param GameList the list of games
     */
    private void selectGame(Label selectGameLabel, ListView<String> GameList) {
        String game = GameList.getSelectionModel().getSelectedItem();

        if (game != null) {
            adventureGameStart.setGame(game);
            adventureGameStart.currGame.setText("Current Game: " + game);
            selectGameLabel.setText("Successfully set current game to " + game);

        }
    }


}
