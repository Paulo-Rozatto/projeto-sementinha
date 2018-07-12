/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class ServicosController implements Initializable {

    @FXML
    private TextField tfTipo;
    @FXML
    private TextField tfPreco;
    @FXML
    private TableView<?> tbl;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colTipo;
    @FXML
    private TableColumn<?, ?> colPreco;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void novo(ActionEvent event) {
    }

    @FXML
    private void editar(ActionEvent event) {
    }

    @FXML
    private void salvar(ActionEvent event) {
    }

    @FXML
    private void excluir(ActionEvent event) {
    }
    
}
