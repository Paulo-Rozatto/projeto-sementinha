package util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

/**
 *
 * @author paulo
 */
public class AlertBox {
    

    public static void error(String text) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void warning(String text) {
        Alert alert = new Alert(AlertType.WARNING);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public static void exception(String text, Exception ex) {
        Alert alert = new Alert(AlertType.ERROR);
        GridPane gp = new GridPane();
        Label lbl = new Label(text);
        TextArea ta = new TextArea(ex.toString());

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 610px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        ta.setMaxHeight(100);

        gp.add(lbl, 0, 0);
        gp.add(ta, 0, 1);

        alert.getDialogPane().setContent(gp);

        alert.showAndWait();
    }

    public static boolean confirmDelete() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        ButtonType confirma = new ButtonType("Confirmar");
        ButtonType cancela = new ButtonType("Cancelar");
        
       alert.getButtonTypes().setAll(confirma, cancela);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja excluir?\n"
                + "Não é possivel desfazer essa operação.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == confirma) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void describe(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        
        alert.setContentText(text);

        alert.showAndWait();
    }
    private AlertBox(){}

}
