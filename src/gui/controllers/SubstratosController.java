/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Substrato;
import db.dao.SubstratoDAO;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.AlertBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class SubstratosController implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @FXML
    private TextArea taDescricao;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnExcluir;

    @FXML
    private TableView<Substrato> tbl;

    @FXML
    private TableColumn<Substrato, Integer> colId;

    @FXML
    private TableColumn<Substrato, String> colNome;

    @FXML
    private TableColumn<Substrato, Double> colPreco;

    private ObservableList<Substrato> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        SubstratoDAO dao = new SubstratoDAO();
        lista.addAll(dao.read());

        tbl.setItems(lista);
    }

    @FXML
    void novo(ActionEvent event) {
        clean();

        changeDisable(false);
        novoItem = true;
    }

    @FXML
    void editar(ActionEvent event) {
        try {
            if (getSelectionedObject() != null) {
                changeDisable(false);
                novoItem = false;
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    void salvar(ActionEvent event) {
        SubstratoDAO dao = new SubstratoDAO();
        Substrato s;

        String nome = tfNome.getText();
        String precoString = tfPreco.getText();
        String descricao = taDescricao.getText();

        if (Validate.substrato(nome, precoString, descricao)) {
            double preco = Double.parseDouble(precoString);
            if (novoItem) {
                s = new Substrato(nome, preco, descricao);
                if (dao.create(s)) {
                    lista.add(dao.readLast());
                    clean();
                }
            } else {
                s = getSelectionedObject();
                s.setNome(nome);
                s.setPreco(preco);
                s.setDescricao(descricao);
                dao.update(s);
            }
            changeDisable(true);
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        SubstratoDAO dao = new SubstratoDAO();

        try {
            Substrato s = getSelectionedObject();

            if (AlertBox.confirmDelete()) {
                if (dao.delete(s.getId())) {
                    lista.remove(s);
                    clean();
                }
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }

    }

    @FXML
    void selecionar(MouseEvent event) {
        try {
            Substrato s = getSelectionedObject();

            tfNome.setText(s.getNome());
            tfPreco.setText(String.valueOf(s.getPreco()));
            taDescricao.setText(s.getDescricao());
        } catch (RuntimeException ex) {
        }
    }

    private int getSelectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Substrato getSelectionedObject() {
        Substrato s = tbl.getItems().get(getSelectionedIndex());
        return s;
    }

    private void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfPreco.setDisable(opt);
        taDescricao.setDisable(opt);
        btnSalvar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }
    
    private void clean(){
        tfNome.setText("");
        tfPreco.setText("");
        taDescricao.setText("");
    }
}
