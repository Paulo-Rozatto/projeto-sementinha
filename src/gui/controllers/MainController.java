package gui.controllers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
        switchToChild(0);
    }

    private void switchToChild(int index) {
        stackPane.getChildren().get(0).setVisible(false);
        stackPane.getChildren().get(1).setVisible(false);
        stackPane.getChildren().get(2).setVisible(false);
        stackPane.getChildren().get(3).setVisible(false);
        stackPane.getChildren().get(4).setVisible(false);
        
        stackPane.getChildren().get(index).setVisible(true);
    }
}
