package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author paulo
 */
public class Main extends Application {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/panes/main.fxml"));
        
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/images/icon.png"));
        stage.setScene(scene);
        stage.show();
    }
    
}
