package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import util.AlertBox;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class MainController implements Initializable {

    @FXML
    private Button btnSementes;

    @FXML
    private Button btnRecipientes;

    @FXML
    private Button btnSubstratos;

    @FXML
    private Button btnServicos;

    @FXML
    private Button btnPlantio;

    @FXML
    private StackPane stackPane;

    @FXML
    void switchToSementes(ActionEvent event) {
        switchToChild(0);
    }

    @FXML
    void switchToRecipientes(ActionEvent event) {
        switchToChild(1);
    }

    @FXML
    void switchToSubstratos(ActionEvent event) {
        switchToChild(2);
    }

    @FXML
    void switchToServicos(ActionEvent event) {
        switchToChild(3);
    }

    @FXML
    void switchToPlantio(ActionEvent event) {
        switchToChild(4);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String[] paths = {
                "/gui/panes/sementes.fxml",
                "/gui/panes/recipientes.fxml",
                "/gui/panes/substratos.fxml",
                "/gui/panes/servicos.fxml",
                "/gui/panes/plantios.fxml"
            };

            for (String p : paths) {
                Node n = FXMLLoader.load(getClass().getResource(p));
                stackPane.getChildren().add(n);
            }

            switchToChild(0);
        } catch (IOException ex) {
            AlertBox.exception("Erro ao carregar o progama.", ex);
        }

    }

    private void switchToChild(int index) {

        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);
        stackPane.getChildren().get(2).setVisible(false);
        stackPane.getChildren().get(3).setVisible(false);
        stackPane.getChildren().get(4).setVisible(false);

        stackPane.getChildren().get(index).setVisible(true);

        /*
        try {
            
            String[] paths = {
                "/gui/panes/sementes.fxml",
                "/gui/panes/recipientes.fxml",
                "/gui/panes/substratos.fxml",
                "/gui/panes/servicos.fxml",
                "/gui/panes/plantios.fxml"
            };

            stackPane.getChildren().clear();
            Node n = FXMLLoader.load(getClass().getResource(path));
            stackPane.getChildren().add(n);
        } catch (IOException ex) {
            AlertBox.error("Não foi possível carregar o progama");
        }
         */
    }
}
