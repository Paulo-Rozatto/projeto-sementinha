package util;

import beans.Semente;
import db.dao.FatoresDAO;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

/**
 *
 * @author paulo
 */
public class DialogBox {

    public DialogBox() {
    }

    ;

    public void error(String text) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void warning(String text) {
        Alert alert = new Alert(AlertType.WARNING);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void exception(String text, Exception ex) {
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

    public boolean confirmDelete() {
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

    public void describe(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        alert.setContentText(text);

        alert.showAndWait();
    }

    public void fatores() {
        Alert alert = new Alert(AlertType.NONE);
        GridPane gp = new GridPane();

        TextField plantio = new TextField();
        TextField quimica = new TextField();
        TextField fisica = new TextField();
        TextField estrati = new TextField();

        ButtonType confirma = new ButtonType("Salvar");
        ButtonType cancela = new ButtonType("Cancelar");

        double fatores[] = Semente.getFatores();

        plantio.setText(String.valueOf(fatores[0]));
        quimica.setText(String.valueOf(fatores[1]));
        fisica.setText(String.valueOf(fatores[2]));
        estrati.setText(String.valueOf(fatores[3]));

        gp.add(new Label("Planito indireto"), 0, 0);
        gp.add(plantio, 0, 1);
        gp.add(new Label("Quebra química"), 0, 2);
        gp.add(quimica, 0, 3);
        gp.add(new Label("Quebra Física"), 0, 4);
        gp.add(fisica, 0, 5);
        gp.add(new Label("Estratificação"), 0, 6);
        gp.add(estrati, 0, 7);

        alert.getButtonTypes().setAll(confirma, cancela);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 200px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        alert.getDialogPane().setContent(gp);

        Optional<ButtonType> result;
        boolean valido = false;
        while (!valido) {
            result = alert.showAndWait();

            if (result.get() == confirma) {
                if (Validate.fatores(plantio.getText(), quimica.getText(), fisica.getText(), estrati.getText())) {
                    valido = true;
                    fatores[0] = Double.parseDouble(plantio.getText());
                    fatores[1] = Double.parseDouble(quimica.getText());
                    fatores[2] = Double.parseDouble(fisica.getText());
                    fatores[3] = Double.parseDouble(estrati.getText());

                    Semente.setFatores(fatores[0], fatores[1], fatores[2], fatores[3]);
                    FatoresDAO fdao = new FatoresDAO();
                    fdao.update(fatores[0], fatores[1], fatores[2], fatores[3]);
                }
            } else {
                valido = true;
            }
        }
    }
}
