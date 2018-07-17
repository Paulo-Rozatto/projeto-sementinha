/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Servico;
import db.dao.ServicoDAO;
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
import util.AlertBox;
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
        
        ServicoDAO dao = new ServicoDAO();
        lista.addAll(dao.read());
        tbl.setItems(lista);
    }

    @FXML
    private void novo(ActionEvent event) {
        clean();

        changeDisable(false);
        novoItem = true;
    }

    @FXML
    private void editar(ActionEvent event) {
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
    private void salvar(ActionEvent event) {
        ServicoDAO dao = new ServicoDAO();
        Servico s;

        String tipo = tfTipo.getText();
        String precoString = tfPreco.getText();

        if (Validate.servico(tipo, precoString)) {
            double preco = Double.parseDouble(precoString);
            if (novoItem) {
                s = new Servico(tipo, preco);
                if (dao.create(s)) {
                    lista.add(dao.readLast());
                    clean();
                }
            } else {
                s = getSelectionedObject();
                s.setTipo(tipo);
                s.setPreco(preco);
                dao.update(s);
            }
            changeDisable(true);
            tbl.getSelectionModel().select(s);
        }
    }

    @FXML
    private void excluir(ActionEvent event) {
        ServicoDAO dao = new ServicoDAO();

        try {
            Servico s = getSelectionedObject();

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
            Servico s = getSelectionedObject();

            tfTipo.setText(s.getTipo());
            tfPreco.setText(String.valueOf(s.getPreco()));
        } catch (RuntimeException ex) {
        }
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

    private void clean() {
        tfTipo.setText("");
        tfPreco.setText("");
    }

}
