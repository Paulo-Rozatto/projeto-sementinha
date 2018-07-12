/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class SementesController implements Initializable {
     @FXML
    private TextField tfNome;

    @FXML
    private TextField tfEspecie;

    @FXML
    private TextField tfPreco;

    @FXML
    private RadioButton rbGramas;

    @FXML
    private ToggleGroup preco;

    @FXML
    private RadioButton rbUnidades;

    @FXML
    private RadioButton rbDireto;

    @FXML
    private ToggleGroup plantio;

    @FXML
    private RadioButton rbIndireto;

    @FXML
    private ComboBox<?> cbDormencia;

    @FXML
    private TableView<?> tbl;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colPlantio;

    @FXML
    private TableColumn<?, ?> colPreco;

    @FXML
    private TableColumn<?, ?> colDormencia;

    @FXML
    void editar(ActionEvent event) {

    }

    @FXML
    void excluir(ActionEvent event) {

    }

    @FXML
    void novo(ActionEvent event) {

    }

    @FXML
    void salvar(ActionEvent event) {

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
}
