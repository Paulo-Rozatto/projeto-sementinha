/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Servico;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.Validate;

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
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnExcluir;
    
    @FXML
    private TableView<Servico> tbl;
    @FXML
    private TableColumn<Servico, Integer> colId;
    @FXML
    private TableColumn<Servico, String> colTipo;
    @FXML
    private TableColumn<Servico, Double> colPreco;

    private ObservableList<Servico> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        tbl.setItems(lista);
    }

    @FXML
    private void novo(ActionEvent event) {
        tfTipo.setText("");
        tfPreco.setText("");
        changeDisable(false);
        novoItem = true;
    }

    @FXML
    private void editar(ActionEvent event) {
        changeDisable(false);
        novoItem = false;
    }

    @FXML
    private void salvar(ActionEvent event) {
        Servico s;
        String tipo = tfTipo.getText();
        String precoString = tfPreco.getText();

        if (Validate.servico(tipo, precoString)) {
            double preco = Double.parseDouble(precoString);
            if (novoItem) {
                s = new Servico(tipo, preco);
                lista.add(s);
            } else {
                s = getSelectionedObject();
                s.setTipo(tipo);
                s.setPreco(preco);
            }
            changeDisable(true);
        }
    }

    @FXML
    private void excluir(ActionEvent event) {
        int i = getSelectionedIndex();
        lista.remove(i);
    }

    @FXML
    void selecionar(MouseEvent event) {
        Servico s = getSelectionedObject();

        tfTipo.setText(s.getTipo());
        tfPreco.setText(String.valueOf(s.getPreco()));
    }

    private int getSelectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Servico getSelectionedObject() {
        Servico s = tbl.getItems().get(getSelectionedIndex());
        return s;
    }

    private void changeDisable(boolean opt) {
        tfTipo.setDisable(opt);
        tfPreco.setDisable(opt);
        btnSalvar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

}
