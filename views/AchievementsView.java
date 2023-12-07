package views;

import AdventureModel.AchievementList;
import AdventureModel.AdventureGame;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Class for the new view made for displaying achievements.
 */
public class AchievementsView {
    /**
     * AchievementsView constructor
     * __________________________
     * Creates the popup box for achievements
     * @param adventureGameView the AdventureGameView object being used
     * @param achievements an Observable list containing strings with achievement description and completion
     * status for every achievement.
     */
    public AchievementsView(AdventureGameView adventureGameView, ObservableList<String> achievements) {
        VBox box = new VBox();
        final Stage dialog = new Stage(); //dialogue box
        Scene scene = new Scene(box, 500, 200);
        dialog.setScene(scene);
        dialog.setTitle("Achievements");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        ListView<String> lview  = new ListView<>();
        lview.setItems(achievements);
        box.getChildren().addAll(lview);
        VBox.setVgrow(lview, Priority.ALWAYS);

        dialog.setScene(scene);
        dialog.show();
    }
}
