package views;

import AdventureModel.AchievementList;
import views.AdventureGameView;
import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import java.io.IOException;


public class AdventureGameStart {
    Stage stage;

    Scene scene;
    
    AdventureGameView adventureGameView;

    AdventureGame model;
    Button startButton, loadButton, exitButton, gameButton;

    Label title, currGame;

    GridPane gridPane = new GridPane();

    HBox buttonBox = new HBox(20);

    String game;

    public AdventureGameStart(Stage mainStage){
        this.stage = mainStage;
        game = "TinyGame";
        startUI();

    }

    public void startUI(){
        stage = new Stage();
        stage.setTitle("Adventure Game");
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setMaxWidth(800);
        stage.setMaxHeight(600);

        title = new Label("Adventure Game");
        title.setFont(new Font("Arial", 50));
        title.setTextFill(Color.WHITE);



        startButton = new Button("Start");
        startButton.setFont(new Font("Arial", 20));
        startButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        startButton.setPrefSize(200, 50);

        addStartEvent();

        AdventureGameView.makeButtonAccessible(startButton, "start game", "This is a button to start the game", "Use this button to start the game.");

        loadButton = new Button("Load");
        loadButton.setFont(new Font("Arial", 20));
        loadButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        loadButton.setPrefSize(200, 50);
        addLoadEvent();

        AdventureGameView.makeButtonAccessible(loadButton, "load game", "This is a button to load a game", "Use this button to load a game.");

        exitButton = new Button("Exit");
        exitButton.setFont(new Font("Arial", 20));
        exitButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        exitButton.setPrefSize(200, 50);
        addExitEvent();

        gameButton = new Button("Set Game");
        gameButton.setFont(new Font("Arial", 20));
        gameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        gameButton.setPrefSize(200, 50);
        addGameEvent();

        AdventureGameView.makeButtonAccessible(exitButton, "exit game", "This is a button to exit the game", "Use this button to exit the game.");

//        achievementButton = new Button("Achievements");
//        achievementButton.setFont(new Font("Arial", 20));
//        achievementButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
//        achievementButton.setPrefSize(200, 50);

//        achievementButton.setOnAction(e -> {
//            stage.close();
//            AchievementView achievementView = new AchievementView();
//        });

        buttonBox.getChildren().addAll(startButton, loadButton, gameButton, exitButton);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);



        currGame = new Label("Current Game: " + game);
        currGame.setFont(new Font("Arial", 20));
        currGame.setTextFill(Color.WHITE);
        currGame.setAlignment(Pos.CENTER);

        gridPane.add(buttonBox,0,2, 1, 1 );
        gridPane.add(title, 0, 0, 1, 1);
        gridPane.add(currGame, 0, 1, 1, 1);
        gridPane.setStyle("-fx-background-color: #121212;");

        scene = new Scene(gridPane, 1000, 800);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
    /**
     * Adds an event to the button to load a game from the current game.
     */
    public void addLoadEvent(){
        loadButton.setOnAction(e -> {
            model = new AdventureGame(game);
            stage.close();
            stage = new Stage();

            this.adventureGameView = new AdventureGameView(model, stage);
            LoadView loadView = new LoadView(adventureGameView);
            try {
                AchievementList.populateList(this.model);
            } catch (IOException ioe) {
                System.out.println("IO Error");
            }

            AchievementList.populateCategories();
        });
    }
    /**
     * Adds an event to the button to start the game.
     */
    public void addStartEvent(){
        startButton.setOnAction(e -> {
            model = new AdventureGame(game);
            stage.close();
            stage = new Stage();

            this.adventureGameView = new AdventureGameView(model, stage);
        });
    }


    /**
     * Adds an event to the button to change the current game.
     */
    public void addGameEvent(){
        gameButton.setOnAction(e -> {

            gameSelectView gameSelectView = new gameSelectView(this);
            gridPane.requestFocus();

        });

    }
    /**
     * Adds an event to the button to exit the game.
     */
    public void addExitEvent(){
        exitButton.setOnAction(e -> {
            Platform.exit();
        });
    }

    /**
     * Getter method for the current game.
     * @return name of the current game
     */
    public String getGame(){
        return game;
    }
    /**
     * Setter method for the current game.
     * @param game name of the current game
     */
    public void setGame(String game){
        this.game = game;
    }





}

