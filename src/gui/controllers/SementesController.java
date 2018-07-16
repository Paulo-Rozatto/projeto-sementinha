/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Semente;
import db.dao.SementeDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import util.AlertBox;
import util.Validate;

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
    private ToggleGroup tgPreco;

    @FXML
    private RadioButton rbUnidades;

    @FXML
    private ComboBox<String> cbPlantio;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnExcluir;

    @FXML
    private ComboBox<String> cbDormencia;

    @FXML
    private TableView<Semente> tbl;

    @FXML
    private TableColumn<Semente, Integer> colId;

    @FXML
    private TableColumn<Semente, String> colNome;

    @FXML
    private TableColumn<Semente, String> colPlantio;

    @FXML
    private TableColumn<Semente, Double> colPreco;

    @FXML
    private TableColumn<Semente, String> colDormencia;

    private ObservableList<Semente> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //adição dos componetes das combo boxes
        cbDormencia.getItems().addAll("Nenhuma", "Quebra Química", "Quebra Física", "Estratificação");
        cbPlantio.getItems().addAll("Direto", "Indireto");

        //Atribuição dos atributos da classe Semente para cada coluna da  tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colPlantio.setCellValueFactory(cellData -> cellData.getValue().tipoPlantioProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());
        colDormencia.setCellValueFactory(cellData -> cellData.getValue().dormenciaProperty());

        SementeDAO dao = new SementeDAO();
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
        SementeDAO dao = new SementeDAO();
        Semente s;
        String nome, especie, plantio, dormencia, precoString;
        boolean precoEmGramas;

        nome = tfNome.getText();
        especie = tfEspecie.getText();
        plantio = cbPlantio.getValue();
        dormencia = cbDormencia.getValue();
        precoString = tfPreco.getText();
        precoEmGramas = rbGramas.isSelected();

        if (Validate.semente(nome, especie, precoString, plantio, dormencia)) {
            double preco = Double.parseDouble(precoString);
            if (novoItem) {
                s = new Semente(nome, especie, preco, precoEmGramas, plantio, dormencia);
                if (dao.create(s)) {
                    lista.add(dao.readLast());
                     clean();
                }
            } else {
                s = getSelectionedObject();
                s.setNome(nome);
                s.setEspecie(especie);
                s.setTipoPlantio(plantio);
                s.setDomercia(dormencia);
                s.setPreco(preco);
                s.setPrecoEmGramas(precoEmGramas);
                dao.update(s);
            }
            changeDisable(true);
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        SementeDAO dao = new SementeDAO();
        
        try {
            Semente s = getSelectionedObject();

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
            Semente s = getSelectionedObject();

            tfNome.setText(s.getNome());
            tfEspecie.setText(s.getEspecie());
            tfPreco.setText(String.valueOf(s.getPreco()));
            rbGramas.setSelected(s.isPrecoEmGramas());
            rbUnidades.setSelected(!s.isPrecoEmGramas());
            cbPlantio.setValue(s.getTipoPlantio());
            cbDormencia.setValue(s.getDormencia());

        } catch (RuntimeException ex) {}
    }

    private int getSelectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Semente getSelectionedObject() {
        Semente s = tbl.getItems().get(getSelectionedIndex());
        return s;
    }

    private void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfEspecie.setDisable(opt);
        tfPreco.setDisable(opt);
        rbGramas.setDisable(opt);
        rbUnidades.setDisable(opt);
        cbPlantio.setDisable(opt);
        cbDormencia.setDisable(opt);
        btnSalvar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    private void clean() {
        tfNome.setText("");
        tfEspecie.setText("");
        tfPreco.setText("");
        rbGramas.setSelected(true);
        cbPlantio.setValue(null);
        cbDormencia.setValue(null);
    }
}
