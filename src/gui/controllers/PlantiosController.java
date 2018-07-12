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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class PlantiosController implements Initializable {

    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<?> cbSemente;
    @FXML
    private TextField tfSemente;
    @FXML
    private ComboBox<?> cbRecipiente;
    @FXML
    private TextField tfRecipiente;
    @FXML
    private ComboBox<?> cbSubstrato;
    @FXML
    private Button btnDescricao;
    @FXML
    private TableView<?> tblServicos;
    @FXML
    private TableColumn<?, ?> colServicos;
    @FXML
    private TableColumn<?, ?> colHoras;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnCalcularTotal;
    @FXML
    private TableView<?> tbl;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colSemente;
    @FXML
    private TableColumn<?, ?> colRecipiente;
    @FXML
    private TableColumn<?, ?> colSubstrato;
    @FXML
    private TableColumn<?, ?> colData;
    @FXML
    private TableColumn<?, ?> colTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void descreverSubstrato(ActionEvent event) {
    }

    @FXML
    private void calcularTotal(ActionEvent event) {
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
