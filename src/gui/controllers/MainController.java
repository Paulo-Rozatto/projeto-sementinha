package gui.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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
        changeMenuColor(btnSementes);
    }

    @FXML
    void switchToRecipientes(ActionEvent event) {
        switchToChild(1);
        changeMenuColor(btnRecipientes);
    }

    @FXML
    void switchToSubstratos(ActionEvent event) {
        switchToChild(2);
        changeMenuColor(btnSubstratos);
    }

    @FXML
    void switchToServicos(ActionEvent event) {
        switchToChild(3);
        changeMenuColor(btnServicos);
    }

    @FXML
    void switchToPlantio(ActionEvent event) {
         switchToChild(4);
        changeMenuColor(btnPlantio);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //carrega as telas
        try {
            insertPane("/gui/panes/sementes.fxml");
            insertPane("/gui/panes/recipientes.fxml");
            insertPane("/gui/panes/substratos.fxml");
            insertPane("/gui/panes/servicos.fxml");
            insertPane("/gui/panes/plantios.fxml");
        } catch (IOException ex) {
            System.out.println("Painel não encontrado: " + ex);
        }

        switchToChild(0);
    }

    private void insertPane(String path) throws IOException {
        Pane newPane = FXMLLoader.load(getClass().getResource(path));
        stackPane.getChildren().add(newPane);
    }

    private void switchToChild(int index) {
        for (int i = 0; i < stackPane.getChildren().size(); i++) {
            stackPane.getChildren().get(i).setVisible(false);
        }
        stackPane.getChildren().get(index).setVisible(true);
    }

    private void changeMenuColor(Button btn) {
        btnSementes.setStyle("-fx-background-color: #123456");
        btnRecipientes.setStyle("-fx-background-color: #123456");
        btnSubstratos.setStyle("-fx-background-color: #123456");
        btnServicos.setStyle("-fx-background-color: #123456");
        btnPlantio.setStyle("-fx-background-color: #123456");

        btn.setStyle("-fx-background-color: #123478");
    }

}
