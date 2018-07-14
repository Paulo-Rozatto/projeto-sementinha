/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Recipiente;
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
public class RecipientesController implements Initializable {

    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfVolume;
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
    private TableView<Recipiente> tbl;
    @FXML
    private TableColumn<Recipiente, Integer> colId;
    @FXML
    private TableColumn<Recipiente, String> colNome;
    @FXML
    private TableColumn<Recipiente, Double> colVolume;
    @FXML
    private TableColumn<Recipiente, Double> colPreco;

    private ObservableList<Recipiente> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Atribuição dos atributos da classe Recipiente para cada coluna da  tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colVolume.setCellValueFactory(cellData -> cellData.getValue().volumeProperty().asObject());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        tbl.setItems(lista);
    }

    @FXML
    private void novo(ActionEvent event) {
        tfNome.setText("");
        tfVolume.setText("");
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
        Recipiente r;
        String nome = tfNome.getText();
        String volumeString = tfVolume.getText();
        String precoString = tfPreco.getText();

        if (Validate.recipiente(nome,volumeString,precoString)) {
            double volume = Double.parseDouble(volumeString);
            double preco = Double.parseDouble(precoString);
            if (novoItem) {
                r = new Recipiente(nome, volume, preco);
                lista.add(r);
            } else {
                r = getSelectionedObject();
                r.setNome(nome);
                r.setVolume(volume);
                r.setPreco(preco);

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
        Recipiente r = getSelectionedObject();

        tfNome.setText(r.getNome());
        tfVolume.setText(String.valueOf(r.getVolume()));
        tfPreco.setText(String.valueOf(r.getPreco()));
    }

    private int getSelectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Recipiente getSelectionedObject() {
        Recipiente r = tbl.getItems().get(getSelectionedIndex());
        return r;
    }

    private void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfVolume.setDisable(opt);
        tfPreco.setDisable(opt);
        btnSalvar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

}
