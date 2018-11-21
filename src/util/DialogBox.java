package util;

import beans.QuebraDormencia;
import beans.TipoPlantio;
import db.dao.QuebraDormenciaDAO;
import db.dao.TipoPlantioDAO;
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

        return result.get() == confirma;
    }

    public void describe(String text) {
        Alert alert = new Alert(AlertType.INFORMATION);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        alert.setContentText(text);

        alert.showAndWait();
    }

    public void fator(TipoPlantio tp) {
        Alert alert = new Alert(AlertType.WARNING);
        GridPane gp = new GridPane();

        Validate validar = new Validate();
        TipoPlantioDAO tpDAO = new TipoPlantioDAO();

        TextField tfFator = new TextField();

        ButtonType confirma = new ButtonType("Salvar");
        ButtonType cancela = new ButtonType("Cancelar");

        gp.add(new Label("O fator é unico para cada tipo de plantio.\n"
                + "As alterações feitas são aplicáveis a todas sementes."), 0, 0);
        gp.add(new Label("\n" + tp.getNome()), 0, 1);
        gp.add(tfFator, 0, 2);
        
        tfFator.setText(String.valueOf(tp.getFator()).replace(".", ","));

        alert.getButtonTypes().setAll(confirma, cancela);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 500px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        alert.getDialogPane().setContent(gp);

        Optional<ButtonType> result;
        boolean valido = false;
        String fatorS;
        
        while (!valido) {
            result = alert.showAndWait();
            fatorS = tfFator.getText().replace(",", ".");
            
            if (result.get() == confirma) {
                
                if (validar.fator(fatorS)) {
                    double fator = Double.parseDouble(fatorS);
                    tp.setFator(fator);
                    tpDAO.update(tp.getId(), fator);
                    valido = true;
                }
                
            } else {
                valido = true;
            }
        }
    }

    public void fator(QuebraDormencia qd) {
        Alert alert = new Alert(AlertType.WARNING);
        GridPane gp = new GridPane();
        Validate validar = new Validate();
        QuebraDormenciaDAO qdDAO = new QuebraDormenciaDAO();
        TextField tfFator = new TextField();

        ButtonType confirma = new ButtonType("Salvar");
        ButtonType cancela = new ButtonType("Cancelar");

        tfFator.setText(String.valueOf(qd.getFator()).replace(".", ","));

        gp.add(new Label("O fator é unico para cada tipo de plantio.\n"
                + "As alterações feitas são aplicáveis a todas sementes."), 0, 0);
        gp.add(new Label("\n" + qd.getNome()), 0, 1);
        gp.add(tfFator, 0, 2);

        alert.getButtonTypes().setAll(confirma, cancela);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 500px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);

        alert.getDialogPane().setContent(gp);

        Optional<ButtonType> result;
        boolean valido = false;
        String fatorS = tfFator.getText().replace(",", ".");
        while (!valido) {
            result = alert.showAndWait();
            if (result.get() == confirma) {
                if (validar.fator(fatorS)) {
                    double fator = Double.parseDouble(fatorS);
                    qdDAO.update(qd.getId(), fator);
                }
            } else {
                valido = true;
            }
        }
    }
}
